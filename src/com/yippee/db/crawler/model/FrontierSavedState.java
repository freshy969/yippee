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
	
//	public FrontierSavedState(int version, Map<Integer, Queue<String>> queueStrings){
//		this.version = version;
//
//		prioritySets = new HashMap<Integer, Set<String>>();
//		
//		//For each queue
//		for(Integer i : queueStrings.keySet()){
//			Queue<String> queue = queueStrings.get(i);
//			Set<String> prioritySet = new HashSet<String>();
//			
//			//for each URL in the queue
//			for(int j = 0; j < queue.size(); j++){
//				String urlString = queue.poll();
//				prioritySet.add(urlString);
//			}
//			
//			prioritySets.put(i, prioritySet);
//		}	
//	}
	
	public static FrontierSavedState makeNewFrontierSavedStateStrings(int version, Map<Integer, Queue<String>> queueStrings){
		FrontierSavedState f = new FrontierSavedState();
		f.version = version;
		f.prioritySets = new HashMap<Integer, Set<String>>();
		
		for(Integer i : queueStrings.keySet()){
			Queue<String> queue = queueStrings.get(i);
			Set<String> prioritySet = new HashSet<String>();
			
			//for each URL in the queue
			for(int j = 0; j < queue.size(); j++){
				String urlString = queue.poll();
				prioritySet.add(urlString);
			}
			
			f.prioritySets.put(i, prioritySet);
		}	
		
		
		return f;
	}
	
	public int getVersion(){
		return version;
	}
	
	public Map<Integer, Set<String>> getPrioritySets(){
		return prioritySets;
	}
	
}
