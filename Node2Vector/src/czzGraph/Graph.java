package czzGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
图 
@author CZZ*/
public class Graph {
	
	/**
	 初始化的随机id起点*/
	private int random;
	
	/**
	 是否为有向图*/
	private boolean _isDirected;
	
	/**
	  是否为带权图*/
	private boolean _isWeighted;
	
	/**
	  节点集合*/
	private HashMap<Integer, Node> _nodeList;

	/**
	 边集合，采用邻接表形式，其中Node为起点*/
	private HashMap<Integer, HashMap<Integer, Edge>> _edgeList;
	
	/*================================方法 methods================================*/

	/**
	 图的默认构造函数*/
	public Graph() {
		_nodeList = new HashMap<Integer, Node>();
		_edgeList = new HashMap<Integer, HashMap<Integer, Edge>>();
		Random rand = new Random(65535);
		random = rand.nextInt();
		this._isDirected = false;
		this._isWeighted = false;
	}
	
	/**
	 图的构造函数
	 @param isDirected 是否为有向图
	 @param isWeighted 是否为带权图*/
	public Graph(boolean isDirected, boolean isWeighted) {
		_nodeList = new HashMap<Integer, Node>();
		_edgeList = new HashMap<Integer, HashMap<Integer, Edge>>();
		Random rand = new Random(65535);
		random = rand.nextInt();
		this._isDirected = isDirected;
		this._isWeighted = isWeighted;
	}
	
	public boolean isEmpty() {
		return (this._nodeList.size() == 0 && this._edgeList.size() == 0);
	}
	
	/**
	 @param id 节点的id
	 @return 节点的引用*/
	public Node getNode(int id) {
		return this._nodeList.get(id);
	}
	
	/**
	 @param id1 起点id
	 @param id2 终点id
	 @return 边的引用*/
	public Edge getEdge(int id1, int id2) {
		Edge ret = null;
		HashMap<Integer, Edge> edgeMap = this._edgeList.get(id1);
		if(edgeMap != null) {
			ret = edgeMap.get(id2);
		}
		return(ret);
	}
	
	/**
	  是否为有向图*/
	public boolean isDirected() {
		return _isDirected;
	}
	
	/**
	  是否为带权图*/
	public boolean isWeighted() {
		return _isWeighted;
	}
	
	/**
	  添加一个节点（ID，名字）*/
	public boolean addNode(int id, String name) {
		boolean notexist = false;
		if(_nodeList.get(id) == null) {
			_nodeList.put(id, new Node(id, name));
			notexist = true;
		}
		return notexist;
	}
	
	/**
	  添加一个节点（id）*/
	public boolean addNode(int id) {
		return addNode(id, null);
	}
	
	/**
	 * 添加一个节点(随机分配id,设置名字)，
	 @return 返回成功获取的id，分配id失败返回-1*/
	public int addNode(String name) {
		int newId = -1;			//节点分配id不能为负数
		int i = 0;
		boolean flag = true;
		for(i = 0; i < 10; i++){		//随机分配id,尝试10次
			newId = random;
			if(addNode(newId, name)) {		//获取成功
				random++;
				flag = false;
				break;					//准备返回id
			}
			Random rand = new Random(65535);		//尝试随机选择下一个id
			random = rand.nextInt();
		}
		if(flag) {
			newId = -1;				//10次尝试失败，未能随机出合适的id，返回-1
		}
		return newId;
	}
	
	/**
	 * 添加一个节点(随机分配id,名字也为空)，
	 @return 返回成功获取的id，分配id失败返回-1*/
	public int addNode() {
		return addNode(null);
	}
	
	/**连接已经存在的两个节点
	 * @param id1 节点1的id
	 * @param id2 节点2的id
	 * @return 操作成功返回true
	 */
	public boolean addEdge(int id1, int id2, int weight) {
		Node v1 = this._nodeList.get(id1);
		Node v2 = this._nodeList.get(id2);
		return addEdge(v1, v2, weight);
	}
	
