package com.yippee.db.model;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class AnchorHit extends Hit {
	String URL;
	
	public AnchorHit(String docID, String word, int position, String URL) {
		super(docID, word, position);
		// TODO Auto-generated constructor stub
		this.URL = URL;
	}
	
	public AnchorHit(){}

	public String getUrl() {
		return URL;
	}
}
