package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Different components of the crawler pass Message objects around. Messages
 * always needs a (predefined) type, a URL, a timestamp and a number of
 * directives. The url is broken into a number of pieces for easy retrieval.
 *
 * From the nature of the crawler a message is only created to encapsulate
 * information for a specific url and pass them around.
 */
public class Message {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Message.class);
    /**
     * We handle solely http; we return Type.NOX o/w
     */
    private String protocol;
    /**
     * The port, usually 8080, IF IT IS ANOTHER PORT WE (should) IGNORE THE URL,
     * returning Type.NOX
     *
     * Note, port = 0 means the user did not define port, assuming 80
     */
    private int port;
    /**
     * The host is of a the domain name (containing all levels) for instance:
     * www.domain.com, gr.ubuntu.org, sub.88.de
     */
    private String host;
    /**
     * Last file .html, html or other extensions
     */
    private String file;
    /**
     * The /actual/path/to/the/resource/ just before the last file or action
     * and after
     */
    private String path;
    /**
     * The query parameters are pretty useless at that point
     */
    private String params;
    /**
     * It encapsulates status information or progress condition. This can be
     * either:
     *  *   "NEW" for new urls,
     *  *   "UPD" for urls with updated  info (like robots.txt),
     *  *   "FIN" for the last message and
     *  *   "NOX" for invalid urls
     */
    public static enum Type {NEW, UPD, FIN, NOX}
    /**
     * One of the types defines above
     */
    private Type type;
    /**
     * The crawl delay is fed from robots.txt, if such a resource exists; if
     * not, the crawlDelay is 0.
     */
    private int crawlDelay;
    /**
     * The list of disallow directives -- afterwards, the only only thing to do
     * for str : disallow {if request.contains(str) abort}
     */
    private ArrayList<String> disallow;
    /**
     * The timestamp in seconds the latest hit to the same server was done
     */
    private int timestamp;
    /**
     * The URL is not in the robots.txt disallow directives.
     */
    private boolean isAllowed = true;



    /**
     * The default constructor. The url is expected to be of the form:
     * http://domain[/path/to][/action[.extension][?parameters]]
     * sections in [] are not required
     *
     * @param spec the given url as a String
     * @throws MalformedURLException
     */
    public Message(String spec) throws MalformedURLException{
        PropertyConfigurator.configure("log/log4j.properties");
        
        //1. protocol
        //2. type
        if (spec.startsWith("http://")) {
            protocol = "http";
            spec = spec.substring(7);
            type = Type.NEW;
        } else {
            type = Type.NOX;
            return;
        }
        String tempPath = "";
        //3. query
        params = "";
        if (spec.contains("?")) {
            params = spec.split("\\?")[1];
            spec = spec.split("\\?")[0];
        }
        //4. host + path
        host = "";
        path = "";
        file = "";
        if (spec.contains("/")){
            host = spec.split("/")[0];
            path = "/"+spec.split("/",2)[1];
            //5. file
            int  slash  = path.lastIndexOf("/");
            int  dot    = path.lastIndexOf(".");
            // if it is not a file ok -- maybe it is a service
            if (slash > dot) {
                if (slash+1>=path.length()) { //this/is/a/dir/
                    //do nothing, we are initialized correctly
                } else { //this/is/a/service
                    file = path.substring(slash+1,path.length());
                    path = path.substring(0,slash+1);
                }
                // it is a file -- MAYBE keep this info to
                // check .html .html .xhtml .xml .rss .atom
            } else {
                if (slash>0) {
                    file = path.substring(slash+1,path.length());
                    path = path.substring(0,slash+1);
                } else {
                    file = path.substring(slash+1,path.length());
                    path = path.substring(0,slash+1);
                }
            }
        } else {
            host = spec;
            path = "/";
        }
        port = 0; //6. domain + port
        if (host.contains(":")){
            try {
                port = Integer.parseInt(host.split(":")[1]);
                host = host.split(":")[0];
            } catch (NumberFormatException e) {
                type = Type.NOX;
            }
        }
        if (type == Type.NOX) {
            throw new MalformedURLException();
        } else {
            log();
        }
    }

    /**
     * Log the message
     */
    public void log() {
        logger.info("Message url: " + getURL());
    }


    /**
     * Get the protocol -- it is only http at that point
     *
     * @return "http"
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Get the port, usually 80.
     * NOTE, IF PORT returns 0, then the requester SHOULD ASSUME PORT 80.
     * this is only for testing purposes, where we need to be able to make
     * a distinction between given AURLs with port and without port
     * (for assert equals)
     *
     * @return the port integer
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the domain host
     *
     * @return the host string
     */
    public String getHost() {
        return host;
    }

    /**
     * Get the final part of the path, that is the file or service to be
     * invoked, if such a file or service exists, "" o/w
     *
     * @return the file/service string
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the full path, except filename/service
     *
     * @return the path string
     */
    public String getPath() {
        return path;
    }

    /**
     * Get the request query parameters as a whole string. Due to space
     * considerations, these are not broken into <key,value> pairs.
     *
     * @return the query parameters string
     */
    public String getParams() {
        return params;
    }

    /**
     * Get the augmented type.
     *
     * @return augmented type, in the form of a constant
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the augmented type.
     *
     * @param type augmented type, in the form of a constant
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns getPath() in the style of the URL class, which is AURL-equivalent
     * to getPath() + getFile()
     *
     * @return the full path containing /path/to and /file.xtnsion
     */
    public String getOldPath(){
        //System.out.println("\t"+getPath()+"|"+getFile());
        String s = getPath()+getFile();
        return s;
    }

    /**
     * Get the full URL, containing all information as a concatenated string
     *
     * @return the full url string
     */
    public String getURL(){
        String url = "" + protocol + "://" + host;
        url += (port==0)? "" : ":"+port;
        url += path+file;
        url += (params.equals(""))? "" : "?"+params;
        return url;
    }

    /**
     * Gets the disallow directives for this url -- if directives are null it
     * means the domain has no such directives
     *
     * @return the disallow directives array list
     */
    public ArrayList<String> getDisallow() {
        return disallow;
    }

    /**
     * Sets the disallow directives for this url -- if directives are null it
     * means the domain has no such directives
     *
     * @param disallow the disallow directives array list to be inserted
     */
    public void setDisallow(ArrayList<String> disallow) {
        this.disallow = disallow;
    }

    /**
     * Gets the crawl delay for this url -- if such a directive does not exist
     * then the delay is by default 0 (the upper classes take care of how to
     * interpret this value, as usually 0 means crawl at a minimum period of 10
     * seconds).
     *
     * @return the crawl delay
     */
    public int getCrawlDelay() {
        return crawlDelay;
    }

    /**
     * Sets the crawl delay for this url -- if such a directive does not exist
     * then the delay is 0
     *
     * @param crawlDelay the crawl delay in seconds
     */
    public void setCrawlDelay(int crawlDelay) {
        this.crawlDelay = crawlDelay;
    }

    /**
     * Get the timestamp in seconds
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp in seconds
     *
     * @param timestamp the timestamp in seconds
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Check if the URL is allowed to be crawled or not
     *
     * @return true if allowed, false o/w
     */
    public boolean isAllowed() {
        return isAllowed;
    }

    /**
     * Set whether the URL is allowed to be crawled or not
     *
     * @param allowed true if allowed, false o/w
     */
    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

}