	/**连接已经存在的两个节点
	 * @param v1 节点1
	 * @param v2 节点2
	 * @return 操作成功返回true
	 */
	public boolean addEdge(Node v1, Node v2, int weight) {
		boolean ret = false;
		if(v1 != null && v2!= null) {
			HashMap<Integer, Edge> eV1 = this._edgeList.get(v1.getId());
			if(eV1 == null) {
				eV1 = new HashMap<Integer, Edge>();			//如果没有分配空间，则现场分配
				this._edgeList.put(v1.getId(), eV1);
			}
			int id2 = v2.getId();
			if(eV1.get(id2) == null) {						//避免重复插入
				Edge newEdge = new Edge(v1, v2, weight);
				eV1.put(id2, new Edge(v1, v2, weight));			//插入新的边
				v1.arriveAt(newEdge);					//v1记录有一条新边
				v2.comeFrom(v1);						//v2记录v1可以到达v2
				ret = true;
			}
		}
		return ret;
	}
	
	/**
	 从图中删除节点（id）
	 @return 删除前节点的度*/
	public int removeNode(int id) {
		int ret = -1;
		Node v = this._nodeList.get(id);
		if(v != null) {
			ret = v.getDegree();
			HashMap<Integer, Edge> outEdgeList = this._edgeList.get(id);
			if(outEdgeList != null) {
				outEdgeList.clear();			//1.删除由此节点出发的边
			}
			this._edgeList.remove(id);
			
			HashMap<Integer, Node> inNodeList = v.getInNodeList();
			Set<Integer> inNodeKeySet = inNodeList.keySet();		//id集合
			Iterator<Integer> iter = inNodeKeySet.iterator();		//迭代器
			HashMap<Integer, Edge> v1ev2Set = null;			//从起点出发，到达此节点的边的集合
			int v1id = -1;									//临时起点v1的id
			while(iter.hasNext()) {
				v1id = iter.next();
				v1ev2Set =_edgeList.get(v1id);
				v1ev2Set.remove(id);							//2.删除可以到达此节点的边
				if(v1ev2Set.size() == 0) {
					v1ev2Set.clear();
					_edgeList.remove(v1id);
				}
			}
			v.beforeDeleteSelf();					//断开其他节点与此节点有关的记录
			this._nodeList.remove(id);
		}
		return ret;
	}
	
	/**
	 从图中删除节点v
	 @return 删除前节点的度*/
	public int removeNode(Node v) {
		int id = v.getId();
		return removeNode(id);
	}
	
	/**
	 删除从v1到v2的边
	 @param v1 边的起点
	 @param v2 边的终点
	 @return 删除结果，true删除成功*/
	public boolean removeEdge(Node v1, Node v2) {
		boolean ret = false;
		if(v1 != null && v2 != null) {
			HashMap<Integer, Edge> v1Set = this._edgeList.get(v1.getId());
			if(v1Set!=null) {
				if(v1Set.get(v2.getId()) != null) {
					v1Set.remove(v2.getId());
					v1.removeEdge(v1, v2);
					v2.removeEdge(v1, v2);
					ret = true;
				}
			}
		}
		return ret;
	}
	
	/**
	 删除图中的边
	 @param id1 起点的id
	 @param id2 终点的id
	 @return 删除结果，true删除成功*/
	public boolean removeEdge(int id1, int id2) {
		Node v1 = this._nodeList.get(id1);
		Node v2 = this._nodeList.get(id2);
		return removeEdge(v1, v2);
	}
	
	/**
	 删除图中的边
	 @param id1 起点的id
	 @param id2 终点的id
	 @return 删除结果，true删除成功*/
	public boolean removeEdge(Edge vv) {
		boolean ret = false;
		if(vv != null) {
			Node v1 = vv.getV1();
			Node v2 = vv.getV2();
			ret = removeEdge(v1, v2);
		}
		return ret;
	}
	
