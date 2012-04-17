package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

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
		//TODO test fails, url returned includes a '/' 
		assertEquals(url, frontier.pull().getUrl());
	}
}
