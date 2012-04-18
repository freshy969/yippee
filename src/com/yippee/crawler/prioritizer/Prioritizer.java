package com.yippee.crawler.prioritizer;

import java.net.URL;

public interface Prioritizer {
	
	/**
	 * Gets priority for the URL
	 * @param url
	 * @return 
	 */
	public int getPriority(URL url);
}
