package com.yippee.db.crawler;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class URLFrontierManagerTest {
	
	URLFrontierManager manager;

	@Before
	public void setUp() throws Exception {
		manager = new URLFrontierManager("db/test/crawler");
	}

	
	
	@Test
	public void testConstructor() {
		assertTrue(manager instanceof URLFrontierManager);
	}

}
