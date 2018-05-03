package czzWord2Vec;

/**
 词语，待转换（嵌入，embedding）为向量的词语*/
public class Word<T>{
	
	/**
	 词（如果使用的真的是“词语”）*/
	public T word;
	
	/**
	 待统计的词频，也可以以此为依据建立Huffman树*/
	public int wordFrequency;
	
	/*================================方法 methods================================*/
	
	/**
	 空构造方法*/
	public Word() {
		this.word = null;
		this.wordFrequency = 0;
	}
	
	/**
	 构造方法*/
	public Word(T word) {
		this.word = word;
		this.wordFrequency = 1;
	}
	
	/**
	 构造方法*/
	public Word(int wordFrequency) {
		this.word = null;
		this.wordFrequency = wordFrequency;
	}
}
