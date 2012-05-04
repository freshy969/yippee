package com.yippee.crawler.frontier;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.yippee.crawler.Message;
import com.yippee.crawler.RobotsModule;
import com.yippee.crawler.prioritizer.Prioritizer;
import com.yippee.crawler.prioritizer.PrioritizerFactory;
import com.yippee.crawler.prioritizer.PrioritizerType;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;

import org.apache.log4j.Logger;

/**
 *
 */
public class MercatorCentralized implements URLFrontier{
	/**
	 * Create logger in the Log4j hierarchy named by by software component
	 */
	static Logger logger = Logger.getLogger(MercatorCentralized.class);
	
	final int NUM_QUEUE_MULTIPLIER = 3;
	final int NUM_WORKER_THREADS;

	
	
	//Prioritizer
	Prioritizer prioritizer;

	//Random queue chooser with bias (used in the "refill" operation)


	//Some number (fixed?) of front-end FIFO queues (one per priority level)
	//Priority Level -> Queue<URL>
	Map<Integer, Queue<URL>> frontEndQueues;

	//Some number (fixed?) of back-end FIFO queues (many more than worker threads)
	//hostname -> Queue<URL>
	Map<String, Integer> hostToQueueTable;
	Map<Integer, Queue<URL>> backEndQueues;


	//Priority queue (heap) (for next time back-end queue can be selected)
	Queue<Integer> nextHostToCrawl;


	//RobotsModule
	RobotsModule robots;


	public MercatorCentralized(int numPriorityLevels, int numWorkerThreads){
		//Initialize prioritizer
		NUM_WORKER_THREADS = numWorkerThreads;
		prioritizer = PrioritizerFactory.get(PrioritizerType.RANDOM, numPriorityLevels);
		robots = new RobotsModule();

		//Initialize front end queues
		frontEndQueues = new ConcurrentHashMap<Integer, Queue<URL>>();
		for(int i = 0; i < numPriorityLevels; i++){
			frontEndQueues.put(1, new LinkedBlockingQueue<URL>());
		}

		//Initialize back-end queues

		hostToQueueTable = new ConcurrentHashMap<String, Integer>();

		backEndQueues = new ConcurrentHashMap<Integer, Queue<URL>>();
		for(int i = 0; i < NUM_WORKER_THREADS * NUM_QUEUE_MULTIPLIER; i++){
			backEndQueues.put(i, new LinkedBlockingQueue<URL>());
		}


		//Initialize priority queue
		nextHostToCrawl = new PriorityBlockingQueue<Integer>();
	}







	/**
	 * Returns the next URL to crawl in the form of a crawler.Message
	 * 
	 * The "Back-end queue selector" component from the Mercator design is incorporated in this method.
	 */
	public Message pull() throws InterruptedException {
		//Based on priority queue of time to next crawl pick a back end queue

		//Get top/head of priority queue -> returns index of queue to pick from
		int priorityQueueHead = nextHostToCrawl.poll();

		//UPDATED PRIORTY QUEUE FOR THIS ELEMENT
		//TODO - poll removes the element from the queue.  We don't want it
		//		removed unless that host's backend queue is empty.
		//	Either encapsulate this, or handle it here?

		Queue<URL> backEndHostQueue = backEndQueues.get(priorityQueueHead);
		URL url = backEndHostQueue.poll();
		
		
		
		//If backend queue is empty, call refill thread
//		if(backEndHostQueue.isEmpty()){
//			// TODO Call refill thread
//			//maintanenceThread?
//			MercatorMaintenance maintanaceThread = new MercatorMaintenance(this, );
//			Thread t = new Thread(maintanaceThread);
//			t.start();	
//		}
			
			
		
		
		

		//TODO - Put this URL back in frontier for crawling again (with explicit low priority?)
		//	via this.push(Message), or other facility?
		//TODO - this should be done in a way that does not make the caller of pull wait
		reinsertURL(url);
		
		



		Message retVal = new Message(url.toString()); 
		return retVal;
	}

