package czzWord2Vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 文章
 * @author CZZ*/
public class Passage<T> {

	/**
	 * 文章存储方式，数组或者文件*/
	public enum PassageStorage {ArrayList, File};
	
	/**
	 * 句子*/
	private ArrayList<T[]> _sentences;
	
	/**
	 * 文章中的句子数量*/
	private long count;
	
	/**
	 * 读取指针*/
	private int pointer;
	
	/**
	 * 文章存储方式，数组或者文件*/
	private PassageStorage passageStorage;
	
	/**
	 * 文章所在文件*/
	private File passageFile;
	
	/**
	 * 读文件*/
	private BufferedReader bufferedReader;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public Passage(PassageStorage passageStorage, String passageFileName) {
		this.passageStorage = passageStorage;
		count = 0;
		pointer = -1;
		if (passageStorage == PassageStorage.ArrayList)  _sentences = new ArrayList<T[]>();
		else if (passageStorage == PassageStorage.File) getStorageFile(passageFileName);
	}
	

	private boolean getStorageFile(String passageFileName) {
		boolean ret = false;
		passageFile = new File(passageFileName);
        if (passageFile.exists()) {
        	String readLine;
        	try {
				bufferedReader = new BufferedReader(new FileReader(this.passageFile));
				if((readLine = bufferedReader.readLine()) != null){				//第一行，总行数
					this.count = Integer.parseInt(readLine);
				}
				bufferedReader.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	if(count > 0) ret = true;
        }
        else passageFile = null;
		return ret;
	}
	
	/**
	 * 从二维数组加载句子
	 * @param 单词二维数组*/
	public void loadSentences(T[][] words) {
		_sentences.clear();					//清空
		pointer = -1;
		for(int i = 0; i < words.length; i++) {
			_sentences.add(words[i]);
			count++;
		}
		if(count > 0) pointer = 0;
	}
	
	/**
	 * 从ArrayLisT加载句子
	 * @param 句子列表*/
	public void loadSentences(ArrayList<T[]> words) {
		_sentences.clear();					//清空
		pointer = -1;
		_sentences.addAll(words);
		count = _sentences.size();
		if(count > 0) pointer = 0;
	}
	
	/**
	 * @return 句子数量*/
	public long getSentenceCount() {
		return count;
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
	 * @return 下一个句子，从文件读取专用*/
	public Integer[] getNextSentence() {
		String[] numstr = null;
		Integer[] ret = null;
		if(passageStorage == PassageStorage.File) {
			if(pointer >= this.count) pointer = 0;			//首尾相连
			String readLine;
			int sentenceCount = -1;
			if(pointer <= 0) {								//重新打开文件
				pointer = 0;
				try {
					bufferedReader = new BufferedReader(new FileReader(this.passageFile));
					if((readLine = bufferedReader.readLine()) != null){				//第一行，总行数
						sentenceCount = Integer.parseInt(readLine);
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(sentenceCount != this.count) {
					//TODO:文件读取错误
				}
			}
			try {
				if ((readLine = bufferedReader.readLine()) != null) {  //读取一行
					numstr = readLine.split(",| ");
			   		if(numstr.length > 0) {
			   			ret = new Integer[numstr.length];
			   			for(int i = 0; i < numstr.length; i++) {
			   				ret[i] = Integer.parseInt(numstr[i]);
			   			}
			   		}
			       	pointer++;
				}
				if(readLine == null) {				//文件读到头了
					if(pointer != this.count) {
						System.out.println("数据行数计数出错");
					}
					pointer = -1;
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	/**
	 * @return 下一个句子编号*/
	public int getPointer() {
		return this.pointer;
	}
	
	/**
	 * 返回指定编号的句子
	 * @param index 句子索引号
	 * @return 下一个句子*/
	public T[] getSentence(int index) {
		T[] ret = null;
		if(index >= 0 && _sentences != null && index < _sentences.size()) ret = _sentences.get(index);
		return ret;
	}
}
