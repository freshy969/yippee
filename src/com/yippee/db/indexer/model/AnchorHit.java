package com.yippee.db.indexer.model;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class AnchorHit extends Hit {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8830533605719711098L;
	String URL;
	
	public AnchorHit(String docID, String word,  int position, String URL) {
		super(URL,docID, word, position);
		// TODO Auto-generated constructor stub
		this.URL = URL;
	}
	
	public AnchorHit(){}

	public String getUrl() {
		return URL;
	}
}
