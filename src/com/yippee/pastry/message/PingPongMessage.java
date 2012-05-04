package com.yippee.pastry.message;

import org.apache.log4j.Logger;
import rice.p2p.commonapi.NodeHandle;

public class PingPongMessage {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PastryMessage.class);
    /**
     * The handle of the node from which the message was sent
     */
    NodeHandle from;
    /**
     * The actual content of the message
     */
    String content;
    /**
     * The response of the message
     */
    boolean wantResponse = true;

    /**
     * The message constructor (needs to keep the handle from which it was sent
     * and the actual content to deliver)
     *
     * @param from the handle of the node from which it was sent
     * @param content the actual content to deliver
     */
    public PingPongMessage(NodeHandle from, String content) {
        this.from = from;
        this.content = content;
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
     * Get the content
     *
     * @return the content string
     */
    public String getContent() {
        return content;
    }

    /**
     * Setup content
     *
     * @param content the content string
     */
    public void setContent(String content) {
        this.content = content;
    }

}
