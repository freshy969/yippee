package com.yippee.db.managers;

import com.sleepycat.je.DatabaseException;
import com.yippee.db.model.RobotsTxt;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;
import org.apache.log4j.Logger;

import java.io.File;

public class RobotsManager {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsManager.class);
    private static DBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public RobotsManager(String location) {
        myDbEnv = new DBEnv();
        // Path to the environment home //TODO CHECK IF EXISTS
        // Environment is <i>not</i> readonly
        myDbEnv.setup(new File(location), false);
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
            dao = new DAL(myDbEnv.getEntityStore());
            dao.getRobotsById().put(robotsTxt);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * Read a robotsTxt entry from the database, and update the given continuation.
     * if entry exists. Return true if key exists (and continuation gets updated)
     * and false if not.
     *
     * @param key the keyword requested
     * @return true if continuation is updated, false o/w
     */
    public boolean read(String key, RobotsTxt continuation) {
        boolean result = false;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            RobotsTxt temp = dao.getRobotsById().get(key);
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
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Delete a Robots.txt entry from the database (used mainly for testing)
     *
     * @param key the key of the entry to be deleted
     * @return true if deleted, false o/w
     */
    public boolean delete(String key) {
        boolean result = true;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            result = dao.getRobotsById().delete(key);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }
}