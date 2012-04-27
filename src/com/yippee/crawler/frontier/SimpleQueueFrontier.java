package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleQueueFrontier implements URLFrontier {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(SimpleQueueFrontier.class);

	BlockingQueue<Message> urls;
	
	public SimpleQueueFrontier(){
		urls = new LinkedBlockingQueue<Message>();
	}

	public Message pull() throws InterruptedException {
		// TODO Auto-generated method stub
		return urls.take();
	}

	public void push(Message message) {
		// TODO Auto-generated method stub
		if(message != null && message.getURL() != null){
			System.out.println("Message pushed to frontier: " + message.getURL());
			urls.add(message);
		}
			
	}

	public boolean save() {
		// TODO Auto-generated method stub
		
		
		
		
		return false;
	}

	public boolean load() {
		// TODO Auto-generated method stub
		return false;
	}

}
