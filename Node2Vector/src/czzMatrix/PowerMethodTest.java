package czzMatrix;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PowerMethodTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPowerMethod() {
		float[][] m = {{1, 2, 3}, {4, 4, 4}, {5, 6, 0}};
		Matrix mat = new Matrix();
		mat.load(m);
		PowerMethod pm = new PowerMethod(mat);
		Eigen eig = pm.maxEigen();
		assertEquals(9.7202, eig.eigenvalue, 1e-4);
		int i;
		float[] eigvec = {-0.3716f, -0.6924f, -0.6185f};
		int symble = 1;
		if(eigvec[0] * eig.eigenvector.get(0, 0) < 0) symble = -1;			//协调特征向量正负
		for(i = 0; i < eig.eigenvector.getRow(); i++) {
			assertEquals(eigvec[i] * symble, eig.eigenvector.get(i, 0), 1e-4);			//特征向量正负问题
		}
	}

	@Test
	public void testPowerMethod2() {
		float[][] m = {{1, 4, 3}, {4, 5, 6}, {3, 6, 9}};
		Matrix mat = new Matrix();
		mat.load(m);
		PowerMethod pm = new PowerMethod(mat);
		Eigen eig = pm.maxEigen();
		assertEquals(14.9730, eig.eigenvalue, 1e-4);
		int i;
		float[] eigvec = {0.3263f, 0.5800f, 0.7465f};
		int symble = 1;
		if(eigvec[0] * eig.eigenvector.get(0, 0) < 0) symble = -1;			//协调特征值正负
		for(i = 0; i < eig.eigenvector.getRow(); i++) {
			assertEquals(eigvec[i] * symble, eig.eigenvector.get(i, 0), 1e-4);			//特征向量正负问题
		}
	}
}
