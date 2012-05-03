package com.yippee.crawler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yippee.db.crawler.model.RobotsTxt;
import com.yippee.util.Configuration;

public class RobotsTxtCache extends LinkedHashMap<String, RobotsTxt> {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsTxtCache.class);
	private static final long serialVersionUID = -3181513645658365078L;
	private int capacity;
	
	public RobotsTxtCache(){
		this.capacity = Configuration.getInstance().getRobotsCacheSize();
	}
	
	@Override
	public boolean removeEldestEntry(Map.Entry<String, RobotsTxt> eldest) {
		boolean result = this.size() > capacity;
		if(result) logger.debug("Cache eviction");
		return result;
	}
}
