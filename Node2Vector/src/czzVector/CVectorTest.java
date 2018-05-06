package czzVector;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CVectorTest {

	private float delta = 0.1f;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAdd() {
		int size = 5;
		IVector v1 = new CVector(size);
		IVector v2 = new CVector(size);
		IVector v3 = new CVector();
		IVector v4;
		for(int i = 0; i < v1.getSize(); i++) {
			v1.getVector()[i] = i;						//01234
			v2.getVector()[i] = v2.getSize() - i;		//54321
		}
		v3.resizeLoad(size, size);
		v4 = v1.new_Add(v2);					//v4 = v1 + v2;
		assertArrayEquals(v3.getVector(), v4.getVector(), delta);
		v1.add(v2);
		assertArrayEquals(v3.getVector(), v1.getVector(), delta);
	}
	
	@Test
	public void testMulti() {
		int size = 5;
		IVector v1 = new CVector(size);
		IVector v2 = new CVector(size);
		IVector v3;
		for(int i = 0; i < v1.getSize(); i++) {
			v1.getVector()[i] = i;						//01234
			v2.getVector()[i] = v2.getSize() - i;		//54321
		}
		v3 = v2.new_Multi(2);					//v4 = 2v1;
		float[] f1 = {10, 8, 6, 4, 2}; 
		assertEquals(20, v1.multiply(v2), delta);						//4+6+6+4
		assertArrayEquals(f1, v3.getVector(), delta);		
	}

	public void testLength() {
		int size = 5;
		IVector v1 = new CVector(size);
		for(int i = 0; i < v1.getSize(); i++) {
			v1.getVector()[i] = i;						//01234
		}
		assertEquals(30, v1.getLength(), delta);						//4+6+6+4
	}
}
