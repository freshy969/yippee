package com.yippee.util;

import com.yippee.pastry.YippeeEngine;
import org.apache.log4j.Logger;

/**
 * This class implements a shared configuration among all threads.
 */
public class Configuration {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Configuration.class);
    /**
     * Singleton instance
     */
    private static Configuration _instance;
    /**
     * Number of threads
     */
    private int crawlerThreadNumber;
    /**
     * Shutdown threads
     */
    private boolean shutdown = false;
    /**
     * the path to berkeleyDB
     */
    private String berkeleyDBRoot;

    /**
     * Store Pastry Engine
     */
    private YippeeEngine pastryEngine;

    /**
     * Load a thread-safe (yet lazy!) singleton
     *
     * @return Configuration
     */
    public static synchronized Configuration getInstance() {
        if (_instance == null) {
            _instance = new Configuration();
        }
        return _instance;
    }

    /**
     * Load the default configuration (default constructor)
     */
    private Configuration() {
    	
    }

    /**
     * logs the current configuration
     */
    private void printConfiguration() {
        //LOG ARGUMENTS
        logger.info("Server reconfiguration complete:");
        logger.info("Threads \t:" + crawlerThreadNumber);
        logger.info("Database \t:" + berkeleyDBRoot);
    }

    /**
     * Issue a shutdown command, for all threads
     */
    public void shutdown() {
        shutdown = true;
    }


    /**
     * Get the number of threads <i>only</i> for the crawler
     *
     * @return get the number of threads
     */
    public int getCrawlerThreadNumber() {
        return crawlerThreadNumber;
    }

    /**
     * Set the number of threads <i>only</i> for the crawler
     *
     * @param crawlerThreadNumber the number of threads
     */
    public void setCrawlerThreadNumber(int crawlerThreadNumber) {
        this.crawlerThreadNumber = crawlerThreadNumber;
    }

    /**
     * Check whether a shutdown command was issued
     *
     * @return true if not (if application is running), false if shutdown issued
     */
    public boolean isUp() {
        return !shutdown;
    }

    /**
     * Get the directory of BerkeleyDB on disk
     *
     * @return the path to berkeleyDB on disk
     */
    public String getBerkeleyDBRoot() {
        return berkeleyDBRoot;
    }

    /**
     * Set the directory of BerkeleyDB on disk
     *
     * @param berkeleyDB the given path
     */
    public void setBerkeleyDBRoot(String berkeleyDB) {
        this.berkeleyDBRoot = berkeleyDB;
    }

    /**
     * Get the node engine
     *
     * @return The pastry engine
     */
    public YippeeEngine getPastryEngine() {
        return pastryEngine;
    }

    /**
     * Set the pastry application
     *
     * @param engine the pastry application running on this node
     */
    public void setPastryEngine(YippeeEngine engine) {
        this.pastryEngine = engine;
    }

}
