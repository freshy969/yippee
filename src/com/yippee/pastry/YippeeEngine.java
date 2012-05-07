package com.yippee.pastry;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.indexer.model.Hit;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
            nodeFactory = new NodeFactory(localPort, address, inetAddress);
            logger.info("Starting ring..");
            yippeePastryApp = new YippeePastryApp(nodeFactory);
    		yippeePastryApp.startDaemonListener(8888);
    		yippeePastryApp.startQueryDaemon();
    		System.out.println("Node ID: " + yippeePastryApp.getNode().getId());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that sends a ping to a random node in the ring
     */
    public void sendPing(){

        yippeePastryApp.sendPingPongMessage();
    }

    /**
     * A method that distributes a URL to the ring
     */
    public void sendURL(URL url){
//        logger.info("About to send crawler message");
        yippeePastryApp.sendCrawlerMessage(url);
    }

    /**
     * Sets up the url frontier for the pastry substrate
     *
     * @param urlFrontier the urlFrontier to be used
     */
    public void setupURLFrontier(URLFrontier urlFrontier){
        yippeePastryApp.setupURLFrontier(urlFrontier);
    }
    
    public void sendList(String word, ArrayList<Hit> list){
    	yippeePastryApp.sendList(nodeFactory.getIdFromString(word), word,list);
    }
}
