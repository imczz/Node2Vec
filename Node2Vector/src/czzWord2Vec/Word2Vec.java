package czzWord2Vec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

import czzVector.CVector;
import czzVector.IVector;

/**
 * word2vec是一种处理一种语言中词语关系的方式，是一种词语的“嵌入”方式（将单点One-hot表示转换为分布式Distributed表示）；
 * 词向量可以被看成是一种副产物，某种深度神经网络的参数，但是词向量有一些独特的性质，比如可以通过余弦判断两个词向量对应的词的距离
 * @author CZZ*/
public class Word2Vec<T> {

	/**
	 模型类型，skip-gram模型，或者CBOW模型*/
	public enum ModelType {Skip_gram, CBOW};
	
	/**
	 分层柔和最大值Hierarchical Softmax，或者负采样Negative Sampling*/
	public enum TrainMethod {HS, NS, BOTH};
	
	/**
	 * 模型类型*/
	private ModelType modelType;
	
	/**
	 * 训练方式*/
	private TrainMethod trainMethod;
	
	/**
	 词向量维度，也是神经网络隐藏层的神经元个数*/
	private int dimensions;

	/**
	 上下文窗口大小c（windows），在前后都有单词的情况下，最多取得当前词前后各WindowSize个单词，也就是最多2*WindowSize个单词；
	 但是如果从距离来考虑，前后分别选择[1,WindowSize]，这样靠近中心词的词相对来说更容易被选择*/
	private int windowSize;
	
	/**
	 * 负采样数量*/
	private int negative;
	
	/**
	 迭代次数*/
	private int iteratorNumber;
	
	/**
	 最低词频，少于这个频率的单词不会被计入词典vocabulary*/
	private int minWordCount;
	
	/**
	 同时运行的线程数*/
	private int threadNumber;
	
	/**
	 设置一个初始化学习率*/
	private float startLearnRate;
	
	/**
	 当前学习率*/
	private float learnRate;
	
	/**
	 * 文章，每行一句，每句都是一串“词语”*/
	private Passage<T> passags;
	
	private ExpTable expTable;
	
	/**
	 * 词典*/
	private Vocabulary<T> vocabulary;
	
	/**
	 * 模型参数，也就是训练出的每个词的向量*/
	private IVector[] _models;					
	
	/**
	 * 哈夫曼树中间节点的参数“西塔”*/
	private IVector[] _huffmanTheta;			//x * θ
	
	/**
	 * 负采样参数“西塔”*/
	private IVector[] _negTheta;			//x * θ
	
	/**
	 * 是否经过初始化*/
	private boolean initialized;
	
	/*================================方法 methods================================*/
	
	/*
	/**
	 * 空构造方法*
	public Word2Vec() {
		vocabulary = new Vocabulary<T>();
		setParam(ModelType.CBOW, TrainMethod.BOTH, 5, 200, 5, 0.05f, 0, 1, 1);
	}*/
	
	/**
	 * 构造方法
	 * @param mt 模型类型
	 * @param tm 训练方式
	 * @param dimensions 嵌入的向量维度
	 * @param windowSize 窗口大小
	 * @param learnRate 学习率
	 * @param minWordCount 最低词频
	 * @param iteratorNumber 全部样本迭代次数
	 * @param threadNumber 并行线程数
	 * */
	public Word2Vec(ModelType modelType, TrainMethod trainMethod, int negative, int dimensions, int windowSize, float learnRate, int minWordCount, int iteratorNumber, int threadNumber) {
		expTable = new ExpTable();
		initialized = false;
		setParam(modelType, trainMethod, negative, dimensions, windowSize, learnRate, minWordCount, iteratorNumber, threadNumber);
	}
	
	private boolean setParam(ModelType modelType, TrainMethod trainMethod, int negative, int dimensions, int windowSize, float learnRate, int minWordCount, int iteratorNumber, int threadNumber) {
		this.modelType = modelType;
		this.trainMethod = trainMethod;
		this.negative = negative;
		this.dimensions = dimensions;
		this.windowSize = windowSize;
		this.startLearnRate = learnRate;
		this.minWordCount = minWordCount;
		this.iteratorNumber = iteratorNumber;
		this.threadNumber = threadNumber;
		return true;
	}
	
	/**
	 获取维度大小*/
	public int getDimensions() {
		return dimensions;
	}

	/**
	 获取窗口大小*/
	public int getWindowSize() {
		return windowSize;
	}

