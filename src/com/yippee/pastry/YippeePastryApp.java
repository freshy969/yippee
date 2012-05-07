package com.yippee.pastry;

import org.apache.commons.lang3.StringEscapeUtils;
import com.yippee.crawler.frontier.URLFrontier;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import com.yippee.db.indexer.BarrelManager;
import com.yippee.db.indexer.DocEntryManager;
import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.indexer.model.Hit;
import com.yippee.db.indexer.model.HitList;
import com.yippee.pastry.message.*;
import com.yippee.search.DaemonListener;
import com.yippee.search.QueryDaemon;
import com.yippee.search.SearchEngine;
import com.yippee.util.SocketQueue;
import org.apache.log4j.Logger;
import rice.p2p.commonapi.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class YippeePastryApp implements Application {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(YippeePastryApp.class);
    /**
     * The current pastry substrate node
     */
    private Node node;
    /**
     * The current endpoint
     */
    private Endpoint endpoint;
    /**
     * The node factory
     */
    private NodeFactory nodeFactory;
    /**
     * The urlFrontier in which
     */
    private URLFrontier urlFrontier;
    /**
     * For storing HitLists
     */
    private BarrelManager barrelManager;
    /**
     * For grabbing Docs
     */
    private DocEntryManager docManager;
    /**
     * For handling Search
     */   
    private static SocketQueue socketQueue;
    private static HashMap<UUID, String> queryMap;
    private static HashMap<UUID, Socket> socketMap;
    private static HashMap<UUID, ArrayList<ResultMessage>> queryResultMap;
    private static HashMap<UUID, ArrayList<ResultMessage>> docResultMap;
    private static HashMap<UUID, HashMap<String, Float>> tfMap;
    
    /**
     * Constructor
     *
     * @param nodeFactory
     */
    public YippeePastryApp(NodeFactory nodeFactory) {
        logger.info("Register Application");
        this.nodeFactory = nodeFactory;
        node = nodeFactory.getNode();
        endpoint = node.buildEndpoint(this, "Yippee App");
        barrelManager = new BarrelManager();
        docManager = new DocEntryManager();
        // endpoint.accept(new PastryAppSocketReceiver(node, endpoint));
        endpoint.register();

        // Query data structures
        socketQueue = new SocketQueue(100);
        socketMap = new HashMap<UUID, Socket>();
        queryMap = new HashMap<UUID, String>();
        queryResultMap = new HashMap<UUID, ArrayList<ResultMessage>>();
        docResultMap = new HashMap<UUID, ArrayList<ResultMessage>>();

        tfMap = new  HashMap<UUID, HashMap<String, Float>>();
    }

    public void setupURLFrontier(URLFrontier urlFrontier) {
        this.urlFrontier = urlFrontier;
    }
    
    /**
     * Called when the Pastry application receives a message. It pushes the url
     * to the URLFrontier (maybe through a duplicate URL eliminator).
     */
    public void deliver(Id targetId, Message message) {

        if (message instanceof CrawlerMessage) {
            handleCrawlerMessage(targetId, (CrawlerMessage) message);
        } else if (message instanceof IndexerMessage) {
            handleIndexerMessage(targetId, (IndexerMessage) message);
        } else if (message instanceof QueryMessage) {
            handleQueryMessage(targetId, (QueryMessage) message);
        } else if (message instanceof ResultMessage) {
            handleResultMessage(targetId, (ResultMessage) message);
        } else if (message instanceof PingPongMessage) {
            handlePingPongMessage(targetId, (PingPongMessage) message);
        } else {
            logger.error("Unknown pastry message received!");
            logger.error(message);
        }
    }

    /**
     * Called as a result of deliver recieving a PingPongMessage
     *
     * @param targetId
     * @param message
     */
    private void handlePingPongMessage(Id targetId, PingPongMessage message) {

        if (message.getContent().equals("PING")) {
            logger.debug("Received PING at ID " + targetId + " from node " +
                    message.getFrom().getId() + "; returning PONG");

            PingPongMessage reply = new PingPongMessage(node.getLocalNodeHandle(), "PONG");
            sendDirect(message.getFrom(), reply);
        } else if (message.getContent().equals("PONG")) {
            logger.debug("Received PONG from node " + message.getFrom().getId());

        } else {
            logger.error("Malformed PingPongMessage");
            logger.error(message);
        }
    }


    /**
     * Called as a result of deliver receiving a QueryMessage
     * 
     * @param targetId
     * @param message
     */
    private void handleQueryMessage(Id targetId, QueryMessage message) {
    	String query = message.getWord();
    	
    	if (message.isDocQuery()) {
    		DocEntry de = docManager.getDocEntry(message.getWord());
        	ResultMessage rm = new ResultMessage(node.getLocalNodeHandle(), de, query, message.getQueryID(), message.queryLength(), true);
        	sendResult(message.getNodeHandle().getId(), rm);
    	} else {
    		
        	logger.info("Received term: " + query);
        	
        	// Null if none found
        	HitList list = barrelManager.getHitList(query);
        	
        	if (list == null) 
        	   	logger.info("No Hits: \"" + query + "\"");
        	else
        		logger.info("Found Hits: \"" + query + "\"=" + list.getHitList().size());
        	
        	list.setHitList(null);
        	
        	ResultMessage rm = new ResultMessage(node.getLocalNodeHandle(), list, query, message.getQueryID(), message.queryLength());
        	sendResult(message.getNodeHandle().getId(), rm);
    	}
    }
    
    /**
     * Called as a result of deliver receiving a ResultMessage
     * 
     * @param targetId
     * @param message
     */
    private void handleResultMessage(Id targetId, ResultMessage message) {
    	UUID queryID = message.getQueryID();

    	logger.info("Received result: " + message.getWord());

    	if (message.isDocResult()) {
    		putDocResult(queryID, message);
	    	ArrayList<ResultMessage> results = getDocResults(queryID);

	    	if (results.size() == message.queryLength()) {
	    		
	    		logger.info("Gathered docs: " + results.size());
	    		
	    		ArrayList<DocEntry> deList = new ArrayList<DocEntry>();
	    		
	    		for (int i = 0; i < results.size(); i++) {
	    			DocEntry de = results.get(i).getDocEntry();
	    	
	    			if (de != null) {
	    				deList.add(de);
	    				HashMap<String, Float> tfidf = tfMap.get(queryID);
		    			
		    			float value = tfidf.get(de.getURL());
		    			de.setTfidf(value);
	    			}
	    		}
	    		
	    		Collections.sort(deList);
	    		
	    		// Return results to the Socket
	    		sendResultToSocket(queryID, deList);
	    	}
    		
    	} else {
	    	
	    	putQueryResult(queryID, message);
	    	ArrayList<ResultMessage> results = getQueryResults(queryID);
	    	
	    	// If all the query words have been collected
	    	if (results.size() == message.queryLength()) {
	    	
	    		String originalQuery = getQuery(queryID);
	    		logger.info("Completed query: " + originalQuery);
	    		
	    		// Check if any results were found
	    		boolean nullResults = false;
	    		for (int i = 0; i < results.size(); i++) {
	    			if (results.get(i).getHitList() != null)
	    				break;
	    			else if (i == results.size() - 1)
	    				nullResults = true;
	    		}
	    		
	    		if (nullResults) {
	    			logger.info("No results found.");
		    		sendResultToSocket(queryID, null);
		    	} else {
		    		
		    		// Send statistics to SearchEngine
		    		SearchEngine se = new SearchEngine(results);    		
		    		se.init();
		    		se.calculateTfidf();
		    		
		    		ArrayList<URL> docList = se.getMatchedUrls();
		    		tfMap.put(queryID, se.getTfMap());
		
		    		// Send DocEntry Query Messages
		    		if (docList.size() == 0) {
		    			logger.info("No results found.");
		    			sendResultToSocket(queryID, null);
		    		} else {
			    		for (int i = 0; i < docList.size(); i++) {
			    			URL url = docList.get(i);
//			    			logger.info(url);	
			
			    			Id destination = nodeFactory.getIdFromString(url.getHost());
			    	        String content = url.toString();
			    			
			    			QueryMessage qm = new QueryMessage(node.getLocalNodeHandle(), content, queryID, docList.size(), true);
			    			
//			    			logger.debug("Sending Query URL " + content + " DocEntry at " + url.getHost());
			    			
			    			sendDocMessage(destination, qm);
			    		}
		    		}    		
		    	}
	    	}
    	}
    }
    
    /**
     * Called as a result of deliver receiving an IndexerMessage
     *
     * @param targetId
     * @param message
     */
    private void handleIndexerMessage(Id targetId, IndexerMessage message) {
        logger.debug("Saving in barrels");
        barrelManager.addDocHits(message.getHitList());
    }
    
    /**
     * Handle the crawler messages
     *
     * @param targetId the target node
     * @param message  the actual crawler message
     */
    private void handleCrawlerMessage(Id targetId, CrawlerMessage message) {
        String urlString = message.getUrl();
        logger.info("Pushing [" + urlString + "] to the URLFRONTIER");
        com.yippee.crawler.Message msg = new com.yippee.crawler.Message(urlString);
        if (msg.getType() == com.yippee.crawler.Message.Type.NEW) {
            urlFrontier.push(msg);
        }
    }

    /**
     * Called to route a message to the id
     */
    void sendPingPongMessage() {
        Id destination = nodeFactory.nidFactory.generateNodeId();
        logger.debug("Sending PING to " + destination);
        PingPongMessage message = new PingPongMessage(node.getLocalNodeHandle(), "PING");
        endpoint.route(destination, message, null);
    }

    void sendCrawlerMessage(URL url) {
        Id destination = nodeFactory.getIdFromString(url.getHost());
        String content = url.toString();
        CrawlerMessage message = new CrawlerMessage(content);
        logger.debug("Sending URL " + content + "to node closest to" + url.getHost());
        endpoint.route(destination, message, null);
    }

    void sendDocMessage(Id destination, Message message) {
        endpoint.route(destination, message, null);
    }
    
    /**
     * Called to directly send a message to the node handle
     */
    public void sendDirect(NodeHandle nh, Message message) {
        logger.debug(this + " sending direct to " + nh);
        //     message.wantResponse = false;
        endpoint.route(null, message, nh);
    }

    public void sendSocketDirect(NodeHandle nh, ArrayList<Hit> list) {
        logger.debug(this + " sending hit list direct to " + nh);
        endpoint.connect(nh, new PastryAppSocketSender(node, endpoint, list), 30000);
    }

    public void sendList(Id idToSendTo, String word, ArrayList<Hit> list) {
        logger.debug(this + " sending hit list for [" + word + "] to " + idToSendTo);
        IndexerMessage message = new IndexerMessage(node.getLocalNodeHandle(), word, list);
        //	message.wantResponse = false;
        endpoint.route(idToSendTo, message, null);
    }

    public void sendQuery(Id idToSendTo, QueryMessage message) {
        logger.info(this + " sending query for [" + message.getWord() + "] to " + idToSendTo);
        endpoint.route(idToSendTo, message, null);
    }
    
    public void sendResult(Id idToSendTo, ResultMessage message) {
        logger.info(this + " sending result for [" + message.getWord() + "] to " + idToSendTo);
        endpoint.route(idToSendTo, message, null);
    }

    /**
     * This is always true in our application.
     *
     * @param routeMessage a message
     * @return true always
     */
    public boolean forward(RouteMessage routeMessage) {
        return true;
    }

    /**
     * Called when we hear about a new neighbor.
     * We do not make use of this method for now.
     */
    public void update(NodeHandle arg0, boolean arg1) {
    }
    
    public Socket getSocket(UUID id) {
    	return socketMap.get(id);
    }
    
    public void putSocket(UUID id, Socket socket) {
    	socketMap.put(id, socket);
    }
    
    public String getQuery(UUID id) {
    	return queryMap.get(id);
    }
    
    public void putQuery(UUID id, String keywords) {
    	queryMap.put(id, keywords);
    }
    
    public ArrayList<ResultMessage> getQueryResults(UUID id) {
    	return queryResultMap.get(id);
    }
    
    public ArrayList<ResultMessage> getDocResults(UUID id) {
    	return docResultMap.get(id);
    }
    
    public void putQueryResult(UUID id, ResultMessage msg) {
    	ArrayList<ResultMessage> list;
    	
    	if (queryResultMap.get(id) == null)
    		list = new ArrayList<ResultMessage>();
    	else
    		list = queryResultMap.get(id);
    	
    	list.add(msg);
    	
    	queryResultMap.put(id, list);
    }
    
    public void putDocResult(UUID id, ResultMessage msg) {
    	ArrayList<ResultMessage> list;
    	
    	if (docResultMap.get(id) == null)
    		list = new ArrayList<ResultMessage>();
    	else
    		list = docResultMap.get(id);
    	
    	list.add(msg);
    	
    	docResultMap.put(id, list);
    }
    
    public Node getNode() {
    	return node;
    }
    
    public NodeFactory getNodeFactory() {
    	return nodeFactory;
    }

    /**
     * Starts the daemon listener thread
     *
     * @param port port on which the daemon listens
     */
    public void startDaemonListener(int port) {
        Thread daemon = new Thread(new DaemonListener(port, socketQueue));
        daemon.setDaemon(true);
        daemon.start();
    }

    /**
     * Starts the daemon query thread
     */
    public void startQueryDaemon() {
        Thread daemon = new Thread(new QueryDaemon(this, socketQueue));
        daemon.setDaemon(true);
        daemon.start();
    }
    
    public void sendResultToSocket(UUID queryID, ArrayList<DocEntry> deList) {

		Socket client = getSocket(queryID);
		
		PrintWriter out;
		try {
			out = new PrintWriter(client.getOutputStream());
			logger.info("Query Completed!\n");
			
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			out.println("<?xml-stylesheet type=\"text/xsl\" href=\"yippee.xsl\"?>");
			
			out.println("<documentcollection>");

			out.println("<query>" + StringEscapeUtils.escapeXml(queryMap.get(queryID)) + "</query>");
			
			if (deList == null) {
				out.println("<document><description>No matching documents found!</description></document>");
			} else {
				for (int i = 0; i < deList.size(); i++) {
					DocEntry de = deList.get(i);
					if (de.getTitle() == null)
						continue;
					
					out.println("<document>");
					out.println("<title>" + StringEscapeUtils.escapeXml(de.getTitle()) + "</title>");
					out.println("<link>" + StringEscapeUtils.escapeXml(de.getURL()) + "</link>");
					out.println("<description>" + de.getTfidf() + "</description>");
					out.println("</document>");
				}
			}
			
			out.println("</documentcollection>");
			
			out.flush();
			logger.info("Query Completed!\n");
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
