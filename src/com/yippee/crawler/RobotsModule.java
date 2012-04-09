package com.yippee.crawler;

import com.yippee.Configuration;
import com.yippee.db.managers.RobotsManager;
import com.yippee.db.model.RobotsTxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class RobotsModule {

    private URL host;

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
            host = new URL(url.getHost());
            String db = Configuration.getInstance().getBerkeleyDB();
            RobotsManager rm = new RobotsManager(db);
            RobotsTxt robotsTxt = new RobotsTxt();
            if (!rm.read(host.getHost(),robotsTxt)) {
                robotsTxt = fetchRobots();
                rm.create(robotsTxt);
            }
            rm.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fetch robots.txt the host of the url passed to the constructor.
     *
     * @return the RobotsTxt entity object to be inserted to the database
     */
    private RobotsTxt fetchRobots() {
        URLConnection urlConnection = null;
        try {
            urlConnection = host.openConnection();
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
