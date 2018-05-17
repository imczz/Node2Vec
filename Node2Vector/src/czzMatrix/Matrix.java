package czzMatrix;

import java.util.LinkedList;

public class Matrix {
	
	/**
	 * 矩阵二维数组*/
	private float [][] matrix;
	
	/**
	 * 行数*/
	private int row;
	
	/**
	 * 列数*/
	private int column;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法*/
	public Matrix(){
		this.row = 0;
		this.column = 0;
		this.matrix = null;
	}
	
	/**
	 * 无初始化数值构造方法
	 * @param row 行数
	 * @param column 列数*/
	public Matrix(int row, int column){
		this.row = row;
		this.column = column;
		this.matrix = new float[row][];
		for(int i = 0; i < row; i++) {
			this.matrix[i] = new float[column];
		}
	}
	
	/**
	 * 构造方法
	 * @param row 行数
	 * @param column 列数
	 * @param num 初始化数值*/
	public Matrix(int row, int column, float num){
		this.row = row;
		this.column = column;
		this.matrix = new float[row][];
		for(int i = 0; i < row; i++) {
			this.matrix[i] = new float[column];
			for(int j = 0; j < column; j++) {
				this.matrix[i][j] = num;
			}
		}
	}
	
	/**
	 * 根据二维数组装载此矩阵，矩阵未初始化或者相同形状可以装载
	 * @param mat 二维数组*/
	public boolean load(float[][] mat) {
		boolean ret = false;
		int r = mat.length, c;
		if(r > 0) {
			c = mat[0].length;
			if(c > 0) {
				if(this.matrix == null) {					//矩阵未初始化
					this.row = r;
					this.column = c;
					this.matrix = new float[this.row][];
					for(int i = 0; i < this.row; i++) {
						this.matrix[i] = new float[this.column];
					}
				}
				if(this.row == r && this.column == c) {			//矩阵同形
					for(int i = 0; i < this.row; i++) {
						for(int j = 0; j < this.column; j++) {
							this.matrix[i][j] = mat[i][j];
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * @return 矩阵行数*/
	public int getRow() {
		return this.row;
	}
	
	/**
	 * @return 矩阵列数*/
	public int getColumn() {
		return this.column;
	}
	
	/**
	 * @return 矩阵*/
	public float[][] getMatrix() {
		return this.matrix;
	}
	
	/**
	 * 获得矩阵中的值,行列从0开始
	 * @param row 第row行 0 1 2
	 * @param column 第column列 0 1 2
	 * @return 矩阵相应位置的值，或者表示不存在的null*/
	public Float get(int row, int column) {
		Float ret = null; 
		if(row >= 0 && column >=0 && row < this.row && column < this.column) {
			ret = matrix[row][column];
		}
		return ret;
	}
	
	/**
	 * 设置矩阵中的值,行列从0开始
	 * @param row 第row行 0 1 2
	 * @param column 第column列 0 1 2
	 * @param f 设置的数*/
	public void set(int row, int column, float f) {
		if(row >= 0 && column >=0 && row < this.row && column < this.column) {
			matrix[row][column] = f;
		}
	}
	
	/**
	 * 矩阵乘法a * b
	 * @param a 矩阵a
	 * @param b 矩阵b
	 * @return 矩阵a * b*/
	public static Matrix multiply(Matrix a, Matrix b) {
		int ar, ac, br, bc;
		ar = a.getRow();
		ac = a.getColumn();
		br = b.getRow();
		bc = b.getColumn();
		int i, j, k;
		Matrix ret = null;
		if(ar > 0 && ac == br && br > 0 && bc > 0) {
			ret = new Matrix(ar, bc, 0);
			for(i = 0; i < ar; i++) {
				for(j = 0; j < bc; j++) {
					ret.getMatrix()[i][j] = 0;
					for(k = 0; k < ac; k++) {
						ret.getMatrix()[i][j] += a.get(i, k) * b.get(k, j);
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 矩阵乘法，this * a
	 * @return 结果矩阵*/
	public Matrix multiply(Matrix m) {
		return Matrix.multiply(this, m);
	}
	
	/**
	 * 矩阵数乘，每个位置乘f
	 * @param f 一个浮点数*/
	public void multiply(float f) {
		for(int i = 0; i < this.row; i++) {
			for(int j = 0; j < this.column; j++) {
				matrix[i][j] *= f;
			}
		}
	}
	
	/**
	 * 获取当前矩阵的转置矩阵
	 * @return 当前矩阵的转置矩阵*/
	public Matrix transposition() {
		Matrix ret = null;
		if(this.row > 0 && this.column > 0) {
			ret = new Matrix(this.column, this.row);
			for(int i = 0; i < this.row; i++) {
				for(int j = 0; j < this.column; j++) {
					ret.set(j, i, this.get(i, j));
				}
			}
		}
		return ret;
	}
	
	/**
	 * 此矩阵加上与此矩阵同形的矩阵m
	 * @param m 与此矩阵同形矩阵m*/
	public void add(Matrix m) {
		if(this.row == m.getRow() && this.column == m.getColumn()) {
			for(int i = 0; i < this.row; i++) {
				for(int j = 0; j < this.column; j++) {
					this.matrix[i][j] += m.get(i, j);
				}
			}
		}
	}
	
	/**
	 * 将矩阵m复制r * c次，新矩阵相当于r行c列个m*/
	public Matrix repeat(Matrix m, int row, int column) {
		Matrix ret = null;
		int mr = m.getRow();
		int mc = m.getColumn();
		if(mr > 0 && mc > 0 && row > 0 && column > 0) {
			ret = new Matrix(row * mr, column * mc);
			for(int a = 0; a < mr; a++) {
				for(int b = 0; b < mc; b++) {
					for(int c = 0; c < row; c++) {
						for(int d = 0; d < column; d++) {
							ret.set(c * mc + a, d * mr + b, m.get(a, b));
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 矩阵变为负矩阵*/
	public void negative() {
		for(int i = 0; i < this.row; i++) {
			for(int j = 0; j < this.column; j++) {
				this.matrix[i][j] = -this.matrix[i][j];
			}
		}
	}
	
	/**
	 * 矩阵的列的平均数
	 * @return 1行c列矩阵，c个分量的向量，每个分量是一列的平均数*/
	public Matrix means() {
		Matrix ret = null;
		if(this.row > 0 && this.column > 0) {
			ret = new Matrix(1, this.column, 0);
			for(int i = 0; i < this.row; i++) {
				for(int j= 0; j < this.column; j++) {
					ret.matrix[1][j] += this.matrix[i][j];
				}
			}
			ret.multiply(1.0f / this.row);
		}
		return ret;
	}
	
	/**矩阵的子矩阵
	 * @param r0 行起始（包含）
	 * @param r1 行结束（包含）
	 * @param c0 列起始（包含）
	 * @param c1 列结束（包含）
	 * @return 行r0到r1，列c0到c1的，子矩阵
	 * */
	public Matrix subMatrix(int r0, int r1, int c0, int c1) {
		Matrix ret = null;
		if(r0 <= r1 && r1 < this.row && c0 <= c1 && c1 < this.column) {
			ret = new Matrix(r1 - r0 + 1, c1 - c0 + 1);
			for(int i = r0; i <= r1; i++) {
				for(int j= c0; j <= c1; j++) {
					ret.matrix[i][j] += this.matrix[r0 + i][c0 + j];
				}
			}
		}
		return ret;
	}
	
	/**
	 * n-1阶的去掉i行j列的矩阵（代数余子式）
	 * @param i 准备去掉的第i行
	 * @param j 准备去掉的第j列
	 * @return n-1阶的子矩阵*/
	public Matrix subIJ(int i, int j) {
		Matrix ret = null;
		if(i >= 0 && i < this.row && j >= 0 && j < this.column) {
			ret = new Matrix(this.row - 1, this.column - 1);
			int r, c;
			for(int a = 0; a < this.row; a++) {
				if(a == i) continue;
				r = a;
				if(a > i) r--; 
				for(int b = 0; b < this.column; b++) {
					if(b == j) continue;
					c = b;
					if(b > j) c--;
					ret.matrix[r][c] += this.matrix[a][b];
				}
			}
		}
		return ret;
	}
	
	/**
	 * 方阵的行列式*/
	public Float determinant() {
		Float ret =  null;
		if(this.row == this.column && this.row > 0) {
			LinkedList<Matrix> matrixQueue = new LinkedList<Matrix>();				//待处理的矩阵队列，用队列代替递归
			LinkedList<Float> coefficientQueue = new LinkedList<Float>();				//系数队列
			Matrix mat;
			int n;
			matrixQueue.add(this);
			coefficientQueue.add(1f);
			ret = 0f;
			while(matrixQueue.size() > 0) {
				mat = matrixQueue.peek();			//当前栈顶元素
				n = mat.getRow();
				if(n == 1) {
					ret += coefficientQueue.poll() * mat.get(0, 0);
				}
				else if(n == 2) {
					ret += coefficientQueue.poll() * (mat.get(0, 0) * mat.get(1, 1) - mat.get(0, 1) * mat.get(1, 0));
				}
				else if(n > 2) {
					int symbol = 1;
					for(int i = 0; i < n; i++) {
						matrixQueue.addLast(mat.subIJ(0, i));				//按第一行展开
						coefficientQueue.addLast(symbol * coefficientQueue.peek() * mat.get(0, i));
						symbol = -symbol;
					}
					coefficientQueue.poll();
				}
				matrixQueue.poll();
			}
		}
		return ret;
	}
	
	/**
	 * 方阵的伴随矩阵*/
	public Matrix adjoint() {
		//TODO
		return null;
	}
	
	/**
	 * 方阵的逆矩阵*/
	public Matrix inverse() {
		//TODO
		return null;
	}
	
	/**
	 * 协方差矩阵*/
	public static Matrix cov(Matrix m) {
		//TODO
		return null;
	}
}