	public int getIteratorNumber() {
		return iteratorNumber;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public int getMinWordCount() {
		return minWordCount;
	}

	public float getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(int learnRate) {
		this.learnRate = learnRate;
	}
	
	public void init(ArrayList<T[]> words) {
		this.vocabulary = new Vocabulary<T>();
		this.vocabulary.loadVocabulary(words);
		this.vocabulary.sortVocabulary();			//词典中的词语按照词频从小到大排序
		this.passags = new Passage<T>();
		this.passags.loadSentences(words);
		this.vocabulary.frequencyFilter(minWordCount);				//过滤低频词
		if(this.trainMethod == TrainMethod.BOTH || this.trainMethod == TrainMethod.HS) {
			this.vocabulary.getHuffmanCode();			//建立哈夫曼树，获得每个词的编码
		}
		
		initModels();				//初始化参数
		this.learnRate = this.startLearnRate;
		initialized = true;
	}
	
	/**
	 * 用随机数填充向量v*/
	public void randomSetVector(IVector v) {
		if(v != null) {
			float[] va = v.getVector();
			if(va != null) {
				Random rand = new Random();
				for(int i = 0; i <va.length; i++) {
					//va[i] = (float) rand.nextDouble();
					va[i] = ((rand.nextFloat() - 0.5f) / this.dimensions);
				}
			}
		}
		
	}
	
	/**
	 * 初始化各个参数*/
	public void initModels() {
		_models = new IVector[this.vocabulary.getVocabularyLength()];
		if(this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
			_huffmanTheta = new IVector[this.vocabulary.getVocabularyLength()];
		if(this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH)
			_negTheta = new IVector[this.vocabulary.getVocabularyLength()];
		for(int i = 0; i < this.vocabulary.getVocabularyLength(); i++) {
			_models[i] = new CVector(this.dimensions);
			randomSetVector(_models[i]);						//填充随机数
			if(this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
				_huffmanTheta[i] = new CVector(this.dimensions);
			if(this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH)
				_negTheta[i] = new CVector(this.dimensions);
		}
	}
	
	/**
	 * 从训练好的文件读取参数*/
	public boolean loadModels() {
		//TODO
		return false;
	}
	
	/**
	 * 开始训练*/
	public void startTrainning() {
		if(this.initialized) {				//经过初始化
			int localIteratorNumber = 1;			//当前迭代次数
			int c;									//[1, windowSize]的随机窗口大小，使词语之间关系更为密切
			Random rand = new Random();
			HWord<T> word;
			T contextWord;							//上下文
			int contextIndex;					//上下文的索引号
			int thetaIndex;						//西塔索引号
			IVector e;
			float f, g;
			for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++) { //迭代次数
				for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++) {
					T[] sentence = this.passags.getSentence(sentenceIndex);
					for(int sentence_position = 0; sentence_position < sentence.length; sentence_position++) {
						c = Math.max((rand.nextInt() + 11) % this.windowSize, 1);		//[1, windowSize]的随机窗口大小
						word = this.vocabulary.getWord(sentence[sentence_position]);
						this.learnRate *= 0.999f;							//减小学习率
						if (this.learnRate < this.startLearnRate * 0.0001f) this.learnRate = this.startLearnRate * 0.0001f;
						if(this.modelType == ModelType.CBOW) {
							//TODO
						}//if(this.modelType == ModelType.CBOW)
						else if(this.modelType == ModelType.Skip_gram) {
							for (int i = c; i < this.windowSize * 2 + 1 - c; i++) {			//遍历当前词的上下文
								if (i != this.windowSize)					//是当前词的上下文，而不是当前词
								{
									c =	sentence_position -	this.windowSize + i;
									if (c <	0) continue;
									if (c >= sentence.length) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextWord = sentence[c];
									contextIndex = this.vocabulary.getWordIndex(contextWord);
									if(contextIndex == -1) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									e = new CVector(this.dimensions);		//e=0
									// HIERARCHICAL	SOFTMAX
									if (this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
										for	(int l = 0;	l <	word.code.size(); l++)		//从huffman路径分层SoftMax
										{
											thetaIndex = word.point.get(l);
											// Propagate hidden -> output
											f = this._models[contextIndex].multiply(this._huffmanTheta[thetaIndex]);	//f = x * theta;
											if (f <= -this.expTable.getMaxX()) continue;
											else if (f >= this.expTable.getMaxX()) continue;
											else f = expTable.getSigmoid(f);								//sigmoid函数
											// 'g' is the	gradient multiplied	by the learning	rate
											g = (1 - word.code.get(l) - f) * this.learnRate;				//偏导数乘学习率
											// Propagate errors output ->	hidden
											e.add(this._huffmanTheta[thetaIndex].new_Multi(g));				//e += g * theta
											// Learn weights hidden -> output
											this._huffmanTheta[thetaIndex].add(this._models[contextIndex].new_Multi(g));
										}
									// NEGATIVE	SAMPLING
									if (negative > 0) {
										//TODO
									}
									// Learn weights input -> hidden
									this._models[contextIndex].add(e);
								}//if (i != this.windowSize)
							}//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)	
						}//if(this.modelType == ModelType.Skip_gram)
					}//for(int sentence_position = 0; sentence_position < this.passags.getSentence(sentenceIndex); sentence_position++) {
				}//for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++)
			}//for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++)
		}//if(this.initialized)
	}
	
	
	public IVector[] getModels() {
		return this._models;
	}
	
	/**
	 * 输出到文件file
	 * @param file 会被写入的文件路径
	 * @throws IOException */
	public void outputFile(String file) throws IOException {
		File f = new File(file);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		String str;
        out.write(this.vocabulary.getVocabularyLength() + " " + this.dimensions + "\n");
        for(int i = 0; i < this._models.length; i++) {
        	out.write((i + 1) + " ");
        	for(int j = 0; j < this._models[i].getVector().length; j++) {
        		if(j != 0) out.write(" ");
        		str = _models[i].getVector()[j] + "";
        		out.write(str);
        	}
        	if(i != this._models.length - 1) out.write("\n");
        }
        out.flush();
        out.close();
	}
}
