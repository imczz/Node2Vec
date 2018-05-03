package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 词典，1.统计文章中的单词，2.适当过滤低频词，3.统计词频，4.按照需要建立词频Huffman树*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 词典*/
	private ArrayList<HWord<T> > _vocabulary;
	
	/**
	 词典中词的索引，可以记录词典中是否存在某个单词，也可以记录这个单词在词典中的个位置编号*/
	private HashMap<T, Integer> _wordIndex;
	
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
	
	public Vocabulary() {
		_vocabulary = new ArrayList<HWord<T> >();
		_wordIndex = new HashMap<T, Integer>();
		_isSorted = false;
	}
	
	public boolean isSorted() {
		return _isSorted;
	}

	/**
	 获取词典长度*/
	public int getVocabularySize() {
		return _vocabulary.size();
	}
	
	/**
	 重新建立词典的索引*/
	private void reIndex() {
		_wordIndex.clear();
		for(int i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//更新索引
		}
	}
	
	/**
	 词典中是否存在词语word*/
	public boolean hasWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) ret = true;
		return ret;
	}
	
	public HWord<T> getWord(T word) {
		HWord<T> ret = null;
		if(hasWord(word)) ret = _vocabulary.get(_wordIndex.get(word));
		return ret;
	}
	
	/**
	 从数组装载词典，并且计算词频*/
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
	 向词典中装载词语*/
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
	 根据词典中词频排序，并且重建索引*/
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer<T>());
		reIndex();
		_isSorted = true;
	}

	/**
	 获得词典中每个词语的Huffman编码*/
	public boolean getHuffmanCode() {
		boolean ret = false;
		if(this._vocabulary.size() < 2) return ret;		//只有一个节点，不编码
		if(_isSorted) {
			int i, j;		//i遍历叶子节点，j遍历中间节点
			HWord<T> min1, min2, tempmin;			//最小的两个节点的引用
			int min1n, min2n;				//最小的两个节点的索引
			HWord<T> parentNode;
			ArrayList<HWord<T> > nodeList = new ArrayList<HWord<T> >();				//Huffman树中间节点，n-1个
			HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();		//记录某个节点的父节点
			int vLength = _vocabulary.size();						//词典长度
			min1 = _vocabulary.get(0);
			min2 = _vocabulary.get(1);
			min1.code.add(new Byte((byte) 0));			//最小的是右孩子，编号0
			min2.code.add(new Byte((byte) 1));			//次小的是左孩子，编号1
			parentNode = new HWord<T>();
			parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//父节点=左右孩子值的和
			nodeList.add(parentNode);
			parentMap.put(0, vLength);			//0与1的父节点都是nodeList的0号元素
			parentMap.put(1, vLength);
			i = 2;											//叶子节点指针位置
			j = 0;											//中间节点指针位置
			int k;
			for(k = 1; k < vLength - 1; k++) {
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
					parentIndex = parentMap.get(parentIndex);			//父节点的父节点
				}
			}
			nodeList.clear();
			parentMap.clear();
			ret = true;
		}
		
		return ret;
	}
	
}
