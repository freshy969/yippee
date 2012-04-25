package com.yippee.db.crawler;

import java.net.URL;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.crawler.model.FrontierSavedState;
import com.yippee.db.util.DAL;

public class URLFrontierManager {

	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(URLFrontierManager.class);
    private static CrawlerDBEnv myDbEnv;
    private DAL dao;
    /**
     * Version of the latest sate saved to the database
     */
    private int version;
    
    /**
     * The constructor does not take a folder as an argument, to disable
     * overwrites or writes to other locations. The rest of the managers check
     * to make sure they do not write to this folder.
     */
    public URLFrontierManager(String location) {
    	// Path to the environment home
        // Environment is NOT readonly
        myDbEnv = CrawlerDBEnv.getInstance(location, false);
        dao = new DAL(myDbEnv.getCrawlerStore());
        version = getLatestVersionNumber();
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
    		PrimaryIndex<Integer,FrontierSavedState> index = dao.getFrontierStateByVersion();
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

	/**
     * Method to put the forntier state in the database
     * 
     * @param queues - Map of priority to collection of urls
     * @return true if successful
     */
	public boolean storeState(Map<Integer, Queue<URL>> queues){
		boolean successful = false;
		FrontierSavedState f = new FrontierSavedState(version + 1, queues);
		
		try{
			dao.getFrontierStateByVersion().put(f);
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
	 * Method to load the latest frontier saved state from the database
	 * @return The saved state with the greatest version number
	 */
	public FrontierSavedState loadState(){
		//Retur
		return null;
	}
    
    
}
