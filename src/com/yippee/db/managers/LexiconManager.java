package com.yippee.db.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import rice.pastry.Id;


import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.persist.EntityCursor;
import com.yippee.db.model.DocAug;
import com.yippee.db.model.RobotsTxt;
import com.yippee.db.model.Word;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;

public class LexiconManager {
	static Logger logger = Logger.getLogger(LexiconManager.class);
	private static DBEnv myDbEnv;
	private DAL dao;
	
    /**
     * The constructor does not take a folder as an argument, to disable
     * overwrites or writes to other locations. The rest of the managers check
     * to make sure they do not write to this folder.
     */
    public LexiconManager(String location, String locationWordList) {
        myDbEnv = new DBEnv();
        // Path to the environment home
        // Environment is <i>not</i> readonly
        myDbEnv.setup(new File(location), false);
        //if lexicon database is empty, then fill it. otherwise words already in database env
        if(isEmpty()){
        	init(locationWordList);
        }
    }
    
    /**
     * Initial import of wordlist to database, should only do this once
     *
     * @return true if successful, otherwise false
     */
    public boolean init(String locationOfWordList) {
        boolean success = true;
        File f = new File(locationOfWordList);
        try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			//add words into database
			String word = reader.readLine();
			while(word!=null) {
				createWord(word);
				word = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			logger.warn("Exception", e);
            success = false;
		} catch (IOException e) {
			logger.warn("Exception", e);
            success = false;
		}
        return success;
    }
    
    /**
     * Initial import of wordlist to database, creates word object
     *
     * @return true if successful, otherwise false
     */
    public boolean createWord(String word) {
    	//System.out.println("in createWord");
        boolean success = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            Word w = new Word();
            w.setWord(word);
            w.setId(assignWordId(word));
            dao.getLexiconById().put(w);
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
     * assigns wordid of requested word
     * 
     * @param word
     * @return
     */
    public byte[] assignWordId(String word) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] content = word.getBytes();
		md.update(content);
		byte[] shaDigest = md.digest();
		return shaDigest;
    }
    
    /**
     * sees if word is in lexicon
     * 
     * @param word
     * @return
     */
    public boolean contains(String word) {
    	dao = new DAL(myDbEnv.getEntityStore());  
    	return dao.getLexiconById().contains(word);
    }
    
    /**
     * Method to test if empty
     * @return
     */
    public boolean isEmpty(){
    	boolean result = false;
    	dao = new DAL(myDbEnv.getEntityStore());  
    	result = dao.getLexiconById().count()==0;
    	return result;
    }
    
    /**
     * returns id of word, otherwise null
     * @param word
     * @return
     */
    public byte[] getWordId(String word) {
        try {
            // open data access layer
            dao = new DAL(myDbEnv.getEntityStore());
            return dao.getLexiconById().get(word).getId();  
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * returns id of word, otherwise null
     * @param word
     * @return
     */
    public String getWordById(byte[] id) {
        dao = new DAL(myDbEnv.getEntityStore());
        EntityCursor<Word> curs = dao.getLexiconCursor();
        Word w = curs.next();
        int count=0;
        String word = null;
          while(w!=null) {
        	  if((new String(w.getId())).equals(new String(id))) {
        		  word = w.getWord();
        		  break;
        		  }
        	  w = curs.next();
          }
          curs.close();
          return word;
    }
    
    /**
     * for testing purposes. prints out words towards the end
     */
    public void list() {
        dao = new DAL(myDbEnv.getEntityStore());
        EntityCursor<Word> curs = dao.getLexiconCursor();
        Word w = curs.next();
        int count=0;
          while(w!=null) {
        	  if(count>115510) System.out.println(w.getWord()+", "+new String(w.getId()));
        	  w = curs.next();
        	  count++;
          }
          curs.close();
      }

    /**
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }
    
}
