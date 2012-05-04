package com.yippee.pastry;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.indexer.BarrelManager;
import com.yippee.db.indexer.model.Hit;
import com.yippee.pastry.message.*;
import com.yippee.search.DaemonListener;
import com.yippee.util.SocketQueue;
import org.apache.log4j.Logger;
import rice.p2p.commonapi.*;

import java.net.URL;
import java.util.ArrayList;

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

    private BarrelManager barrelManager;

    private SocketQueue queryQueue;

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
        // endpoint.accept(new PastryAppSocketReceiver(node, endpoint));
        endpoint.register();
        queryQueue = new SocketQueue(100);
    }

    public void setupURLFrontier(URLFrontier urlFrontier) {
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
    public void deliver(Id targetId, Message message) {

        if (message instanceof CrawlerMessage) {
            handleCrawlerMessage(targetId, (CrawlerMessage) message);
        } else if (message instanceof IndexerMessage) {
            handleIndexerMessage(targetId, (IndexerMessage) message);
        } else if (message instanceof QueryMessage) {
            handleQueryMessage(targetId, (QueryMessage) message);
        } else if (message instanceof PingPongMessage) {
            handlePingPongMessage(targetId, (PingPongMessage) message);
        } else {
            logger.error("Unknown pastry message received!");
            logger.error(message);
        }
    }

    /**
     * Called as a result of deliver recieving a PingPongMessage
     * @param targetId
     * @param message
     */
    private void handlePingPongMessage(Id targetId, PingPongMessage message) {

            if (message.getContent().equals("PING")) {
                logger.debug(	"Received PING to ID " + targetId + " from node " +
                				message.getFrom().getId() + "; returning PONG");
                
                sendDirect(message.getFrom(), "PONG");
                
            } else if (message.getContent().equals("PONG")) {
                logger.debug("Received PONG from node " + message.getFrom().getId());
                
            } else {
            	logger.error("Malformed PingPongMessage");
            	logger.error(message);
            }
        
    }

   
    /**
     * 
     * @param targetId
     * @param message
     */
	private void handleQueryMessage(Id targetId, QueryMessage message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called as a result of deliver receiving an IndexerMessage
	 * @param targetId
	 * @param message
	 */
	private void handleIndexerMessage(Id targetId, IndexerMessage message) {
		logger.info("Saving in barrels");
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

    void sendCrawlerMessage(URL url){
        Id id = nodeFactory.getIdFromString(url.getHost());
        String content = url.toString();
        logger.info("Sending URL "+content+"to node closest to"+url.getHost());
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
        endpoint.connect(nh, new PastryAppSocketSender(node, endpoint, list), 30000);
    }

    public void sendList(Id idToSendTo, String word, ArrayList<Hit> list) {
        logger.info(this + " sending hit list for [" + word + "] to " + idToSendTo);
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
    public void update(NodeHandle arg0, boolean arg1) {
    }
}
