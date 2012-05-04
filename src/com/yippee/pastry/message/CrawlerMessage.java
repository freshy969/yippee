package com.yippee.pastry.message;

import org.apache.log4j.Logger;

/**
 *
 * An implementation of the pastry application message sent around nodes. The
 * Pastry Message class delivered by pastry, used by the crawler
 */
public class CrawlerMessage implements rice.p2p.commonapi.Message{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(CrawlerMessage.class);
    /**
     * The actual url of the message
     */
    String url;

    /**
     * The message constructor (needs to keep the handle from which it was sent
     * and the actual content to deliver)
     *
     * @param url the actual content to deliver
     */
    public CrawlerMessage(String url) {
        this.url = url;
    }

    /**
     * The priority of the message delivery/route. This is set to default value.
     *
     * @return the priority
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Get the url content
     *
     * @return the url string
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setup url content
     *
     * @param url the string url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
