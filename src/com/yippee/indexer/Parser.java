package com.yippee.indexer;

import com.yippee.db.model.DocAug;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author tdu2 This class takes a DocAug object from the manager queue and
 *         parses out the relevant parameters for the indexer.
 *
 * @author nvasilakis changed parseDoc to public
 * 
 */
public class Parser {
	Document doc;
	
	public Parser() {	
	}
	
	/**
	 * Parses the HTML content of DocAug object and returns it as Document DOM.
	 * @throws FileNotFoundException 
	 */
	public Document parseDoc(DocAug docAug) throws FileNotFoundException {
		File tmpFile = new File("tmp.file");
		PrintWriter fileOut;
		
		try {
			fileOut = new PrintWriter(tmpFile);
			fileOut.print(docAug.getDoc());
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
				
		// Create DOM object from HTML object
		Document doc = tidy.parseDOM(new FileInputStream(tmpFile), null);
		return doc;	
	}
		
}
