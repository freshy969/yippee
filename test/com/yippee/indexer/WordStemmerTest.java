package com.yippee.indexer;

import com.yippee.indexer.WordStemmer;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WordStemmerTest {
	
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
}
