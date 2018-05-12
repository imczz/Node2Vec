package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 词典，1.统计文章中的单词，2.适当过滤低频词，3.统计词频，4.按照需要建立词频Huffman树
 @author CZZ*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 词典*/
	private ArrayList<HWord<T> > _vocabulary;
	
	/**
	 词典中词的索引，可以记录词典中是否存在某个单词，也可以记录这个单词在词典中的个位置编号*/
	private HashMap<T, Integer> _wordIndex;
	
	/**
	 词典长度，因为有可能过滤掉低频词，所以展示长度可能会减少*/
	private int _vocabularyLength;
	
	/**
	 最小词频，小于最小词频的词会被删除*/
	private int _lessFrequency;
	
	/**
	 第一个词，当词典处于排序状态，StartPointer就是第一个大于等于Lessfrequency的词的索引号（数组地址偏移量）,如果未过滤，为0*/
	private int _startPointer;
	
	/**
	 * 词频随机数采样预制表长度*/
	private int _unigramTableSize;
	
	/**
	 * 词频随机数采样预制表*/
	int[] _unigramTable;
	
	/**
	 比较器类
	 * @param <E> 单词的类型*/
	class WordFrequencyComparer<E> implements Comparator<Word<E>>  {

		public int compare(Word<E> o1, Word<E> o2) {
			int ret = o1.wordFrequency - o2.wordFrequency;
			if(ret > 0) ret = 1;
			else if(ret <0) ret = -1;
			return ret;
		}
	}
	
	private boolean _isSorted;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空参数构造方法*/
	public Vocabulary() {
		this._vocabulary = new ArrayList<HWord<T> >();
		this._wordIndex = new HashMap<T, Integer>();
		this._isSorted = false;
		this._lessFrequency = 0;				//不过滤低频词
		this._startPointer = 0;
		this._vocabularyLength = 0;
		this._unigramTableSize = 100000;
		this._unigramTable = null;
	}
	
	/**
	 * 重新建立词典的索引*/
	private void reIndex() {
		_wordIndex.clear();
		for(int i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//更新索引
		}
	}
	
	/**
	 * @return 词典是否按照词频排序*/
	public boolean isSorted() {
		return _isSorted;
	}

	/**
	 * @return 词典总长度（不过滤低频词总长）*/
	public int getVocabularyFullSize() {
		return _vocabulary.size();
	}
	
	/**
	 * @param word 待查找词
	 * @return 词典中是否存在词语word*/
	public boolean hasWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) ret = true;
		return ret;
	}
	
	/**
	 * @param word 待查找词
	 * @return 查找词语word在词典中的索引编号*/
	public int getWordIndex(T word) {
		int ret = -1;
		if(hasWord(word)) ret = _wordIndex.get(word);
		return ret;
	}
	
	/**
	 * @param word 词语
	 * @return word在词典中存储的实例*/
	public HWord<T> getWord(T word) {
		HWord<T> ret = null;
		if(hasWord(word)) ret = _vocabulary.get(_wordIndex.get(word));
		return ret;
	}
	
	/**
	 * @param word 词语
	 * @return word在词典中存储的索引号*/
	public HWord<T> getWordByIndex(int index) {
		HWord<T> ret = null;
		if(_vocabulary != null && index < _vocabulary.size()) ret = _vocabulary.get(index);
		return ret;
	}
	
	/**
	 * 从数组装载词典，并且计算词频
	 * @param words 第一维是句子集合，第二维是句子中的每个词*/
	public void loadVocabulary(T[][] words) {
		int i, j;
		for(i = 0; i < words.length; i++) {
			for(j = 0; j < words[i].length; j++) {
				addWord(words[i][j]);
			}
		}
		_isSorted = false;
	}
	
	/**
	 * 从句子列表装载词典，并且计算词频
	 * @param words 第一维是句子集合，第二维是句子中的每个词*/
	public void loadVocabulary(ArrayList<T[]> words) {
		T[] arr;
		for(int i = 0; i < words.size(); i++) {
			arr = words.get(i);
			for(int j = 0; j < arr.length; j++) {
				addWord(arr[j]);
			}
		}
		_isSorted = false;
	}
	
	/**
	 * 向词典中添加1个词语，如果已经存在则词频+1
	 * @param word 待添加的词语
	 * @return 添加结果*/
	public boolean addWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) {
			_vocabulary.get(_wordIndex.get(word)).wordFrequency++;		//词频+1
		}
		else {
			_vocabulary.add(new HWord<T>(word));					//加入词典
			_wordIndex.put(word, _vocabulary.size() - 1);		//添加索引
		}
		_isSorted = false;					//改为未排序状态
		return ret;
	}
	
	/**
	 * 向词典中添加一个句子中的所有词语
	 * @param word 待添加词语的句子
	 * @return 添加结果*/
	public boolean addWord(T[] words) {
		for(int i = 0; i < words.length; i++) {
			addWord(words[i]);
		}
		_isSorted = false;
		return true;
	}
	
	/**
	 * 移除某个词
	 * @param word 转被删除的词语
	 * @return 移除成功*/
	public boolean removeWord(T word) {
		boolean ret = false;
		if(hasWord(word)) {
			int index = _wordIndex.get(word);
			_wordIndex.remove(word);
			_vocabulary.remove(index);
			reIndex();			//需要重新建立索引
			ret = true;
		}
		//_isSorted = false;			//不改变排序方式
		return ret;
	}
	
	/**
	 * 根据词典中词频排序，并且重建索引*/
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer<T>());
		reIndex();
		_isSorted = true;
	}

	/**
	 * 获得词典中每个词语的Huffman编码
	 * @return 处理结果*/
	public boolean getHuffmanCode() {
		boolean ret = false;
		if(this.getVocabularyLength() < 2) return ret;		//没有节点或者，只有一个节点，不编码，返回false
		if(_isSorted) {				//已经排序过
			int i, j;		//i遍历叶子节点，j遍历中间节点
			HWord<T> min1, min2, tempmin;			//最小的两个节点的引用
			int min1n, min2n;				//还没有插入Huffman树的最小的两个节点的索引
			HWord<T> parentNode;
			ArrayList<HWord<T> > nodeList = new ArrayList<HWord<T> >();				//Huffman树中间节点，n-1个
			HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();		//记录某个节点的父节点
			int vLength = this.getVocabularyFullSize();						//词典长度
			min1 = _vocabulary.get(_startPointer + 0);
			min2 = _vocabulary.get(_startPointer + 1);
			min1.code.clear();					//清空编码
			min2.code.clear();
			min1.code.add(new Byte((byte) 0));			//最小的是右孩子，编号0
			min2.code.add(new Byte((byte) 1));			//次小的是左孩子，编号1
			parentNode = new HWord<T>();				//生成第一个中间节点
			parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//父节点=左右孩子值的和
			nodeList.add(parentNode);
			parentMap.put(_startPointer + 0, vLength);			//“0”与“1”的父节点都是nodeList的0号元素
			parentMap.put(_startPointer + 1, vLength);
			i = _startPointer + 2;							//叶子节点指针位置
			j = 0;											//中间节点指针位置
			int k;
			for(k = _startPointer + 1; k < vLength - 1; k++) {
				tempmin = nodeList.get(j);
				if (i < vLength && (min1 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //比较叶子节点与中间节点
					min1n = i;
					i++;
				}
				else {		
					min1 = tempmin;
					min1n = vLength + j;
					j++;
				}
				tempmin = nodeList.get(j);			//寻找次最小节点的过程
				if (i < vLength && (min2 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //比较叶子节点与中间节点
					min2n = i;
				    i++;
				}
				else {		
					min2 = tempmin;
					min2n = vLength + j;
					j++;
				}
				min1.code.clear();					//清空编码
				min2.code.clear();
				min1.code.add(new Byte((byte) 0));			//最小的是右孩子，编号0
				min2.code.add(new Byte((byte) 1));			//次小的是左孩子，编号1
				parentNode = new HWord<T>();
				parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//父节点=左右孩子值的和
				nodeList.add(parentNode);
				parentMap.put(min1n, vLength + k);			//min1与min2的父节点都是nodeList的0号元素
				parentMap.put(min2n, vLength + k);			//也可以j加上vLength，逻辑上把叶子节点与中间节点放置在一起，统一编码
			}
			HWord<T> leafNode;			//叶子节点
			int parentIndex = -1;			//双亲节点索引号
			for(k = 0; k < vLength; k++) {				//遍历叶子节点，给叶子节点编码
				leafNode = _vocabulary.get(k);			//当前叶子节点
				parentIndex = parentMap.get(k);			//当前叶子节点的父节点
				while(parentIndex != vLength * 2 - 2) {
					parentNode = nodeList.get(parentIndex - vLength);		//获取父节点
					leafNode.code.add(0, parentNode.code.get(0));	//父节点;
					leafNode.point.add(0, parentIndex - vLength);
					parentIndex = parentMap.get(parentIndex);			//父节点的父节点
				}
				leafNode.point.add(0, this.getVocabularyLength() - 2);
			}
			nodeList.clear();
			parentMap.clear();
			ret = true;						//处理成功
		}
		
		return ret;
	}

	/**
	 * @return 词典长度*/
	public int getVocabularyLength() {
		this._vocabularyLength = this._vocabulary.size() - this._startPointer;
		return _vocabularyLength;
	}

	/**
	 * @return 设置的最小词频*/
	public int getLessFrequency() {
		return _lessFrequency;
	}

	/**
	 * @return 词典数组中，大于等于最小词频的起始偏移量*/
	public int getStartPointer() {
		return _startPointer;
	}
	
	/**
	 * 过滤低频词，首先设置lessFrequency，之后排序（如果未排序），之后设置开始指针
	 * @param lessFrequency 按照自然数lessFrequency过滤低频词
	 * @return true过滤成功，或者false失败*/
	public boolean frequencyFilter(int lessFrequency) {
		boolean ret = false;
		if(lessFrequency < 0) lessFrequency = 0;			//应该大于等于零
		this._lessFrequency = lessFrequency;
		if(!this._isSorted) {			//如果没有排序
			this.sortVocabulary();		//将词典排序
		}
		if(lessFrequency == 0) {						//不过滤
			this._startPointer = -1;
			this._vocabularyLength = _vocabulary.size();
			ret = true;
		}
		else if(_vocabulary.size() > 0 && lessFrequency > _vocabulary.get(_vocabulary.size() - 1).wordFrequency) {		//全部过滤掉，错误的情况
			this._startPointer = _vocabulary.size() - 1;
			this._vocabularyLength = 0;
		}
		else {
			Iterator<HWord<T>> iter = this._vocabulary.iterator();
			this._startPointer = 0;
			while(iter.hasNext()) {
				if(iter.next().wordFrequency >= lessFrequency) break;			//大于（等于）最小词频，会被保留
				this._startPointer++;
			}
			this._vocabularyLength = this._vocabulary.size() - this._startPointer;			//处理词典长度
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 初始化用于负采样的预制表，通过随机数查表，得到单词，也就是完成了一次负采样*/
	public void initUnigramTable() {
		int a, i;
		double train_words_pow = 0;				//总和
		double d1, power = 0.75;					//x^a > x(<0a<1,0<1x)
		this._unigramTable = new int[this._unigramTableSize];
		for (a = 0; a < this.getVocabularyLength(); a++) train_words_pow += Math.pow(this._vocabulary.get(a).wordFrequency, power);
		i = this.getStartPointer();
		d1 = Math.pow(this._vocabulary.get(i).wordFrequency, power) / train_words_pow;
		for (a = 0; a < this._unigramTableSize; a++) {
			this._unigramTable[a] = i;
			if (a / (double)this._unigramTableSize > d1) {
				i++;
				d1 += Math.pow(this._vocabulary.get(i).wordFrequency, power) / train_words_pow;
			}
			if (i >= this.getVocabularyLength()) i = this.getVocabularyLength() - 1;
		}
	}
	
	/**
	 * 按照词频获取一个词
	 * @return 被选择的词在词典中的索引号*/
	public int negSamplingWord() {
		int ret = -1;
		Random rand = new Random();
		ret = this._unigramTable[rand.nextInt(this._unigramTableSize)];
		return ret;
	}
}
