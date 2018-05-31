package czzWord2Vec;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试词典添加词语删除词语等功能
 * @author CZZ*/
public class VocabularyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVocabulary() {
		Vocabulary<String> V = new Vocabulary<String>(Vocabulary.WordType.String);
		String str = "in computer science and information theory, a huffman code is a particular type of optimal prefix code that is commonly used for lossless data compression The process of finding and or using such a code proceeds by means of huffman coding, an algorithm developed by David A. huffman while he was a Sc.D. student at MIT, and published in the 1952 paper a method for the construction of minimum-Redundancy codes the output from huffman's algorithm can be viewed as a variable-length code table for encoding a source symbol (such as a character in a file). The algorithm derives this table from the estimated probability or frequency of occurrence (weight) for each possible value of the source symbol. As in other entropy encoding methods, more common symbols are generally represented using fewer bits than less common symbols. huffman's method can be efficiently implemented, finding a code in time linear to the number of input weights if these weights are sorted.[2] However, although optimal among methods encoding symbols separately, Huffman coding is not always optimal among all compression methods.";
		String[] stra = str.split("\\.");
		for(int i = 0; i < stra.length; i++) {
			String[] strb = stra[i].split(",") ;
			for(int j = 0; j < strb.length; j++) {
				String[] words = strb[j].split(" ");
				for(int k = 0; k < words.length; k++) {
					V.addWord(words[k]);
				}
			}
		}
		assertFalse(V.isSorted());					//未排序
		V.sortVocabulary();						//排序过程
		assertTrue(V.isSorted());					//已经排序
		assertTrue(V.hasWord("the"));				//单词“the”存在
		assertEquals(V.getWord("a").wordFrequency, 10);		//计算“a”的出现次数（词频）
		assertTrue(V.removeWord("a"));				//删除单词“a”
		assertFalse(V.removeWord("a"));				//再次删除单词“a”
		assertFalse(V.hasWord("a"));			//单词“a”不存在
	}

}
