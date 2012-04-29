package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;

/**
 * This class implements the (fixed-size) thread pool (Araneae is the order of
 * spiders, in our case implementing a pool of multiple spiders). It always has a specified
 * number of threads running; if a thread is somehow terminated while it's still
 * in use, it's automatically replaced with a new thread. Tasks are submitted to
 * the pool via an internal  URLFrontier, which  holds extracted URLs and offers
 * them  at a  priority-based model (handled internally). The simplest priority-
 * based model is a FIFO model.
 *
 * Using worker threads minimizes the overhead due to thread creation and avoids
 * degrading serving speed in high loads due to thrashing.
 */
public class Araneae {
     /** Create logger in the Log4j hierarchy named by by software component*/
    static Logger logger = Logger.getLogger(Araneae.class);
    /** Thread pool */
    Thread[] threads;
    /** Spider (worker) pool */
    Spider[] spiders;

    /**
     * Create a new thread pool of given size, with a shared URLFrontier among
     * all spiders.
     *
     * @param urlFrontier the shared URL resource queue
     */
    public Araneae(URLFrontier urlFrontier){
        int size = Configuration.getInstance().getCrawlerThreadNumber();
        logger.debug("Creating " + size +" threads");
        spiders = new Spider[size];
        threads = new Thread[size];
        for (int i=0; i<size; i++){
            String id = "Spider #" + i;
            Spider spider = new Spider(urlFrontier,id,spiders,this);
            spiders[i] = spider;
            threads[i] = new Thread(spider, id);
            threads[i].start();
        }
    }

    /**
     * Shuts down all threads gracefully
     *
     * @return true
     */
    public boolean shutdown(){
        logger.info(" Shutting down idle threads");
        for (int i=0; i<threads.length;i++){
            logger.info(" Shutting down thread" + threads[i].getName());
            threads[i].interrupt();
        }
        return true;
    }


}
