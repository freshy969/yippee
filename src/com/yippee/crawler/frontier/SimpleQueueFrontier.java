package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;

import org.apache.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleQueueFrontier implements URLFrontier {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(SimpleQueueFrontier.class);

	BlockingQueue<URL> urls;
	
	
	public SimpleQueueFrontier(){
		urls = new LinkedBlockingQueue<URL>();
	}

	public Message pull() throws InterruptedException {	
		URL url = urls.take();
		
		logger.debug("Giving url to thread: " + url.toString());
		return new Message(url.toString());
	}

	public void push(Message message) {
		if(message != null && message.getURL() != null){
			logger.debug("Message pushed to frontier: " + message.getURL());
			urls.add(message.getURL());
		}
			
	}

	public boolean save() {
		logger.debug("SimpleQueueFrontier storing state... ");
		URLFrontierManager fm = new URLFrontierManager();
		
		Map<Integer, Queue<URL>> queues = new HashMap<Integer, Queue<URL>>();
		queues.put(0, urls);

		return fm.storeState(queues);
	}

	public boolean load() {
		
		logger.debug("SimpleQueueFrontier loading state... ");

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
