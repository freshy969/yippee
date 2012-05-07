package com.yippee.db.indexer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
	private HashMap<String, Float> atfMap;
	
	/**
	 * list of hits for that word
	 */
//	private ArrayList<Hit> hitList;
//	private ArrayList<Hit> anchorList;
	
	public HitList(String i) {
		id = i;
		wordId = i.getBytes();
//		hitList = new ArrayList<Hit>();
		tfMap = new HashMap<String, Float>(); 
//		anchorList = new ArrayList<Hit>();
		atfMap = new HashMap<String, Float>(); 
	}
	
	public HitList() {
//		hitList = new ArrayList<Hit>();
		tfMap = new HashMap<String, Float>(); 
//		anchorList = new ArrayList<Hit>();
		atfMap = new HashMap<String, Float>(); 
	}
	
	public HashMap<String, Float> getTfMap(){
		return tfMap;
	}
	
	public HashMap<String, Float> getAtfMap(){
		return atfMap;
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
	
//	public void addHit(Hit hit) {
//		hitList.add(hit);
//	}
//	
	
//	public void setHitList(ArrayList<Hit> hits) {
//		hitList = hits;
//	}
	
	public byte[] getWordId() {
		return wordId;
	}
	
//	public ArrayList<Hit> getHitList() {
//		return hitList;
//	}
	
//	public void sortHitsByDocId() {
//		
//   	 Collections.sort(hitList,new Comparator<Hit>() {
//         public int compare(Hit hit1, Hit hit2) {
//             return hit1.getDocId().compareTo(hit2.getDocId());
//         }
//     });
//   	  
//	}
	
	public void addHitList(ArrayList<Hit> hits) {
		HashMap<String, Float> normalizedLength = new HashMap<String, Float>();
		for(int i=0; i<hits.size(); i++){
			Hit hit = hits.get(i);
			
			if(hit.isAnchor()){
//				anchorList.add(hit);
				if(!atfMap.containsKey(hit.getDocId())){
					atfMap.put(hit.getDocId(), new Float(1));
				} else {
					Float f = atfMap.remove(hit.getDocId());
					atfMap.put(hit.getDocId(), f+1);
				}
			} else {
//				hitList.add(hit);
				//System.out.println(hit.getDocLength());
				normalizedLength.put(hit.getDocId(), new Float(hit.getDocLength()));
				if(!tfMap.containsKey(hit.getDocId())){
					tfMap.put(hit.getDocId(), new Float(1));
				} else {
					Float f = tfMap.remove(hit.getDocId());
					tfMap.put(hit.getDocId(), f+1);
				}
			}		
		}
		
		Set<String> keys = normalizedLength.keySet();
		Iterator iter = keys.iterator();
		while(iter.hasNext()){
			String doc = (String)iter.next();
			float tf = tfMap.get(doc);
			float tf_norm = normalizedLength.get(doc);
		//	System.out.println(tf+", "+tf_norm);
			tfMap.put(doc, tf/tf_norm);
		//	System.out.println(tf/tf_norm);
		}
		
		HashMap<String, Float> temp = new HashMap<String, Float>();
		temp.putAll(atfMap);
		temp.putAll(tfMap);
		df = temp.keySet().size();
	//	System.out.println(df);
	}
	

}
