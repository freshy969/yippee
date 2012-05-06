package com.yippee.db.indexer;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.util.DAL;


public class DocEntryManager {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(AnchorManager.class);
    private static IndexerDBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public DocEntryManager() {
        myDbEnv = IndexerDBEnv.getInstance(false);
    }

    public boolean addDocEntry(DocEntry de) {
        boolean success = true;
    	
        try {

        	dao = new DAL(myDbEnv.getIndexerStore());
        	PrimaryIndex<String, DocEntry> docIndex = dao.getDocEntryByURL();
        	
        	docIndex.put(de);
        	
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            success = false;
            System.out.println("Exception: " + e.toString());
        } catch (IllegalArgumentException e){
        	logger.warn("Exception", e);
            success = false;
            System.out.println("Exception: " + e.toString());
        }
        
    	return success;
    }
    
    public DocEntry getDocEntry(String URL) {
    	
    	DocEntry docEntry = null;
    	
    	try {

        	dao = new DAL(myDbEnv.getIndexerStore());
        	PrimaryIndex<String, DocEntry> docIndex = dao.getDocEntryByURL();
        	
        	docEntry = docIndex.get(URL);
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            System.out.println("Exception: " + e.toString());
        } catch (IllegalArgumentException e){
        	logger.warn("Exception", e);
            System.out.println("Exception: " + e.toString());
        }

        return docEntry;
    }
    
    public boolean updatePageRank(String url, float pagerank){
    	
    	try {
        	dao = new DAL(myDbEnv.getIndexerStore());
        	PrimaryIndex<String, DocEntry> docIndex = dao.getDocEntryByURL();
        	
        	DocEntry entry = docIndex.get(url);
        	entry.setPagerank(pagerank);
        	docIndex.put(entry);
        	
        } catch (DatabaseException e) {
        	logger.warn("DatabaseException", e);
        	return false;
        } catch (IllegalArgumentException e){
        	logger.warn("IllegalArgumentException", e);
        	return false;
        }
    	
    	return false;
    }
}
