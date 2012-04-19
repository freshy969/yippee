package com.yippee.db.model;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class AnchorHit extends Hit {
	String URL;
	
	public AnchorHit(String docID, byte[] wordID, int position, String URL) {
		super(docID, wordID, position);
		// TODO Auto-generated constructor stub
		this.URL = URL;
	}
	
	public AnchorHit(){}

	public String getUrl() {
		return URL;
	}
}
