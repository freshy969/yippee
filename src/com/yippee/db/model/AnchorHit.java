package com.yippee.db.model;

public class AnchorHit extends Hit {
	String URL;
	
	public AnchorHit(String docID, byte[] wordID, int position, String URL) {
		super(docID, wordID, position);
		// TODO Auto-generated constructor stub
		this.URL = URL;
	}

	public String getUrl() {
		return URL;
	}
}
