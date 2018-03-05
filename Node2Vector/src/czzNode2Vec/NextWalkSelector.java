package czzNode2Vec;

import java.util.Random;

import czzSelectItem.ProbabilityPair;
import czzSelectItem.Selector;

/**
 Node2Vec遍历，下一步选择器
 @author CZZ*/
public class NextWalkSelector {
	
	/**
	 选择的选择算法*/
	private int _algorithm;
	
	/**
	 规范化（加和为1）的概率*/
	private boolean _normalized;
	
	/**
	 邻居的id*/
	private Integer[] _neighbors;
	
	/**
	 对应邻居节点的概率*/
	private float[] _probabilities;
	
	/**
	 概率求和*/
	private float _sum;

	/**
	 alias方法需要的J和q*/
	private ProbabilityPair[] jq;

	/*================================方法 methods================================*/
	
	/**
	 构造方法
	 @param algorithm 1:普通选择，2：alias选择
	 @param neighbors 邻居节点id数组
	 @param probability 节点id对应的概率数组
	 @param normalized 是否将概率数组归一化*/
	public NextWalkSelector(int algorithm, Integer[] neighbors, float[] probabilities, boolean normalized){
		this._algorithm = algorithm;
		this._neighbors = neighbors;
		this._probabilities = probabilities;
		this._normalized = normalized;
		this._sum = getProbSum();					//未求和
		normalized();
		if(algorithm == 1) {
			if(!this._normalized) {
				this._normalized = true;
				normalized();
			}
			jq = Selector.alias_setup(this._probabilities);
		}
	}
	
	/**
	 返回概率序列的和*/
	private float getProbSum() {
		float sum = 0;
		if(this._probabilities != null) {
			int n = this._probabilities.length;
			for(int i = 0; i < n; i++) {
				sum += this._probabilities[i];
			}
		}
		else {
			sum = -1;
		}
		return sum;
	}
	
	/**
	 把概率序列归一化，之后就可以调用alias_setuo方法*/
	private void normalized() {
		if(this._normalized && this._sum > 0 && this._sum != 1) {			//需要并且可以归一化
			int n = this._probabilities.length;
			for(int i = 0; i < n; i++) {
				this._probabilities[i] /= this._sum;
			}
			this._sum = 1;
		}
	}
	
	/**
	 把概率序列求和拼成一个桶，向这个桶中随机丢一个点，查看这个点位于哪个部分，单次实现可见czzSelectItem包Selector类select方法
	 @return 下一步的id*/
	private int czzBucketSelect() {
		int ret = -1;
		int n = this._probabilities.length;
		float result = 0;			//随机结果
		float sum = this._sum;
		if(sum > 0) {					//只要有数值就可以比较大小，不能恰好是0
			Random random = new Random();
			result = random.nextFloat() * sum;			//[0, 1) * sum
			float tempSum = 0;			//临时和
			for(ret = 0; ret < n ; ret++) {
				tempSum += this._probabilities[ret];
				if(tempSum > result) break;				//因为nextFloat是左闭右开区间，所以不取等号
			}
		}
		return this._neighbors[ret];					//返回索引对应的节点id
	}
	
	/**
	 alias选择
	 @return 下一步的id*/
	private int aliasSelect() {
		int ret = Selector.alias_draw(jq);
		return this._neighbors[ret];
	}
	
	/**
	 根据用户选择的算法，选择下一步的节点
	 @return 下一步的id*/
	public int getItemRandomly() {
		int ret;
		if(this._algorithm == 1) ret =  aliasSelect();
		else ret =  czzBucketSelect();
		return ret;
	}
}
