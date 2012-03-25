package com.yippee.crawler;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {
	Message m;

	@Before
	public void setUp(){
		m = new Message();
	}

	@Test
	public void testMessageConstructor(){
		assertTrue(m instanceof Message);
	}

}
