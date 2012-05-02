package com.yippee.crawler.frontier;

import org.apache.log4j.Logger;

import com.yippee.crawler.Message;

public class PoliteSimpleQueue implements URLFrontier {

	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PoliteSimpleQueue.class);
	
	public Message pull() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}


	public void push(Message message) {
		// TODO Auto-generated method stub

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
