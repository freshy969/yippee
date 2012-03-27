package com.yippee.db.util;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

import java.io.File;

public class DBEnv {
    private Environment environment;
    private EntityStore entityStore;

    /**
     * The constructor does nothing
     */
    public DBEnv() {
    }

    public void setup(File target, boolean readonly) throws DatabaseException {
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        StoreConfig storeConfig = new StoreConfig();
        environmentConfig.setReadOnly(readonly);
        storeConfig.setReadOnly(readonly);

        // If not readonly, then enable creation
        environmentConfig.setAllowCreate(!readonly);
        storeConfig.setAllowCreate(!readonly);

        // Create the actual objects
        environment = new Environment(target, environmentConfig);
        entityStore = new EntityStore(environment, "EntityStore", storeConfig);
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