package com.yippee.db.crawler.model;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class FrontierSavedStateTest {
	FrontierSavedState fss;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		fss = new FrontierSavedState(1, new HashMap<Integer, Queue<URL>>());
		assertTrue(fss instanceof FrontierSavedState);
	}

}
