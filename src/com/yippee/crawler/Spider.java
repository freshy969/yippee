package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.util.Configuration;
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
    private URLFrontier urlFrontier;
    private String id;
    private Spider[] spiders;
    private Araneae araneae;
    private boolean running;

    /**
     * The -not so default- constructor. It keeps references to the whole thread
     * pool of araneae (in order to shutdown if needed from the Frontier), the
     * URLFrontier, its own thread it and the other spiders.
     *
     * @param urlFrontier
     * @param id
     * @param spiders
     * @param araneae
     */
    public Spider(URLFrontier urlFrontier, String id, Spider[] spiders, Araneae araneae) {
        this.urlFrontier = urlFrontier;
        this.id = id;
        this.spiders = spiders;
        this.araneae = araneae;
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
        logger.info("Thread " + Thread.currentThread().getName() + ": Starting");
        while (running && Configuration.getInstance().isUp()) {
            try {
                Message url = urlFrontier.pull();
                //url.getURL()


            } catch (InterruptedException e) {
                //e.printStackTrace();
                logger.info("Thread " + Thread.currentThread().getName() + ": Shutting down..");
                running = false;
                break;
            }

        }

    }
}
