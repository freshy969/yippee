package com.yippee.indexer;

import com.yippee.db.crawler.model.DocAug;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

/**
 * @author tdu2 This class takes a DocAug object from the manager queue and
 *         parses out the relevant parameters for the indexer.
 *
 * @author nvasilakis changed parseDoc to public
 * 
 */
public class Parser {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Parser.class);
	Document doc;
	
	public Parser() {	
	}
	
	/**
	 * Parses the HTML content of DocAug object and returns it as Document DOM.
	 * @throws FileNotFoundException 
	 */
	public Document parseDoc(DocAug docAug) {
		
		String content = docAug.getDoc();
		
        ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
		
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowErrors(0);
		tidy.setShowWarnings(false);
				
		// Create DOM object from HTML object
		Document doc = tidy.parseDOM(is, null);
		return doc;	
	}
		
}
