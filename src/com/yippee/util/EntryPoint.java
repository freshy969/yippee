package com.yippee.util;

import com.yippee.crawler.Araneae;
import com.yippee.crawler.Message;
import com.yippee.crawler.frontier.FrontierFactory;
import com.yippee.crawler.frontier.FrontierType;
import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.indexer.Indexer;
import com.yippee.pastry.PingPong;
import com.yippee.pastry.YippeeEngine;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This the entry point of the Yippe engine back-end , providing convenient setup
 * for the whole application. Among others, it creates the Pastry ring, the configuration
 * environment, the crawler, the database environment and more. Most importantly,
 * components are started by private methods, so we can have granular component
 * initialization. For instance, we can start only the crawler or pastry ring or
 * any combination of components.
 * <p/>
 * TODO: We can add a config.properties file to read configuration from there on startup
 */
public class EntryPoint {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(EntryPoint.class);
    /**
     * The command-line arguments used to launch the search engine back-end.
     */
    private String[] arguments;
    /**
     * TODO: THESE NEED TO BE GIVEN DYNAMICALLY -- this is where caution message applies to.
     */
    final int NO_OF_THREADS = 1;

    /**
     * The default constructor does the minimum of setting up the logger
     * properties for the rest of the components (even if we are going to log
     * failure due to argument error).
     */
    private EntryPoint() {
        // SET logger
        PropertyConfigurator.configure("log/log4j.properties");
    }

    /**
     * Configures application based on arguments, after making sure that args
     * are in expected form. Next, components are started by private methods,
     * so we can have granular component initialization. For instance, we can
     * start only the crawler or pastry ring or any combination of components.
     *
     * @param args the user-provided arguments (usually ant)
     * @return true if everything ok; false o/w
     */
    private boolean configure(String[] args) {
        if (args.length < 4) {
            System.out.println("There are no arguments;");
            System.out.println("please check README file, or run 'ant usage'");
            logger.error("Error: No arguments");
            return false;
        } else {
            arguments = args;
            System.out.println("2012, Yippee!");
            cautionMessage();
            String database = args[4];
            logger.info("Database relative path to project root: " + database );
            Configuration.getInstance().setBerkeleyDBRoot(database);
            return true;
        }
    }

    /**
     * Here we add any todos/caution messages we need to show to the user who
     * launches the application. It is called by the constructor.
     */
    private void cautionMessage() {
        // TODO: THESE ARE SOME OF THE CONFIGURATIONS NEED TO BE DONE
        System.out.println("TODO: \n\t * set Thread number \n\t * set Daemon port");
    }

    /**
     * This method fires up the Distributed Hash Table (DHT) service with the
     * aid of FreePastry. More information can be found in com.yippee.pastry
     * package.
     *
     * @return true if everything ok; false o/w;
     */
    private boolean setUpSubstrate() {
        YippeeEngine yippeeEngine = new YippeeEngine(Integer.parseInt(arguments[0]),
                arguments[1], Integer.parseInt(arguments[2]));
        Configuration.getInstance().setPastryEngine(yippeeEngine);
        PingPong pingPong = new PingPong();
        new Thread(pingPong, "Ping Pong Thread").start();
        // sleep for a number of seconds so that when the rest of the services
        // launch, pastry is up and running
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This method fires up the Crawler service with the required configuration.
     * More information can be found in com.yippee.crawler package.
     *
     * @return true if everything ok; false o/w;
     */
    private boolean setupCrawler(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Configuration.getInstance().setCrawlerThreadNumber(NO_OF_THREADS);
        URLFrontier urlFrontier = FrontierFactory.get(FrontierType.SIMPLE);
        Configuration.getInstance().getPastryEngine().setupURLFrontier(urlFrontier);
        boolean success = true;
        // only overwrite database with new seeds iff an overwrite flag was given
        if (args[args.length-1].equals("--overwrite")) {
            logger.warn("Overwrite -- loading frontier from the url feed");
            if ((args.length > 4) && (!args[3].contains("--"))) {
                if (!seed(urlFrontier, args[3])) {
                    success = false;
                }
            } else {
                success = false;
            }
        } else { // load URLFrontier from database
            logger.warn("No overwrite -- loading frontier from the database");
            //urlFrontier.load();
        }
        if (success) {
            Configuration.getInstance().getPastryEngine().sendPing();
            Araneae threadPool = new Araneae(urlFrontier);

        } else {
            String error = "FATAL: No file found to load urls from";
            logger.error(error);
            System.out.println(error);
        }
        return success;
    }

    /**
     *
     * @param urlFrontier
     * @param seed
     * @return
     */
    private boolean seed(URLFrontier urlFrontier, String seed) {

        File seedFile = new File(seed);
        if (!seedFile.exists()) {
            return false;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(seed));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while (scanner.hasNextLine()) {
                String urlString = new StringBuilder(scanner.nextLine()).toString();
                if (urlString.startsWith("#")) continue;
                String aLog = "New URL [" + urlString +"]";
                //URL url = new URL(urlString);
                //Configuration.getInstance().getPastryEngine().sendURL(url);
                Message msg = new Message(urlString);
                if (msg.getType()== Message.Type.NEW){
                    urlFrontier.push(msg);
                }
                logger.info(aLog);
            }
        //} catch (MalformedURLException e) {
        //    e.printStackTrace();
        } finally {
            scanner.close();
        }
        return true;
    }


    /**
     * The entry point for the whole Yippee Engine.
     * <p/>
     * Currently, it takes the following
     * arguments:
     * <p/>
     * 1.   The port number on the local machine to which the Pastry node
     * should bind;
     * 2.   The IP address of the Pastry bootstrap node;
     * 3.   The port number of the Pastry bootstrap node;
     * <p/>
     * TODO: We could add a parameter -CIRPS on which component to start
     *
     * @param args The command line arguments at the order specified above, for
     *             instance 9001 130.91.140.235 9001 4444 DB/db1
     */
    public static void main(String[] args) {
        EntryPoint entryPoint = new EntryPoint();
        // Pastry
        if (!entryPoint.configure(args)) return;
        entryPoint.setUpSubstrate();
        // Crawler
        if (!entryPoint.setupCrawler(args)) return;

        Indexer ih = new Indexer();
        ih.makeThreads();
    }

}
