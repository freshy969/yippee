package com.yippee.crawler.frontier;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

/**
 *
 */
public class MercatorCentralized implements URLFrontier{
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(MercatorCentralized.class);
    
    //Prioritizer?
    
    //Random queue chooser with bias
    
    
	//Some number (fixed?) of front-end FIFO queues (one per priority level)
    //Priority Level -> Queue<URL>
	Map<Integer, Queue<URL>> frontEndQueues;
	
	//Some number (fixed?) of back-end FIFO queues (many more than worker threads)
	//
	//hostname -> Queue<URL>
	Map<String, Integer> hostToBackEndQueue;
	Map<Integer, Queue<URL>> backEndQueues;
	
	
	//Priority queue (heap) (for next time back-end queue can be selected)
	Queue<Integer> nextToCrawl;
	
	
	//RobotsMOdule
	
	//Default constructor
	public MercatorCentralized(){}
	
	public MercatorCentralized(int numPriorityLevels, int numWorkerThreads){
		//Initilaize front end queues
		frontEndQueues = new HashMap<Integer, Queue<URL>>();
		for(int i = 0; i < numPriorityLevels; i++){
			frontEndQueues.put(1, new LinkedBlockingQueue<URL>());
		}
		
		//Initialize backend queues
		
		hostToBackEndQueue = new HashMap<String, Integer>();
		
		backEndQueues = new HashMap<Integer, Queue<URL>>();
		for(int i = 0; i < numWorkerThreads; i++){
			backEndQueues.put(i, new LinkedBlockingQueue<URL>());
		}
		
		
		//Initialize priority queue
		nextToCrawl = new PriorityBlockingQueue<Integer>();
	}
	
	
	
	
	
   

    public Message pull() throws InterruptedException {
    	//Based on priority queue of time to next crawl pick a back end queue
    	
        return null;
    }

    public void push(Message message) {
    	//Put URL into one od the front end queues based on priority from Prioritizer
    	
    	
    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }
}
