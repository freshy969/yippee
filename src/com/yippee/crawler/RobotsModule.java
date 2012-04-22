package com.yippee.crawler;

import com.yippee.util.Configuration;
import com.yippee.db.crawler.RobotsManager;
import com.yippee.db.crawler.model.RobotsTxt;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class RobotsModule {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(RobotsModule.class);
    /**
     * The host url
     */
    private URL robotsURL;

    /**
     * It checks if the url can be crawled with respect only to disallows and
     * <i>not</i> crawl-delay. If needed, it fetches robots.txt and stores all
     * information to the database
     *
     * @param url the url to be crawled
     * @return yes if allowed; no o/w
     */
    public boolean alowedToCrawl(URL url) {
        try {
            robotsURL = new URL(url.getProtocol() + "://" + url.getHost() + "/robots.txt");
            String dbPath = Configuration.getInstance().getBerkeleyDBPath();
            RobotsManager rm = new RobotsManager(dbPath);
            RobotsTxt robotsTxt = new RobotsTxt();
            if (!rm.read(robotsURL.getHost(),robotsTxt)) {
                robotsTxt = fetchRobots();
                rm.create(robotsTxt);
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 
     * @return
     */
    public int getCrawlDelay(URL url){
    	
    	 String dbPath = Configuration.getInstance().getBerkeleyDBPath();
         RobotsManager rm = new RobotsManager(dbPath);
         RobotsTxt robotsTxt = new RobotsTxt();
         
         if (!rm.read(robotsURL.getHost(),robotsTxt)) {
        	 //Unsuccessful read
        	 robotsTxt = fetchRobots();
             rm.create(robotsTxt);
         }

    	return robotsTxt.getCrawlDelay();
    }

    /**
     * Fetch robots.txt the host of the url passed to the constructor.
     *
     * @return the RobotsTxt entity object to be inserted to the database
     */
    private RobotsTxt fetchRobots() {
        URLConnection urlConnection = null;
        try {
            urlConnection = robotsURL.openConnection();
            urlConnection.setRequestProperty("user-agent", "cis455crawler");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            boolean record = false; // for recording disallows
            // TODO: MAKE INSTANCE VARS
            Set disallow = new HashSet<String>();
            String content;
            int crawlDelay;
            while ((line = in.readLine()) != null)
                if ((line.contains("User-agent:")) && (!line.contains("*"))
                        && (!line.contains("cis455crawler"))) {
                    record = false;

                } else if ((line.contains("User-agent:")) && ((line.contains("*"))
                        || (line.contains("cis455crawler")))) {
                    record = true;
                    if (line.contains("cis455crawler")) // reset recording
                        disallow = new HashSet<String>();
                }
            // record all directives
            if (line.contains("Disallow:") && record) {
                String dis = line.split(":")[1].trim();
                if (dis.startsWith("/")) {
                    dis = dis.substring(1, dis.length());
                }
                if (dis.endsWith("/")) {
                    dis = dis.substring(0, dis.length() - 1);
                }
                disallow.add(dis);
                //logger.info("Disallow: " + dis + " (trimmed)");
            } else if (line.contains("Crawl-delay:") && record) {
                crawlDelay = Integer.parseInt(line.split(":")[1].trim());
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: LOG ERROR
        }

        return new RobotsTxt();

    }
}
