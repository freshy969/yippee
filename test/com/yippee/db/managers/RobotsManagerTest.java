package com.yippee.db.managers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.yippee.crawler.HttpResponse;
import com.yippee.db.model.RobotsTxt;

public class RobotsManagerTest {
	
	RobotsManager rm;
	RobotsTxt robots;

	@Before
	public void setUp() throws Exception {
		rm = new RobotsManager("db");
		
	}
	
	@Test
	public void testConstructor(){
		assertTrue(rm instanceof RobotsManager);
	}
	
	@Test
	public void testCreateReturnsFalseForRobotsTxtWithoutInitializedFields(){
		robots = new RobotsTxt(new HttpResponse());
		assertFalse(rm.create(robots));
	}
	
	@Test
	public void testCreatePutsWellFormedRobotsTxtIntoDb(){
		//assertTrue(rm.create(robots));
		fail("not implemented");
	}
	
	@Test
	public void testCreateReturnsFalseForADuplicate(){
		fail("not implemented");
	}
	
	@Test
	public void testCanExtractStoredRobotsTxtFromDb(){
		fail("not implemented");
	}
	
	@Test
	public void testDeleteRemovesRobotsTxtFromDb(){
		fail("not implemented");
	}
	
	

}
