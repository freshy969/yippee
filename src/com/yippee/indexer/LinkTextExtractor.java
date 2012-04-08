package com.yippee.indexer;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LinkTextExtractor {
	ArrayList<String> links;
	ArrayList<String> text;
	
	public LinkTextExtractor() {
		links = new ArrayList<String>();
		text = new ArrayList<String>();
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
			
			// Links
			if (child.getNodeName().equals("a")) {
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
				}				
			} else if (child.getNodeType() == Node.TEXT_NODE) {
				text.add(child.getNodeValue());
			}
			
			// Recursively find more links
			if (child.hasChildNodes()) {
				extract(url, child);
			}
		}
	}
	
	public ArrayList<String> getLinks() {
		return links;
	}
	
	public ArrayList<String> getText() {
		return text;
	}
}
