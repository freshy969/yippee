package com.yippee.db.util;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import org.apache.log4j.Logger;
import com.yippee.db.DbShutdownHook;


import java.io.File;
import java.io.IOException;

public class DBEnv {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(DBEnv.class);
    private Environment environment;
    private EntityStore entityStore;
    
    private static DBEnv _instance;

    /**
     * The constructor does nothing
     */
    public DBEnv() {}
    
    public static DBEnv getInstance(String dbLocation){
    	if(_instance != null) return _instance;
    	else {
    		try{
    			setup(new File(dbLocation), false); 
        		return _instance;
        		
    		} catch(DatabaseException e){
    			e.printStackTrace();
    			return null;
    		} 
    	}
    }

    private static void setup(File target, boolean readonly) throws DatabaseException {
    		
    		_instance = new DBEnv();
    	
    		EnvironmentConfig environmentConfig = new EnvironmentConfig();
    		StoreConfig storeConfig = new StoreConfig();
    		environmentConfig.setReadOnly(readonly);
    		storeConfig.setReadOnly(readonly);

    		// If not readonly, then enable creation
    		environmentConfig.setAllowCreate(!readonly);
    		storeConfig.setAllowCreate(!readonly);

    		// Create the actual objects
    		_instance.environment = new Environment(target, environmentConfig);
    		_instance.entityStore = new EntityStore(_instance.environment, "EntityStore", storeConfig);


    		//Guarantee environment is shutdown upon system exit
    		/*
    		 *  This technique comes from an online lecture which can be found at:
    		 *  http://www.youtube.com/watch?v=7JvmIYjyYYE
    		 */
    		DbShutdownHook shutdownHook = new DbShutdownHook(_instance.environment, _instance.entityStore);
    		Runtime.getRuntime().addShutdownHook(shutdownHook);       
    }



    // Return a handle to the entity store
    public EntityStore getEntityStore() {
        return entityStore;
    }

    // Return a handle to the environment
    public Environment getEnv() {
        return environment;
    }

    // Close the store and environment.
    public void close() {
        if (entityStore != null) {
            try {
                entityStore.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing store: " + dbe.toString());
                System.exit(-1);
            }
        }
        if (environment != null) {
            try {
                // Finally, close the environment.
                environment.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing MyDbEnv: " + dbe.toString());
                System.exit(-1);
            }
        }
    }

}