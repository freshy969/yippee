package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PoliteSimpleQueue implements URLFrontier {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PoliteSimpleQueue.class);
	
	private Random rand;
	private ArrayList<URL> current;
	private ArrayList<URL> next;
	private AtomicInteger counter;
	
    
    public PoliteSimpleQueue(){
    	counter = new AtomicInteger();
    	rand = new Random(System.nanoTime());
    	current = new ArrayList<URL>();
    	next = new ArrayList<URL>();
    	
    }
	
	public Message pull() throws InterruptedException {
		
		synchronized(current){
			if(current.isEmpty()){
				switchQueues();
			}
		}
		
		
		//Pre-condition: current and next were swapped if current was empty at start of this method.s
		URL toSend = null;
		synchronized(current){
			if(current.isEmpty()) return null;
			toSend = current.remove(rand.nextInt(current.size()));
            logger.info("Pull queue length: " + current.size());
		}
		
		//We've decided not to reinsert a URL once its crawled
		//this.next.add(toSend);
		
		return new Message(toSend.toString()) ;
	}

	/**
	 * Switches current queue with next.  Called as a result of current being empty.
	 */
	private void switchQueues() {
		logger.info("Switching queues in PoliteSimpleQueue");
		
		//lock on current is always grabbed first
		synchronized(current){
			synchronized(next){
				ArrayList<URL> tmp = current;
				current = next;
				next = tmp;
			}
		}
		
	}

	/**
	 * Pushes the incoming URL to the <next> queue.
	 */
	public void push(Message message) {
		if(message != null && message.getURL() != null){
			synchronized(next){
				next.add(message.getURL());
				
				if(counter.addAndGet(1) % 1000000 == 0) {
	                logger.info("Push queue length: " + next.size());
					this.save(); 
				}
				logger.debug("" + counter.get());
				
			}	
		}
	}

	public boolean save() {
		logger.info("PoliteSimpleQueue storing state... ");
		URLFrontierManager fm = new URLFrontierManager();
		
		Map<Integer, Queue<URL>> queues = new HashMap<Integer, Queue<URL>>();
		
		Queue<URL> currentQueue = new LinkedList<URL>();
		Queue<URL> nextQueue = new LinkedList<URL>();
		
		
		synchronized(current){
			for(URL url : current){
				currentQueue.add(url);
			}
			
			synchronized(next){
				for(URL url : next){
					nextQueue.add(url);
				}
			}
			
			counter.set(0);
		}
		
		queues.put(1, currentQueue);
		queues.put(-1, nextQueue);
		
		return fm.storeState(queues);
	}

	public boolean load() {
		

		URLFrontierManager fm = new URLFrontierManager();

		FrontierSavedState state = fm.loadState();
		if(state != null){

			logger.info("PoliteSimpleQueue loading state... ");

			Map<Integer, Set<String>> queues = state.getPrioritySets();
			
			for(Integer i : queues.keySet()){
				if(i > 0){
					
					Set<String> urlStrings = queues.get(i);
					for(String s : urlStrings){
						try {
							this.current.add(new URL(s));
							
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
					
				} else {
					Set<String> urlStrings = queues.get(i);
					for(String s : urlStrings){
						try {
							this.next.add(new URL(s));
							
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
					
					
				}
				
				
			}
			return true;
			
		} else {
			return false;
		}
		
		
	}

    public boolean isSeen(String url) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
