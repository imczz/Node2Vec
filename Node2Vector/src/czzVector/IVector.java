package czzVector;

/**
 * 向量接口，维数，长度，范数
 * @author CZZ*/
public interface IVector {

	/**
	 * 复制一个新的向量*/
	public void copy(IVector v);
	
	/**
	 * 新申请一个向量，长度为size*/
	public void resize(int size);
	
	/**
	 * 新申请一个向量，长度为size，每个都初始化为seed*/
	public void resizeLoad(int size, float seed);

	/**
	 * 将向量所有分量变成seed*/
	public void load(float seed);
	
	/**
	 * 返回向量数组*/
	public float[] getVector();
	
	/**
	 * 返回向量的维度*/
	public int getSize();
	
	/**
	 * 返回向量的长度（模，2-范数）*/
	public float getLength();
	
	/**
	 * 向量的数乘*/
	public void multiply(float number);
	
	/**
	 * 数乘（返回一个新的向量）*/
	public IVector new_Multi(float number);
	
	/**
	 * 向量乘法*/
	public float multiply(IVector v2);
	
	/**
	 * 向量对应分量相加*/
	public void add(IVector v2);
	
	/**
	 * 向量对应分量相加（返回一个新的向量）*/
	public IVector new_Add(IVector v2);
	
	/**
	 * 向量之间的欧式距离*/
	public float distance(IVector v);
}
