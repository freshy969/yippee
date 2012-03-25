package com.yippee.crawler;

/**
 * Different components of the crawler pass Message objects around. Messages
 * always needs a (predefined) type, a URL, a timestamp and a number of
 * directives.
 */
public class Message {
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

    }

}
