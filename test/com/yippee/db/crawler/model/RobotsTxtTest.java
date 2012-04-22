package com.yippee.db.crawler.model;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.yippee.db.crawler.model.RobotsTxt;

import static org.junit.Assert.assertTrue;

public class RobotsTxtTest {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsTxtTest.class);
	RobotsTxt robotsTxt;


	@Before
	public void setUp() throws Exception {	
		robotsTxt = new RobotsTxt();
	}
	
	@Test
	public void testConstructor(){
		assertTrue(robotsTxt instanceof RobotsTxt);
	}

	
//	@Test
//	public void testSomething(){
//		HttpModule robo = new HttpModule();
//		robo.setStatus(200);
//		robotsTxt = new RobotsTxt(robo);
//		
//		
//		
//	}
	

}
