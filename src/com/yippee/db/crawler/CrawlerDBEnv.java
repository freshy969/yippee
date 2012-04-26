package com.yippee.db.crawler;

import java.io.File;

import org.apache.log4j.Logger;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import com.yippee.db.util.DbShutdownHook;
import com.yippee.util.Configuration;

public class CrawlerDBEnv {

	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(CrawlerDBEnv.class);
    private Environment environment;
    private EntityStore crawlerStore;

    private static CrawlerDBEnv _instance;
    
    public static CrawlerDBEnv getInstance(boolean readonly){
    	if(_instance != null) return _instance;
    	else{
    		String location = Configuration.getInstance().getBerkeleyDBRoot() + "/crawler";
    		System.out.println(location);
    		setup(location, readonly);
    		return _instance;
    	}
    		
    }

	private static void setup(String location, boolean readonly) {		
		// TODO Auto-generated method stub
		_instance = new CrawlerDBEnv();
		
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setReadOnly(readonly);
		environmentConfig.setNodeName("Crawler Environment");
		
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setReadOnly(readonly);

		// If not readonly, then enable creation
		environmentConfig.setAllowCreate(!readonly);
		storeConfig.setAllowCreate(!readonly);
		
		// Create the actual objects
		_instance.environment = new Environment(new File(location), environmentConfig);
		_instance.crawlerStore = new EntityStore(_instance.environment, "Crawler Store", storeConfig);

		
		//Guarantee environment is shutdown upon system exit
		/*
		 *  This technique comes from an online lecture which can be found at:
		 *  http://www.youtube.com/watch?v=7JvmIYjyYYE
		 */
		DbShutdownHook shutdownHook = new DbShutdownHook(_instance.environment, _instance.crawlerStore);
		Runtime.getRuntime().addShutdownHook(shutdownHook); 
		
	}
	
	public EntityStore getCrawlerStore(){
		if(_instance != null) return _instance.crawlerStore;
		else return null;
	}
    
		
}
