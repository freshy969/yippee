package com.yippee.db.crawler.model;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class FrontierSavedState {

	@PrimaryKey
	int version;
	Map<Integer, Set<String>> prioritySets;
	
	//Default constructor for Berkeley
	public FrontierSavedState(){}
	
	public FrontierSavedState(int version, Map<Integer, Queue<URL>> queues) {
		this.version = version;

		prioritySets = new HashMap<Integer, Set<String>>();
		
		//For each queue
		for(Integer i : queues.keySet()){
			Queue<URL> queue = queues.get(i);
			Set<String> prioritySet = new HashSet<String>();
			
			//for each URL in the queue
			for(int j = 0; j < queue.size(); j++){
				URL url = queue.poll();
				prioritySet.add(url.toString());
			}
			
			prioritySets.put(i, prioritySet);
		}	
	}
	
	public int getVersion(){
		return version;
	}
	
	public Map<Integer, Set<String>> getPrioritySets(){
		return prioritySets;
	}
	
}
