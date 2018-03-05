package czzSelectItem;

/**
 可能性对数据结构，对应python代码中的J和q
 @author CZZ*/
public class ProbabilityPair {
	
	/**
	 可能性*/
	public float prob;
	
	/**
	 下标*/
	public int index;
	
	public String toString() {
		StringBuilder str = new StringBuilder("[J:" + index + "|p:" + prob + "]");
		return str.toString();
	}
}
