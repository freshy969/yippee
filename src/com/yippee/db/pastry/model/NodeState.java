package com.yippee.db.pastry.model;

import com.sleepycat.persist.model.Entity;

@Entity
public class NodeState {
	
	int version;
	
	//NodeID? (is it even serializable?)
	//String?
	//

	/**
	 * Default Constructor for Berkeley DB
	 */
	public NodeState() {}
	
}
