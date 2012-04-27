package com.yippee.db.pastry;

import org.apache.log4j.Logger;

import com.yippee.db.crawler.CrawlerDBEnv;
import com.yippee.db.util.DAL;

public class PastryManager {

	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PastryManager.class);
    private static PastryDBEnv myDbEnv;
    private DAL dao;
    
    
    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public PastryManager() {
        myDbEnv = PastryDBEnv.getInstance(false);
        dao = new DAL(myDbEnv.getPastryStore());
        
        // Path to the environment home //TODO CHECK IF EXISTS
        // Environment is <i>not</i> readonly
        //myDbEnv.setup(new File(location), false);
    }
}
