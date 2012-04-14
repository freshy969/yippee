package com.yippee.db.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class Hit {
	/**
	 * variables we want to keep that we extract from document about each word
	 * we can add more, if find things are meaningful
	 */
	@PrimaryKey
	private String key;
	
	private String docID;
	private byte[] wordID;
	private boolean caps = false;
	private boolean bold = false;
	private boolean ital = false;
	private int position;
	
	/**
	 * constructor for a Hit - need docID it came from, wordId for that word, and 
	 * position of word in the document
	 * 
	 * @param docID
	 * @param wordID
	 * @param position
	 */
	public Hit(String docID, byte[] wordID, int position) {
		this.docID = docID;
		this.wordID = wordID;
		this.key = new String(wordID)+docID+position;
		this.position = position;
	}
	
	public Hit() {}
	
	/**
	 * sets capitalization variable to true or false. default is fault.
	 * @param b
	 */
	public void setCaps(boolean b){
		caps = b;
	}
	
	/**
	 * sets bold variable to true or false. default is fault.
	 * @param b
	 */
	public void setBold(boolean b){
		bold = b;
	}
	
	/**
	 * sets italicize variable to true or false. default is fault.
	 * @param b
	 */
	public void setItalicize(boolean b){
		ital = b;
	}
	
	/**
	 * 
	 * @return whether word was capitalized
	 */
	public boolean getCaps(){
		return caps;
	}
	
	/**
	 * 
	 * @return whether word was bolded
	 */
	public boolean getBold(){
		return bold;
	}

	/**
	 * 
	 * @return whether word was italisized
	 */
	public boolean getItalicize(){
		return ital;
	}
	
	/**
	 * 
	 * @return docId of Hit
	 */
	public String getDocId() {
		return docID;
	}
	
	/**
	 * 
	 * @return wordId of Hit
	 */
	public byte[] getWordId() {
		return wordID;
	}
	
	/**
	 * 
	 * @return position of word in document it came from
	 */
	public int getPostion() {
		return position;
	}
}
