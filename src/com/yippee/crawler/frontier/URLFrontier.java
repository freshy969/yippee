package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
/**
 * The URLFrontier interface encodes the frontier queue functionality in which
 * URL's are pushed by the threads and pulled by the threads at a FIFO basis.
 * Note that, as described in the Mercator paper, the pushing priority should be
 * handled by each thread, and the pulling priority should be handled by the
 * URLFrontier implementation.
 * <p/>
 * The concrete class will need to implement push, pop, save to disk and load
 * from disk.It gets to handle the prioritization scheme and persistence I/O.
 */
public interface URLFrontier {

    /**
     * Pulls the message from the shared queue.
     *
     * @return a message containing the url information, robots.txt etc
     * @throws InterruptedException when empty and a new item is pushed
     */
    public Message pull() throws InterruptedException;

     /**
     * Pushes the URL to the shared queue.
     *
     * @param message a message object containing the all url information
     *        augmented with info from robots.txt etc.
     */
    public void push(Message message);

    /**
     * Save the state of the URLFrontier to persistent storage.
     *
     * @return true if saved successfully, false o/w
     */
    public boolean save();

    /**
     * Load the state of the URLFrontier from persistent storage.
     *
     * @return tre if loading completed successfully, false o/w
     */
    public boolean load();
}
