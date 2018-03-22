

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import czzGraph.Edge;
import czzGraph.Graph;
import czzNode2Vec.IGraph;

/**
 GraphForNodeToVec.专门为Node2Vec编写的图，用来进行图（Graph）模块与N2V模块的适配。实现接口IGraph，
 @author CZZ*/
public class Graph4N2V<T> /*extends Graph */implements IGraph{
	
	private Graph<T> _G;
	
	/**
	 构造函数*/
	public Graph4N2V(){
		this._G = new Graph<T>(false, false);		//默认为无向无权图
	}

	/**
	 增加从Graph构造本身的方法*/
	public Graph4N2V(Graph<T> G, boolean isDirected, boolean isWeighted){
		this._G = G;
		if(isDirected) G.toDirected();
		else {
			if(G.isDirected()) G.toUndirected(true);			//有向图转化为无向图，去掉单向边的方向（补充反向边）
		}
		if(isWeighted) G.toWeighted();
	}
	
	/**
	 从文件装载图
	 @param file 文件路径
	 @param fileType 文件类型（拓展名）
	 @param isDirected 有向图
	 @param isWeighted 带权图
	 @return 装载结果， true：装载成功,false：装载失败*/
	@Override
	public boolean loadGraphFromFile(String file, String fileType, boolean isDirected, boolean isWeighted) {
		boolean ret = false;
		if(fileType.equals("edgelist")) {			//.edgelist文件
			ret = this._G.loadGraphFromEdgeListFile(file, isDirected, isWeighted);
		}
		return ret;
	}
	
	/**
	 添加id为“id”的节点
	 @param id 待添加的节点的id
	 @return true：添加成功*/
	@Override
	public boolean addNode(int id) {
		return this._G.addNode(id);
	}
	
	/**
	 移除id为“id”的节点
	 @param id 待移除的节点的id
	 @return true：移除成功*/
	@Override
	public boolean removeNode(int id) {
		boolean ret = false;
		if(this._G.removeNode(id) >= 0) ret = true;
		return ret;
	}
	
	/**
	 是否具有某个id为“id”的节点
	 @param id 待检验的节点的id
	 @return true：图包含id为id的节点*/
	@Override
	public boolean hasNode(int id) {
		boolean ret = false;
		if(this._G.getNode(id) != null) ret = true;
		return ret;
	}
	
	/**
	 添加一条从id1节点到id2节点的权值为weight的边
	 @param id1 起始节点id
	 @param id2 到达的节点的id
	 @param weight 边的权值
	 @return true：添加操作成功执行，false：边已经存在或者其他原因*/
	@Override
	public boolean addEdge(int id1, int id2, Integer weight) {
		return this._G.addEdge(id1, id2, weight);
	}
	
	/**
	 移除从id1节点到id2节点的边，若为无向图则同时删除边id2-id1
	 @param id1 起始节点
	 @param id2 到达的节点
	 @return true:删除操作成功执行，false：边不存在或者其他失败原因*/
	@Override
	public boolean removeEdge(int id1, int id2) {
		return this._G.removeEdge(id1, id2);
	}
	
	/**
	 是否具有从id1到id2的边
	 @param id1 起始节点id
	 @param id2 到达的节点的id
	 @return true:图中存在从id1节点到达id2节点的路径*/
	@Override
	public boolean hasEdge(int id1, int id2) {
		boolean ret = false;
		if(this._G.getEdge(id1, id2) != null) ret = true;
		return ret;
	}
	
	/**
	 获取边的权值
	 @param id1 起始节点id
	 @param id2 到达的节点的id
	 @return 如果边存在则返回边的权值，若返回null则边不存在*/
	@Override
	public Integer getEdgeWeight(int id1, int id2) {
		Integer ret = null;
		Edge<T> edge = this._G.getEdge(id1, id2);
		if(edge != null) ret = edge.weight();
		return ret;
	}
	
	/**
	 修改边的权值（无向图会修改双向权值。边存在才能修改，否则请调用addEdge方法）
	 @param id1 起始节点id
	 @param id2 到达的节点的id
	 @param weight 边的权值
	 @return 无返回值，请提前检查边是否存在*/
	@Override
	public void setEdgeWeight(int id1, int id2, Integer weight) {
		this._G.setEdgeWeight(id1, id2, weight);
	}
	
	/**
	 判断某个节点（id）可以到达的邻居
	 @param id 某个节点的id
	 @return id为id的节点的邻居数组*/
	@Override
	public Integer[] neighbors(int id) {
		czzGraph.Node<T> node = this._G.getNode(id);
		int n = node.getOut_Degree();
		ArrayList<Integer> nodelist = new ArrayList<Integer>();
		Iterator<Integer> iter = node.getOutEdgeList().keySet().iterator();
		while (iter.hasNext()) {
			nodelist.add(iter.next());
		}
		//nodelist.sort();
		Integer[] ret = new Integer[n];
		return nodelist.toArray(ret);
	}
	
	/**
	 返回图的节点数组
	 @return 图中所有节点的id构成的数组*/
	public Integer[] nodesArray() {
		List<Integer> nodesList = this._G.getNodesIdList();
		Integer[] ret = new Integer[nodesList.size()];
		return nodesList.toArray(ret);
	}
	
	/**
	 清空这个图*/
	@Override
	public void clear() {
		this._G.clear();
	}
}
