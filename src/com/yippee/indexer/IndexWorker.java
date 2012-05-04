package com.yippee.indexer;

import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.indexer.DocArchiveManager;
import com.yippee.db.indexer.DocEntryManager;
import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

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
    static Logger linkLogger = Logger.getLogger(IndexWorker.class + ".hadoop");
	DocAugManager dam;
	DocArchiveManager darcm;
	DocEntryManager dem;
	long pollDelay;
	NodeIndex nodeIndex;
	
	public IndexWorker(NodeIndex nodeIndex) {
		Configuration.getInstance().setBerkeleyDBRoot("db/test");
		try {
			this.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        linkLogger.warn("Start writing .hadoop file (csv?)");
		dam = new DocAugManager();
		darcm = new DocArchiveManager();
		dem = new DocEntryManager();
		this.nodeIndex = nodeIndex;
	}

	public void run() {
		
		while(true) {
			pollDelay = 500;
			
			DocAug docAug = null; 
			
			while(docAug == null) {
				try {
//					System.out.println("Waiting... " + pollDelay + "ms");
					this.sleep(pollDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				docAug = dam.poll();
				
				if (pollDelay <= 60000)
					pollDelay *= 2;
			}
				
			logger.info("Retrieved: " + docAug.getId());
			Parser parser = new Parser();
			FancyExtractor fe = new FancyExtractor(docAug.getId());
	    	
	    	Document doc = null;
			
	    	doc = parser.parseDoc(docAug);
	
	    	try {
				fe.extract(docAug.getUrl(), doc);
			} catch (MalformedURLException e) {
				logger.warn("MalformedURL in: " + docAug.getUrl());				
			}
	    	
	   
	    	HashMap<String, ArrayList<Hit>> hitList = fe.getHitList();
	 	    
	    	// Send hits to ring 
	    	nodeIndex.addAllHits(hitList);
	    	nodeIndex.printIndex();
	    	
	    	// Write links to file for PageRank
	    	appendLinks(docAug.getUrl(), fe.getLinks());
	    	
	    	String docTitle = fe.getTitle();
	    	
	    	DocEntry docEntry = new DocEntry(docTitle, docAug.getUrl(), null , docAug.getTime());
	    	dem.addDocEntry(docEntry);
	    	darcm.store(docAug);
	    }		
	}	

	public void appendLinks(String url, ArrayList<String> links) {
		for (int i = 0; i < links.size(); i++) {
			logger.warn(url + ", " + links.get(i));
		}
	}
}
