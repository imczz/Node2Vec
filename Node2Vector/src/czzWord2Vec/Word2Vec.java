package czzWord2Vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
	
	/**
	 * 预先计算一个sigmoid函数表，稍微提高效率*/
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
	
	/**
	 * 设置各个参数
	 * @return 设置成功或者失败*/
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
	 @return 维度大小*/
	public int getDimensions() {
		return dimensions;
	}

	/**
	 @return 窗口大小*/
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * @return 迭代次数*/
	public int getIteratorNumber() {
		return iteratorNumber;
	}

	/**
	 * @return 设置的线程数*/
	public int getThreadNumber() {
		return threadNumber;
	}

	/**
	 * 设置线程数*/
	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	/**
	 * @return 设置的最小词频*/
	public int getMinWordCount() {
		return minWordCount;
	}

	/**
	 * @return 当前的学习率*/
	public float getLearnRate() {
		return learnRate;
	}

	/**
	 * 设置当前学习率
	 * @deprecated
	 * @param 设置学习率*/
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
		if(this.trainMethod == TrainMethod.BOTH || this.trainMethod == TrainMethod.NS) {
			this.vocabulary.initUnigramTable();			//建立用于负采样的表
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
	 * 从训练好的文件读取参数
	 * @return 加载成功或失败*/
	public boolean loadModels(String file) {
		boolean ret = false;
		File f = new File(file);
        if (f.exists()) {				//文件存在
        	BufferedReader reader = null; 
        	try { 
        		reader = new BufferedReader(new FileReader(file)); 
        		String tempString = null;
        		String[] str = null;
        		tempString = reader.readLine();
        		str = tempString.split(" ");
        		if(str.length == 2) {
        			int num = Integer.parseInt(str[0]);				//词语个数
        			int dim = Integer.parseInt(str[1]);				//次向量维度
        			if(num > 0 && dim > 0) _models = new IVector[num];
        			for(int i = 0; i < num; i++) {
        				_models[i] = new CVector(dim);
        				tempString = reader.readLine();
                		str = tempString.split(" ");
        				for(int j = 0; j < dim; j++) {
        					_models[i].getVector()[j] = Float.parseFloat(str[j + 1]);
        				}
        			}
        		}
        		reader.close();
        		ret = true;
        	} catch (IOException e) { 
        		e.printStackTrace(); 
        	} finally { 
        		if (reader != null){ 
        			try { 
        				reader.close(); 
        			} catch (IOException e1) { 
        				
        			} 
        		} 
        	} 
        }
		return ret;
	}
	
	/**
	 * 开始训练*/
	public void startTrainning() {
		if(this.initialized) {				//经过初始化
			int localIteratorNumber = 1;			//当前迭代次数
			int c;
			int randomWindow;									//[1, windowSize]的随机窗口大小，使词语之间关系更为密切
			Random rand = new Random();
			HWord<T> word, contextX;								//中心词的上下文
			int wordIndex, contextIndex, thetaIndex;		//中心词,上下文在词典中的索引号;西塔索引号
			T contextWord;									//上下文
			int label;								//负采样的Lω(u) u∈ω∪Neg(ω)
			IVector e, ehs, ens;				//hs与ns的e
			IVector xsum;						//CBOW的上下文求和
			float f, g;
			int cw;				//CBOW上下文计数
			for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++) { //迭代次数
				for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++) {
					T[] sentence = this.passags.getSentence(sentenceIndex);
					for(int sentence_position = 0; sentence_position < sentence.length; sentence_position++) {
						randomWindow = (rand.nextInt() + 11) % this.windowSize;		//randomWindow = [0, windowSize - 1],随机窗口大小windowSize - randomWindow = [1, windowSize]
						wordIndex = this.vocabulary.getWordIndex(sentence[sentence_position]);
						if(wordIndex < this.vocabulary.getStartPointer()) continue;				//当前词被过滤
						wordIndex -= this.vocabulary.getStartPointer();		//因为有单词被过滤，所以model,theta等数组索引改变位置
						word = this.vocabulary.getWord(sentence[sentence_position]);
						this.learnRate *= 0.9999f;							//减小学习率
						if (this.learnRate < this.startLearnRate * 0.0001f) this.learnRate = this.startLearnRate * 0.0001f;
						if(this.modelType == ModelType.CBOW) {
							cw = 0;
							e = new CVector(this.dimensions);		//e=0
							xsum = new CVector(this.dimensions);			//xw = 0;
							for (int i = randomWindow; i < this.windowSize * 2 + 1 - randomWindow; i++) {			//遍历当前词的上下文
								if (i != windowSize)				//取上下文中，除了当前词
								{
									c =	sentence_position -	this.windowSize + i;			//c={sp-w,sp-w+1, ..., sp-1, sp+1, ..., sp+w-1, sp+w}
									if (c <	0) continue;
									if (c >= sentence.length) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextWord = sentence[c];
									contextIndex = this.vocabulary.getWordIndex(contextWord);
									contextX = this.vocabulary.getWord(contextWord);
									if(contextIndex == -1) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextIndex -= this.vocabulary.getStartPointer();		//因为有单词被过滤，所以model,theta等数组索引改变位置
									if(contextIndex < 0) continue;			//当前词被过滤掉了
									xsum.add(this._models[contextIndex]);
									cw++;
								}
							}//for (int i = randomWindow; i < this.windowSize * 2 + 1 - randomWindow; i++)
							if (cw > 0) {				//避免除数为0
								xsum.multiply(1f / cw);				//X /= cw;
								// HIERARCHICAL	SOFTMAX
								if (this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH) {
									for	(int l = 0;	l <	word.code.size(); l++) {		//从huffman路径分层SoftMax
										thetaIndex = word.point.get(l);
										// Propagate hidden -> output
										f = xsum.multiply(this._huffmanTheta[thetaIndex]);
										if (f < -this.expTable.getMaxX()) continue;
										else if (f > this.expTable.getMaxX()) continue;
										else f = expTable.getSigmoid(f);								//sigmoid函数
										// 'g' is the gradient multiplied by the learning rate
										g = (1 - word.code.get(l) - f) * this.learnRate;				//偏导数乘学习率
										// Propagate errors output -> hidden
										e.add(this._huffmanTheta[thetaIndex].new_Multi(g));			//e += g * theta
										// Learn weights hidden -> output
										this._huffmanTheta[thetaIndex].add(xsum.new_Multi(g));			//theta += g * xm
									}
								}
								// NEGATIVE	SAMPLING
								if (this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH) {			
									for (int d = 0; d < this.negative + 1; d++) {
										if (d == 0)	{
											thetaIndex = wordIndex;
											label =	1;
										}
										else {
											thetaIndex = this.vocabulary.negSamplingWord();
											if (thetaIndex == wordIndex) continue;
											label =	0;
										}
										f = xsum.multiply(this._negTheta[thetaIndex]);
										if (f > this.expTable.getMaxX()) g = (label - 1) * this.learnRate;
										else if (f < -this.expTable.getMaxX()) g = (label - 0) * this.learnRate;
										else g = (label - expTable.getSigmoid(f)) * this.learnRate;
										e.add(this._negTheta[thetaIndex].new_Multi(g));			//e += g * theta
										this._negTheta[thetaIndex].add(xsum.new_Multi(g));			//theta += g * xm
									}
								}
								// hidden -> in
								for (int i = randomWindow; i < this.windowSize * 2 + 1 - randomWindow; i++) {
									c =	sentence_position -	this.windowSize + i;			//c={sp-w,sp-w+1, ..., sp-1, sp+1, ..., sp+w-1, sp+w}
									if (c <	0) continue;
									if (c >= sentence.length) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextWord = sentence[c];
									contextIndex = this.vocabulary.getWordIndex(contextWord);
									contextX = this.vocabulary.getWord(contextWord);
									if(contextIndex == -1) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextIndex -= this.vocabulary.getStartPointer();		//因为有单词被过滤，所以model,theta等数组索引改变位置
									if(contextIndex < 0) continue;			//当前词被过滤掉了
									this._models[contextIndex].add(e);
								}
							}
						}//if(this.modelType == ModelType.CBOW)
						else if(this.modelType == ModelType.Skip_gram) {
							for (int i = randomWindow; i < this.windowSize * 2 + 1 - randomWindow; i++) {			//遍历当前词的上下文
								if (i != this.windowSize)					//是当前词的上下文，而不是当前词
								{
									c =	sentence_position -	this.windowSize + i;			//c={sp-w,sp-w+1, ..., sp-1, sp+1, ..., sp+w-1, sp+w}
									if (c <	0) continue;
									if (c >= sentence.length) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextWord = sentence[c];
									contextIndex = this.vocabulary.getWordIndex(contextWord);
									contextX = this.vocabulary.getWord(contextWord);
									if(contextIndex == -1) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextIndex -= this.vocabulary.getStartPointer();		//因为有单词被过滤，所以model,theta等数组索引改变位置
									if(contextIndex < 0) continue;			//当前词被过滤掉了
									ehs = new CVector(this.dimensions);		//e=0
									ens = new CVector(this.dimensions);		//e=0
									// HIERARCHICAL	SOFTMAX
									if (this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH) {
										for	(int l = 0;	l <	contextX.code.size(); l++)		//从huffman路径分层SoftMax
										{
											thetaIndex = contextX.point.get(l);
											// Propagate hidden -> output
											f = this._models[wordIndex].multiply(this._huffmanTheta[thetaIndex]);	//f = v(ω) * theta(u,j-1); u∈context(ω)
											if (f < -this.expTable.getMaxX()) continue;
											else if (f > this.expTable.getMaxX()) continue;
											else f = expTable.getSigmoid(f);								//sigmoid函数
											// 'g' is the	gradient multiplied	by the learning	rate
											g = (1 - contextX.code.get(l) - f) * this.learnRate;				//偏导数乘学习率
											// Propagate errors output ->	hidden
											ehs.add(this._huffmanTheta[thetaIndex].new_Multi(g));				//e += g * theta
											// Learn weights hidden -> output
											this._huffmanTheta[thetaIndex].add(this._models[wordIndex].new_Multi(g));	//theta += g * x
										}
										this._models[wordIndex].add(ehs);				// Learn weights input -> hidden
									}
									// NEGATIVE SAMPLING
									if (this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH) {
										for (int d = 0; d < this.negative + 1; d++) {
											if (d == 0)
											{
												thetaIndex = wordIndex;
												label =	1;					//1个正例
											}
											else
											{
												thetaIndex = this.vocabulary.negSamplingWord();
												if (thetaIndex == wordIndex) continue;
												label =	0;					//negative个负例
											}
											f = this._models[contextIndex].multiply(this._negTheta[thetaIndex]);
											if (f > this.expTable.getMaxX()) g = (label - 1) * this.learnRate;
											else if (f < -this.expTable.getMaxX()) g = (label - 0) * this.learnRate;
											else g = (label - expTable.getSigmoid(f)) * this.learnRate;
											ens.add(this._negTheta[thetaIndex].new_Multi(g));				//e += g * theta
											this._negTheta[thetaIndex].add(this._models[contextIndex].new_Multi(g));
										}
										this._models[contextIndex].add(ens);// Learn weights input -> hidden
									}
								}//if (i != this.windowSize)
							}//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)	
						}//if(this.modelType == ModelType.Skip_gram)
					}//for(int sentence_position = 0; sentence_position < this.passags.getSentence(sentenceIndex); sentence_position++) {
				}//for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++)
			}//for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++)
		}//if(this.initialized)
	}
	
	/**
	 * @return 模型的引用*/
	public IVector[] getModels() {
		return this._models;
	}
	
	/**
	 * 根据词典中词的索引获取单词
	 * @param 某个词的索引
	 * @return 这个词*/
	public T getWordByIndex(int index) {
		T ret = null;
		HWord<T> word = this.vocabulary.getWordByIndex(index);
		if(word != null) ret = word.word;
    	return ret;
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
        for(int i = this.vocabulary.getStartPointer(); i < this.vocabulary.getVocabularyFullSize(); i++) {
        	HWord<T> word = this.vocabulary.getWordByIndex(i);
        	out.write(word.word + " ");
        	for(int j = 0; j < this._models[i - this.vocabulary.getStartPointer()].getVector().length; j++) {
        		if(j != 0) out.write(" ");
        		str = _models[i - this.vocabulary.getStartPointer()].getVector()[j] + "";
        		out.write(str);
        	}
        	if(i != this._models.length - 1) out.write("\n");
        }
        out.flush();
        out.close();
	}
}
