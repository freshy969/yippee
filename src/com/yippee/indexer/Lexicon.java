package com.yippee.indexer;

import java.util.ArrayList;

/**
 * The Lexicon takes 
 */
public class Lexicon {
	ArrayList<String> lexicon;
	
	public Lexicon() {
		lexicon = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @return arraylist of strings that represents all words in the lexicon currently
	 */
	public ArrayList<String> getLexicon() {
		return lexicon;
	}
	
	/**
	 * takes the array of strings of words and adds them to the lexicon if don't exist yet
	 * 
	 * @param words
	 */
	public void addListToLexicon(String[] words) {
		for(int i=0; i<words.length; i++){
			if(!isInLexicon(words[i])){
				addWord(words[i]);
			}
		}
	}
	
	/**
	 * adds the word to the lexicon
	 * 
	 * @param word
	 */
	public void addWord(String word) {
		lexicon.add(word);
	}
	
	/**
	 * checks if the word is in the lexicon
	 * 
	 * @param word
	 * @return true if in lexicon already, otherwise false
	 */
	public boolean isInLexicon(String word) {
		return lexicon.contains(word);
	}

}
