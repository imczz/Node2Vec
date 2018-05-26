package czzMatrix;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MatrixTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeterminant() {
		float[][] m = {{1, 2, 3, 5}, {4, 5, 6, 2}, {8, 9, 9, 1}, {3, 2, 1, 4}};
		Matrix mat = new Matrix();
		mat.load(m);
		assertEquals(39, mat.determinant(), 1e-4);
	}

	@Test
	public void testRepeat() {
		float[][] m = {{1, 2, 3}, {4, 5, 6}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix mr = mat.repeat(2, 2);
		assertEquals(4, mr.getRow());
		assertEquals(6, mr.getColumn());
		float[][] ma = {{1, 2, 3, 1, 2, 3}, {4, 5, 6, 4, 5, 6}, {1, 2, 3, 1, 2, 3}, {4, 5, 6, 4, 5, 6}};
		for(int i = 0; i < 4; i++) {
			assertArrayEquals(ma[i], mr.getMatrix()[i], 1e-6f);
		}
	}
	
	@Test
	public void testMultiply() {
		float[][] m1 = {{1, 2, 3}, {4, 5, 6}};
		float[][] mtest1 = {{2, 4, 6}, {8, 10, 12}};
		Matrix mat1 = new Matrix();
		mat1.load(m1);
		Matrix mat2 = Matrix.multiply(mat1, 2);						//乘数
		int i;
		for(i = 0; i < mat2.getRow(); i++) {
			assertArrayEquals(mtest1[i], mat2.getMatrix()[i], 1e-6f);
		}
		float[][] mtest2 = {{17, 22, 27}, {22, 29, 36}, {27, 36, 45}};
		float[][] mtest3 = {{14, 32}, {32, 77}};
		Matrix mat1Transposition = mat1.transposition();				//转置
		Matrix multy1t1 = mat1Transposition.multiply(mat1);
		Matrix multy3t3 = mat1.multiply(mat1Transposition);
		for(i = 0; i < mat1.getColumn(); i++) {
			assertArrayEquals(mtest2[i], multy1t1.getMatrix()[i], 1e-6f);
		}
		for(i = 0; i < mat1.getRow(); i++) {
			assertArrayEquals(mtest3[i], multy3t3.getMatrix()[i], 1e-6f);
		}
	}
	
	@Test
	public void testMean() {
		float[][] m = {{1, 2, 3}, {6, 5, 4}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix mean = mat.mean();
		float[] mtest = {3.5f, 3.5f, 3.5f};
		assertArrayEquals(mtest, mean.getMatrix()[0], 1e-6f);
	}
	
	@Test
	public void testCov() {
		float[][] m = {{1, 2, 3}, {6, 5, 4}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix cov = mat.cov();
		float[][] mtest = {{12.5f, 7.5f, 2.5f}, {7.5f, 4.5f, 1.5f}, {2.5f, 1.5f, 0.5f}};
		for(int i = 0; i < cov.getRow(); i++) {
			assertArrayEquals(mtest[i], cov.getMatrix()[i], 1e-6f);
		}
	}
	
	@Test
	public void testAdjoint(){
		float[][] m = {{1, 2, 3}, {6, 5, 4}, {8, 9, 7}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix adj = mat.adjoint();
		float[][] mtest = {{-1, 13, -7}, {-10, -17, 14}, {14, 7, -7}};
		for(int i = 0; i < adj.getRow(); i++) {
			assertArrayEquals(mtest[i], adj.getMatrix()[i], 1e-6f);
		}
	}
	
	@Test
	public void testQR() {
		float[][] m = {{1, 2, 3}, {6, 5, 4}, {8, 9, 7}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix[] QR = Matrix.QR(mat);
		float[][] mtestQ = {{-0.0995f, 0.5687f, -0.8165f}, {-0.5970f, -0.6906f, -0.4082f}, {-0.7960f, 0.4468f, 0.4082f}};
		float[][] mtestR = {{-10.0499f, -10.3484f, -8.2588f}, {0, 1.7061f, 2.0717f}, {0, 0, -1.2247f}};
		Matrix matQR = Matrix.multiply(QR[0], QR[1]);
		for(int i = 0; i < matQR.getRow(); i++) {
			assertArrayEquals(m[i], matQR.getMatrix()[i], 1e-3f);			//m = Q * R
		}
		float vm;
		for(int i = 0; i < QR[0].getColumn(); i++) {
			for(int j = 0; j < QR[0].getColumn(); j++) {
				if(i==j) continue;
				else {
					vm = 0;
					for(int k = 0; k < QR[0].getRow(); k++) {
						vm += QR[0].get(k, i) * QR[0].get(k, j);
					}
					assertEquals(0, vm, 1e-4);								//Q的列两两正交
				}
			}
		}
		for(int i = 0; i < QR[1].getRow(); i++) {
			for(int j = 0; j < QR[1].getColumn(); j++) {
				if(i > j) assertEquals(0, QR[1].get(i, j), 1e-4);			//R是上三角矩阵
			}
		}
		//A转置乘A为单位矩阵E，也可以证明正交矩阵
		Matrix E = Matrix.multiply(QR[0], QR[0].transposition());
		for(int i = 0; i < QR[0].getRow(); i++) {
			for(int j = 0; j < QR[0].getColumn(); j++) {
				if(i == j) assertEquals(1, E.get(i, j), 1e-4);			//对角线1
				else assertEquals(0, E.get(i, j), 1e-4);				//0
			}
		}
		//与matlabQR分解结果，A=QR，Q的每一列是负正负正
		for(int i = 0; i < QR[0].getRow(); i++) {
			assertArrayEquals(mtestQ[i], QR[0].getMatrix()[i], 1e-3f);
		}
		for(int i = 0; i < QR[1].getRow(); i++) {
			assertArrayEquals(mtestR[i], QR[1].getMatrix()[i], 1e-3f);
		}
		
	}
	
	@Test
	public void testDiag() {
		float[][] m = {{1, 2, 3}, {4, 4, 4}, {5, 6, 0}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix diag = Matrix.diag(mat);
		/*
		 * V =
			-0.3716   -0.7690   -0.4364
			-0.6924    0.6391   -0.2182
			-0.6185    0.0149    0.8729

		   D =
			9.7202         0         0
			0   -0.7202         0
			0         0   -4.0000
		 * */
		float[] test = {9.7202f, -4, -0.7202f};
		for(int i = 0; i < diag.getRow(); i++) {
			assertEquals(diag.get(i, i), test[i], 1e-4);
		}
	}
	
	@Test
	public void testDiag2() {
		float[][] m = {{1, 4, 3}, {4, 5, 6}, {3, 6, 9}};
		Matrix mat = new Matrix();
		mat.load(m);
		Matrix diag = Matrix.diag(mat);
		/*
		 * V =
			0.8014    0.5013    0.3263
   			-0.5885    0.5633    0.5800
    		0.1070   -0.6568    0.7465

		   D =
			-1.5371         0         0
         	0    1.5642         0
         	0         0   14.9730
		 * */
		float[] test = {14.9730f, 1.5642f, -1.5371f};
		for(int i = 0; i < diag.getRow(); i++) {
			assertEquals(diag.get(i, i), test[i], 1e-4);
		}
	}
	
	@Test
	public void testgetMaxTwoDiags() {
		float[][] m = {{1, 2, 3, 4}, {5, 6, 7, 8}, {12, 11, 10, 9}, {16, 14, 13, 15}};
		Matrix mat = new Matrix();
		mat.load(m);
		Eigen eig = Matrix.getMaxTwoDiags(mat);
		/*
		 * V =
			-0.1594   -0.5113    0.3273    0.1444
   			-0.3694   -0.4903   -0.7638   -0.0491
   			-0.5233    0.5794    0.5455   -0.7734
   			-0.7512    0.4031   -0.1091    0.6153

		   D =
			34.3380         0         0         0
         	0   -3.6356         0         0
         	0         0   -0.0000         0
         	0         0         0    1.2977
		 * */
		int i, j;
		float[] testValue = {34.3380f, -3.6356f, 1.2977f, 0};
		for(i = 0; i < 2; i++) {
			assertEquals(testValue[i], eig.eigenvalues[i], 1e-4);			//特征值
		}
		float[][] testVector = {{-0.1594f, -0.5113f, 0.1444f, 0.3273f},{-0.3694f, -0.4903f, -0.0491f, -0.7638f},{-0.5233f, 0.5794f, -0.7734f, 0.5455f},{-0.7512f, 0.4031f, 0.6153f, -0.1091f}};
		int symble;
		for(i = 0; i < 2; i++) {					//列
			symble = 1;
			if(testVector[0][i] * eig.eigenvectors.get(0, i) < 0) symble = -1;			//符号
			for(j = 0; j < mat.getRow(); j++) {								//行
				assertEquals(testVector[j][i] * symble, eig.eigenvectors.get(j, i), 1e-4);
			}
		}
	}
}
