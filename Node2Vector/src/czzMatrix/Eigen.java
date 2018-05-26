package czzMatrix;

/**
 * 保存特征值与特征向量的结构，用来函数返回
 * @author CZZ*/
public class Eigen {

	/**
	 * 特征值，长度为l，l<n*/
	public float[] eigenvalues;
	
	/**
	 * 特征向量矩阵，每列是矩阵对应特征值的特征向量*/
	public Matrix eigenvectors;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法*/
	public Eigen() {
		this.eigenvalues = null;
		this.eigenvectors = null;
	}
}
