package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import org.apache.log4j.Logger;

/**
 * This class implements a spider worker,the building block of the Araneae
 * ThreadPool. All workers share the same task queue in order to handle
 * crawling URLs from the URLFrontier at will.
 */
public class Spider implements Runnable {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Spider.class);

    /**
     * Default constructor
     *
     * @param urlFrontier
     * @param id
     * @param spiders
     * @param araneae
     */
    public Spider(URLFrontier urlFrontier, String id, Spider[] spiders, Araneae araneae) {

    }

    /**
     * The run method is a standard entry point to run or execute in each
     * worker  thread. The Runnable  interface defines  this method, run,
     * meant to contain the code executed in the thread.
     *
     * In our case, when the spider runs simply downloads page content, based
     * on a number of conditions.
     */
    public void run() {

    }
}
