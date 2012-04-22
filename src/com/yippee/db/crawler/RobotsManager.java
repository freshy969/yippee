package com.yippee.db.crawler;

import com.sleepycat.je.DatabaseException;
import com.yippee.db.crawler.model.RobotsTxt;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;
import org.apache.log4j.Logger;

import java.io.File;

public class RobotsManager {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsManager.class);
    private static CrawlerDBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public RobotsManager(String location) {
        myDbEnv = CrawlerDBEnv.getInstance(location, false);
        // Path to the environment home //TODO CHECK IF EXISTS
        // Environment is <i>not</i> readonly
        //myDbEnv.setup(new File(location), false);
    }

    /**
     * Insert a robots.txt representation to the database
     *
     * @param robotsTxt the RobotsTxt entry to be inserted
     * @return true if everything ok; false o/w
     */
    public boolean create(RobotsTxt robotsTxt) {
        boolean success = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getCrawlerStore());
            dao.getRobotsById().put(robotsTxt);
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            success = false;
        } catch (IllegalArgumentException e){
        	logger.warn("Exception", e);
            success = false;
        }
        return success;
    }

    /**
     * Read a robotsTxt entry from the database, and update the given continuation.
     * if entry exists. Return true if key exists (and continuation gets updated)
     * and false if not.
     *
     * @param host the host for which the RobotsTxt is being requested
     * @return true if continuation is updated, false o/w
     */
    public boolean read(String host, RobotsTxt continuation) {
        boolean result = false;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getCrawlerStore());
            RobotsTxt temp = dao.getRobotsById().get(host);
            if (temp != null) {
                temp.getCrawlDelay();
                temp.getDisallows();
                temp.getHost();
                continuation.setCrawlDelay(temp.getCrawlDelay());
                continuation.setDisallows(temp.getDisallows());
                continuation.setHost(temp.getHost());
                result = true;
            }
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            result = false;
        }
        return result;
    }

    /**
     * Delete a Robots.txt entry from the database (used mainly for testing)
     *
     * @param host the key of the entry to be deleted
     * @return true if deleted, false o/w
     */
    public boolean delete(String host) {
        boolean result = true;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getCrawlerStore());
            result = dao.getRobotsById().delete(host);
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            result = false;
        }
        return result;
    }
    
    /**
     * Method to test 
     * @return
     */
    public boolean isEmpty(){
    	boolean result = false;
    	dao = new DAL(myDbEnv.getCrawlerStore());            
        result = dao.getRobotsCursor().count() == 0;
    	return result;
    }

}