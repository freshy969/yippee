package com.yippee.db.pastry;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.crawler.CrawlerDBEnv;
import com.yippee.db.crawler.model.FrontierSavedState;
import com.yippee.db.pastry.model.NodeState;
import com.yippee.db.util.DAL;

public class PastryManager {

	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PastryManager.class);
    private static PastryDBEnv myDbEnv;
    private DAL dao;
    private int latestVersion;
    
    
    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public PastryManager() {
        myDbEnv = PastryDBEnv.getInstance(false);
        dao = new DAL(myDbEnv.getPastryStore());
        latestVersion = getLatestVersionNumber();
        // Path to the environment home //TODO CHECK IF EXISTS
        // Environment is <i>not</i> readonly
        //myDbEnv.setup(new File(location), false);
    }


    //FIXME Store state should take a parameter for the Node ID, but not sure what type that will be
	public boolean storeState(String id) {
		boolean successful = false;
		int newVersionNumber = latestVersion + 1;
		NodeState state = new NodeState();
		
		try{
			dao.getNodeStateByVersion().put(state);
			successful =  true;
			latestVersion = newVersionNumber;
			
		} catch(DatabaseException e){
			logger.warn("DatabaseException", e);
			successful = false;
		} catch (IllegalArgumentException e){
        	logger.warn("IllegalArgumentException", e);
            successful = false;
        }
		
		return successful;		
	}
	
	/**
     * Returns the version number of the last stored state.
     * Returns Integer.MAX_VALUE in the event of a database error. 
     * Can't return -1 to indicate error like other methods that return ints because negative values can be used 
     * 	to represent something Frontier specific.  
     * @return 
     */
    private int getLatestVersionNumber() {    
    	int lastVersion = Integer.MAX_VALUE;
    	
    	try{
    		PrimaryIndex<Integer, NodeState> index = dao.getNodeStateByVersion();
    		EntityCursor<Integer> keys = index.keys();
    		Integer last = keys.last();
    		
    		
    		
    		if(last != null) lastVersion = last.intValue();
    		else lastVersion = 0;
    		
    		keys.close();
    		
    	} catch(DatabaseException e){
    		logger.warn("DatabaseException", e);
    	}

		return lastVersion;
	}
}
