package com.yippee.db.model;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RobotsTxtTest {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsTxtTest.class);
	RobotsTxt robotsTxt;


	@Before
	public void setUp() throws Exception {	
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
