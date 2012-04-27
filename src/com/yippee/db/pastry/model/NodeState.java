package com.yippee.db.pastry.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class NodeState {
	
	@PrimaryKey
	int version;
	
	//NodeID? (is it even serializable?)
	//String?
	//

	/**
	 * Default Constructor for Berkeley DB
	 */
	public NodeState() {}
	
}
