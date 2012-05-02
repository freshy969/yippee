package com.yippee.indexer;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yippee.db.indexer.model.AnchorHit;
import com.yippee.db.indexer.model.Hit;

import java.util.ArrayList;
import java.util.HashMap;
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
	boolean bold, ital, anchor, title;
	WordStemmer stemmer;
	String docId, docTitle;
	int pos = 0;
	HashMap<String, ArrayList<Hit>> hitList;
	ArrayList<Hit> anchorList; 
	
	public FancyExtractor(String docId) {
		links = new ArrayList<String>();
		text = new ArrayList<String>();
		format = new Stack<String>();
		bold = false;
		ital = false;
		anchor = false;
		title = false;
		
		stemmer = new WordStemmer();
		hitList = new HashMap<String, ArrayList<Hit>>();
		anchorList = new ArrayList();
		this.docId = docId;
	}
	
	/**
	 * Recursively extracts all <a> tags in an HTML document and returns them as a list to be added to the queue to be crawled
	 * 
	 * @param url URL of the document, to evaluate relative links
	 * @param parent the parent node of this node
	 * @return All links found in this branch
	 */
	public void extract (String url, Node parent) {
		
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
					
					links.add(link);
					format.push("a");
					anchor = true;
				}				
			} else if (child.getNodeName().equals("b") || child.getNodeName().equals("strong")) {
				// Bolded
				format.push("b");
				bold = true;
			} else if (child.getNodeName().equals("i") || child.getNodeName().equals("em")) {
				// Italicized
				format.push("i");
				ital = true;
			} else if (child.getNodeName().equals("title")) {
				// Title Node
				format.push("title");
				title = true;
			} else if (child.getNodeType() == Node.TEXT_NODE) {
				// Text
				
				String sentence = child.getNodeValue();
				
				if (title){
					docTitle = sentence;
				}
				
				sentence = removePunctuation(sentence).toLowerCase();
				
				String[] stemlist = stemmer.stemList(sentence.split("\\s+"));
				
				boolean[] formatting = {anchor, ital, bold};
				
				for (int i = 0; i < stemlist.length; i++) {
					// Create Hits
					
//					byte[] wordId = lexicon.getWordId(stemlist[i]);
//					
//					if (wordId == null) {
//						lexicon.addNewWord(stemlist[i]);
//						wordId = lexicon.getWordId(stemlist[i]);
//					}
					
					String word = stemlist[i];
					
					Hit hit;
								
					if (anchor) {
						hit = new AnchorHit(docId, word, i, docId);
						anchorList.add(hit);
					} 
						
					hit = new Hit(docId, word, pos+i);
						
					if (ital)
						hit.setItalicize(true);
					
					if (bold)				
						hit.setBold(true);
			
					
					ArrayList<Hit> list;
					
					if (hitList.containsValue(word)) {
						list = hitList.get(word);
						
					} else {
						list = new ArrayList<Hit>();
					}
					
					list.add(hit);
					
					hitList.put(word, list);	
				}
				
				pos += stemlist.length;
				
				text.add(child.getNodeValue());
			} else {
				// Other
				format.push(child.getNodeName());

			}
			
			// Recursively find more links
			if (child.hasChildNodes()) {
				extract(url, child);
			}
		}
		

		if(!format.isEmpty()) {
			String tag = format.pop();	
			
			if ("a".equals(tag)) {
				anchor = false;
			} else if ("b".equals(tag)) {
				bold = false;
			} else if ("i".equals(tag)) { 
				ital = false;
			} else if ("title".equals(tag)) {
				title = false;
			}
		}		
		
	}
	
	public String getTitle() {
		return docTitle;
	}
	
	public HashMap<String, ArrayList<Hit>> getHitList() {
		return hitList;
	}
	
	public ArrayList<Hit> getAnchorList() {
		return anchorList;
	}
	
	public ArrayList<String> getLinks() {
		return links;
	}
	
	public ArrayList<String> getText() {
		return text;
	}
	
	public String removePunctuation(String input) {
		String result = input.replaceAll("[,;@/!<>#\\.\\*\\?\\[\\]\\(\\)]| - ", " ");
		
		return result;
	}
}
