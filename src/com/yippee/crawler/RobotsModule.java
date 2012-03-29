package com.yippee.crawler;

import java.net.URL;

import com.yippee.db.managers.RobotsManager;

public class RobotsModule {
	
	private RobotsManager robotsManager;
	
	public boolean alowedToCrawl(URL url){
		return false;
	}
}
