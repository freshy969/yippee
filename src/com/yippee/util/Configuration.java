package com.yippee.util;

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
    private int threadNumber;
    /**
     * Shutdown threads
     */
    private boolean shutdown = false;
    /**
     * the path to berkeleyDB
     */
    private String berkeleyDB;

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
        logger.info("Threads \t:" + threadNumber);
        logger.info("Database \t:" + berkeleyDB);
    }

    /**
     * Issue a shutdown command, for all threads
     */
    public void shutdown() {
        shutdown = true;
    }


    /**
     * Get the number of threads
     *
     * @return get the number of threads
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * Set the number of threads
     *
     * @param threadNumber the number of threads
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
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
    public String getBerkeleyDB() {
        return berkeleyDB;
    }

    /**
     * Set the directory of BerkeleyDB on disk
     *
     * @param berkeleyDB the given path
     */
    public void setBerkeleyDB(String berkeleyDB) {
        this.berkeleyDB = berkeleyDB;
    }

}
