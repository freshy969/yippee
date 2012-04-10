package com.yippee.crawler;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpModule {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HttpModule.class);
    /**
     * Open connection
     */
    private URLConnection connection = null;

    /**
     * Default constructor. Builds the connection but does not issue the call,
     * since we may need to add more headers to the request.
     *
     * @param url the url to be requested
     */
    public HttpModule(URL url) {


        try {
            logger.debug("Initialize with " + url.toString());
            connection = url.openConnection();
            connection.addRequestProperty("User-Agent","cis455crawler");


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String inputLine;
    }

    /**
     *
     */
    public void setAction() {

    }

}
