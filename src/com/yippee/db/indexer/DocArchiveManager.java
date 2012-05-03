package com.yippee.db.indexer;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.util.DAL;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class DocArchiveManager {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(DocArchiveManager.class);
    private static IndexerDBEnv myDbEnv;
    private DAL dao;

    /**
     * The constructor does not take a folder as an argument, to disable
     * overwrites or writes to other locations. The rest of the managers check
     * to make sure they do not write to this folder.
     */
    public DocArchiveManager() {
        myDbEnv = IndexerDBEnv.getInstance(false);
        dao = new DAL(myDbEnv.getIndexerStore());
//        System.out.println("DAL: " + dao.toString());
//    	System.out.println("DocINDEX: " + dao.getDocById().toString());
        // Path to the environment home
        // Environment is <i>not</i> readonly
    }


    /**
     * Insert an augmented document to the database
     *
     * @param docAug the document to be inserted
     * @return true if everything ok; false o/w
     */
    public boolean store(DocAug docAug) {
        boolean success = true;
        try {
            // Open the data accessor. This is used to store
            // persistent objects.m
            dao.getDocArcByURL().put(docAug);
            logger.info("Stored DocAug: " + docAug.getUrl());
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
            result = dao.getDocArcByURL().get(key);
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
            result = dao.getDocArcByURL().delete(key);
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Gives back a list of the entire archive.
     *
     * @return returns an ArrayList of all the documents indexed, which is empty if none.
     */
    public ArrayList<DocAug> getAllDocs() {
         ArrayList<DocAug> result = new ArrayList<DocAug>();
         
        try {
            // open data access layer
            EntityCursor<DocAug> cursor = dao.getDocCursor();
            Iterator<DocAug> docIterator = cursor.iterator();
            while (docIterator.hasNext()) {
                result.add(docIterator.next());
            }
            cursor.close();
        } catch (DatabaseException e) {
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }
}