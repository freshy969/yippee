package com.yippee.db.crawler;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import com.yippee.db.crawler.model.FrontierSavedState;

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
	
	@Test
	public void testInsertFrontierState(){
		Map<Integer, Queue<URL>> emptyState = new HashMap<Integer, Queue<URL>>();
		assertTrue(manager.storeState(emptyState));
	}
	
	@Test
	public void testVersionNumberSavedIsCorrect(){
		int previousVersion = manager.latestVersion;
		
		Map<Integer, Queue<URL>> emptyState = new HashMap<Integer, Queue<URL>>();
		manager.storeState(emptyState);
		
		assertTrue(manager.latestVersion > previousVersion);
	}
//	
//	@Test
//	public void testVersionNumberRetrievedIsCorrect(){
//		fail("test not written");
//	}

	@Test
	public void testRetrieveFrontierState() throws MalformedURLException{
		Map<Integer, Queue<URL>> state = new HashMap<Integer, Queue<URL>>();
		Queue<URL> sampleQueue = new PriorityQueue<URL>();
		sampleQueue.add(new URL("http://crawltest.cis.upenn.edu"));
		state.put(1, sampleQueue);
		
		
		manager.storeState(state);
		
		
		FrontierSavedState loadedState = manager.loadState();
		Map<Integer, Queue<URL>> priorities = loadedState.getPriorityMaps();
		
		//Only one priority level stored in the state
		assertEquals(1, priorities.keySet().size());
		
		Queue<URL> priorityLevelOne = priorities.get(1);
		//Priority level 1 only has 1 URL
		assertEquals(1, priorities.get(1).size());
		assertEquals(new URL("http://crawltest.cis.upenn.edu"), priorityLevelOne.poll());
	}
	
}
