package com.yippee.crawler.frontier;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.yippee.crawler.Message;

public class SimpleQueueFrontier implements URLFrontier {
	
	BlockingQueue<Message> urls;
	
	public SimpleQueueFrontier(){
		urls = new PriorityBlockingQueue<Message>();
	}

	@Override
	public Message pull() throws InterruptedException {
		// TODO Auto-generated method stub
		return urls.take();
	}

	@Override
	public void push(Message message) {
		// TODO Auto-generated method stub
		urls.add(message);
	}

	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load() {
		// TODO Auto-generated method stub
		return false;
	}

}
