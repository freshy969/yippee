package com.yippee.db.indexer.model;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class DocEntry implements Comparable<DocEntry> {

	@PrimaryKey
	private String URL;

	private String title, host;
	private Date lastCrawled;
	private float pagerank;
	private float tfidf;
	
	public DocEntry() {};
	
	public DocEntry (String URL, String title, String host, Date lastCrawled, String blurb) {
		this.URL = URL;
		this.title = title;
		this.host = host;
		this.lastCrawled = lastCrawled;
		this.pagerank = 1;
//		this.blurb = blurb;
	}
	
	public void update (String title, Date lastCrawled) {
		this.setTitle(title);
		this.setLastCrawled(lastCrawled);
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getURL() {
		return URL;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLastCrawled(Date lastCrawled) {
		this.lastCrawled = lastCrawled;
	}

	public Date getLastCrawled() {
		return lastCrawled;
	}

	public void setPagerank(float pagerank) {
		this.pagerank = pagerank;
	}

	public float getPagerank() {
		return pagerank;
	}

	public void setTfidf(float tfidf) {
		this.tfidf = tfidf;
	}

	public float getTfidf() {
		return tfidf;
	}

	@Override
	public int compareTo(DocEntry de) {
		float b_pagerank = de.getPagerank();
		float b_tfidf = de.getTfidf();
		
		if (getTfidf() * pagerank > b_tfidf * b_pagerank) 
			return -1;
		
		else return 1;
	}

//	public void setBlurb(String blurb) {
//		this.blurb = blurb;
//	}
//
//	public String getBlurb() {
//		return blurb;
//	}
//	
	
}