	/**
	 清空图*/
	public void clear() {
		if(!this.isEmpty()) {
			/*
			Set<Integer> kSet = this._nodeList.keySet();		//id集合
			Iterator<Integer> iter = kSet.iterator();		//迭代器
			while(iter.hasNext()) {
				this.removeNode(iter.next());
			}
			this._edgeList.clear();
			*/
			Set<Integer> edgeKeySet = this._edgeList.keySet();		//id集合
			Iterator<Integer> iter = edgeKeySet.iterator();		//迭代器
			while(iter.hasNext()) {
				this._edgeList.get(iter.next()).clear();
			}
			this._edgeList.clear();
			this._nodeList.clear();
		}
	}
	
	/**
	 从EdgeList文件加载一个图
	 @param file 文件路径
	 @param isDirected 是否为有向图
	 @param isWeighted 是否为带权图
	 @return 加载成功或失败*/
	public boolean loadGraphFromEdgeListFile(String file, boolean isDirected, boolean isWeighted) {
		boolean ret = false;			//返回值	
		if(this.isEmpty()) {
			try {
	            File fp = new File(file);
	            BufferedReader bufread;
	            String read;
	            String[] numstr = null;
	            int id1 = -1, id2 = -1, weight;
	            Integer iWeight = null;
	            bufread = new BufferedReader(new FileReader(fp));
	            while ((read = bufread.readLine()) != null) {  //读取一行
	            	numstr = read.split(" ");
	            	weight = 0;
	            	id1 = Integer.parseInt(numstr[0]);
	            	id2 = Integer.parseInt(numstr[1]);
	            	if(numstr.length > 3) {
	            		weight = Integer.parseInt(numstr[2]);			//带权图，但是如果没有权值，weight = 0;
	            	}
	            	if(isDirected) {		//带权图
	            		iWeight = weight;
	            	}
	            	else {
	            		iWeight = 1;
	            	}
	            	if(this.getNode(id1) == null) this.addNode(id1); 
	            	if(this.getNode(id2) == null) this.addNode(id2);
	            	if(this.getEdge(id1, id2) == null) this.addEdge(id1, id2, iWeight);
	            	if(isWeighted) {
	            		if(this.getEdge(id2, id1) == null) this.addEdge(id2, id1, iWeight);
	            	} 
	            }  
	            bufread.close();
	            this._isDirected = isDirected;
	            this._isWeighted = isWeighted;
	            ret = true;					//加载成功
	        } catch (FileNotFoundException ex) {  
	            ex.printStackTrace();  
	        } catch (IOException ex) {  
	            ex.printStackTrace();  
	        }
		}
		return ret;
	}
	
	public StringBuffer toMatrixString() {
		StringBuffer str = new StringBuffer();
		int n = this._nodeList.size();
		int i, j;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Set<Integer> nodeKeySet = this._nodeList.keySet();		//id集合
		Iterator<Integer> iter = nodeKeySet.iterator();		//迭代器
		int tempid = -1;									//临时起点v1的id
		while(iter.hasNext()) {
			tempid = iter.next();
			idList.add(tempid);
		}
		HashMap<Integer, Edge> v1Edges = null;
		for(i = -1; i < n; i++) {
			if(i >= 0) v1Edges = this._edgeList.get(idList.get(i));
			for(j = -1; j < n; j++) {
				if(i == -1) {							//列的头（第一行）
					if(j == -1) str.append(' ');
					else str.append(idList.get(j));
					str.append("\t");
				}
				else {
					if(j == -1) str.append(idList.get(i));		//行的头（第一列）
					else {
						boolean flag1 = true;
						if(v1Edges != null) {			//行头的节点存在可达节点
							Edge e = v1Edges.get(idList.get(j));			
							if(e != null) {					//该节点可达
								if(this.isWeighted()) {			//带权图
									str.append(e.weight());
								}
								else {							//不带权图
									str.append(1);
								}
								flag1 = false;
							}
						}
						if(flag1) {							//行不存在或者节点不可达
							if(this.isWeighted()) {				//带权图
								str.append('i');					//无穷
							}
							else {								//不带权图
								str.append('0');
							}
						}
					}
					str.append("\t");
				}
			}
			if(i != n - 1) str.append('\n');
		}
		return str;
	}
}
