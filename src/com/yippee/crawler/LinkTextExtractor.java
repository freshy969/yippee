package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LinkTextExtractor {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LinkTextExtractor.class);
	ArrayList<String> links;
	ArrayList<String> text;
	
	public LinkTextExtractor() {
		links = new ArrayList<String>();
		text = new ArrayList<String>();
	}

    /**
     * Do a number of housekeeping for .html pages:
     *  *   Tidy-up markup
     *  *   Extract links
     *  *   normalize links
     *
     *  .. and return link urls
     */
    public ArrayList smartExtract(URL url, String content) throws CrawlerException {
        String path = url.getPath();
        String responseText = "";
        ArrayList anchors = new ArrayList<String>();
        System.out.println("Path to tidyUp:" + path);
        if (!path.contains(".") || path.substring(path.lastIndexOf(".")).contains("htm")) {
            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            tidy.setDocType("strict");
            tidy.setMakeClean(true);
            tidy.setQuiet(false);
            tidy.setIndentContent(true);
            tidy.setSmartIndent(true);
            tidy.setIndentAttributes(true);
            tidy.setShowWarnings(false);
            tidy.setShowErrors(0);
            tidy.setWord2000(true);
            // Even if errors where found, try to parse as much as possible!
            tidy.setForceOutput(true);
            //tidy.setWrapAttVals(true);
            //tidy.setWraplen(99999999);
            Document document = tidy.parseDOM(is, os);
            // the number of errors that occurred in the most recent parse operation.
            if (tidy.getParseErrors() > 0 ){
                throw new CrawlerException();
            }
            //
            //document.normalize();
            NodeList links = document.getElementsByTagName("a");
            //TODO: grab qualified name
            System.out.println("No of links: " + links.getLength());
            for (int i = 0; i <links.getLength(); i++) {
                logger.info("URL:" + i);
                Node node = links.item(i).getAttributes().getNamedItem("href");
                if (node == null) {
                    continue;
                }
                //System.out.println(links.getLength() + "\t"+links.item(i).getLocalName());
                if ((node.getNodeValue() != null) && (!node.getNodeValue().equals(""))) {
                    if (node.getNodeValue().startsWith("http")) {
                        anchors.add(node.getNodeValue());      //getAttributes("href");
                    } else {
                        try {
                            anchors.add(resolve(url.toString(), node.getNodeValue()));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            System.out.println("Error extacting URL!");
                        }
                    }
                } else {
                    System.out.println("There was a null href?");
                }
            }
            responseText = os.toString();
        }
        responseText = responseText.replaceAll("<!DOCTYPE((.|\n|\r)*?)\">", "");
        return anchors;
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
			
			// Links
			if (child.getNodeName().equals("a")) {
				NamedNodeMap attr = child.getAttributes();
				
				if (attr.getNamedItem("href") != null) {
					// Found a link
					String link =  attr.getNamedItem("href").getNodeValue();
					
					
					try{
						// Resolve url if relative
						if (!link.startsWith("http://") && !link.startsWith("https://")) {
							link = resolve(url, link);
						}
						
//						System.out.println("queuing link: " + link);
						links.add(link);
					}catch(MalformedURLException e){
						//Do nothing with this link, absolute url could not be formed
						//TODO Add logging here
					}
					

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
