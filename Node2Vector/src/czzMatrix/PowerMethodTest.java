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
		for(i = 0; i < eig.eigenvector.getRow(); i++) {
			assertEquals(eigvec[i], eig.eigenvector.get(i, 0), 1e-4);
		}
	}

}
