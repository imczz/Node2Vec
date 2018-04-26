package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 词典，1.统计文章中的单词，2.适当过滤低频词，3.统计词频，4.按照需要建立词频Huffman树*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 词典*/
	private ArrayList<Word<T> > _vocabulary;
	
	/**
	 词典中词的索引，可以记录词典中是否存在某个单词，也可以记录这个单词在词典中的个位置编号*/
	private HashMap<T, Integer> _wordIndex;
	
	/**
	 比较器类*/
	class WordFrequencyComparer implements Comparator<Word>  {

		public int compare(Word o1, Word o2) {
			int ret = o1.wordFrequency - o2.wordFrequency;
			if(ret > 0) ret = 1;
			else if(ret <0) ret = -1;
			return ret;
		}
	}
	
	/*================================方法 methods================================*/
	
	/**
	 获取词典长度*/
	public int getVocabularySize() {
		return _vocabulary.size();
	}
	
	/**
	 从数组装载字典，并且计算词频*/
	public void loadVocabulary(T[][] words) {
		int i, j;
		for(i = 0; i < words.length; i++) {
			for(j = 0; j < words[i].length; j++) {
				addWordToVocabulary(words[i][j]);
			}
		}
	}
	
	public boolean addWordToVocabulary(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) {
			_vocabulary.get(_wordIndex.get(word)).wordFrequency++;		//词频+1
		}
		else {
			_vocabulary.add(new Word<T>(word));					//加入词典
			_wordIndex.put(word, _vocabulary.size() - 1);		//添加索引
		}
		return ret;
	}
	
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer());
		_wordIndex.clear();
		int i;
		for(i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//更新索引
		}
	}

}
