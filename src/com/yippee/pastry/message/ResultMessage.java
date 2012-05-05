package com.yippee.pastry.message;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.HitList;


import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 *  The Pastry Message class delivered by pastry, used by the UI
 */
public class ResultMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8665279840924614173L;
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(ResultMessage.class);
    /**
     * The handle of the node from which the message was sent
     * */
    NodeHandle from;
    /**
     * The actual content of the message
     * */
    String word;
    /**
     * Results (if any)
     */
    HitList hitlist;
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
    
	public ResultMessage(NodeHandle from, HitList hitlist, String word,
			UUID queryId, int queryLength) {
		this.from = from;
		this.hitlist = hitlist;
		this.word = word;
		this.queryId = queryId;
		this.queryLength = queryLength;
	}

	public int getPriority() {
		return 10;
	}

	public String getWord() {
		return word;
	}
	
	public HitList getHitList() {
		return hitlist;
	}
	
	public UUID getQueryID(){
		return queryId;
	}
	
	public int queryLength() {
		return queryLength;
	}
}
