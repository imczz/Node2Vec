package czzSelectItem;

import java.util.Random;

/**
 选择器，有一个数值不同的项目序列，按照数值占总数的比例随机选择一个项目
 @author CZZ*/
public class Selector {
	
	/**
	 从项目权值列表中选择某个项目编号。把权值加在一起合并成一个桶，向桶里面随机丢一个点，看这个点落在通的那个组成部分中*/
	public static int select(float[] itemWeights) {
		int ret = -1;
		int n = itemWeights.length;
		float sum = 0;
		for(int i = 0; i <n ; i++) {
			sum += itemWeights[i];
		}
		float result = 0;			//随机结果
		if(sum != 0) {					//只要有数值就可以比较大小，不能恰好是0
			Random random = new Random();
			result = random.nextFloat() * sum;			//[0, 1) * sum
			float tempSum = 0;			//临时和
			for(ret = 0; ret < n ; ret++) {
				tempSum += itemWeights[ret];
				if(tempSum > result) break;				//因为nextFloat是左闭右开区间，所以不取等号
			}
		}
		return ret;
	}
}
