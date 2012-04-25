package com.yippee.db.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.Before;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.yippee.db.model.AnchorHit;
import com.yippee.db.model.DocAug;
import com.yippee.db.model.HitList;
import com.yippee.db.model.Word;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;
import com.yippee.db.model.Hit;

public class AnchorManager {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(AnchorManager.class);
    private static DBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public AnchorManager(String location) {
        myDbEnv = DBEnv.getInstance(location);
        // Path to the environment home
        // Environment is <i>not</i> readonly
    }

	/**
	 * add a document Hit to the hitlist of a word
	 * updates if word already in db, otherwise adds it
	 * 
	 * @param h
	 * @return
	 */
    public boolean addAnchorHit(AnchorHit h){
    	
    	boolean success = true;
    	
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
        
            if(dao.getAnchorById().contains(h.getWord())) {
            	//System.out.println("in here already "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = dao.getAnchorById().get(h.getWord());
            	hl.addHit(h);
            	dao.getAnchorById().delete(h.getWord());
            	//"updates" entry
            	dao.getAnchorById().put(hl);
            	
            } else {
            	//System.out.println("create new entry "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = new HitList(h.getWord());
            	hl.addHit(h);
            	dao.getAnchorById().put(hl);
            }
            
        } catch (DatabaseException e) {
        	logger.warn("Exception", e);
            success = false;
            System.out.println("db exception");
        } catch (IllegalArgumentException e){
        	logger.warn("Exception", e);
            success = false;
            System.out.println("illegal arg exception");
        }
        
        return success;
    }
    
    /**
     * gives hitlist for a given word
     * aka a HitList object
     * @param wordid
     * @return
     */
    public HitList getHitList(byte[] wordid){
        dao = new DAL(myDbEnv.getEntityStore());
        
        return dao.getAnchorById().get(new String(wordid));
    }
    
    /**
     * deletes word entry, should only be used for test
     * @param wordid
     */
    public void deleteWordEntry(byte[] wordid){
        dao = new DAL(myDbEnv.getEntityStore());
        dao.getAnchorById().delete(new String(wordid));
    }
    
    /**
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }

    
}
