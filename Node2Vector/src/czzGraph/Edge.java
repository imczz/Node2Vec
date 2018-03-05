package czzGraph;

/**
 图的边
@author CZZ*/
public class Edge<T> {
	
	/**
	 起点*/
	private Node<T> _v1;
	
	/**
	 终点*/
	private Node<T> _v2;
	
	/**
	 边的权，
	 带权图权或者空引用，无权图0或1*/
	private Integer _weight;
	
	/**
	 节点所在的图*/
	//private Graph location;
	
	/*================================方法 methods================================*/
	
	/**
	 构造方法*/
	public Edge(Node<T> v1, Node<T> v2, Integer weight) {
		this._v1 = v1;
		this._v2 = v2;
		this._weight = weight;
	}
	
	/**
	 @return v1节点引用*/
	public Node<T> getV1() {
		return _v1;
	}
	
	/**
	 @return v2节点引用*/
	public Node<T> getV2() {
		return _v2;
	}
	
	/**
	 @return 边的权值*/
	public Integer weight() {
		return _weight;
	}
}
