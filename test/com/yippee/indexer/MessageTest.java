package com.yippee.indexer;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(MessageTest.class);
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
