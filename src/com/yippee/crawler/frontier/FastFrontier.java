package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import com.yippee.db.crawler.URLFrontierManager;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements the fastest frontier possible, using the least possible memory
 */
public class FastFrontier implements URLFrontier {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FastFrontier.class);
    /**
     * True if our queue is empty, false otherwise
     */
    private boolean empty = true;
    /**
     * This array list has all the current url strings that a thread is currently
     * working on.
     */
    private Queue<String> current = new LinkedList<String>();
    /**
     * Pointing out the first time the queue is initialized
     */
    private boolean init = true;


    /**
     * Pulls the url from the shared queue.
     *
     * @return a Message object containing the link information
     * @throws InterruptedException
     */
    public synchronized Message pull() throws InterruptedException {
        logger.info("RMV FROM QUEUE " + empty + "|" + current.size());
        // Wait until a request is available.
        while (empty) {
            try {
                logger.debug("Waiting");
                wait();
            } catch (InterruptedException e) {
                // We are being interrupted for shutdown
                logger.debug("Thread " + Thread.currentThread().getName() + ": Queue interrupted");
                throw new InterruptedException();
            }
        }
        Message message = new Message(current.poll());
        if ((!empty) && current.size() == 0) {
            notifyAll();
            empty = true;
        }
        logger.info("RMVED FROM QUEUE");
        return message;
    }

    /**
     * Pushes the URL to the shared queue.
     *
     * @param message a java Message object containing all the url information
     */
    public void push(Message message) {
        logger.info("ADD TO QUEUE");
        if (empty && !init) { // even if UPD or NEW it is not empty now
            logger.debug("Queue is empty -- notifyAll");
            empty = false;
            notifyAll();
        }
        if (init) {
            empty = false;
            init = false;
        }
        current.add(message.getURL().toString());
        logger.info("Added: " + message.getURL().toString());
    }

    public synchronized boolean save() {
        logger.info("PoliteSimpleQueue storing state... ");
		URLFrontierManager fm = new URLFrontierManager();
        //fm.storeState(current);
        return true;
    }

    public boolean load() {

        return false;
    }

    public boolean isSeen(String url) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
