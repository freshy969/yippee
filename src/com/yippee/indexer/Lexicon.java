package com.yippee.indexer;

import com.yippee.db.indexer.LexiconManager;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Gives back word ID if has word, otherwise keeps log of words hasn't seen before. 
 */
public class Lexicon {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Lexicon.class);
	LexiconManager lexiconManager;
	ArrayList<String> wordsToAdd; //eventually turn into a file, initially we can reject unseen words
	
	public Lexicon(String wordListlocation) {
		lexiconManager = new LexiconManager(wordListlocation);
		wordsToAdd = new ArrayList<String>();
	}
	
	
	/**
	 * checks if the word is in the lexicon
	 * 
	 * @param word
	 * @return true if in lexicon already, otherwise false
	 */
	public boolean isInLexicon(String word) {
		return lexiconManager.contains(word);
	}
	
	/**
	 * gets wordid of requested word
	 * 
	 * @param word
	 * @return
	 */
	public byte[] getWordId(String word) {
		return lexiconManager.getWordId(word);
	}
	
	/**
	 * gets the word given the id, otherwise null
	 * 
	 * @param b
	 * @return
	 */
	public String getWord(byte[] b) {
		return lexiconManager.getWordById(b);
	}
	
	/**
	 * adds new words to lexicon - ignore temporarily 
	 * aka we'll reject words in the document that aren't in the lexicon
	 * 
	 * @param words
	 * @return false if didnt add any words in array, true if did
	 */
	public boolean addNewWords(String[] words) {
		boolean anythingAdded = false;
		for(int i=0; i<words.length; i++){
			anythingAdded = addNewWord(words[i]) || anythingAdded;
		}
		return anythingAdded;
	}
	
	/**
	 * adds new word to lexicon - ignore temporarily 
	 * aka we'll reject words in the document that aren't in the lexicon
	 * 
	 * @param word
	 * @return false if didnt add the word, true if did
	 */
	public boolean addNewWord(String word) {
		if(lexiconManager.contains(word)) {return false;}
		else {
			wordsToAdd.add(word);
			byte[] wordId = LexiconManager.assignWordId(word);
			lexiconManager.createWord(word, new String (wordId));
			logger.log(Priority.INFO, word);
			return true;
		}
	}
	
	public HashMap<String, byte[]> getLexiconMap() {
		return lexiconManager.getLexiconMap();
	}

	public HashMap<String, String> getStopList() {
		return lexiconManager.getStopList();
	}
	
	//so there aren't any bugs...need to get rid of later
	public Lexicon() {
		
	}
	
	public void addListToLexicon(String[] args) {
		
	}
	
	public ArrayList<String> getLexicon(){
		return null;
	}
	
}
