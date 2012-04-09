package com.yippee.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.DocAug;

/**
 * @author tdu2 The Indexer continually polls the database until no documents
 *         are left, then waits until notified that more documents have been put
 *         into the queue by the Crawler. The Indexer also calls the appropriate
 *         methods for processing the document, calculating corresponding information
 *         retrieval metrics, and storage of the results.
 * 
 */
public class Indexer extends Thread {
	DocAugManager dam;
	
	public Indexer() {
		dam = new DocAugManager();
	}

	public void run() {
		DocAug docAug = getNextDoc();
		
		Parser parser = new Parser();
		try {
			Document doc = parser.parseDoc(docAug);
			LinkTextExtractor extractor = new LinkTextExtractor();
			extractor.extract(docAug.getUrl(), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the next DocAug from the document queue. 
	 */
	public DocAug getNextDoc() {
		DocAug docAug = dam.poll();
		return docAug;
	}
}
