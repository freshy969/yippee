package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

/**
 *
 */
public class MercatorCentralized implements URLFrontier{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(MercatorCentralized.class);

    public Message pull() throws InterruptedException {
        return null;
    }

    public void push(Message message) {

    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }
}
