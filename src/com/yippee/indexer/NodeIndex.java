package com.yippee.indexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.Hit;
import com.yippee.db.indexer.model.HitList;
import com.yippee.util.Configuration;

public class NodeIndex {
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(NodeIndex.class);
	
	private HashMap<String, ArrayList<Hit>> wordIndex;
	private int capacity = 10;
	//private Lexicon lexicon;
	//private HashMap<String, byte[]> lexiconMap;
	
	public NodeIndex() {
		wordIndex = new HashMap<String, ArrayList<Hit>>();
		//lexicon = new Lexicon("doc/lexicon.txt");
		//lexiconMap = lexicon.getLexiconMap();
	}
	
	/**
	 * adds all hits from a document into wordIndex
	 * if word isnt there, make new entry. otherwise append the Hit
	 * 
	 * @param hitMap
	 */
	public synchronized void addAllHits(HashMap<String, ArrayList<Hit>> hitMap) {
		Set<String> keys = hitMap.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			
			String word = iter.next();
			//if(!lexiconMap.containsKey(word)){} else{
	
			ArrayList<Hit> hitList = hitMap.get(word); 
			
			ArrayList<Hit> list;
			
			if(wordIndex.containsKey(word)){
				list = wordIndex.get(word);
			} else {
				list = new ArrayList<Hit>();
			}
			
//			hit.setWordId(lexicon.);
						
			list.addAll(hitList);
			
			wordIndex.put(word, list);
			}
		//}
			
			if (wordIndex.size() > capacity) {
				logger.info("REACHED CAPACITY, SENDING TO RING");
				sendWordsToRing();
				printIndex();
				wordIndex = new HashMap<String, ArrayList<Hit>>();
			}
		
		
	}
	
	public synchronized ArrayList<Hit> getHitList(String word){
		return wordIndex.get(word);
	}

	public synchronized void printIndex(){		
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
//			System.out.println("[" + word + "=" + wordIndex.get(word).size() + "]");
			logger.info("[" + word + /*lexiconMap.get(word) +*/ "=" + wordIndex.get(word).size() + "]");
		}
	}
	
	public synchronized void sendWordsToRing() {
		// HASH keys
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
			ArrayList<Hit> list = wordIndex.get(word);
			Configuration.getInstance().getPastryEngine().sendList(word,list);
		}
			
	}
	
	public synchronized void printAll(){
		
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
			System.out.println("[" + word + "=" + wordIndex.get(word).size() + "]");
		}
		
	}
	
}