package com.yippee.indexer;

import java.util.ArrayList;

import com.yippee.db.managers.LexiconManager;

/**
 * Gives back word ID if has word, otherwise keeps log of words hasn't seen before. 
 */
public class Lexicon {
	LexiconManager lexiconManager;
	ArrayList<String> wordsToAdd; //eventually turn into a file, initially we can reject unseen words
	
	public Lexicon(String dbEnvlocation, String wordListlocation) {
		lexiconManager = new LexiconManager(dbEnvlocation, wordListlocation);
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
			return true;
		}
	}
	
	/**
	 * closes manager
	 */
	public void close() {
		lexiconManager.close();
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
