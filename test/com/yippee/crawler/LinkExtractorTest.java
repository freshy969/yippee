package com.yippee.crawler;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LinkExtractorTest {

	URLFilter filter;
	
	@Before
	public void setUp() throws Exception {
		filter = new URLFilter();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	public void testRelativeUrlsWithSlash(){
		List<String> extractedURLs = new ArrayList<String>();
		extractedURLs.add("/index.html");
		extractedURLs.add("index.html");
		
		//List<URL> absolutes = filter.makeAbsolute("http","crawltest.cis.upenn.edu" , extractedURLs);
		//assertEquals(2, absolutes.size());
		
		
//		for(URL url : absolutes){
//			assertEquals("http://crawltest.cis.upenn.edu/index.html", url);
//		}
	}
}
