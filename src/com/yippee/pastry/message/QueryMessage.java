package com.yippee.pastry.message;

import java.util.ArrayList;
import java.util.UUID;

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
    String query;
    /**
     * The response of the message
     */
    boolean wantResponse = true;
    /**
     * How to call the socket back once query completes
     */
    UUID socketId;
    
	public QueryMessage(NodeHandle localNodeHandle, String query,
			UUID socketId) {
		// TODO Auto-generated constructor stub
		this.from = localNodeHandle;
		this.query = query;
		this.socketId = socketId;
	}

	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getQuery() {
		return query;
	}
}
