package com.yippee.db.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class HitList {
	
	@PrimaryKey
	private String id;
	
	private byte[] wordId;
	
	/**
	 * list of hits for that word
	 */
	private ArrayList<Hit> hitList;
	
	public HitList(String i) {
		id = i;
		wordId = i.getBytes();
		hitList = new ArrayList<Hit>();
	}
	
	public HitList() {
		hitList = new ArrayList<Hit>();
	}
	
	public void addHit(Hit hit) {
		hitList.add(hit);
	}
	
	public byte[] getWordId() {
		return wordId;
	}
	
	public ArrayList<Hit> getHitList() {
		return hitList;
	}
	
	public void sortHitsByDocId() {
		
   	 Collections.sort(hitList,new Comparator<Hit>() {
         public int compare(Hit hit1, Hit hit2) {
             return hit1.getDocId().compareTo(hit2.getDocId());
         }
     });
   	 
	}
	

}
