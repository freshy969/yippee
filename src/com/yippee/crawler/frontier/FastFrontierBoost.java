package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * An implementation of a really fast crawler!
 */
public class FastFrontierBoost implements URLFrontier{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FastFrontierBoost.class);
    /**
     * The queue containing the current url-frontier
     */
    private BlockingQueue<String> current = new ArrayBlockingQueue<String>(10000);

    /**
     * Pulls the url from the shared queue.
     *
     * @return a Message object containing the link information
     * @throws InterruptedException
     */
    public Message pull(){
        int queueSize = current.size();
        if (queueSize>1000){
            logger.info(queueSize);
        }
        return new Message(current.poll());
    }

    public void push(Message message) {
        int queueSize = current.size();
        if (queueSize>1000){
            logger.info(queueSize);
        }
        current.add(message.getURL().toString());
    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }

}
