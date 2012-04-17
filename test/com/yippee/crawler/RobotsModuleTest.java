package com.yippee.crawler;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class RobotsModuleTest {

	RobotsModule rm;
	
	@Before
	public void setUp(){
		rm = new RobotsModule();
	}
	
	@Test
	public void testModuleFetchesRobotsTxt() throws MalformedURLException{

		assertTrue(rm.alowedToCrawl(new URL("http://crawltest.cis.upenn.edu")));
	}

}
