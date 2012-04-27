package com.yippee.db.pastry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yippee.util.Configuration;

public class PastryManagerTest {
	PastryManager pm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}

	@Before
	public void setUp() throws Exception {
		pm = new PastryManager();
	}

	@Test
	public void testConstructor() {
		assertTrue(pm instanceof PastryManager);
	}

	@Test
	public void testSaveNodeState(){
		assertTrue(pm.storeState(""));
	}
	
	@Test
	public void testRetrieveState(){
		//Make new state
		
		//Store state
		
		//Load state and assert it matches what was put in
		fail("not implemented");
	}
	
}
