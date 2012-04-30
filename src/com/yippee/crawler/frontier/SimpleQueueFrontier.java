package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;

import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SimpleQueueFrontier implements URLFrontier {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(SimpleQueueFrontier.class);

	ArrayList<URL> urls;
	
	
	public SimpleQueueFrontier(){
		urls = new ArrayList<URL>();
	}

	public Message pull() throws InterruptedException {	
		URL url = null;
		synchronized(urls){
			url = urls.remove(0); 
		}
		 
		
		logger.debug("Giving url to thread: " + url.toString());
		return new Message(url.toString());
	}

	public void push(Message message) {
		if(message != null && message.getURL() != null){
			logger.debug("Message pushed to frontier: " + message.getURL());
			synchronized(urls){
				urls.add(message.getURL());
			}
			
		}
			
	}

	public boolean save() {
		logger.info("SimpleQueueFrontier storing state... ");
		URLFrontierManager fm = new URLFrontierManager();
		
		Map<Integer, Queue<URL>> queues = new HashMap<Integer, Queue<URL>>();
		Queue<URL> tempQueue = new LinkedList<URL>();
		synchronized(urls){
			for(URL url : urls){
				tempQueue.add(url);
			}
		}
		
		queues.put(0, tempQueue);

		return fm.storeState(queues);
	}

	public boolean load() {
		
		logger.info("SimpleQueueFrontier loading state... ");

		URLFrontierManager fm = new URLFrontierManager();

		FrontierSavedState state = fm.loadState();
		if(state != null){
			Map<Integer, Set<String>> queues = state.getPrioritySets();
			
			for(Integer i : queues.keySet()){
				Set<String> urlStrings = queues.get(i);
				for(String s : urlStrings){
					this.push(new Message(s));
				}
			}
			return true;
			
		} else {
			return false;
		}
		
		
	}

}
