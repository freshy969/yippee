package com.yippee.db.indexer;

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
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.indexer.model.Hit;
import com.yippee.db.indexer.model.HitList;
import com.yippee.db.indexer.model.Word;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;

public class BarrelManager {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(BarrelManager.class);
    private static IndexerDBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public BarrelManager(String location) {
        myDbEnv = IndexerDBEnv.getInstance(location, false);
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
    public boolean addDocHit(Hit h){
    	
    	boolean success = true;
    	
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getIndexerStore());
        
            if(dao.getBarrelById().contains(new String(h.getWordId()))) {
            	//System.out.println("in here already "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = dao.getBarrelById().get(new String(h.getWordId()));
            	hl.addHit(h);
            	dao.getBarrelById().delete(new String(h.getWordId()));
            	//"updates" entry
            	dao.getBarrelById().put(hl);
            	
            } else {
            	//System.out.println("create new entry "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = new HitList(new String(h.getWordId()));
            	hl.addHit(h);
            	dao.getBarrelById().put(hl);
            }
            
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
    
    /**
     * gives hitlist for a given word
     * aka a HitList object
     * @param wordid
     * @return
     */
    public HitList getHitList(byte[] wordid){
        dao = new DAL(myDbEnv.getIndexerStore());
        
        return dao.getBarrelById().get(new String(wordid));
    }
    
    /**
     * deletes word entry, should only be used for test
     * @param wordid
     */
    public void deleteWordEntry(byte[] wordid){
        dao = new DAL(myDbEnv.getIndexerStore());
        dao.getBarrelById().delete(new String(wordid));
    }
    
    
    /**
     * gives hitlist for a given word
     * aka a list of the Hit objects 
     * @param wordid
     * @return
     */
/*    public ArrayList<Hit> getHitList(byte[] wordid){
        dao = new DAL(myDbEnv.getEntityStore());
        EntityCursor<Hit> curs = dao.getBarrelCursor();
        Hit hitList = curs.next();
        ArrayList<Hit> h = new ArrayList<Hit>();
          while(hitList!=null) {
        	  if(Arrays.equals(hitList.getWordId(), wordid)) {
        		  h.add(hitList);
        	  }
        	  hitList = curs.next();
          }
        curs.close();
        return h;
    }*/
/*    
    *//**
     * sorts the hitlist
     * @param hits
     *//*
    public void sortByDocId(ArrayList<Hit> hits){
    	 Collections.sort(hits,new Comparator<Hit>() {
             public int compare(Hit hit1, Hit hit2) {
                 return hit1.getDocId().compareTo(hit2.getDocId());
             }
         });
    }
    */
    
/*    *//**
     * does word exist in barrel
     *//*
    public boolean existWord(byte[] id){
    	dao = new DAL(myDbEnv.getEntityStore());  
    	return dao.getBarrelById().contains(new String(id));
    }
    
    *//**
     * add new wordid entry to barrel db
     *//*
    public void createNewHitListEntry(byte[] wordid, Hit h){
    	HitList hitList = new HitList();
    	hitList.setWordId(wordid);
    	hitList.addHit(h);
    	System.out.println("created hitlist");
    	dao = new DAL(myDbEnv.getEntityStore());  
    	dao.getBarrelById().put(hitList);
    	System.out.println("put inside database");
    }
    
	*//**
	 * updates entry
	 * @param wordid
	 * @param h
	 * @return
	 *//*
    public boolean updateHitListEntry(byte[] wordid, Hit h){
        dao = new DAL(myDbEnv.getEntityStore());
        EntityCursor<HitList> curs = dao.getBarrelCursor();
        HitList hitList = curs.next();
        boolean success = false;
          while(hitList!=null) {
        	  if(Arrays.equals(hitList.getWordId(), wordid)) {
        		  hitList.addHit(h);
        		  hitList.sortListByDocID();
        		  success=true;
        		  break;
        	  }
        	  hitList = curs.next();
          }
        curs.close();
        return success;
    }
    
    public HitList getHitList(byte[] wordid){
        dao = new DAL(myDbEnv.getEntityStore());
        EntityCursor<HitList> curs = dao.getBarrelCursor();
        HitList hitList = curs.next();
        HitList myHitList = null;
          while(hitList!=null) {
        	  if(Arrays.equals(hitList.getWordId(), wordid)) {
        		  myHitList = hitList;
        		  break;
        	  }
        	  hitList = curs.next();
          }
        curs.close();
        return myHitList;
    }*/
    
}
