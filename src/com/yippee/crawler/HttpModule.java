package com.yippee.crawler;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Response code (200, 400 etc)
     */
    private int status;
    /**
     * The hash-map containing the headers
     */
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();
    /**
     * Keep log if an error has occurred or something weird happened
     */
    private boolean valid = true;

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
            valid = false;
        }
    }

    /**
     * Set action to HEAD or GET
     *
     * @param action the action to be
     */
    public void setAction(String action) throws IllegalArgumentException {
        if (action.equals("HEAD") || action.equals("GET"))
            throw new IllegalArgumentException();
        try {
            connection.setRequestMethod(action);
        } catch (ProtocolException e) {
            logger.warn("Protocol Exception");
            valid = false;
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

                status = connection.getResponseCode();
                logger.debug("code "+status);
                for (Map.Entry<String, List<String>> header :
                        connection.getHeaderFields().entrySet()) {
                	
                	if(header.getKey() != null) {
                		headers.put(header.getKey().toLowerCase(), header.getValue());
                	}

                }
                // Returning an empty string is not exactly what we want
                if (status != 200) {
                    valid = false;
                    return null;
                }
                BufferedReader inputReader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));
                String inputLine;
                content = "";
                while ((inputLine = inputReader.readLine()) != null) {
                    content += inputLine;
                }
                inputReader.close();
            } catch (IOException e) {

                logger.warn("Error reading page content ", e);
                content = "";
                valid = false;
            }
        }
        return content;
    }

    /**
     * Keep track if an error has ever occurred or not, and send this to the user.
     * @return
     */
    public boolean isValid(){
        return valid;
    }

}
