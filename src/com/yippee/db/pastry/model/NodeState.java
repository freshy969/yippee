package com.yippee.db.pastry.model;

import rice.pastry.Id;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class NodeState {
	
	@PrimaryKey
	int version;
	
	//NodeID? (is it even serializable?)
	//String?
	Id id;
	//

	/**
	 * Default Constructor for Berkeley DB
	 */
	public NodeState() {}
	
	public NodeState(Id id){
		this.id = id;
	}
	
	public Id getNodeId(){
		return id;
	}
	
}
