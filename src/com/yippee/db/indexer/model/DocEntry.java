package com.yippee.db.indexer.model;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class DocEntry {

	@PrimaryKey
	private String URL;

	private String title, host;
	private Date lastCrawled;

	public DocEntry() {};
	
	public DocEntry (String URL, String title, String host, Date lastCrawled) {
		this.URL = URL;
		this.title = title;
		this.host = host;
		this.lastCrawled = lastCrawled;
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
}
