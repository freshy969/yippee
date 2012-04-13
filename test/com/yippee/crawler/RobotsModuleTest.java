package com.yippee.crawler;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

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
