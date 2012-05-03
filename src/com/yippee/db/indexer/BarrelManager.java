package com.yippee.db.indexer;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;
import com.yippee.db.indexer.model.Hit;
import com.yippee.db.indexer.model.HitList;
import com.yippee.db.util.DAL;

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
    public BarrelManager() {
        myDbEnv = IndexerDBEnv.getInstance(false);
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
    public boolean addDocHits(ArrayList<Hit> list){
    	
    	boolean success = true;
    	
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getIndexerStore());
            String word = list.get(0).getWord();
            if(dao.getBarrelById().contains(word)) {
            	//System.out.println("in here already "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = dao.getBarrelById().get(word);
            	hl.addHitList(list);
            	dao.getBarrelById().delete(word);
            	//"updates" entry
            	dao.getBarrelById().put(hl);
            	
            } else {
            	//System.out.println("create new entry "+h.getDocId()+", "+new String(h.getWordId()));
            	HitList hl = new HitList(word);
            	hl.addHitList(list);
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
     * @param word
     * @return
     */
    public HitList getHitList(String word){
        dao = new DAL(myDbEnv.getIndexerStore());
        
        return dao.getBarrelById().get(word);
    }
    
    /**
     * deletes word entry, should only be used for test
     * @param wordid
     */
    public void deleteWordEntry(String wordid){
        dao = new DAL(myDbEnv.getIndexerStore());
        dao.getBarrelById().delete(wordid);
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
