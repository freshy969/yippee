package com.yippee.pastry.message;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.Hit;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 *  The Pastry Message class delivered by pastry, used by the UI
 */
public class QueryMessage implements Message {
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

    

    
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
