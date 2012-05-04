package com.yippee.pastry.message;

import org.apache.log4j.Logger;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 *
 * An implementation of the pastry application message sent around nodes. The
 * Pastry Message class delivered by pastry, used by the crawler
 */
public class CrawlerMessage implements Message{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(CrawlerMessage.class);
    /**
     * The handle of the node from which the message was sent
     */
    NodeHandle from;
    /**
     * The actual url of the message
     */
    String url;
    /**
     * The response of the message
     */
    boolean wantResponse = true;

    /**
     * The message constructor (needs to keep the handle from which it was sent
     * and the actual content to deliver)
     *
     * @param from the handle of the node from which it was sent
     * @param url the actual content to deliver
     */
    public CrawlerMessage(NodeHandle from, String url) {
        this.from = from;
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
     * Get the node handle
     *
     * @return the node handle
     */
    public NodeHandle getFrom() {
        return from;
    }

    /**
     * Set the from handle.
     *
     * @param from the node handle to be sent
     */
    public void setFrom(NodeHandle from) {
        this.from = from;
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
