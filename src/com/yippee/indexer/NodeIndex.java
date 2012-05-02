package com.yippee.indexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.model.Hit;
import com.yippee.db.indexer.model.HitList;

public class NodeIndex {
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(NodeIndex.class);
	
	private HashMap<String, HitList> wordIndex;
	private int capacity = 3;
	private Lexicon lexicon;
	private HashMap<String, byte[]> lexiconMap;
	
	public NodeIndex() {
		wordIndex = new HashMap<String, HitList>();
		lexicon = new Lexicon("doc/lexicon.txt");
		lexiconMap = lexicon.getLexiconMap();
	}
	
	/**
	 * adds all hits from a document into wordIndex
	 * if word isnt there, make new entry. otherwise append the Hit
	 * 
	 * @param hits
	 */
	public synchronized void addAllHits(HashMap<String, ArrayList<Hit>> hits) {
		Set<String> keys = hits.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			
			String word = iter.next();
	
			ArrayList<Hit> hitList = hits.get(word); 
			
			HitList list;
			
			if(wordIndex.containsKey(word)){
				list = wordIndex.get(word);
			} else {
				list = new HitList(word);
			}
			
//			hit.setWordId(lexicon.);
						
			list.addHitList(hitList);
			
			wordIndex.put(word, list);
			
//			if (wordIndex.size() > capacity) {
//				sendWordsToRing();
//				new HashMap<String, ArrayList<Hit>>();
//			}
		}
		
	}
	
	public synchronized HitList getHitList(String word){
		return wordIndex.get(word);
	}

	public synchronized void printIndex(){		
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
//			System.out.println("[" + word + "=" + wordIndex.get(word).size() + "]");
			logger.info("[" + word + /*lexiconMap.get(word) +*/ "=" + wordIndex.get(word).getHitList().size() + "]");
		}
	}
	
	public synchronized void sendWordsToRing() {
		// HASH keys
		
		// Send keys
		printIndex();		
	}
	
	public synchronized void printAll(){
		
		Set<String> keys = wordIndex.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext()) {
			String word = iter.next();
			System.out.println("[" + word + "=" + wordIndex.get(word).getHitList().size() + "]");
		}
		
	}
	
}