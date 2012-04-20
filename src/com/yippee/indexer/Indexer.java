package com.yippee.indexer;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.AnchorHit;
import com.yippee.db.model.DocAug;
import com.yippee.db.model.Hit;
import com.yippee.util.Configuration;
import com.yippee.util.FancyExtractor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author tdu2 The Indexer continually polls the database until no documents
 *         are left, then waits until notified that more documents have been put
 *         into the queue by the Crawler. The Indexer also calls the appropriate
 *         methods for processing the document, calculating corresponding information
 *         retrieval metrics, and storage of the results.
 * 
 */
public class Indexer extends Thread {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Indexer.class);
	DocAugManager dam;
	long pollDelay;
	
	public Indexer() {
		dam = new DocAugManager("db/test/indexer");
	}

	public void run() {
		
		while(true) {
			pollDelay = 500;

			logger.info("TJ!");
			DocAug docAug = null; 
			
			while(docAug == null) {
				try {
					System.out.println("Waiting... " + pollDelay + "ms");
					this.sleep(pollDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				docAug = dam.poll();
				
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
	    	
	    	Lexicon lexicon = new Lexicon("db/test","doc/lexicon.txt");
	    	
	    	for (int i = 0; i < hitList.size(); i++) {
	    		Hit hit = hitList.get(i);
	    		
	    		System.out.print("[" + hit.getPosition() + "]");
	    		
	    		if(hit.isBold())
	    			System.out.print("[BOLD]");
	    		
	    		if(hit.isItalicize())
	    			System.out.print("[ITAL]");
	    	
	    		System.out.println(": " + lexicon.getWord(hit.getWordId()));	
	    	}
	    	
	    	
	    	hitList = fe.getAnchorList();
	 
	    	// Read anchors test
	    	for (int i = 0; i < hitList.size(); i++) {
	    		AnchorHit hit = (AnchorHit) hitList.get(i);
	    		
	    		System.out.print("[" + hit.getPosition() + "]");
	    		
	    		if(hit.isBold())
	    			System.out.print("[BOLD]");
	    		
	    		if(hit.isItalicize())
	    			System.out.print("[ITAL]");
	    	
	    		System.out.println(": " + lexicon.getWord(hit.getWordId()));
	    		
	    	}

	    	// Read title test
	    	System.out.println("Title: " + fe.getTitle());
	    }
	}
}
