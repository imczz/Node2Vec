package czzClusterAnalysis;

import czzVector.CVector;
import czzVector.IVector;

/**
 * 局类节点，数据结构
 * @author CZZ*/
public class ClusterNode<T> {

	/**
	 * 索引或者名称*/
	public T name;
	
	/**
	 * 向量*/
	public IVector _vector;
	
	/**
	 * 聚类标签*/
	public int label;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public ClusterNode(T name, IVector _vector) {
		this.name = name;
		this._vector = new CVector();
		this._vector.copy(_vector);
		this.label = -1;
	}
}
