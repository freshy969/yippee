package com.yippee.pastry.message;

import java.util.ArrayList;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

import com.yippee.db.indexer.model.Hit;

/**
 * The Pastry Message class delivered by pastry, used by the indexer
 */
public class IndexerMessage implements Message{
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
	    boolean wantResponse = false;
	    
	    ArrayList<Hit> hitList;

	    public IndexerMessage(NodeHandle from, String word, ArrayList<Hit> hitList){
	    	this.from = from;
	    	this.content = word;
	    	this.hitList = hitList;
	    }
	    
	    public ArrayList<Hit> getHitList(){
	    	return hitList;
	    }
	    
	    public String getWord(){
	    	return content;
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
