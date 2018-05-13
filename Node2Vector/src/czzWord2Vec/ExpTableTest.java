package czzWord2Vec;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpTableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExpTable() {
		ExpTable expTable = new ExpTable();
		Random rand = new Random();
		int n = 100;
		float f, delta = 2e-3f;
		for(int i = 0; i < n; i++) {
			f=rand.nextFloat() * 9;
			assertEquals(ExpTable.sigmoid(f), expTable.getSigmoid(f), delta);
		}
		for(int i = 0; i < n; i++) {
			f=-rand.nextFloat() * 9;
			assertEquals(ExpTable.sigmoid(f), expTable.getSigmoid(f), delta);
		}
	}

}
