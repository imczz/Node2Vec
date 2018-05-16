package czzSelectItem;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 根据概率，随机选择某个项目，单元测试
 @author CZZ*/
public class SelectorTest {
	int repeatNum = 10000000;				//测试过100000000，约7.91s
	float delta = 1e-3f;
	
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
		int n = repeatNum;
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
			assertEquals(ap[i], cp[i], delta);
		}
	}
	
	@Test
	public void testAliasSelect() {
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
		int n = repeatNum;
		float probs[] = Selector.toProbs(al);
		ProbabilityPair[] jq = Selector.alias_setup(probs);
		for(i = 0; i < n; i++) {
			e = Selector.alias_draw(jq);
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
			assertEquals(ap[i], cp[i], delta);
		}
	}
	
	@Test
	public void testKInN() {
		int n = 20;
		int k = 7;
		int m = 1000;
		int i, j;
		int[] sum = new int[n];
		for(i = 0; i < n; i++) {
			sum[i] = 0;
		}
		int[] result;
		for(i = 0; i < m; i++) {
			result = Selector.kInN(n, k);
			for(j = 0; j < k; j++) sum[result[j]]++;
		}
		for(i = 0; i < n; i++) {
			assertEquals(k * 1.0 / n, sum[i] * 1.0 / m,  5e-2);
		}
	}
}
