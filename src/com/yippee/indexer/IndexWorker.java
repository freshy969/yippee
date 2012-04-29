package com.yippee.indexer;

import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.indexer.model.AnchorHit;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
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
	DocAugManager dam;
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
		dam = new DocAugManager();
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
				
			System.out.println("Retrieved: " + docAug.getId());
			Parser parser = new Parser();
			FancyExtractor fe = new FancyExtractor(docAug.getId());
	    	
	    	Document doc = null;
			try {
				doc = parser.parseDoc(docAug);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	fe.extract(docAug.getUrl(), doc);
	    	
	    	// Read hits test
	    	ArrayList<Hit> hitList = fe.getHitList();
	    	nodeIndex.addAllHits(hitList);
	    	
/*	    	Lexicon lexicon = new Lexicon("db/test","doc/lexicon.txt");
	    	
	    	for (int i = 0; i < hitList.size(); i++) {
	    		Hit hit = hitList.get(i);
	    		
	    		System.out.print("[" + hit.getPosition() + "]");
	    		
	    		if(hit.isBold())
	    			System.out.print("[BOLD]");
	    		
	    		if(hit.isItalicize())
	    			System.out.print("[ITAL]");
	    	
	    		System.out.println(": " + hit.getWord());	
	    	}*/
	    	
	    	
	    	hitList = fe.getAnchorList();
	 
/*	    	// Read anchors test
	    	for (int i = 0; i < hitList.size(); i++) {
	    		AnchorHit hit = (AnchorHit) hitList.get(i);
	    		
	    		System.out.print("[" + hit.getPosition() + "]");
	    		
	    		if(hit.isBold())
	    			System.out.print("[BOLD]");
	    		
	    		if(hit.isItalicize())
	    			System.out.print("[ITAL]");
	    	
	    		System.out.println(": " + hit.getWord());
	    		
	    	}

	    	// Read title test
	    	System.out.println("Title: " + fe.getTitle());*/
	    }
	}
}
