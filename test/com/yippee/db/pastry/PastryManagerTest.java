package com.yippee.db.pastry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rice.pastry.Id;

import com.yippee.db.pastry.model.NodeState;
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
		//TODO Cant wirte test without some way of generating an Id
		//Id id = new Id();
		//assertTrue(pm.storeState(id));
		
	}
	
	@Test
	public void testRetrieveState(){
		//Make new state
		
		//Store state
		
		//Load state and assert it matches what was put in
		fail("not implemented");
	}
	
}
