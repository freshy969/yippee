package com.yippee.crawler.prioritizer;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class RandomPrioritizerTest {
	static int MAX = 10;
	URL url;
	RandomPrioritizer r;
	
	
	@Before
	public void setUp() throws MalformedURLException{
		r = new RandomPrioritizer(MAX);
		url = new URL("http://crawltest.cis.upenn.edu");
	}

	@Test
	public void testPriorityRange(){
		int maxSeen = -1;
		int minSeen = Integer.MAX_VALUE;
		
		int sample = -1;
		for(int i = 0; i < MAX * MAX; i++){
			sample = r.getPriority(url);
			if(sample > maxSeen) maxSeen = sample;
			if(sample < minSeen) minSeen = sample;
		}
		
		assertEquals(0, minSeen);
		// We expect the range [0, MAX) 
		assertEquals(MAX - 1, maxSeen);
	}

}
