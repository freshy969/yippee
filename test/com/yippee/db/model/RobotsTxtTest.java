package com.yippee.db.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RobotsTxtTest {
	
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
//		HttpResponse robotsResponse = new HttpResponse();
//		robotsResponse.setStatus(200);
//		robotsTxt = new RobotsTxt(robotsResponse);
//		
//		
//		
//	}
	

}
