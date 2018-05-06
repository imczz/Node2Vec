package czzNode2Vec;

/**
 * 图接口，Node2Vec类使用这个接口，其他开发者可以编写自己的类继承IGraph接口
 * @author CZZ*/
public interface IGraph {
	
	/**
	 从文件装载图*/
	public boolean loadGraphFromFile(String name, String fileType, boolean isDirected, boolean isWeighted);
	
	/**
	 添加id为“id”的节点*/
	public boolean addNode(int id);
	
	/**
	 移除id为“id”的节点*/
	public boolean removeNode(int id);
	
	/**
	 是否具有某个id为“id”的节点*/
	public boolean hasNode(int id);
	
	/**
	 添加一条从id1节点到id2节点的权值为weight的边*/
	public boolean addEdge(int id1, int id2, Integer weight);
	
	/**
	 移除从id1节点到id2节点的边，若为无向图则同时删除边id2-id1*/
	public boolean removeEdge(int id1, int id2);
	
	/**
	 是否具有从id1到id2的边*/
	public boolean hasEdge(int id1, int id2);
	
	/**
	 获取边的权值*/
	public Integer getEdgeWeight(int id1, int id2);
	
	/**
	 修改边的权值*/
	public void setEdgeWeight(int id1, int id2, Integer weight);
	
	/**
	 判断某个节点（id）可以到达的邻居*/
	public Integer[] neighbors(int id);
	
	/**
	 返回图的节点数组*/
	public Integer[] nodesArray();
	
	/**
	 清空这个图*/
	public void clear();
}
