package com.yippee.db.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.yippee.db.indexer.model.Word;
import com.yippee.db.util.DAL;

public class LexiconManager {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LexiconManager.class);
	private static IndexerDBEnv myDbEnv;
	private DAL dao;
	private static String endOfWordDeliminator = "::";
	
    /**
     * The constructor takes the BerkeleyDB folder as an argument. It recreates
     * it, if it does not exist.
     */
    public LexiconManager(String location, String locationWordList) {
        myDbEnv = IndexerDBEnv.getInstance(location, false);
        // Path to the environment home
        // Environment is <i>not</i> readonly
        // if lexicon database is empty, then fill it. otherwise words already in database env
        if(isEmpty()){
        	init(locationWordList);
        }
    }
    
    /**
     * Initial import of wordlist to database, should only do this once
     * reads from file of words with associated ids, so all nodes with 
     * lexicon have same wordids for words
     *
     * @return true if successful, otherwise false
     */
    public boolean init(String locationOfWordList) {
        boolean success = true;
        File f = new File(locationOfWordList);
        try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String word = reader.readLine();
			String wordText = null;
			String wordId = null;
			while(word!=null) {
				//if end of word / wordid block -> create Word object
				if(word.length()==0 && wordText!=null && wordId!=null) {
					createWord(wordText, wordId);
					wordText = null;
					wordId = null;
				}
				//we reached a word
				else if(word.endsWith(endOfWordDeliminator)){
					wordText = word.substring(0,word.length()-endOfWordDeliminator.length());
				} 
				//we reached part of the id (could be several lines long)
				else {
					if(wordId==null) {wordId=word;}
					else {wordId+="\n"+word;}
				}
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
    public boolean createWord(String word, String id) {
    	word = word.toLowerCase();
        boolean success = true;
        try {
            // Open the data accessor. This is used to store persistent objects.
            dao = new DAL(myDbEnv.getIndexerStore());
            Word w = new Word();
            w.setWord(word);
            w.setId(id.getBytes());
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
     * sees if word is in lexicon
     * 
     * @param word
     * @return
     */
    public boolean contains(String word) {
    	word = word.toLowerCase();
    	dao = new DAL(myDbEnv.getIndexerStore());  
    	return dao.getLexiconById().contains(word);
    }
    
    /**
     * Method to test if empty
     * @return
     */
    public boolean isEmpty(){
    	boolean result = false;
    	dao = new DAL(myDbEnv.getIndexerStore());  
    	result = dao.getLexiconById().count()==0;
    	return result;
    }
    
    /**
     * returns id of word, otherwise null
     * @param word
     * @return
     */
    public byte[] getWordId(String word) {
    	word = word.toLowerCase();
    	byte[] id = null;
        try {
            // open data access layer
            dao = new DAL(myDbEnv.getIndexerStore());
            Word w = dao.getLexiconById().get(word);
            if(w!=null)
            	id = w.getId();  
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        } 
        return id;
    }
    
    /**
     * returns id of word, otherwise null
     * @param word
     * @return
     */
    public String getWordById(byte[] id) {
        dao = new DAL(myDbEnv.getIndexerStore());
        EntityCursor<Word> curs = dao.getLexiconCursor();
        Word w = curs.next();
        String word = null;
          while(w!=null) {
        	 // if((new String(w.getId())).equals(new String(id))) {
        	  if(Arrays.equals(w.getId(), id)){
        		  word = w.getWord();
        		  break;
        		  }
        	  w = curs.next();
          }
          curs.close();
          return word;
    }
    
    
    /*************** BELOW ARE MANUAL COMMANDS TO CREATE LEXICON  *****************/
    
    /**
     * makes the lexicon file from a list of words
     * will create ids for each word
     * 
     * should only get called once to make the master copy of the lexicon
     * gets called manually
     * 
     * @param locationOfWordList
     * @param nameNewFile
     * @return
     */
    public boolean makeLexiconFile(String locationOfWordList, String nameNewFile) {
    //	locationOfWordList = "doc/en-common.txt";
    //	nameNewFile = "doc/lexicon.txt";
        boolean success = true;
        File fileWords = new File(locationOfWordList);
        File fileLex = new File(nameNewFile);
        try {
			BufferedReader reader = new BufferedReader(new FileReader(fileWords));
			FileWriter writer = new FileWriter(fileLex);
			String word = reader.readLine();
			while(word!=null) {
				writer.write(word+endOfWordDeliminator+"\n"+new String(assignWordId(word))+"\n\n");
				word = reader.readLine();
			}
			reader.close();
			writer.close();
			
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
     * assigns wordid of requested word, this gets called in make lexicon file.
     * nothing else should call this
     * 
     * @param word
     * @return
     */
    public static byte[] assignWordId(String word) {
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
    
    
}
