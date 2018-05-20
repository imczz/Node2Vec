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
				ret = true;
			}
		}
		return ret;
	}
	
	/**
	 * 复制一个m矩阵
	 * @param m 被复制的矩阵*/
	public boolean copy(Matrix m) {
		boolean ret = false;
		if(m.getRow() > 0 && m.getColumn() > 0) {
			this.load(m.getMatrix());
			ret = true;
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
	public static Matrix multiply(Matrix m, float f) {
		Matrix ret = null;
		if(m.row > 0 && m.column > 0) {
			ret = new Matrix(m.row, m.column);
			for(int i = 0; i < m.row; i++) {
				for(int j = 0; j < m.column; j++) {
					ret.set(i, j, m.get(i, j) * f);
				}
			}
		}
		return ret;
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
	 * 将矩阵m复制r * c次，新矩阵相当于r行c列个m
	 * @param m 被复制矩阵
	 * @param rowTimes 行复制次数
	 * @param columnTimes 列复制次数
	 * @return 复制结果*/
	public static Matrix repeat(Matrix m, int rowTimes, int columnTimes) {
		Matrix ret = null;
		int mr = m.getRow();
		int mc = m.getColumn();
		if(mr > 0 && mc > 0 && rowTimes > 0 && columnTimes > 0) {
			ret = new Matrix(rowTimes * mr, columnTimes * mc);
			for(int a = 0; a < mr; a++) {
				for(int b = 0; b < mc; b++) {
					for(int c = 0; c < rowTimes; c++) {
						for(int d = 0; d < columnTimes; d++) {
							ret.set(c * mr + a, d * mc + b, m.get(a, b));
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 将此矩阵复制r * c次，新矩阵相当于r行c列个此矩阵
	 * @param rowTimes 行复制次数
	 * @param columnTimes 列复制次数
	 * @return 复制结果*/
	public Matrix repeat(int rowTimes, int columnTimes) {
		return Matrix.repeat(this, rowTimes, columnTimes);
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
	public Matrix mean() {
		Matrix ret = null;
		if(this.row > 0 && this.column > 0) {
			ret = new Matrix(1, this.column, 0);
			for(int i = 0; i < this.row; i++) {
				for(int j= 0; j < this.column; j++) {
					ret.matrix[0][j] += this.matrix[i][j];
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
		Matrix ret = null;
		if(this.row == this.column && this.row > 0) {
			ret = new Matrix(this.row, this.column);
			int symbol;
			for(int i = 0; i < this.row; i++) {
				if(i % 2 == 0) symbol = 1;
				else symbol = -1;
				for(int j= 0; j < this.column; j++) {
					ret.matrix[j][i] = symbol * this.subIJ(i, j).determinant();
					symbol = -symbol;
				}
			}
		}
		return ret;
	}
	
	/**
	 * 伴随矩阵除行列式求逆矩阵
	 * @return 此矩阵的逆矩阵*/
	private Matrix defineInv() {
		Matrix ret = null;
		if(this.row == this.column && this.row > 0) {
			float det = this.determinant();
			if(det != 0) {						//矩阵可逆
				ret = this.adjoint();
				ret.multiply(1 / this.determinant());
			}
			else ret = null;
		}
		return ret;
	}
	
	/**
	 * 方阵的逆矩阵
	 * @return 此矩阵的逆矩阵*/
	public Matrix inverse() {
		return defineInv();
	}
	
	/**
	 * 协方差矩阵
	 * @param m 矩阵
	 * @return m矩阵的协方差矩阵*/
	public static Matrix cov(Matrix m) {
		int i, j, row, column;
		Matrix ret = null;
		row = m.getRow();
		column = m.getColumn();
		if(row > 0 && column > 0) {
			Matrix mean = m.mean();											//每列均值
			Matrix s = new Matrix(row, column);
			for(i = 0; i < row; i++) {
				for(j = 0; j < column; j++) {
					s.set(i, j, m.get(i, j) - mean.get(0, j));					//对应减去每列的均值
				}
			}
			ret = new Matrix(column, column, 0);
			for(i = 0; i < column; i++) {
				for(j = 0; j < column; j++) {
					for(int k = 0; k < row; k++) {
						ret.getMatrix()[i][j] += s.get(k, i) * s.get(k, j);		//乘转置
					}
					ret.getMatrix()[i][j] /= row - 1;							//除n-1
				}
			}
		}
		return ret;
	}
	
	/**
	 * 协方差矩阵
	 * @return 此矩阵的协方差矩阵*/
	public Matrix cov() {
		return Matrix.cov(this);
	}
	
	/**
	 * @return 矩阵存在且为方阵*/
	public boolean isSquare() {
		boolean ret = (this.row == this.column && this.row > 0);
		return ret;
	}
	
	/**
	 * QR分解，Q为正交矩阵，R为非奇异上三角矩阵，A=QR
	 * @return 数组0为Q，1为R*/
	public static Matrix[] QR(Matrix A) {			//Gram–Schmidt正交化方式
		Matrix[] ret = null;
		if(A.isSquare()) {	//QR分解的A并不一定是方阵，也可能是m行n列，此时Q为m*n,R为n*n
			ret = new Matrix[2];
			//Matrix Q = new Matrix(A.getRow(), A.getColumn());				//Q
			//Matrix R = new Matrix(A.getColumn(), A.getColumn());			//R
			Matrix B = new Matrix();										//beta
			Matrix T = new Matrix(A.getColumn(), A.getColumn(), 0);			//T			Q=AT
			B.load(A.getMatrix());							//b1 = a1;
			float numerator, denominator;
			float[] l2 = new float[A.getColumn()];			//beta的模、二范数
			int i, j, k;
			for(i = 0; i < A.getColumn(); i++) {			//先循环列
				l2[i] = 0;
				for(j = 0; j <= i; j++) {					//行
					if(i == j) T.set(i, j, 1);			//对角线
					else if(i < j) continue;			//下三角，不会执行此分支
					else if(i > j) {					//上三角
						numerator = 0;
						denominator = 0;
						for(k = 0; k < A.getColumn(); k++) {
							numerator += A.get(k, i) * B.get(k, j);					//alpha(i)*beta(j)
							denominator += B.get(k, j) * B.get(k, j);				//beta(j)*beta(j)
						}
						T.set(j, i, -numerator / denominator);					//分量前的参数
						for(k = 0; k < A.getRow(); k++) {
							B.getMatrix()[k][i] += T.get(j, i) * B.get(k, j);	//beta(i) = alpha(i) - sum(k=1,j-1)((alpha(i)beta(k)/beta(k)beta(k))beta(k))
						}
					}
				}
				for(k = 0; k < A.getColumn(); k++) {
					l2[i] += Math.pow(B.get(k, i), 2);
				}
				l2[i] = (float) Math.sqrt(l2[i]);
			}
			int symbol = -1;
			for(i = 0; i < A.getColumn(); i++) {					//每一列都是一个向量
				for(j = 0; j < A.getRow(); j++) {					//向量的分量
					B.getMatrix()[j][i] /= (symbol * l2[i]);				//单位化
					if(i <= j) T.getMatrix()[j][i] /=l2[i];		//对应的施密特正交化参数
				}
				symbol = -symbol;
			}
			ret[0] = B;								//Q为beta列单位化
			//ret[1] = T.inverse();					//R为T的逆矩阵
			ret[1] = B.transposition().multiply(A);		//R=Q^-1 * A(Q^T * A)
		}
		return ret;
	}
	
	public static Matrix diag(Matrix m) {
		Matrix ret = null;
		//int n = 0;
		Matrix mk = m;
		if(m.isSquare()) {
			Matrix[] qr;
			boolean flag = true;
			while(flag) {
				Matrix mk1;
				qr = Matrix.QR(mk);									//A(k) = Q(k) * R(k)
				mk1 = qr[1].multiply(qr[0]);							//A(k+1)=R(k)Q(k)								
				flag = false;
 				total : for(int i = 0; i < mk1.getRow(); i++) {				//对角矩阵
					for(int j = 0; j < mk1.getRow(); j++) {
						if(i > j && Math.abs(mk1.get(i, j)) > 1e-5) {		//下三角为0
							flag = true;					//继续迭代
							break total;
						}
					}
				}
				//n++;
				mk = mk1;										//继续迭代
			}
			ret = mk;
		}
		return ret;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[");
		for(int i = 0; i < this.row; i++) {
			if(i != 0) str.append(", ");
			str.append("[");
			for(int j = 0; j < this.column; j++) {
				if(j != 0) str.append(", ");
				str.append(this.matrix[i][j]);
			}
			str.append("]");
		}
		str.append("]");
		return str.toString();
	}
}
