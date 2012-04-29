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
	String id;
	//

	/**
	 * Default Constructor for Berkeley DB
	 */
	public NodeState() {}
	
	public NodeState(Id id){
		this.id = new String(id.toByteArray());
	}
	
	public Id getNodeId(){
		return Id.build(this.id.getBytes());
	}
	
}
