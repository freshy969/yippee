package com.yippee.db.model;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.yippee.crawler.HttpResponse;

/**
 * Object to store robots.txt policy for a given host
 *
 */
@Entity
public class RobotsTxt {

	@PrimaryKey
	String host;
	int crawlDelay;
	Set<String> disallows;
	
	
	/**
	 * Default constructor required by Berkely DB
	 */
	public RobotsTxt() {}
	
	/**
	 * Constructs a RobotsTxt from the HttpResponse resulting from a request to robots.txt at the host
	 * @param robotsResponse
	 */
	public RobotsTxt(HttpResponse robotsResponse){
		disallows = new HashSet<String>();
		//TODO initialize this object
	}
	
	/**
	 * Returns true if this policy allows our crawler to crawl this <url>
	 * @param url
	 * @return true if the policy allows crawling the <url>
	 */
	public boolean allowedToCrawl(URL url){
		return false;
	}

	public String getHost() {
		return host;
	}

	public int getCrawlDelay() {
		return crawlDelay;
	}

	public Set<String> getDisallows() {
		return disallows;
	}	
}
