package com.yippee.pastry;

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
     * Constructor
     *
     * @param nodeFactory
     */
    public YippeePastryApp(NodeFactory nodeFactory) {
        logger.info("Register Application");
        node = nodeFactory.getNode();
        endpoint = node.buildEndpoint(this, "P2P App");
        endpoint.register();
    }

    /**
     * Called when the Pastry application receives a message. It pushes the url
     * to the URLFrontier (maybe through a duplicate URL eliminator).
     */
	public void deliver(Id id, Message message) {
        PastryMessage om = (PastryMessage) message;
        logger.info("Received message " + om.content + " from " + om.from);
        if (om.wantResponse) { // if it is a query
            if (om.content.equals("PING")) {
                logger.info("Received PING to ID " + id + " from node " +
                        om.from.getId() + "; returning PONG");
                sendDirect(om.from, "PONG");
            } // else for other queries
        } else {
            if (om.content.equals("PONG")) {
                logger.info("Received PONG from node " + om.from.getId());
            }
        }
	}

    /**
     * Called to route a message to the id
     */
    void send(Id idToSendTo, String msgString) {
        if (msgString.equals("PING")) {
            System.out.println("Sending PING to " + idToSendTo);

        }
        logger.info(this + " sending to " + idToSendTo);
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
