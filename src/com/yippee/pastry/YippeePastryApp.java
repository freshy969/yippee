package com.yippee.pastry;

import java.util.ArrayList;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.indexer.BarrelManager;
import com.yippee.db.indexer.model.Hit;
import com.yippee.pastry.PastryAppSocketSender;
import com.yippee.util.SocketQueue;

import com.yippee.pastry.message.CrawlerMessage;
import com.yippee.pastry.message.IndexerMessage;
import com.yippee.pastry.message.PastryMessage;
import com.yippee.pastry.message.PingPongMessage;
import com.yippee.pastry.message.QueryMessage;

import org.apache.log4j.Logger;
import rice.p2p.commonapi.*;

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
     * The urlFrontier in which
     */
    private URLFrontier urlFrontier;
    
    private BarrelManager barrelManager;

	private SocketQueue queryQueue;
    
    /**
     * Constructor
     *
     * @param nodeFactory
     */
    public YippeePastryApp(NodeFactory nodeFactory) {
        logger.info("Register Application");
        node = nodeFactory.getNode();
        endpoint = node.buildEndpoint(this, "Yippee App");
        barrelManager = new BarrelManager();
       // endpoint.accept(new PastryAppSocketReceiver(node, endpoint));
        endpoint.register();
		queryQueue = new SocketQueue(100);
    }

    public void setupURLFrontier(URLFrontier urlFrontier){
        this.urlFrontier = urlFrontier;
    }
    
	/**
	 * Starts the daemon thread
	 * 
	 * @param port port on which the daemon listens
	 */
	public void startDaemonListener(int port) {
		Thread daemon = new Thread(new DaemonListener(port, queryQueue));
		daemon.setDaemon(true);
		daemon.start();
	}
    
    /**
     * Called when the Pastry application receives a message. It pushes the url
     * to the URLFrontier (maybe through a duplicate URL eliminator).
     */
	public void deliver(Id id, Message message) {
		
		if(message instanceof CrawlerMessage){
			handleCrawlerMessage(id, (CrawlerMessage) message);
		} else if(message instanceof IndexerMessage){
			handleIndexerMessage(id, (IndexerMessage) message);
		} else if(message instanceof QueryMessage){
			handleQueryMessage(id, (QueryMessage) message);
		} else if(message instanceof PingPongMessage){
			handlePingPongMessage(id, (PingPongMessage) message);
		}else {
			logger.error("Unknown pastry message received!");
			logger.error(message);
		}
<<<<<<< HEAD
		
		
		
		
		
/*		
        PastryMessage om = (PastryMessage) message;
        logger.debug("Received message " + om.content + " from " + om.from);
        if (om.wantResponse) { // if it is a query
=======
	}

    private void handlePingPongMessage(Id id, PingPongMessage message) {
		if (message.get) { // if it is a query
>>>>>>> Update Message
            if (om.content.equals("PING")) {
                logger.debug("Received PING to ID " + id + " from node " +
                        om.from.getId() + "; returning PONG");
                sendDirect(om.from, "PONG");
            } else {// else for other queries
                // push to the urlFrontier or that node

            }
        } else {
            if (om.content.equals("PONG")) {
                logger.debug("Received PONG from node " + om.from.getId());
            } else if(om.hitList.size()>0) { //message with hitlist in it
        		logger.info("Saving in barrels");
        		barrelManager.addDocHits(om.hitList);
        	}
        }
	}

	private void handleQueryMessage(Id id, QueryMessage message) {
		// TODO Auto-generated method stub
		
	}

	private void handleIndexerMessage(Id id, IndexerMessage message) {
		logger.info("Saving in barrels");
		barrelManager.addDocHits(message.getHitList());		
	}

	private void handleCrawlerMessage(Id id, CrawlerMessage message) {
		String urlString = message.getUrl();
        logger.info("Pushing ["+ urlString +"] to the URLFRONTIER");
        com.yippee.crawler.Message msg = new com.yippee.crawler.Message(urlString);
        if (msg.getType() == com.yippee.crawler.Message.Type.NEW){
            urlFrontier.push(msg);
        }
		
	}

	/**
     * Called to route a message to the id
     */
    void send(Id idToSendTo, String msgString) {
        if (msgString.equals("PING")) {
            logger.debug("Sending PING to " + idToSendTo);
        } else {
            logger.info(this + " sending to " + idToSendTo);
        }
        PastryMessage message = new PastryMessage(node.getLocalNodeHandle(), msgString);
        endpoint.route(idToSendTo, message, null);
    }

    /**
     * Called to directly send a message to the node handle
     */
    public void sendDirect(NodeHandle nh, String msgString) {

        logger.info(this + " sending direct to " + nh);
        PastryMessage message = new PastryMessage(node.getLocalNodeHandle(), msgString);
   //     message.wantResponse = false;
        endpoint.route(null, message, nh);
    }
    
    public void sendSocketDirect(NodeHandle nh, ArrayList<Hit> list) {
    	logger.info(this + " sending hit list direct to " + nh);
        endpoint.connect(nh, new PastryAppSocketSender(node,endpoint,list), 30000);
    }
    
    public void sendList(Id idToSendTo, String word, ArrayList<Hit> list) {
    	logger.info(this + " sending hit list for ["+word+"] to " + idToSendTo);
    	IndexerMessage message = new IndexerMessage(node.getLocalNodeHandle(), word, list);
    //	message.wantResponse = false;
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
	public void update(NodeHandle arg0, boolean arg1) {}
}
