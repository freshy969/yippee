package com.yippee.db.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RobotsTxtTest {
	
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
