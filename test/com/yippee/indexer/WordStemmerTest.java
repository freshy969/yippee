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
		String m = stemmer.stem("caress");
		assertTrue(m.equals("caress"));
	}
	
	@Test
	public void testStemChangeED() {
		String m = stemmer.stem("caressed");
		assertTrue(m.equals("caress"));
	}
	
	@Test
	public void testStemChangeIES() {
		String m = stemmer.stem("fireflies");
		assertTrue(m.equals("firefly"));
	}
	
	@Test
	public void testStemChangeING() {
		String m = stemmer.stem("jumping");
		assertTrue(m.equals("jump"));
	}

	@Test
	public void testStemChangeS() {
		String m = stemmer.stem("sings");
		assertTrue(m.equals("sing"));
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
				assertTrue(results[i].equals("carry"));
			} else if (i == 3) {
				assertTrue(results[i].equals("forebod"));
			} else if (i == 4) {
				assertTrue(results[i].equals("new"));
			}
		}
	}
}
