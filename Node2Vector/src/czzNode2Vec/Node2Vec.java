package czzNode2Vec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
node2vec是图表上的表示学习算法框架。给定任何图，它可以学习节点的连续特征表示，然后可以用于各种下游机器学习任务。
@author CZZ*/
public class Node2Vec {
	
	/**
	 图（接口），处理的核心*/
	private IGraph _G;
	
	/**
	 超参数p，控制遍历的返回前一个节点的概率。Return hyperparameter.*/
	private float _p;
	
	/**
	 超参数q，控制节点继续向前行的概率。Inout hyperparameter.*/
	private float _q;
	
	/**
	 每条路线上节点的数量。Length of walk per source.*/
	private int _walk_length;
	
	/**
	 路径的条数。Number of walks per source.*/
	private int _num_walks;
	
	/**
	 存储节点到邻居的可能性序列的结构<节点id，下一步选择器>*/
	private HashMap<Integer, NextWalkSelector> _alias_nodes;
	
	/**
	 存储边的可能性序列的结构<上一个节点id,此时的节点id,下一步选择器>*/
	private HashMap<Integer, HashMap<Integer, NextWalkSelector>> _alias_edges;
	
	/*================================方法 methods================================*/
	
	/**
	 从现存的图中构造*/
	public Node2Vec(IGraph _G) {
		this._G = _G;
		this._p = 1;
		this._q = 1;
		this._walk_length = 80;
		this._num_walks = 10;
		this._alias_nodes = new HashMap<Integer, NextWalkSelector>();
		this._alias_edges = new HashMap<Integer, HashMap<Integer, NextWalkSelector>>();
	}
	
	/**
	 获取超参数:p*/
	public float getP() {
		return this._p;
	}
	
	/**
	 改变超参数:p*/
	public void setP(float p) {
		this._p = p;
	}
	
	/**
	获取超参数:q*/
	public float getQ() {
		return this._q;
	}
	
	/**
	 改变超参数:q*/
	public void setQ(float q) {
		this._q = q;
	}
	
	/**
	 获取设置的每条路径的步数*/
	public int getWalk_length() {
		return _walk_length;
	}

	/**
	 设置每条路径的步数*/
	public void setWalk_length(int _walk_length) {
		this._walk_length = _walk_length;
	}

	/**
	 获取设置的路径条数*/
	public int getNum_walks() {
		return _num_walks;
	}

	/**
	 设置路径条数*/
	public void setNum_walks(int _num_walks) {
		this._num_walks = _num_walks;
	}
	
	/**
	 同时设置各个参数*/
	public void setParams(float p, float q, int _walk_length, int _num_walks) {
		this.setP(p);
		this.setQ(q);
		this.setWalk_length(_walk_length);
		this.setNum_walks(_num_walks);
	}
	
	/**
	 获得当前节点的某一个邻居的id（起点没有上一步，不需要参数p和q）
	 @param id 起点的id
	 @return 随机选择的邻居的id*/
	public int getNextStep(int id) {
		NextWalkSelector nws = this._alias_nodes.get(id);
		if(nws == null) {
			Integer[] neighbors = this._G.neighbors(id);			//获取所有邻居
			int n = neighbors.length;
			float[] probabilitys = new float[n];
			for(int i = 0; i < n; i++) {
				probabilitys[i] = this._G.getEdgeWeight(id, neighbors[i]);
			}
			nws = new NextWalkSelector(1, neighbors, probabilitys, false);
			this._alias_nodes.put(id, nws);
		}
		return(nws.getItemRandomly());
	}
	
	/**
	 前两个节点是src到dst，获得dst当前节点的某一个邻居节点
	 @param src source，倒数第二步
	 @param destination，倒数第一步 
	 @return 随机选择的dst的邻居的id*/
	public Integer getNextStep(int src, int dst) {
		float p = this._p;
		float q = this._q;
		HashMap<Integer, NextWalkSelector> srcHashMap = this._alias_edges.get(src);
		if(srcHashMap == null) {
			srcHashMap = new HashMap<Integer, NextWalkSelector>();
			this._alias_edges.put(src, srcHashMap);
		}
		NextWalkSelector nws = srcHashMap.get(dst);
		if(nws == null) {
			Integer[] dst_neighbors = this._G.neighbors(dst);			//获取所有邻居
			int n = dst_neighbors.length;
			float[] probabilitys = new float[n];
			for(int i = 0; i < n; i++) {
				if(dst_neighbors[i] == src) {
					probabilitys[i] = this._G.getEdgeWeight(dst, dst_neighbors[i]) / p;
				}
				else if(this._G.hasEdge(dst_neighbors[i], src)) {
					probabilitys[i] = this._G.getEdgeWeight(dst, dst_neighbors[i]);
				}	
				else{
					probabilitys[i] = this._G.getEdgeWeight(dst, dst_neighbors[i]) / q;
				}
			}
			nws = new NextWalkSelector(1, dst_neighbors, probabilitys, false);
			srcHashMap.put(dst, nws);
		}
		return(nws.getItemRandomly());
	}
	
	/**
	 单次N2V遍历。Simulate a random walk starting from start node.
	 @param start_node 开始节点
	 @return 从start_node开始的，长度为walk_length的遍历序列*/
	public Integer[] node2vec_walk(Integer start_node) {
		int walk_length = this._walk_length;
		ArrayList<Integer> walk = new ArrayList<Integer>();
		walk.add(start_node);
		while(walk.size() < walk_length) {
			Integer cur = walk.get(walk.size() - 1);					//当前的点dst
			Integer[] cur_nbrs = this._G.neighbors(cur);
			if(cur_nbrs.length > 0) {							//可以继续走下去
				if(walk.size() == 1) {
					Integer next = this.getNextStep(cur);
					walk.add(next);
				}
				else{
					Integer prev = walk.get(walk.size() - 2);			//前一个节点src
					Integer next = this.getNextStep(prev, cur);
					walk.add(next);
				}
			}
			else{
				break;
			}
		}
		Integer[] ret = new Integer[walk.size()];
		return walk.toArray(ret);
	}
	
	/**
	 重复随机遍历。Repeatedly simulate random walks from each node.*/
	public Integer[][] simulate_walks() {
		ArrayList<Integer[]> walks = new ArrayList<Integer[]>();
		Integer[] nodes = this._G.nodesArray();
		System.out.print("路径迭代");		//Walk iteration
		for(int i = 0; i < this._num_walks; i++) {
			System.out.println((i+1) +  "/" + this._num_walks);			//当前编号/总数
			List<Integer> nodesArray = new ArrayList<Integer>();		
			nodesArray = Arrays.asList(nodes);						//数组转化为列表
			Collections.shuffle(nodesArray);						//打乱列表
			nodes = nodesArray.toArray(nodes);						//列表转化为数组
			for(int j = 0; j < nodes.length; j++) {
				walks.add(this.node2vec_walk(nodes[j]));
			}	
		}
		Integer[][] ret = new Integer[walks.size()][];
		ret = walks.toArray(ret);
		return ret;
	}
}
