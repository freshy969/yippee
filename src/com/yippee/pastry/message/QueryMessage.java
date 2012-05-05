package com.yippee.pastry.message;

import java.util.UUID;

import org.apache.log4j.Logger;


import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 *  The Pastry Message class delivered by pastry, used by the UI
 */
public class QueryMessage implements Message {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3698500143061850653L;
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(QueryMessage.class);
    /**
     * The handle of the node from which the message was sent
     * */
    NodeHandle from;
    /**
     * The actual content of the message
     * */
    String word;
    /**
     * The response of the message
     */
    boolean wantResponse = true;
    /**
     * How to call the socket back once query completes
     */
    UUID queryId;
    /**
     * How to check whether query has been fulfilled
     */
    int queryLength;
    /**
     * If the query was for a doc
     */
    boolean docQuery;
    
	public QueryMessage(NodeHandle from, String word,
			UUID socketId, int queryLength) {
		this.from = from;
		this.word = word;
		this.queryId = socketId;
		this.queryLength = queryLength;
	}

	public QueryMessage(NodeHandle from, String url,
			UUID queryId, int queryLength, boolean docQuery) {
		this.from = from;
		this.word = url;
		this.queryId = queryId;
		this.docQuery = true;
		this.queryLength = queryLength;
	}
	
	public int getPriority() {
		return 9;
	}
	
	public NodeHandle getNodeHandle() {
		return from;
	}
	
	public String getWord() {
		return word;
	}
	
	public UUID getQueryID(){
		return queryId;
	}
	
	public int queryLength() {
		return queryLength;
	}
	
	public boolean isDocQuery() {
		return docQuery;
	}
}
