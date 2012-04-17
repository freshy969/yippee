package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class RobotsModuleTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsModuleTest.class);

	RobotsModule rm;
    private String[] allowed = {
            "http://crawltest.cis.upenn.edu",
    };
	
	@Before
	public void setUp(){
		rm = new RobotsModule();
	}
	
	@Test
	public void testModuleFetchesRobotsTxt() throws MalformedURLException{

		assertTrue(rm.alowedToCrawl(new URL("http://crawltest.cis.upenn.edu")));
	}

}
