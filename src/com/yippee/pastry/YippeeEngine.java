package com.yippee.pastry;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * YippeeEngine makes use of the YippeePastryApp to create an application instance
 * on this node. It is back-end entry point (front-end being the servlet/JSP)
 * which runs on multiple nodes.
 */
public class YippeeEngine {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(YippeeEngine.class);
    /**
     * The YippeePastryApp application running on this node
     */
    private YippeePastryApp yippeePastryApp;
    /**
     * The node factory used by our Pastry App (YippeePastryApp)
     */
    private NodeFactory nodeFactory;

    /**
     * The augmented constructor:
     *
     * @param localPort The port number on the local machine to which the Pastry
     *                  node will bind
     * @param ipAddress The IP address of the Pastry bootstrap node;
     * @param bootPort  The port number of the Pastry bootstrap node;
     */
    public YippeeEngine(int localPort, String ipAddress, int bootPort) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            InetSocketAddress address = new InetSocketAddress(inetAddress, bootPort);
            nodeFactory = new NodeFactory(localPort, address);
            logger.info("Starting ring..");
            yippeePastryApp = new YippeePastryApp(nodeFactory);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that sends a ping to a random node in the ring
     */
    public void sendPing(){
        yippeePastryApp.send(nodeFactory.nidFactory.generateNodeId(), "PING");
    }
}
