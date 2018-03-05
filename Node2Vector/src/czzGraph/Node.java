package czzGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
图中的节点,T为节点装载的内容的类型
@author CZZ*/
public class Node<T> {

	/**
	 编号或者节点标志号*/
	private int _id;
	
	/**
	 节点别名*/
	public String name;
	
	/**
	 * 类型为T的节点的内容*/
	T element;
	
	/**
	  出发可以到达的节点*/
	private HashMap<Integer, Node<T>> _inNodeList;
	
	/**
	  本节点有哪些出边*/
	private HashMap<Integer, Edge<T>> _outEdgeList;
	
	/**
	  节点所在的图*/
	//private Graph location;
	
	/**
	 入度*/
	private int _in_Degree;
	
	/** 
	 出度*/
	private int _out_Degree;
	
	/** 
	 度 */
	private int _degree;

	/*================================方法 methods================================*/
	
	/**
	 节点的构造函数
	 @param id 节点id*/
	public Node(int id){
		this._id = id;
		this.name = null;
		this._in_Degree = 0;
		this._out_Degree = 0;
		this._degree = 0;
		_inNodeList = new HashMap<Integer, Node<T>>();
		_outEdgeList = new HashMap<Integer, Edge<T>>();
	}
	
	/**
	 节点的构造函数
	 @param id 节点id
	 @param name 预设节点名称*/
	public Node(int id, String name){
		this._id = id;
		this.name = name;
		this._in_Degree = 0;
		this._out_Degree = 0;
		this._degree = 0;
		_inNodeList = new HashMap<Integer, Node<T>>();
		_outEdgeList = new HashMap<Integer, Edge<T>>();
	}
	
	/**
	 @return 节点的入度*/
	public int getIn_Degree() {
		return _in_Degree;
	}

	/**
	 @return 节点的出度*/
	public int getOut_Degree() {
		return _out_Degree;
	}
	
	/**
 	 @return 获取节点的度（入度+出度）*/
	public int getDegree() {
		_degree = this._in_Degree + this._out_Degree;
		return _degree;
	}

	/**
	 @return 节点id*/
	public int getId() {
		return _id;
	}

	/**
	 @return 可以到达此节点的节点列表*/
	public HashMap<Integer, Node<T>> getInNodeList(){
		return this._inNodeList;
	}
	
	/**
	 @return 出边列表*/
	public HashMap<Integer, Edge<T>> getOutEdgeList(){
		return this._outEdgeList;
	}
	
	/**
	 记录：当前节点可以通过边v1ev2到达节点v2*/
	public boolean arriveAt(Edge<T> v1ev2) {
		boolean notexist = false;
		int v2id = v1ev2.getV2().getId();
		if(this._outEdgeList.get(v2id) == null){
			this._outEdgeList.put(v2id, v1ev2);
			this._out_Degree++;
			notexist = true;
		}
		return notexist;
	}
	
	/**
	 记录：某个节点v1可以到达当前节点*/
	public boolean comeFrom(Node<T> v1) {
		boolean notexist = false;
		int v1id =v1.getId();
		if(this._inNodeList.get(v1id) == null){
			this._inNodeList.put(v1id, v1);
			this._in_Degree++;
			notexist = true;
		}
		return notexist;
	}
	
	/**
	 图在删除某个节点之后v，其他节点响应此删除操作，切断与这个节点的联系
	 @param v 图中被删除的节点v*/
	public int removeNode(Node<T> v) {
		int id = v.getId();
		return removeNode(id);
	}
	
	/**
	 图在删除某个节点（id）之后，其他节点响应此删除操作，切断与这个节点的联系
	 @param id 图中被删除的节点的id*/
	public int removeNode(int id) {
		int ret = 0;
		Node<T> inNode = _inNodeList.get(id);
		if(inNode != null) {
			_inNodeList.remove(id);
			this._in_Degree--;
			ret += 1;
		}
		Edge<T> outEdge = _outEdgeList.get(id);
		if(outEdge != null) {
			_outEdgeList.remove(id);
			this._out_Degree--;
			ret += 2;
		}
		return ret;
	}
	
	/**
	 删除在节点中保存的边的引用
	 @param id1 边的起点的id
	 @param id2 边的终点的id
	 */
	public boolean removeEdge(int id1, int id2) {
		boolean ret = false;
		if(id1 == this._id) {			//边的起点是自己
			if(_outEdgeList.get(id2) != null) {
				_outEdgeList.remove(id2);
				this._out_Degree--;
				ret = true;
			}
		}
		if(id2 == this._id) {			//边的终点是自己
			if(_inNodeList.get(id1) != null) {
				this._inNodeList.remove(id1);
				this._in_Degree--;
				ret = true;
			}
		}
		return ret;
	}
	
	/**
	 删除在节点中保存的边的引用
	 @param v1 起点
	 @param v2 终点
	 */
	public boolean removeEdge(Node<T> v1, Node<T> v2) {
		int id1 = v1.getId();
		int id2 = v2.getId();
		return removeEdge(id1, id2);
	}
	
	/**
	 删除在节点中保存的边vv的引用
	 @param vv 待删除的边*/
	public boolean removeEdge(Edge<T> vv) {
		int id1 = vv.getV1().getId();
		int id2 = vv.getV2().getId();
		return removeEdge(id1, id2);
	}
	
	/**
	 判断节点（id）与此节点的关系
	 @param id 待判断的节点的id
	 @return 0不可达，1id节点到达此节点，2此节点可以到达id节点，3互相可达*/
	public int judgeNode(int id) {
		int ret = 0;
		Node<T> in = this._inNodeList.get(id);
		Edge<T> out = this._outEdgeList.get(id);
		if(in != null) {
			if(out != null) ret = 3;
			else ret = 1;
		}
		else {
			if(out != null) ret = 2;
			//else ret = 0;
		}
		return ret;
	}
	
	/**
	  图删除此节点时，调用此方法，通知周边节点断开与此节点的联系*/
	public boolean beforeDeleteSelf() {
		Iterator<Entry<Integer, Node<T>>> nodeIter = this._inNodeList.entrySet().iterator();
		Map.Entry<Integer, Node<T>> nodeEntry;
		while (nodeIter.hasNext()) {
			nodeEntry = (Map.Entry<Integer, Node<T>>) nodeIter.next();
			((Node<T>)nodeEntry.getValue()).removeNode(this);			//可以到达此节点的节点，删除此节点的引用
		}
		_inNodeList.clear();									//释放自己的（入节点）记录
		Iterator<Entry<Integer, Edge<T>>> edgeIter = this._outEdgeList.entrySet().iterator();
		Map.Entry<Integer, Edge<T>> edgeEntry;
		while (edgeIter.hasNext()) {
			edgeEntry = (Entry<Integer, Edge<T>>) edgeIter.next();
			((Edge<T>)edgeEntry.getValue()).getV2().removeNode(this);		//此节点可以到达的节点，删除此节点的引用
		}
		_outEdgeList.clear();									//释放自己的（出节点）记录
		return true;
	}
}
