package com.yippee.pastry.message;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.Hit;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 * An implementation of the pastry application message sent around nodes.
 */
public class PastryMessage implements Message {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PastryMessage.class);
    /**
     * The handle of the node from which the message was sent
     * */
    NodeHandle from;
    /**
     * The actual content of the message
     * */
    String content;
    /**
     * The String containing the url
     */
    String referrer;
    /**
     * The response of the message
     */
    boolean wantResponse = true;
    
    ArrayList<Hit> hitList;

    /**
     * The message constructor (needs to keep the handle from which it was sent
     * and the actual content to deliver)
     *
     * @param from the handle of the node from which it was sent
     * @param content the actual content to deliver
     */
    public PastryMessage(NodeHandle from, String content) {
        this.from = from;
        this.content = content;
        this.hitList = new ArrayList<Hit>();
    }
    
    public PastryMessage(NodeHandle from, String word, ArrayList<Hit> hitList){
    	this.from = from;
    	this.content = word;
    	this.hitList = hitList;
    }

    /**
     * The priority of the message delivery/route. This is set to default value.
     *
     * @return the priority
     */
    public int getPriority() {
        return 0;
    }
}
