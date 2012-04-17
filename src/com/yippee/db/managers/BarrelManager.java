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
import com.yippee.db.model.DocAug;
import com.yippee.db.model.Word;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;
import com.yippee.db.model.Hit;

public class BarrelManager {
	static Logger logger = Logger.getLogger(BarrelManager.class);
    private static DBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public BarrelManager(String location) {
        myDbEnv = DBEnv.getInstance(location);
        // Path to the environment home
        // Environment is <i>not</i> readonly
    }

    /**
     * add a document to the hitlist of a word
     */
    public boolean addDocHit(Hit h){
    	boolean success = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            dao.getBarrelById().put(h);
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
     * gives hitlist for a given word
     * aka a list of the Hit objects 
     * @param wordid
     * @return
     */
    public ArrayList<Hit> getHitList(byte[] wordid){
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
    }
    
    /**
     * sorts the hitlist
     * @param hits
     */
    public void sortByDocId(ArrayList<Hit> hits){
    	 Collections.sort(hits,new Comparator<Hit>() {
             public int compare(Hit hit1, Hit hit2) {
                 return hit1.getDocId().compareTo(hit2.getDocId());
             }
         });
    }
    
   
    /**
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }
    
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
    

    
    
    public static void main(String[] args){
    	BarrelManager barrelManager = new BarrelManager("db/test");
    	byte[] wordid1 = {1,2,3};
    	byte[] wordid2 = {1,0,7};
    	byte[] wordid3 = {7,9,0,7,7};
        Hit h1 = new Hit("doc5",wordid1,2);
        Hit h2 = new Hit("doc2",wordid2,67);
        Hit h3 = new Hit("doc2",wordid1,6);
        Hit h4 = new Hit("doc1",wordid1,6907);
        Hit h5 = new Hit("doc1",wordid3,5);  
        Hit h6 = new Hit("doc1",wordid1,5);  
    	barrelManager.addDocHit(h1);
    	barrelManager.addDocHit(h2);
    	barrelManager.addDocHit(h3);
    	barrelManager.addDocHit(h4);
    	barrelManager.addDocHit(h5);
    	barrelManager.addDocHit(h6);
    	ArrayList<Hit> h = barrelManager.getHitList(wordid1);
    	for(Hit a : h) {
    		System.out.println(a.getDocId()+", "+a.getPostion());
    	}
    	
    	//boolean success = true;
    	//ArrayList<Hit> hits = h.getHitList();

    	barrelManager.close();

    }
}
