package czzWord2Vec;

import java.util.ArrayList;

/**
 * 文章
 * @author CZZ*/
public class Passage<T> {

	/**
	 * 句子*/
	private ArrayList<T[]> _sentences;
	
	/**
	 * 读取指针*/
	private int pointer;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public Passage() {
		_sentences = new ArrayList<T[]>();
		pointer = -1;
	}
	
	/**
	 * 从二维数组加载句子
	 * @param 单词二维数组*/
	public void loadSentences(T[][] words) {
		_sentences.clear();					//清空
		pointer = -1;
		for(int i = 0; i < words.length; i++) {
			_sentences.add(words[i]);
		}
		if(_sentences.size() > 0) pointer = 0;
	}
	
	/**
	 * 从ArrayLisT加载句子
	 * @param 句子列表*/
	public void loadSentences(ArrayList<T[]> words) {
		_sentences.clear();					//清空
		pointer = -1;
		_sentences.addAll(words);
		if(_sentences.size() > 0) pointer = 0;
	}
	
	/**
	 * @return 句子数量*/
	public int getSentenceCount() {
		return _sentences.size();
	}
	
	/**
	 * @return 句子序列*/
	public ArrayList<T[]> getSentences() {
		return _sentences;
	}
	
	/**
	 * 改变句子“指针"的位置
	 * @param index 设置的位置
	 * @return 设置成功*/
	public boolean seek(int index) {
		boolean ret = false;
		if(index >= 0 && index < _sentences.size()) {
			this.pointer = index;
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return 下一个句子*/
	public T[] getNextSentence() {
		T[] ret = null;
		if(pointer >= 0) {
			if(pointer >= _sentences.size()) pointer = 0;			//首尾相连
			ret = _sentences.get(pointer++);
		}
		return ret;
	}
	
	/**
	 * 返回指定编号的句子
	 * @param index 句子索引号
	 * @return 下一个句子*/
	public T[] getSentence(int index) {
		T[] ret = null;
		if(index >= 0 && index < _sentences.size()) ret = _sentences.get(index);
		return ret;
	}
}
