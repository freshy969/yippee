package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
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
    public ArrayList<String> smartExtract(URL url, String content) throws CrawlerException {
    	logger.info("smartExtract started on: " + url);
    	
        String path = url.getPath();
        //TODO What is path
        if(path == null) path = "";
       // logger.info("path: '" + path + "'");
        
       // String responseText = "";
        ArrayList<String> anchors = new ArrayList<String>();
        //System.out.println("Path to tidyUp:" + path);
        if (!path.contains(".") || path.substring(path.lastIndexOf(".")).contains("htm")) {

            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
          //  ByteArrayOutputStream os = new ByteArrayOutputStream();
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            tidy.setDocType("strict");
            tidy.setMakeClean(true);
            tidy.setQuiet(true);
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
            Document document = null;
            try{
            	document = tidy.parseDOM(is, null);
            } catch(StringIndexOutOfBoundsException e){
            	logger.warn("Tidy: StringIndexOutOfBoundsException");
            	logger.debug("Tidy: StringIndexOutOfBoundsException", e);
            }
            
            
            //Tidy is returning null for some pages which otherwise seem ok
            // eg. wordpress.com
            
            if(document == null) return anchors;
            
            logger.debug("Document made by tidy: " + document);
            
            // the number of errors that occurred in the most recent parse operation.
            if (tidy.getParseErrors() > 0 ){
                throw new CrawlerException();
            }
            //
            //document.normalize();
            NodeList links = document.getElementsByTagName("a");
            //TODO: grab qualified name
            logger.debug("No of links: " + links.getLength());
            for (int i = 0; i <links.getLength(); i++) {
                Node node = links.item(i).getAttributes().getNamedItem("href");
                if (node == null) {
                    continue;
                }
                //System.out.println(links.getLength() + "\t"+links.item(i).getLocalName());
                if ((node.getNodeValue() != null) && (!node.getNodeValue().equals(""))) {
                    if (node.getNodeValue().startsWith("http")) {
                    	
                    	String href = node.getNodeValue();
                    	if(href.endsWith("/")) href = href.substring(0, href.length() - 1);
                    	
                    	//logger.info("About to add to anchor list: " + node.getNodeValue());
                        anchors.add(href);      //getAttributes("href");
                    } else {
                        try {
                        	//logger.info("About to add to anchor list (resolved):\n\t\t" + url.toString() + " + " + node.getNodeValue());
                        	
                            anchors.add(resolve(url.toString(), node.getNodeValue()));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            logger.debug("Error extacting URL!");
                        }
                    }
                } else {
                    logger.debug("There was a null href?");
                }
            }
           // responseText = os.toString();
        }
        //responseText = responseText.replaceAll("<!DOCTYPE((.|\n|\r)*?)\">", "");
        logger.debug("Done extracting normally");
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
						logger.warn("MalformedURLException", e);
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
	

	public ArrayList<String> getLinkStrings(URL url, String contents) throws CrawlerException {
		return smartExtract(url, contents);
	}
	
	public ArrayList<String> getLinks(){
		return links;
	}
	
	public ArrayList<String> getText() {
		return text;
	}
	
	public String resolve(String baseURL, String relativeURL) throws MalformedURLException {
		//Does baseURL end in slash?
		if(baseURL.endsWith("/")){
			baseURL = baseURL.substring(0, baseURL.length() - 1);
			
			if(relativeURL.startsWith("/")){
				//Case 1A
				//Use URL class to get the root of baseURL (i.e., up to the end of the host)
				return relativeStartsWithSlash(baseURL, relativeURL);
				
			} else if(relativeURL.startsWith("?") || relativeURL.startsWith("#")){
				return baseURL + relativeURL;
			} else{
				//RelativeURL doesn't start with slash
				//Case 1 B, C & D
				return baseURL + "/" +  relativeURL;
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
