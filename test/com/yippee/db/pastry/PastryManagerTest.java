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
	public void test() {
		fail("Not yet implemented");
	}

}