	/**
	 * Receives Message  
	 */
	public void push(Message message) {
		//TODO check other Message paramters first? type, 

		if(message != null && message.getURL() != null){
			//Put URL into one of the front end queues based on priority from Prioritizer
			URL url = message.getURL();

			//Get queue of priority returned by prioritizer, add url to that queue
			if(robots.allowedToCrawl(url))
				frontEndQueues.get((prioritizer.getPriority(url))).add(url);


			//TODO - need to update priority queue
		}


	}

	/**
	 * Reinserts URL back into the frontier. 
	 * Bypasses push so as to take into account this was just crawled and add back with a low priority
	 * @param url
	 */
	private void reinsertURL(URL url){
		//TODO verify this is the right low priority
		int lowPriority = frontEndQueues.size();
		frontEndQueues.get(lowPriority).add(url);
	}

	/**
	 * Stores the state of the frontier to persistent storage
	 */
	public boolean save() {
		URLFrontierManager urlManager = new URLFrontierManager();

		//For each of the queues/urls in the queue,
		// make a QueuedUrl and put it in the database

		Map<Integer, Queue<URL>> state = new HashMap<Integer, Queue<URL>>();

		for(int i = 0; i < frontEndQueues.size(); i++){
			Queue<URL> queue = frontEndQueues.get(i);
			state.put(i, queue);
		}

		for(int i = 0; i < backEndQueues.size(); i++){
			Queue<URL> queue = backEndQueues.get(i);
			//-1 is a special value to indicate backend queues
			state.put(-1, queue);
		}

		urlManager.storeState(state);

		//TODO Return true if success?
		return false;
	}

	
	/**
	 * Loads the frontier with state form persistent storage
	 */
	public boolean load() {
		URLFrontierManager urlManager = new URLFrontierManager();

		FrontierSavedState state = urlManager.loadState();
		if(state != null){
			Map<Integer, Set<String>> queues = state.getPrioritySets(); 

			//Push all the saved urls (with i > 0 strings to the frontier (
			for(Integer i : queues.keySet()){
				Set<String> urlStrings = queues.get(i);

				if(i >= 0 && i < this.frontEndQueues.size()){
					//These urls belong in the front end
					for(String s : urlStrings)
						try {
							this.frontEndQueues.get(i).add(new URL(s));
						} catch (MalformedURLException e) {
							logger.warn("MalformedURLException", e);
						}

				} else if (i < 0){
					//These urls belong in the back end
					for(String s : urlStrings){
						addToBackendQueue(s);
					}
				} else {
					//The priority doesn't have meaning for this frontier type, just push them
					for(String s : urlStrings){
						this.push(new Message(s));
					}
				}


			}

			return true;
		} else {
			return false;
		}

	}

	/**
	 * When loading URLs into the backend queue form storage. 
	 * Will "allocate" backend queues for a host if one does not exists and there are unallocated queues as per
	 * 	the hostToQueue lookup table
	 * @param urlString
	 */
	private void addToBackendQueue(String urlString) {
		try {
			URL url = new URL(urlString);
			String host = url.getHost();


			synchronized(hostToQueueTable){
				//Which backend queue to add it to?
				Integer hostQueueIndex = this.hostToQueueTable.get(host);

				if(hostQueueIndex == null){
					//Host not in table

					if(hostToQueueTable.size() < (NUM_WORKER_THREADS * NUM_QUEUE_MULTIPLIER)){
						//Back end queues not all claimed

						//Add mapping in the table
						addHostToQueueMapping(host);

						// Get the right backend queue index
						hostQueueIndex = hostToQueueTable.get(host);

					} else {
						logger.warn("Can't add host to queue mapping table. It was full.");
					}


				}else{
					// Already a backend queue for this host 
					this.backEndQueues.get(hostQueueIndex).add(url);	
				}
			}


		} catch (MalformedURLException e) {
			logger.warn("MalformedURLException", e);
		}

	}

	/**
	 * 
	 * @param host
	 */
	private void addHostToQueueMapping(String host) {
		// Get an unused integer for this host
		for(int i = 0; i < (NUM_WORKER_THREADS * NUM_QUEUE_MULTIPLIER); i++){
			if(!hostToQueueTable.containsValue(i)){
				hostToQueueTable.put(host, i);
				return ;
			}
		}
		logger.warn("Can't add host to queue mapping table. It was full.");
	}
}
