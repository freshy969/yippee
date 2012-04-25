package com.yippee.db.crawler.model;

import java.net.URL;
import java.util.Map;
import java.util.Queue;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class FrontierSavedState {

	@PrimaryKey
	int version;
	Map<Integer, Queue<URL>> priorityToURLs;
	
	//Default constructor for Berkeley
	public FrontierSavedState(){}
	
	public FrontierSavedState(int version, Map<Integer, Queue<URL>> queues) {
		//TODO fill this in
		this.priorityToURLs = queues;
		this.version = version;
	}
	
	
}
