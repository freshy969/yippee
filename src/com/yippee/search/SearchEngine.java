package com.yippee.search;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.indexer.model.HitList;
import com.yippee.pastry.message.ResultMessage;
import com.yippee.util.SocketQueue;

public class SearchEngine {
    /**
     * For handling QueryMessages
     */   
    private static SocketQueue socketQueue;
    private static HashMap<UUID, String> queryMap;
    private static HashMap<UUID, Socket> socketMap;
    private HashMap<String, DocEntry> matchedPages;
    private HashMap<String, Float> tfidf;
    
    private ArrayList<HitList> results;
    private ArrayList<ResultMessage> resultMessages;
    
	public SearchEngine(ArrayList<ResultMessage> resultMessages) {
		results = new ArrayList<HitList>();
		this.resultMessages = resultMessages;
		
		matchedPages = new HashMap<String, DocEntry>();
		tfidf = new HashMap<String, Float>();
	}

	public void init() {
		for (int i = 0; i < resultMessages.size(); i++) {
			HitList tmp = resultMessages.get(i).getHitList();
			if(tmp != null)
				results.add(tmp);
		}
	}
	
	public void calculateTfidf() {
		for (int i = 0; i < results.size(); i++) {
			HitList hitlist = results.get(i);

			float df = (float) Math.log(10000.0/hitlist.getDf());
			
			// Calculate ATF values
			HashMap<String, Float> atf = hitlist.getAtfMap();
			if (atf != null) {
				Set<String> a_keys = atf.keySet();
				Iterator<String> a_iter = a_keys.iterator(); 
				
				while(a_iter.hasNext()) {
					String doc = a_iter.next();
	
					float tmp = 0;
					
					// Previous TF-IDF value
					if (tfidf.containsKey(doc))
						tmp = tfidf.get(doc);
					
					// ATF value
					float a_value = atf.get(doc);
					
					// Increment + store
					tfidf.put(doc, (float) (tmp + df * a_value * 0.6));
				}
			}
			
			
			// Calculate TF values
			HashMap<String, Float> tf = hitlist.getTfMap();
			if (tf != null) {
				Set<String> t_keys = tf.keySet();
				Iterator<String> t_iter = t_keys.iterator(); 
				while(t_iter.hasNext()) {
					String doc = t_iter.next();
	
					float tmp = 0;
					
					// Previous TF-IDF value
					if (tfidf.containsKey(doc))
						tmp = tfidf.get(doc);
					
					// ATF value
					float t_value = tf.get(doc);
					
					// Increment + store
					tfidf.put(doc, (float) (tmp + df * t_value * 0.4));
				}
			}
		}
		
		System.out.println(tfidf);
	}
	
	/**
	 * Get all the documents matched in collection.
	 * 
	 * @param URLs
	 */
	public void getDocs(ArrayList<String> URLs) {
		Set<String> keys = tfidf.keySet();
		Iterator<String> iter = keys.iterator();

	}
	
	public ArrayList<DocEntry> getRankings() {
		return null;
	}
}
