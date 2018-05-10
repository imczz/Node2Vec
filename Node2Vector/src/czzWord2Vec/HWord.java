package czzWord2Vec;

import java.util.ArrayList;

/**
 可以存储Huffman编码的词语
 @author CZZ*/
public class HWord<T> extends Word<T>{

	/**
	 * huffman编码*/
	public ArrayList<Byte> code;
	
	/**
	 * huffman树中，从根节点到此叶子节点的非叶子节点序列*/
	public ArrayList<Integer> point;
	
	/*================================方法 methods================================*/
	
	/**
	 空构造方法*/
	public HWord() {
		super();
		code = new ArrayList<Byte>();
		point = new ArrayList<Integer>();
	}
	
	/**
	 构造方法*/
	public HWord(T word) {
		super(word);
		code = new ArrayList<Byte>();
		point = new ArrayList<Integer>();
	}

}
