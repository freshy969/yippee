package com.yippee.db.indexer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class HitList {
	
	@PrimaryKey
	private String id;
	
	private byte[] wordId;
	private float idf;
	private float df;
	private HashMap<String, Float> tfMap;
	
	/**
	 * list of hits for that word
	 */
	private ArrayList<Hit> hitList;
	
	public HitList(String i) {
		id = i;
		wordId = i.getBytes();
		hitList = new ArrayList<Hit>();
		tfMap = new HashMap<String, Float>(); 
	}
	
	public HitList() {
		hitList = new ArrayList<Hit>();
		tfMap = new HashMap<String, Float>(); 
	}
	
	public HashMap<String, Float> getTfMap(){
		return tfMap;
	}
	
	public void setTfMap(HashMap<String, Float> map){
		tfMap = map;
	}
	
	public float getIdf(){
		return idf;
	}

	public void setIdf(float f){
		this.idf = f;
	}

	public float getDf(){
		return df;
	}

	public void setDf(float f){
		this.df = f;
	}
	
	public void addHit(Hit hit) {
		hitList.add(hit);
	}
	
	public void addHitList(ArrayList<Hit> hits) {
		hitList.addAll(hits);
	}
	
	public void setHitList(ArrayList<Hit> hits) {
		hitList = hits;
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
