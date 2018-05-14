package czzWord2Vec;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import czzVector.CVector;
import czzVector.IVector;
import czzWord2Vec.Word2Vec.ModelType;
import czzWord2Vec.Word2Vec.TrainMethod;

public class Word2VecTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRandomSetVector() {
		int n = 100;
		int d = 8;
		Word2Vec<Integer> w2v = new Word2Vec<Integer>(ModelType.Skip_gram, TrainMethod.HS, 0, d, 5, 0.025f, 5, 3, 1);
		IVector[] _models = new IVector[n];
		for(int i = 0; i < n; i++) {
			_models[i] = new CVector(d);
			w2v.randomSetVector(_models[i]);						//野割昧字方
			for(int j = 0; j < d; j++) {
				assertTrue(_models[i].getVector()[j] > (-0.5f / d));
				assertTrue(_models[i].getVector()[j] < (0.5f / d));
			}
			
		}
	}

}
