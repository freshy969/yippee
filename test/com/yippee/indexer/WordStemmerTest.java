package com.yippee.indexer;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WordStemmerTest {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(WordStemmerTest.class);
	WordStemmer stemmer;
	
	@Before
	public void setup(){
		stemmer = new WordStemmer();
	}

	@Test
	public void testStemNoChange() {
		String word = "caress";
		assertTrue("caress".equals(stemWord(word)));
	}
	
	@Test
	public void testStemChangeED() {
		String word = "caressed";
		assertTrue("caress".equals(stemWord(word)));
	}
	
	@Test
	public void testStemChangeIES() {
		String word = "fireflies";
		assertTrue("firefli".equals(stemWord(word)));
	}
	
	@Test
	public void testStemChangeING() {
		String word = "jumping";
		assertTrue("jump".equals(stemWord(word)));
	}

	@Test
	public void testStemChangeS() {
		String word = "flares";
		assertTrue("flare".equals(stemWord(word)));
	}
	
	@Test
	public void testApostrophe() {
		String word = "Bob's";
		assertTrue("Bob'".equals(stemWord(word)));
	}
	
	@Test
	public void testContraction() {
		String word = "I've";
		assertTrue("I've".equals(stemWord(word)));
	}
	
	@Test 
	public void testStemList() {
		String[] wordlist = {"what", "blazes", "carries", "foreboding", "news"};
		
		String[] results = stemmer.stemList(wordlist);
		
		for (int i = 0; i < results.length; i++) {
			if (i == 0) {
				assertTrue(results[i].equals("what"));
			} else if (i == 1) {
				assertTrue(results[i].equals("blaze"));
			} else if (i == 2) {
				assertTrue(results[i].equals("carri"));
			} else if (i == 3) {
				assertTrue(results[i].equals("forebod"));
			} else if (i == 4) {
				assertTrue(results[i].equals("new"));
			}
		}
	}
	
	public String stemWord(String input) {
		String word = input;
		stemmer.add(word.toCharArray(), word.length());
		stemmer.stem();
		int resultLength = stemmer.getResultLength();
		char[] resultBuffer = stemmer.getResultBuffer();

		return new String(resultBuffer).substring(0, resultLength);
	}
}
