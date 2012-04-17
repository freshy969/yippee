package com.yippee.db.managers;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.yippee.db.model.DocAug;
import com.yippee.db.util.DAL;
import com.yippee.db.util.DBEnv;

import java.io.File;
import java.util.Iterator;

public class DocAugManager {
    private static DBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor does not take a folder as an argument, to disable
     * overwrites or writes to other locations. The rest of the managers check
     * to make sure they do not write to this folder.
     */
    public DocAugManager(String location) {
        myDbEnv = DBEnv.getInstance(location);
        // Path to the environment home
        // Environment is <i>not</i> readonly
    }


    /**
     * Insert an augmented document to the database
     *
     * @param docAug the document to be inserted
     * @return true if everything ok; false o/w
     */
    public boolean create(DocAug docAug) {
        boolean success = true;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            dao.getDocById().put(docAug);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * Read an augmented document from the database
     *
     * @param key the id of the document
     * @return the augmented document
     */
    public DocAug read(String key) {
        DocAug result = null;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            result = dao.getDocById().get(key);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Remove an augmented document from the database
     *
     * @param key the id of the document
     * @return true if ok; false if an exception was thrown
     */
    public boolean delete(String key) {
        boolean result = true;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.
            dao = new DAL(myDbEnv.getEntityStore());
            result = dao.getDocById().delete(key);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Close the database environment
     */
    public void close() {
        myDbEnv.close();
    }

    /**
     * Push an augmented document from the database
     */
    public boolean push(DocAug doc) {
        return create(doc);
    }

    /**
     * Poll an augmented document from the database
     *
     * @return the "next" document from the database, null if there are no items
     */
    public DocAug poll() {
         DocAug result = null;
        try {
            // open data access layer
            dao = new DAL(myDbEnv.getEntityStore());
            EntityCursor<DocAug> cursor = dao.getDocCursor();
            Iterator<DocAug> docIterator = cursor.iterator();
            if (docIterator.hasNext()) {
                result = docIterator.next();
            }
            cursor.close();
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
        if (result != null) delete(result.getId()); // remove object from db
        return result;
    }

    /**
     * Peek an augmented document from the database (read but not remove it)
     *
     * @return the "next" document from the database
     */
    public DocAug peek() {
         DocAug result = null;
        try {
            // open data access layer
            dao = new DAL(myDbEnv.getEntityStore());
            EntityCursor<DocAug> cursor = dao.getDocCursor();
            Iterator<DocAug> docIterator = cursor.iterator();
            if (docIterator.hasNext()) {
                result = docIterator.next();
            }
            cursor.close();
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }
}