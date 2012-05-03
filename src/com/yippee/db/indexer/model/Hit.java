package com.yippee.db.indexer.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class Hit implements Serializable {
	/**
	 * variables we want to keep that we extract from document about each word
	 * we can add more, if find things are meaningful
	 */
	
	private String docID;
	private byte[] wordID;
	private String word;
	private boolean caps = false;
	private boolean bold = false;
	private boolean ital = false;
	private boolean anchor = false;
	private String docIDfrom;
	private int position;
				
	/**
	 * constructor for a normal Hit - need docID it came from, wordId for that word, and 
	 * position of word in the document
	 * 
	 * @param docID
	 * @param word
	 * @param position
	 */
	public Hit(String docID, String word, int position) {
		this.docID = docID;
		this.word = word;
		this.position = position;
	}
	
	/**
	 * Constructor for Anchor Hits - need docID it points to, docID it came from, and
	 * wordId for that word
	 * 
	 * @param docPointingTo
	 * @param docFrom
	 * @param wordID
	 */
	public Hit(String docPointingTo, String docFrom, String word, int position) {
		this.docID = docPointingTo;
		this.docIDfrom = docFrom;
		this.word = word;
		this.position = position;
		anchor = true;
	}
	
	public Hit(){}
	
	
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
	 * sets anchor variable to true or false. default is fault.
	 * @param b
	 */
	public void setAnchor(boolean b){
		anchor = b;
	}
	
	/**
	 * 
	 */
	public void setWordId(byte[] wordID) {
		this.wordID = wordID;
	}
	
	
	/**
	 * 
	 * @return whether or not an anchor hit
	 */
	public boolean isAnchor(){
		return anchor;
	}
	
	/**
	 * sets document this anchor hit came from
	 * @param b
	 */
	public String getDocCameFrom(){
		return docIDfrom;
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
	public boolean isBold(){
		return bold;
	}

	/**
	 * 
	 * @return whether word was italisized
	 */
	public boolean isItalicize(){
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
	 * @return word of Hit
	 */
	public String getWord() {
		return word;
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
	public int getPosition() {
		return position;
	}

	//@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Hit hit = (Hit) o;
        return getWord().compareTo(hit.getWord()) ;
	}
	
}
