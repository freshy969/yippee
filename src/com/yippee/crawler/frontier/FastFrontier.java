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
     * Pulls the url from the shared queue.
     *
     * @return a Message object containing the link information
     * @throws InterruptedException
     */
    public synchronized Message pull() throws InterruptedException {
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
        return message;
    }

    /**
     * Pushes the URL to the shared queue.
     *
     * @param message a java Message object containing all the url information
     */
    public void push(Message message) {
        if (empty) { // even if UPD or NEW it is not empty now
            logger.debug("Queue is empty -- notifyAll");
            empty = false;
            notifyAll();
        }
        current.add(message.getURL().toString());
        logger.debug("Added: " + message.getURL().toString());
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
}
