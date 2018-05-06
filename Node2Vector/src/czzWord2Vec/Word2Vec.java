package czzWord2Vec;

/**
 * word2vec是一种处理一种语言中词语关系的方式，是一种词语的“嵌入”方式（将单点One-hot表示转换为分布式Distributed表示）；
 * 词向量可以被看成是一种副产物，某种深度神经网络的参数，但是词向量有一些独特的性质，比如可以通过余弦判断两个词向量对应的词的距离
 * @author CZZ*/
public class Word2Vec {

	/**
	 模型类型，skip-gram模型，或者CBOW模型*/
	public enum ModelType {Skip_gram, CBOW};
	
	/**
	 分层柔和最大值Hierarchical Softmax，或者负采样Negative Sampling*/
	public enum TrainMethod {HS, NS};
	
	/**
	 词向量维度，也是神经网络隐藏层的神经元个数*/
	private int dimensions;

	/**
	 上下文窗口大小c（windows），在前后都有单词的情况下，最多取得当前词前后各WindowSize个单词，也就是最多2*WindowSize个单词；
	 但是如果从距离来考虑，前后分别选择[1,WindowSize]，这样靠近中心词的词相对来说更容易被选择*/
	private int windowSize;
	
	/**
	 迭代次数*/
	private int iteratorNumber;
	
	/**
	 最低词频，少于这个频率的单词不会被计入词典vocabulary*/
	private int minWordCount;
	
	/**
	 同时运行的线程数*/
	private int threadNumber;
	
	/**
	 设置一个初始化学习率*/
	private int learnRate;
	
	/*================================方法 methods================================*/
	
	public Word2Vec() {

	}
	
	/**
	 获取维度大小*/
	public int getDimensions() {
		return dimensions;
	}

	/**
	 获取窗口大小*/
	public int getWindowSize() {
		return windowSize;
	}

	public int getIteratorNumber() {
		return iteratorNumber;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public int getMinWordCount() {
		return minWordCount;
	}

	public int getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(int learnRate) {
		this.learnRate = learnRate;
	}
}
