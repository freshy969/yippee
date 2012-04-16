package com.yippee.pastry;

import com.yippee.util.Configuration;
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
    public YippeeEngine(int localPort, String ipAddress, int bootPort, String database) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            InetSocketAddress address = new InetSocketAddress(inetAddress, bootPort);
            nodeFactory = new NodeFactory(localPort, address);
            logger.info("Starting ring..");
            yippeePastryApp = new YippeePastryApp(nodeFactory, database);
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


    /**
     * The entry point for the whole Yippee Engine. It takes the following
     * arguments:
     * <p/>
     * 1.   The port number on the local machine to which the Pastry node
     * should bind;
     * 2.   The IP address of the Pastry bootstrap node;
     * 3.   The port number of the Pastry bootstrap node;
     *
     * @param args  The command line arguments at the order specified above, for
     *              instance 9001 130.91.140.235 9001 4444 DB/db1
     */
    public static void main(String[] args) {
        if (args.length<0) {
            System.out.println("There are no arguments;");
            System.out.println("please check README file, or run 'ant usage'");
            logger.error("Error: No arguments");
            return;
        }

        System.out.println("2012, Yippee!");
        // TODO: THESE ARE SOME OF THE CONFIGURATIONS NEED TO BE DONE
        System.out.println("TODO: \n\t * set Thread number \n\t * set Daemon port");
        final int NO_OF_THREADS = 1;
        final String BERKELEY_DB = "db/test";

        YippeeEngine yippeeEngine = new YippeeEngine(Integer.parseInt(args[0]),
                args[1], Integer.parseInt(args[2]), args[4]);

        Configuration.getInstance().setPastryEngine(yippeeEngine);
        Configuration.getInstance().setThreadNumber(NO_OF_THREADS);
        Configuration.getInstance().setBerkeleyDB(args[4]);

    }
}
