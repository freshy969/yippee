package com.yippee.crawler;

import com.yippee.db.crawler.RobotsManager;
import com.yippee.db.crawler.model.RobotsTxt;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
     * The robots.txt database manager
     */
    private RobotsManager rm;
    private ConcurrentMap<String, RobotsTxt> robotsCache;

    public RobotsModule() {
        rm = new RobotsManager();
        robotsCache = new ConcurrentHashMap<String, RobotsTxt>();
    }

    /**
     * It checks if the url can be crawled with respect only to disallows and
     * <i>not</i> crawl-delay. If needed, it fetches robots.txt and stores all
     * information to the database
     *
     * @param url the url to be crawled
     * @return yes if allowed; no o/w
     */
    public boolean alowedToCrawl(URL url) {
        boolean result = true;
        try {
            robotsURL = new URL(url.getProtocol() + "://" + url.getHost() + "/robots.txt");
            
            if(robotsCache.containsKey(url.getHost())){
            	//answer from cache
            	logger.info("Robots cache hit for: " + url.getHost());
            	return robotsCache.get(url.getHost()).allowedToCrawl(url);
            }
            
            
            //Not in cache, check DB
            RobotsTxt robotsTxt = new RobotsTxt();
            if (!rm.read(robotsURL.getHost(), robotsTxt)) {
                robotsTxt = fetchRobots();
            }

            if (robotsTxt != null) {
                rm.create(robotsTxt);
                
                for (String disallow : robotsTxt.getDisallows()) {
                    if (url.toString().contains(disallow)) {
                        result = false;
                    }
                }
                
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.info("Error with robots.txt");
        }
        return result;
    }

    /**
     * Gets the crawl delay of the given url
     *
     * @param url the resource whose delay we need.
     * @return the crawl delay for this url
     */
    public int getCrawlDelay(URL url) {
        RobotsTxt robotsTxt = new RobotsTxt();

        if (!rm.read(robotsURL.getHost(), robotsTxt)) {
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
        HttpURLConnection urlConnection = null;
        RobotsTxt robotsTxt = new RobotsTxt();
        try {
            logger.debug("["+robotsURL.toString()+"] Starting fetch robots");
            urlConnection = (HttpURLConnection) robotsURL.openConnection();
            urlConnection.setRequestProperty("user-agent", "cis455crawler");

            if (urlConnection.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));

                logger.debug("["+robotsURL.toString()+"] Robots were fetched");

                String line = "";
                boolean record = false; // indicates true when recording disallows
                Set<String> disallow = new HashSet<String>();
                int crawlDelay = 0;
                while ((line = in.readLine()) != null) {
                    if ((line.contains("User-agent:")) && (!line.contains("*"))
                            && (!line.contains("cis455crawler"))) {
                        record = false;

                    } else if ((line.contains("User-agent:")) && ((line.contains("*"))
                            || (line.contains("cis455crawler")))) {
                        record = true;
                        if (line.contains("cis455crawler")) // reset recording
                            disallow = new HashSet<String>();
                    }

                    if (line.contains("Disallow:") && record) {
                        String[] disArray = line.split(":");
                        if (disArray.length>2) {
                            String dis = line.split(":")[1].trim();
                            if (dis.startsWith("/")) {
                                dis = dis.substring(1, dis.length());
                            }
                            if (dis.endsWith("/")) {
                                dis = dis.substring(0, dis.length() - 1);
                            }
                            disallow.add(dis);
                        }
                        //logger.info("Disallow: " + dis + " (trimmed)");
                    } else if (line.contains("Crawl-delay:") && record) {
                        String[] crawlArray = line.split(":");
                        if (crawlArray.length>2) {
                            crawlDelay = Integer.parseInt(crawlArray[1].trim());
                        }
                    }
                }

                robotsTxt.setCrawlDelay(crawlDelay);
                robotsTxt.setDisallows(disallow);
                robotsTxt.setHost(robotsURL.toString());
                logger.debug("["+robotsURL.toString()+"] Fetch robots done\n\n");
                in.close();
            } else {
                logger.debug("No robots.txt at: " + robotsURL.toString()+"; storing dummy constraints");
                robotsTxt.setCrawlDelay(0);
                robotsTxt.setDisallows(new HashSet<String>());
                robotsTxt.setHost(robotsURL.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Error parsing robots.txt for " + robotsURL.toString());
        }
        return robotsTxt;
    }
}
