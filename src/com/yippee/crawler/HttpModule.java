package com.yippee.crawler;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class HttpModule {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HttpModule.class);
    /**
     * Store the connection
     */
    private URLConnection connection;

    /**
     * Default constructor. Builds the connection but does not issue the call,
     * since we may need to add more headers to the request.
     *
     * @param url the url to be requested
     */
    public HttpModule(URL url) {
        try {
            logger.debug("Open connection to" + url.toString());
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent","cis455crawler");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // read
        //BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
    }

    /**
     * Set action to HEAD or GET
     *
     * @param action the action to be
     *
     * TODO: Somehow force GET or HEAD instead of exception
     */
    public void setAction(String action) throws IllegalArgumentException{
        if (action.equals("HEAD") || action.equals("GET"))
                throw new IllegalArgumentException();
        //set property
        //connection.addRequestProperty("User-Agent","GET");
    }

}
