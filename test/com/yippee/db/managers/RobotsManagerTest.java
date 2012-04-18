package com.yippee.db.managers;

import com.yippee.db.model.RobotsTxt;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class RobotsManagerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsManagerTest.class);
	
	RobotsManager rm;
	RobotsTxt robots;

	
	
	@Before
	public void setUp() throws Exception {
		rm = new RobotsManager("db/test");
		
		robots = new RobotsTxt();
		robots.setCrawlDelay(10);
		robots.setDisallows(new HashSet<String>());
		robots.setHost("cis.upenn.edu");
		
		rm.create(robots);
	}
	
//	@After
//	public void tearDown(){
//		rm.delete(robots.getHost());
//		rm.close();
//	}
	
	@Test
	public void testConstructor(){
		assertTrue(rm instanceof RobotsManager);
	}
	
//	@Test
//	public void testCreateReturnsFalseForRobotsTxtWithoutInitializedFields(){
//		RobotsTxt emptyRobots = new RobotsTxt();
//		assertFalse(rm.create(emptyRobots));
//	}
//	
//	@Test
//	public void testCreatePutsWellFormedRobotsTxtIntoDb(){
//		RobotsTxt newRobots = new RobotsTxt();
//
//		newRobots = new RobotsTxt();
//		newRobots.setCrawlDelay(10);
//		newRobots.setDisallows(new HashSet<String>());
//		newRobots.setHost("cis.upenn.edu");
//		assertTrue(rm.create(newRobots));
//		
//		rm.delete(newRobots.getHost());
//	}
//	
//	@Test
//	public void testCreateReturnsFalseForADuplicate(){
//		RobotsTxt newRobots = new RobotsTxt();
//
//		newRobots = new RobotsTxt();
//		newRobots.setCrawlDelay(10);
//		newRobots.setDisallows(new HashSet<String>());
//		newRobots.setHost("cis.upenn.edu");
//		assertTrue(rm.create(newRobots));
//		
//		RobotsTxt duplicate = new RobotsTxt();
//		duplicate.setCrawlDelay(10);
//		duplicate.setDisallows(new HashSet<String>());
//		duplicate.setHost("cis.upenn.edu");
//		
//		assertFalse(rm.create(duplicate));
//	}
//	
//	@Test
//	public void testCanExtractStoredRobotsTxtFromDb(){
//		RobotsTxt readFromDb = new RobotsTxt();;
//		assertTrue(rm.read("cis.upenn.edu", readFromDb));
//		
//		assertEquals(robots.getHost(), readFromDb.getHost());
//		assertEquals(robots.getCrawlDelay(), readFromDb.getCrawlDelay());
//		
//		assertTrue("Extracted Robots has things not in the Robots stored", robots.getDisallows().containsAll(readFromDb.getDisallows()));
//		assertTrue("Extracted robots is missing items that were stored", readFromDb.getDisallows().containsAll(robots.getDisallows()));
//	
//	}
//	
//	@Test
//	public void testDeleteRemovesRobotsTxtFromDb(){
//		fail("not implemented");
//		assertTrue(rm.create(robots));
//		
//		
//		assertTrue(rm.delete(robots.getHost()));
//		//assertFalse(rm.read(key, continuation));
//	}
	
	

}
