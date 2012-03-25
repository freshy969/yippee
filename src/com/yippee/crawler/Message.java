package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Different components of the crawler pass Message objects around. Messages
 * always needs a (predefined) type, a URL, a timestamp and a number of
 * directives.
 */
public class Message {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Message.class);
    /**
     * It encapsulates status information or progress condition. This can be
     * either:
     *  *   "NEW" for new messages,
     *  *   "UPD" for urls with updated  info (like robots.txt data),
     *  *   "FIN" for messages (once used, discard) and
     *  *   "NOX" for invalid messages
     */
    public static enum Type {NEW, UPD, FIN, NOX}
    /**
     * One of the types defined above
     */
    private Type type;


    /**
     * Default constructor
     */
    public Message(){
        // Use dynamic level, push this to the outermost class
        PropertyConfigurator.configure("log/log4j.properties");
        // Create a new message and log it
        logger.info("Creating new message");
    }

    /**
     * This is just to test the logger -- it works!
     *
     * @param args
     */
    public static void main(String[] args){
        Message ms = new Message();
    }

}
