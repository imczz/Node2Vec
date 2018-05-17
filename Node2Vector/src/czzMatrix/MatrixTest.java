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

}
