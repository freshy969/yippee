package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import com.yippee.db.crawler.model.FrontierSavedState;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * An implementation of a really fast crawler!
 */
public class FastFrontierBoost implements URLFrontier {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FastFrontierBoost.class);
    private final int MAX_SIZE = 10000;
    /**
     * The queue containing the current url-frontier
     */
    protected BlockingQueue<String> current = new ArrayBlockingQueue<String>(MAX_SIZE);
    /**
     * The queue containing the urls seen (this is the duplicate url eliminator)
     */
    protected BlockingQueue<String> seen = new ArrayBlockingQueue<String>(MAX_SIZE * 100);
    /**
     * Number of pushed urls before we save state
     */
    int saveState = 1000;

    /**
     * Pulls the url from the shared queue.
     *
     * @return a Message object containing the link information
     * @throws InterruptedException
     */
    public synchronized Message pull() {
        String url = current.poll();
        logger.debug("PULL: " + url);
        return new Message(url);
    }

    /**
     * Push the url to the shared queue
     *
     * @param message a message object containing the all url information
     */
    public synchronized void push(Message message) {

        String url = message.getURL().toString();
        //logger.info(">>>>>>>>TRY: " + url);
        if (seen.contains(url)) {
            logger.debug("seen!");
            return;
        }

        int queueSize = current.size();
        if ((queueSize > 1000) && (queueSize < MAX_SIZE - 1)) {
            logger.info(queueSize);
        } else if (queueSize == MAX_SIZE - 1) {
            logger.info("QUEUE IS FULL: Discarding url:" + url);
            return;
        }
        logger.debug("PUSH: " + url);
        current.add(url);
        seen.add(url);
        return;
    }

    public boolean save() {
		logger.debug("FastFrontierBoost storing state... ");
		URLFrontierManager fm = new URLFrontierManager();
		Map<Integer, Queue<String>> queues = new HashMap<Integer, Queue<String>>();
		
		queues.put(1, current);
		queues.put(-1, seen);
		
		return fm.storeStateStrings(queues);
	}

	public boolean load() {
		logger.debug("FastFrontierBoost loading state... ");
		URLFrontierManager fm = new URLFrontierManager();

		FrontierSavedState state = fm.loadState();
		if(state != null){
			Map<Integer, Set<String>> queues = state.getPrioritySets();
			
			for(Integer i : queues.keySet()){
				if(i > 0){
					for(String s : queues.get(i)){
						current.add(s);
					}
					
				} else {
					for(String s : queues.get(i)){
						seen.add(s);
					}
				}
			}
			return true;
			
		} else {
			return false;
		}
	}

    public synchronized boolean isSeen(String url) {
        if (seen.contains(url)) {
            logger.debug("Seen " + url);
            return true;
        } else return false;
    }
}
