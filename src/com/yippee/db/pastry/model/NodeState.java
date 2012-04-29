package com.yippee.db.pastry.model;


import rice.p2p.commonapi.Id;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class NodeState {
	
	@PrimaryKey
	int version;
	String idString;

	/**
	 * Default Constructor for Berkeley DB
	 */
	public NodeState() {}
	
	public NodeState(Id id){
		this.idString = id.toStringFull();
	}
	
	public String getNodeIdString(){
		return idString;
	}
	
}
