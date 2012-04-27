package com.yippee.db.pastry;

import java.io.File;

import org.apache.log4j.Logger;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import com.yippee.db.util.DbShutdownHook;
import com.yippee.util.Configuration;

public class PastryDBEnv {
	
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PastryDBEnv.class);
    private Environment environment;
    private EntityStore pastryStore;

    private static PastryDBEnv _instance;
    
    public static PastryDBEnv getInstance(boolean readonly){
    	if(_instance != null) return _instance;
    	else{
    		String location = Configuration.getInstance().getBerkeleyDBRoot() + "/pastry";
    		System.out.println(location);
    		setup(location, readonly);
    		return _instance;
    	}
    		
    }
    
    private static void setup(String location, boolean readonly) {		
		// TODO Auto-generated method stub
		_instance = new PastryDBEnv();
		
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setReadOnly(readonly);
		environmentConfig.setNodeName("Pastry Environment");
		
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setReadOnly(readonly);

		// If not readonly, then enable creation
		environmentConfig.setAllowCreate(!readonly);
		storeConfig.setAllowCreate(!readonly);
		
		// Create the actual objects
		_instance.environment = new Environment(new File(location), environmentConfig);
		_instance.pastryStore = new EntityStore(_instance.environment, "Pastry Store", storeConfig);

		
		//Guarantee environment is shutdown upon system exit
		/*
		 *  This technique comes from an online lecture which can be found at:
		 *  http://www.youtube.com/watch?v=7JvmIYjyYYE
		 */
		DbShutdownHook shutdownHook = new DbShutdownHook(_instance.environment, _instance.pastryStore);
		Runtime.getRuntime().addShutdownHook(shutdownHook); 
		
	}
	
	public EntityStore getPastryStore(){
		if(_instance != null) return _instance.pastryStore;
		else return null;
	}

}
