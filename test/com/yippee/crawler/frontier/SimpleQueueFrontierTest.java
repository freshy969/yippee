package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;
import com.yippee.util.Configuration;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleQueueFrontierTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(SimpleQueueFrontierTest.class);
	SimpleQueueFrontier frontier;

	@BeforeClass
	public static void setUpBeforeClass() {
		Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}
	
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
		assertEquals(new URL(url), frontier.pull().getURL());
	}
	
	@Test
	public void testSave(){
		frontier.push(new Message("http://crawltest.cis.upenn.edu"));
		assertTrue(frontier.save());
	}
	
	@Test
	public void testLoad() throws MalformedURLException{
		//Get current state in the db
		URLFrontierManager man = new URLFrontierManager();
		Map<Integer, Set<String>> queues = man.loadState().getPrioritySets();
		
		//Load the state into the frontier
		frontier.load();
		
		//Compare the retreived state and that in the loaded frontier
		int count = 0;
		for(Integer i : queues.keySet()){
			Set<String> urlStrings = queues.get(i);
			for(String s : urlStrings){
				count++;
				assertTrue(frontier.urls.contains(new URL(s)));
			}
		}
		assertFalse(0 == count);
		assertEquals(count, frontier.urls.size());
	}
	
	
}
