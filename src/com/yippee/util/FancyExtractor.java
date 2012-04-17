package com.yippee.util;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yippee.indexer.HitFactory;
import com.yippee.indexer.Lexicon;
import com.yippee.indexer.WordStemmer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * TODO: Guys, could you run FancyExtractor against LinExtractor Test cases
 * TODO: so that we make sure that we extract the same ids? (URLs)
 */
public class FancyExtractor {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FancyExtractor.class);
	ArrayList<String> links;
	ArrayList<String> text;
	Stack<String> format;
	boolean bold, ital, anchor;
	WordStemmer stemmer;
	HitFactory hitFactory;
	Lexicon lexicon;
	String docId;
	int pos = 0;
	
	public FancyExtractor(String docId) {
		links = new ArrayList<String>();
		text = new ArrayList<String>();
		format = new Stack<String>();
		bold = false;
		ital = false;
		anchor = false;
		
		stemmer = new WordStemmer();
		lexicon = new Lexicon();
		hitFactory = new HitFactory(lexicon);
		this.docId = docId;
	}
	
	/**
	 * Recursively extracts all <a> tags in an HTML document and returns them as a list to be added to the queue to be crawled
	 * 
	 * @param url URL of the document, to evaluate relative links
	 * @param parent the parent node of this node
	 * @return All links found in this branch
	 */
	protected void extract (String url, Node parent) {
		
		NodeList nodes = parent.getChildNodes();
//		ArrayList<String> links = new ArrayList<String>(); 
		
		for (int z = 0; z < nodes.getLength(); z++) {
			Node child = nodes.item(z);
			
			if (child.getNodeName().equals("a")) {
				// Links
				NamedNodeMap attr = child.getAttributes();
				
				if (attr.getNamedItem("href") != null) {
					// Found a link
					String link =  attr.getNamedItem("href").getNodeValue();
					
					// Prepend url if relative 
					if (!link.startsWith("http")) {
						int tmp = url.lastIndexOf("/");
						url = url.substring(0, tmp + 1);
						link = url + link;
					}
					
//					System.out.println("queuing link: " + link);
					links.add(link);
					format.push("a");
//					System.out.println("[PUSH]: " + child.getNodeName());
					anchor = true;
				}				
			} else if (child.getNodeName().equals("b") || child.getNodeName().equals("strong")) {
				// Bolded
				format.push("b");
//				System.out.println("[PUSH]: " + child.getNodeName());
				bold = true;
			} else if (child.getNodeName().equals("i") || child.getNodeName().equals("em")) {
				// Italicized
				format.push("i");
//				System.out.println("[PUSH]: " + child.getNodeName());
				ital = true;
			} else if (child.getNodeType() == Node.TEXT_NODE) {
				// Text
				
				String sentence = child.getNodeValue();
				sentence = sentence.replaceAll("\\W", " ");
				String[] stemlist = stemmer.stemList(sentence.split("\\s+"));

				for (int i = 0; i < stemlist.length; i++) {
					System.out.print("[" + (pos+i) + "]");
			
					if (anchor) 
						System.out.print("[ANCH]");
					
					if (ital)
						System.out.print("[ITAL]");
					
					if (bold)				
						System.out.print("[BOLD]");
			
					System.out.println(": " + stemlist[i]);
				}
				
				pos += stemlist.length;
				
				text.add(child.getNodeValue());
			} else {
				// Other
				format.push(child.getNodeName());
//				System.out.println("[PUSH]: " + child.getNodeName());

			}
			
			// Recursively find more links
			if (child.hasChildNodes()) {
				extract(url, child);
			}
		}
		
				
		if(!format.isEmpty()) {
			String tag = format.pop();	
//			System.out.println("[POP]: " + tag);
			
			if ("a".equals(tag)) {
				anchor = false;
			} else if ("b".equals(tag)) {
				bold = false;
			} else if ("i".equals(tag)) 
				ital = false;
		}		
		
	}
	
	
	
	public ArrayList<String> getLinks() {
		return links;
	}
	
	public ArrayList<String> getText() {
		return text;
	}
}
