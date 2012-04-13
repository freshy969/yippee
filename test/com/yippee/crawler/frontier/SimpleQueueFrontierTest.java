package com.yippee.crawler.frontier;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yippee.crawler.Message;

public class SimpleQueueFrontierTest {
	SimpleQueueFrontier frontier;

	@Before
	public void setUp() throws Exception {
		frontier = new SimpleQueueFrontier();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testPushPullOneItem() throws MalformedURLException, InterruptedException{
		String url = "http://crawltest.cis.upenn.edu";
		frontier.push(new Message(url));
		assertEquals(url, frontier.pull().getURL());
	}

}
