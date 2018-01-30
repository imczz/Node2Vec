package czzSelectItem;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SelectorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelect() {
		int i;
		float[] al = new float[5];
		al[0] = (float) 2.5;
		al[1] = (float) 3.5;
		al[2] = (float) 5.5;
		al[3] = (float) 6.5;
		al[4] = (float) 2;
		float suma = 0, sumc = 0;
		int[] counter = new int[5];
		counter[0] = 0;
		counter[1] = 0;
		counter[2] = 0;
		counter[3] = 0;
		counter[4] = 0;
		int e = -1;
		int n = 10000;
		for(i = 0; i < n; i++) {
			e = Selector.select(al);
			assertTrue(e >= 0 && e < 5);
			counter[e]++;
		}
		for(i = 0; i < 5; i++) {
			suma += al[i];
			sumc += counter[i];
		}
		float ap[] = new float[5];
		float cp[] = new float[5];
		for(i = 0; i < 5; i++) {
			ap[i] = al[i] / suma;
			cp[i] = counter[i] / sumc;
			assertEquals(ap[i], cp[i], (float)1e-2);
		}
	}
}
