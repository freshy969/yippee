package com.yippee.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
//		dam = new DocAugManager("db");
	}

	public void run() {
		
//		while(true) {
			DocAug docAug = getNextDoc();
			Parser parser = new Parser();
			LinkTextExtractor linkEx = new LinkTextExtractor();
			
			try {
				parser.parseDoc(docAug);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<String> text = linkEx.getText();
			
			//TODO Store these to their appropriate places, such that PageRank/Search Engine can use them later.
			ArrayList<String> lexicon = createLexicon(text);
			ArrayList<String> links = linkEx.getLinks();
//		}
	}
	
	/**
	 * Gets the next DocAug from the document queue. 
	 */
	public DocAug getNextDoc() {
		DocAug docAug = dam.poll();
		return docAug;
	}
	
	/**
	 * Parses the entire text content of a document, filtering text for non-alphanumerical symbols, and parsing them through the stemmed results.
	 * 
	 * @param text
	 * @return
	 */
	public ArrayList<String> createLexicon(ArrayList<String> text) {
		Lexicon lexicon = new Lexicon();
		WordStemmer stemmer = new WordStemmer();
		
		for (int i = 0; i < text.size(); i++) {
			String sentence = text.get(i);
			sentence = sentence.replaceAll("\\W", " ");
			String[] stemlist = stemmer.stemList(sentence.split("\\s+"));
			
			lexicon.addListToLexicon(stemlist);
		}
		
		return lexicon.getLexicon();
	}
}
