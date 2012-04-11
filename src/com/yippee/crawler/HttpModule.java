package com.yippee.crawler;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpModule {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HttpModule.class);
    /**
     * Store the Http connection
     */
    private HttpURLConnection connection;
    /**
     * Store the actual content of the page
     */
    private String content;

    /**
     * Default constructor. Builds the connection but does not issue the call,
     * since we may need to add more headers to the request.
     *
     * @param url the url to be requested
     */
    public HttpModule(URL url) {
        try {
            logger.debug("Open connection to" + url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "cis455crawler");
        } catch (IOException e) {
            logger.warn("ERROR trying to open" + url.toString());
            e.printStackTrace();
        }
    }

    /**
     * Set action to HEAD or GET
     *
     * @param action the action to be
     *               <p/>
     *               TODO: Somehow force GET or HEAD instead of exception
     */
    public void setAction(String action) throws IllegalArgumentException {
        if (action.equals("HEAD") || action.equals("GET"))
            throw new IllegalArgumentException();
        try {
            connection.setRequestMethod(action);
        } catch (ProtocolException e) {
            logger.warn("Protocol Exception");
            e.printStackTrace();
        }
    }

    /**
     * Set the connection timeout if needed
     *
     * @param seconds in seconds
     */
    public void setTimeout(int seconds) {
        connection.setConnectTimeout(seconds * 1000);
    }

    /**
     * Get the actual html (etc) content from the remote web resource.
     *
     * @return content as a string
     */
    public String getContent() {
        if (content == null) {
            try {
                BufferedReader inputReader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
                    content += inputLine;
                }
                inputReader.close();
            } catch (IOException e) {
                logger.warn("There was an error reading page content");
                e.printStackTrace();
            }
        }
        return content;
    }

}
