package com.yippee.indexer;

import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.indexer.DocArchiveManager;
import com.yippee.db.indexer.DocEntryManager;
import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author tdu2 The Indexer continually polls the database until no documents
 *         are left, then waits until notified that more documents have been put
 *         into the queue by the Crawler. The Indexer also calls the appropriate
 *         methods for processing the document, calculating corresponding information
 *         retrieval metrics, and storage of the results.
 * 
 */
public class IndexWorker extends Thread {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(IndexWorker.class);
    /**
     * This is the logger which appends for the page rank
     */
    static Logger linkLogger = Logger.getLogger(IndexWorker.class.getName() + ".hadoop");
	DocAugManager dam;
	DocArchiveManager darcm;
	DocEntryManager dem;
	long pollDelay;
	NodeIndex nodeIndex;
	ArrayList<DocAug> archiveHolder;
	
	public IndexWorker(NodeIndex nodeIndex) {
		Configuration.getInstance().setBerkeleyDBRoot("db/test");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        linkLogger.warn("Start writing .hadoop file (csv?)");
		dam = new DocAugManager();
		darcm = new DocArchiveManager();
		dem = new DocEntryManager();
		this.nodeIndex = nodeIndex;
		archiveHolder = new ArrayList<DocAug>();
	}

	@Override
	public void run() {
		
		while(true) {
			pollDelay = 500;
			
			DocAug docAug = null; 
			
			while(docAug == null) {
				try {
//					System.out.println("Waiting... " + pollDelay + "ms");
					Thread.sleep(pollDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!nodeIndex.isArchiveMode()){
					docAug = dam.poll();
				} else {
					docAug = nodeIndex.poll();
				}
				if (pollDelay <= 60000)
					pollDelay *= 2;
		    	if(!nodeIndex.isArchiveMode() && archiveHolder.size()>0) 
	    			addToArchive();
		    	
			}
		//	System.out.println("Retrieved: " + docAug.getId());
//			logger.info("Retrieved: " + docAug.getId());
			Parser parser = new Parser();
			FancyExtractor fe = new FancyExtractor(docAug.getId());
	    	
	    	Document doc = null;
			
	    	try {
	    		doc = parser.parseDoc(docAug);
	    	} catch (StringIndexOutOfBoundsException e) {
				logger.info("StringIndexOutOfBounds in: " + docAug.getUrl());				
	    	}
	
	    	try {
				fe.extract(docAug.getUrl(), doc);
			} catch (MalformedURLException e) {
				logger.info("MalformedURL in: " + docAug.getUrl());				
			} catch (NullPointerException e) {
				//e.printStackTrace();
				logger.info("NullPointerException in: " + docAug.getUrl());
			}
	    	
	   
	    	HashMap<String, ArrayList<Hit>> hitList = fe.getHitList();
	    	hitList = updateDocLength(hitList);
	 	    
	    	// Send hits to ring 
	    	nodeIndex.addAllHits(hitList);
//	    	nodeIndex.printIndex();
	    	
	    	// Write links to file for PageRank
	    	appendLinks(docAug.getUrl(), fe.getLinks());
	    	
	    	String docTitle = fe.getTitle();
	    	
	    	DocEntry docEntry = new DocEntry(docAug.getUrl(),docTitle, null , docAug.getTime());
	    	dem.addDocEntry(docEntry);
	    	if(!nodeIndex.isArchiveMode()) {
	    		archiveHolder.add(docAug);
	    		if(archiveHolder.size()>20)
	    			addToArchive();
	    	}
	    	
	    		//darcm.store(docAug);
	    }		
	}	
	
	public HashMap<String,ArrayList<Hit>> updateDocLength(HashMap<String,ArrayList<Hit>> hitList){
		double docLength = 0;
		Set<String> keys = hitList.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()){
			String word = iter.next();
			docLength += Math.pow(hitList.get(word).size(), 2);
		}
		
		docLength = Math.sqrt(docLength);
		
		keys = hitList.keySet();
		iter = keys.iterator();
		while(iter.hasNext()){
			String word = iter.next();
			ArrayList<Hit> hl = hitList.get(word);
			for(int i=0; i<hl.size(); i++){
				Hit h = hl.get(i);
				h.setDocLength(docLength);
			}
		}
		
		return hitList;
	}
	
	public void addToArchive(){
		for(int i=0; i<archiveHolder.size(); i++){
			darcm.store(archiveHolder.get(i));
		}
		archiveHolder = new ArrayList<DocAug>();
	}

	public void appendLinks(String url, ArrayList<String> links) {
        int numLinks = links.size();
		for (int i = 0; i < links.size(); i++) {
			linkLogger.warn("'"+url + "', '1', '" + links.get(i) +"', '" + numLinks +"'");
		}
	}

    public static void main(String[] args){
        PropertyConfigurator.configure("log/log4j.properties");
        System.out.println("woohoo");
        linkLogger.warn("woohoo");
        logger.warn("woohoo");
    }

}
