package com.yippee.db.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;


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
        if(isEmpty()){
        	init(locationWordList);
        }
    }
    
    /**
     * Initial import of wordlist to database
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
				create(word);
				word = reader.readLine();
			}
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
     * Initial import of wordlist to database
     *
     * @return true if successful, otherwise false
     */
    public boolean create(String word) {
        boolean success = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            Word w = new Word();
            w.setId(word);
            w.setWord(word);
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
     * gets wordid of requested word
     * 
     * @param word
     * @return
     */
    public byte[] getWordId(String word) {
    	return null;
    }
    
    /**
     * sees if word is in lexicon
     * 
     * @param word
     * @return
     */
    public boolean contains(String word) {
    	return true;
    }
    
    /**
     * Method to test 
     * @return
     */
    public boolean isEmpty(){
    	boolean result = false;
    	dao = new DAL(myDbEnv.getEntityStore());  
    	result = dao.getLexiconById().count()==0;
    	return result;
    }
    
    public String peek(String word) {
      //  boolean result = false;
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
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }
    
}
