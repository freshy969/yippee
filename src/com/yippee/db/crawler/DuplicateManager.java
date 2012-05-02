package com.yippee.db.crawler;

import com.sleepycat.je.DatabaseException;
import com.yippee.db.crawler.model.DuplicateURL;
import com.yippee.db.util.DAL;
import org.apache.log4j.Logger;

/**
 * The manager for the Duplicate URL Eliminator. It is responsible for managing
 * the Duplicate model, and providing an abstraction
 */
public class DuplicateManager {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(DuplicateManager.class);
    private static CrawlerDBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public DuplicateManager() {
        myDbEnv = CrawlerDBEnv.getInstance(false);
        dao = new DAL(myDbEnv.getCrawlerStore());
    }

    /**
     * Check if the URL exists in the database
     *
     * @param url the url key of the entry to be checked
     * @return true if exists; false o/w
     */
    public boolean exists(String url) {
        boolean exists = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            DuplicateURL temp = dao.getDuplicateByURL().get(url);
            if (temp == null) {
                temp = new DuplicateURL();
                temp.setUrl(url);
                temp.setHits(1);
                dao.getDuplicateByURL().put(temp);
                exists = false;
            } else {
                temp.addHit();
                dao.getDuplicateByURL().put(temp);
                exists = true;
            }
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            exists = false;
        } catch (IllegalArgumentException e){
        	logger.warn("Exception", e);
            exists = false;
        }
        return exists;
    }
}
