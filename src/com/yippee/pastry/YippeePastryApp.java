package com.yippee.pastry;

import com.yippee.crawler.frontier.URLFrontier;
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

    /**
     * Constructor
     *
     * @param nodeFactory
     */
    public YippeePastryApp(NodeFactory nodeFactory) {
        logger.info("Register Application");
        node = nodeFactory.getNode();
        endpoint = node.buildEndpoint(this, "Yippee App");
        endpoint.register();
    }

    public void setupURLFrontier(URLFrontier urlFrontier){
        this.urlFrontier = urlFrontier;
    }

    /**
     * Called when the Pastry application receives a message. It pushes the url
     * to the URLFrontier (maybe through a duplicate URL eliminator).
     */
	public void deliver(Id id, Message message) {
        PastryMessage om = (PastryMessage) message;
        logger.debug("Received message " + om.content + " from " + om.from);
        if (om.wantResponse) { // if it is a query
            if (om.content.equals("PING")) {
                logger.debug("Received PING to ID " + id + " from node " +
                        om.from.getId() + "; returning PONG");
                sendDirect(om.from, "PONG");
            } else {// else for other queries
                // push to the urlFrontier or that node
                String urlString = om.content;
                logger.info("Pushing ["+ urlString +"] to the URLFRONTIER");
//                com.yippee.crawler.Message msg = new com.yippee.crawler.Message(urlString);
//                if (msg.getType() == com.yippee.crawler.Message.Type.NEW){
//                    urlFrontier.push(msg);
//                }
            }
        } else {
            if (om.content.equals("PONG")) {
                logger.debug("Received PONG from node " + om.from.getId());
            }
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
        message.wantResponse = false;
        endpoint.route(null, message, nh);
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
