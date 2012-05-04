package com.yippee.indexer;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yippee.db.indexer.model.AnchorHit;
import com.yippee.db.indexer.model.Hit;

import java.net.MalformedURLException;
import java.net.URL;
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
		anchorList = new ArrayList<Hit>();
		this.docId = docId;
	}
	
	/**
	 * Recursively extracts all <a> tags in an HTML document and returns them as a list to be added to the queue to be crawled
	 * 
	 * @param url URL of the document, to evaluate relative links
	 * @param parent the parent node of this node
	 * @return All links found in this branch
	 */
	public void extract (String url, Node parent) throws MalformedURLException {
		
		NodeList nodes = parent.getChildNodes();
//		ArrayList<String> links = new ArrayList<String>(); 
		
		for (int z = 0; z < nodes.getLength(); z++) {
			Node child = nodes.item(z);
			
			// Skip bad tags which we can't recognize!
			if (child.getNodeName() == null)
				continue;
			
			if (child.getNodeName().equals("a")) {
				// Links
				NamedNodeMap attr = child.getAttributes();
				
				if (attr.getNamedItem("href") != null) {
					// Found a link
					String link =  attr.getNamedItem("href").getNodeValue();
					
					if (!link.startsWith("http"))
						link = resolve(docId, link);
					
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
					
					Hit hit = new Hit(docId, word, pos+i);
						
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
					
					if (anchor) {
						// LINK NEEDS TO BE TESTED!
						hit = new AnchorHit(docId, word, i, links.get(links.size() - 1));
						list.add(hit);
						anchorList.add(hit);
					} 
					
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
		String result = input.replaceAll("[,:;@/!<>#\\.\\*\\?\\[\\]\\(\\)]| - ", " ");
		
		return result;
	}
	
	public String resolve(String baseURL, String relativeURL) throws MalformedURLException {
		//Does baseURL end in slash?
		if(baseURL.endsWith("/")){
			if(relativeURL.startsWith("/")){
				//Case 1A
				//Use URL class to get the root of baseURL (i.e., up to the end of the host)
				return relativeStartsWithSlash(baseURL, relativeURL);
				
			} else if(relativeURL.startsWith("?") || relativeURL.startsWith("#")){
				return baseURL + relativeURL;
			} else{
				//RelativeURL doesn't start with slash
				//Case 1 B, C & D
				return baseURL + relativeURL;
			}
		}else {
			//baseURL Doesn't End With Slash
			if(relativeURL.startsWith("/")){
				//Case 2A (Same as case 1A
				return relativeStartsWithSlash(baseURL, relativeURL);
				
			} else if(relativeURL.startsWith("?") || relativeURL.startsWith("#")){
				return baseURL + relativeURL;
			}else{
				//Cases 2 B, C & D
				//base Does NOT end in slash
				URL url = new URL(baseURL);
				//System.out.println("Stripped baseURL: " + baseURL.substring(url.getProtocol().length() + 3));
				
				if( baseURL.substring(url.getProtocol().length() + 3).contains("/") ){
					
					//Has slash other than protocol
					//Append relative URL to last slash in baseURL
					return baseURL.substring(0, baseURL.lastIndexOf('/') + 1) + relativeURL;
					
				}else {
					//No other / besides protocol
					return baseURL + "/" + relativeURL;
				}
				
			}
		}
		
	}

	private String relativeStartsWithSlash(String baseURL, String relativeURL)
			throws MalformedURLException {
		URL url = new URL(baseURL);
		int port = url.getPort();
		return url.getProtocol() + "://" + url.getHost() + ((port == 80 || port == -1) ? "" : ":" + url.getPort()) + relativeURL;
	}
}
