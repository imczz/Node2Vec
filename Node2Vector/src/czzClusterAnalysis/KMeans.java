package czzClusterAnalysis;

import java.util.ArrayList;

import czzSelectItem.Selector;
import czzVector.IVector;

/**
 * k均值聚类算法，继承自抽象类“聚类”*/
public class KMeans<T> extends Cluster<T>{

	@Override
	public boolean runCluster(int k) {
		boolean ret = false;
		int n = this.nodes.size();
		if(n > 0 && k > 0 && k < n) {
			int[] seeds = Selector.kInN(n, k);							//在0——n-1这n个编号中选择k个
			int[] centerNum = new int[k];								//每个聚类中心有几个元素
			int i, index, j;
			ClusterNode<T> temp;
			ArrayList<ClusterNode<T> > arr = new ArrayList<ClusterNode<T> >();			//质心
			float minDistance, tempDistance;										//最小距离
			int minIndex;
			for(i = 0; i < k; i++) {
				index = seeds[i];
				this.nodes.get(index).label = i + 1;					//选择k个中心节点作为初始
				temp = this.nodes.get(i);
				this.nodes.set(i, this.nodes.get(index));
				this.nodes.set(index, temp);
				temp = new ClusterNode<T>(null, this.nodes.get(i)._vector);			//初始化k个聚类质心
				temp.label = i + 1;
				arr.add(temp);
				centerNum[i] = 1;					//中心有一个元素
			}
			//arr.trimToSize();
			IVector vec;
			for(i = k; i < n; i++) {
				temp = this.nodes.get(i);
				minDistance = temp._vector.distance(arr.get(0)._vector);
				minIndex = 0;
				for(j = 1; j < arr.size(); j++) {
					tempDistance = temp._vector.distance(arr.get(j)._vector);
					if(minDistance > tempDistance) {
						minDistance = tempDistance;
						minIndex = j;
					}
				}
				temp.label = arr.get(minIndex).label;
				vec = arr.get(minIndex)._vector;					//质心
				vec.multiply(centerNum[minIndex]);
				vec.add(temp._vector);
				centerNum[minIndex]++;
				vec.multiply(1.0f / centerNum[minIndex]);			//计算新的质心
			}
			ret = true;
		}
		return ret;
	}

}
