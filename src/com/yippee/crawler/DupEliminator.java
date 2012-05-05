package com.yippee.crawler;

import com.yippee.db.crawler.DuplicateManager;

import java.net.URL;

/**
 * The Duplicate URL Eliminator class is responsible for removing duplicate URLs
 * from the iterative process of crawling.
 */
public class DupEliminator {

    /**
     * Tests whether a URL has already been seen in the past.
     *
     * @param url the url to be tested
     * @return true if exists; false o/w
     *
     * TODO: It is static, check if we need to create new manager each time
     */
    public static boolean exists(String url){
        DuplicateManager duplicateManager = new DuplicateManager();
        return duplicateManager.exists(url);
    }

    /**
     * Tests whether a URL has already been seen in the past.
     *
     * @param url the URL object to be tested
     * @return true if exists; false o/w
     */
    public static boolean exists(URL url){
        DuplicateManager duplicateManager = new DuplicateManager();
        return duplicateManager.exists(url.toString());
    }
}
