package czzWord2Vec;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试词典的生成哈夫曼树方法
 * @author CZZ*/
public class VocabularyHuffmanTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHuffman() {
		Vocabulary<String> V = new Vocabulary<String>(Vocabulary.WordType.String);
		String str = "a a b b b c c c c c d d d d e e e e f f f f f f f";
		/*2 a a
		3 b b b
		5 c c c c c
		4 d d d d
		4 e e e e
		7 f f f f f f f*/
		String[] words = str.split(" ");
		for(int i = 0; i < words.length; i++) {
			V.addWord(words[i]);
		}
		assertFalse(V.getHuffmanCode());
		V.sortVocabulary();
		assertTrue(V.getHuffmanCode());
		/*
		                25()(4)
		        15(1)(3)       10(0)(2)
		    8(1)(1) f7(0)   c5(1)   5(0)(0)
		 d4(1) e4(1)              b3(1) a2(0)
		 */
		HWord<String> a = V.getWord("a");//000
		HWord<String> b = V.getWord("b");//001
		HWord<String> c = V.getWord("c");//01
		HWord<String> d = V.getWord("d");//110
		HWord<String> e = V.getWord("e");//111
		HWord<String> f = V.getWord("f");//10
		assertArrayEquals(new Byte[]{0, 0, 0}, a.code.toArray());
		assertArrayEquals(new Byte[]{0, 0, 1}, b.code.toArray());
		assertArrayEquals(new Byte[]{0, 1}, c.code.toArray());
		assertArrayEquals(new Byte[]{1, 1, 0}, d.code.toArray());
		assertArrayEquals(new Byte[]{1, 1, 1}, e.code.toArray());
		assertArrayEquals(new Byte[]{1, 0}, f.code.toArray());
		assertArrayEquals(new Integer[]{4, 2, 0} ,a.point.toArray());
		assertArrayEquals(new Integer[]{4, 2, 0} ,b.point.toArray());
		assertArrayEquals(new Integer[]{4, 2} ,c.point.toArray());
		assertArrayEquals(new Integer[]{4, 3, 1} ,d.point.toArray());
		assertArrayEquals(new Integer[]{4, 3, 1} ,e.point.toArray());
		assertArrayEquals(new Integer[]{4, 3} ,f.point.toArray());
	}

}
