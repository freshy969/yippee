package com.yippee.crawler;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Different components of the crawler pass Message objects around. Messages
 * always needs a (predefined) type, a URL, a timestamp and a number of
 * directives. The url is broken into a number of pieces for easy retrieval.
 * <p/>
 * From the nature of the crawler a message is only created to encapsulate
 * information for a specific url and pass them around.
 */
public class Message {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Message.class);

    /**
     * It encapsulates status information or progress condition. This can be
     * either:
     * *   "NEW" for new urls,
     * *   "UPD" for urls with updated  info (like robots.txt),
     * *   "FIN" for the last message and
     * *   "NOX" for invalid urls
     */
    public static enum Type {
        NEW, UPD, FIN, NOX
    }

    /**
     * The actual URL of the message.
     */
    private URL url;
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
     * The timestamp in seconds the latest hit to the same server was done
     */
    private int timestamp;
    /**
     * The URL is not in the robots.txt disallow directives.
     */
    private boolean isAllowed = true;
    /**
     * The list of disallow directives -- afterwards, the only only thing to do
     * for str : disallow {if request.contains(str) abort}
     */
    private List<String> disallow;

    /**
     * The message constructor.It creates a message based on the url. The
     * default type is NEW. If an exception is thrown, then NOX is recorded.
     *
     * @param url the given url as a String
     */
    public Message(String url) {
        try {
            this.url = new URL(url);
            type = Type.NEW;
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            type = Type.NOX;
        }
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCrawlDelay() {
        return crawlDelay;
    }

    public void setCrawlDelay(int crawlDelay) {
        this.crawlDelay = crawlDelay;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

    public List<String> getDisallow() {
        return disallow;
    }

    public void setDisallow(List<String> disallow) {
        this.disallow = disallow;
    }

}
