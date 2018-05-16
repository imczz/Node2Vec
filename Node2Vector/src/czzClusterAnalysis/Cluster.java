package czzClusterAnalysis;

import java.util.ArrayList;

import czzVector.IVector;

/**
 * 聚类分析的类
 * @author CZZ*/
public abstract class Cluster<T> {

	/**
	 * 等待聚类的节点*/
	protected ArrayList<ClusterNode<T>> nodes;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public Cluster() {
		nodes = new ArrayList<ClusterNode<T>>();
	}
	
	/**
	 * 添加待聚类节点
	 * @param name 索引或者名称或者编号
	 * @param v 节点向量
	 * @return 添加成功（暂时不用哈希表防止重复插入）*/
	public boolean addNode(T name, IVector v) {
		nodes.add(new ClusterNode<T>(name, v));
		return true;
	}
	
	/**
	 * 开始聚类*/
	public abstract boolean runCluster(int k);
	
	public ArrayList<ClusterNode<T>> getNodes() {
		return nodes;
	}
}
