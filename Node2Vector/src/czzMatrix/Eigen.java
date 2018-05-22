package czzMatrix;

/**
 * 保存特征值与特征向量的结构，用来函数返回
 * @author CZZ*/
public class Eigen {

	/**
	 * 特征值*/
	public float eigenvalue;
	
	/**
	 * 特征向量*/
	public Matrix eigenvector;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法*/
	public Eigen() {
		this.eigenvalue = 0;
		this.eigenvector = null;
	}
	
	/**
	 * 构造方法*/
	public Eigen(float eigenvalue, Matrix eigenvector) {
		this.eigenvalue = 0;
		this.eigenvector = null;
	}
}
