package com.yippee.crawler.frontier;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.yippee.crawler.Message;
import com.yippee.crawler.prioritizer.Prioritizer;
import com.yippee.crawler.prioritizer.PrioritizerFactory;
import com.yippee.crawler.prioritizer.PrioritizerType;

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
    Prioritizer prioritizer;
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
	Queue<Integer> nextHostToCrawl;
	
	
	//RobotsMOdule
	
	//Default constructor
	public MercatorCentralized(){}
	
	public MercatorCentralized(int numPriorityLevels, int numWorkerThreads){
		//Init prioritizer
		prioritizer = PrioritizerFactory.get(PrioritizerType.RANDOM, numPriorityLevels);
		
		
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
		nextHostToCrawl = new PriorityBlockingQueue<Integer>();
	}
	
	
	
	
	
   

    public Message pull() throws InterruptedException {
    	//Based on priority queue of time to next crawl pick a back end queue
    	
    	//Get top/head of priority queue -> returns index of queue to pick from
    	int priorityQueueHead = nextHostToCrawl.poll();
    	//TODO - poll removes the element from the queue.  We don't want it
    	//		removed unless that host's backend queue is empty.
    	//	Either encapsulate this, or handle it here?
    	
    	
    	
    	//
    	URL url = backEndQueues.get(priorityQueueHead).poll();
    	
    	
    	//TODO - Put this URL back in frontier for crawling again (with explicit low priority?)
    	//	via this.push(Message), or other facility?
    	//TODO - this should be done in a way that does not make the caller of pull wait.
    	reinsertURL(url);
    	
    	
    	
    	Message retVal = new Message(url.toString()); 
        return retVal;
    }

    public void push(Message message) {
    	//Put URL into one of the front end queues based on priority from Prioritizer
    	URL url = message.getURL();
    	
    	//Get queue of priority returned by prioritizer, add url to that queue
    	frontEndQueues.get((prioritizer.getPriority(url))).add(url);
    	
    	//TODO - Do we have RobotsTxt? If yes, no problem.  If no, need to get it
    }
    

    private void reinsertURL(URL url){
    	int lowPriority = frontEndQueues.size();
    	frontEndQueues.get(lowPriority).add(url);
    }
    
    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }
}
