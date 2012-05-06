package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * This class implements the URL Frontier, in which URL's are pushed
 * by the threads and handled by the threads at a FIFO basis.
 * <p/>
 * TODO: Optimize with a priority queue!
 */
public class CoralliaFrontier implements URLFrontier {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(CoralliaFrontier.class);
    /**
     * All future urls, to be requested by threads. These are handled by the
     * URLFrontier at a priority-based model (naive FIFO at the beginning, more
     * sophisticated pattern based on the mercator URLFrontier design at a later
     * point)
     */
    private Map<String, Queue<Message>> future = new LinkedHashMap<String, Queue<Message>>();
    private Map<String, Queue<Message>> shadowed = new LinkedHashMap<String, Queue<Message>>();
    /**
     * True if our queue is empty, false otherwise
     */
    private boolean empty = true;
    /**
     * The hashmap containing the <host,timeout> of each pulled domain.
     * timeout can be:
     * * -1 on pull URL -- THIS SHADOWS THE URL
     * * 0 if there is no robots.txt or no timeout in robots -- this is the
     * default value upon pushing a new url
     * * >0 for the timeout read in robots.txt
     */
    private Map<String, Integer> delays = new HashMap<String, Integer>();
    private Map<String, Integer> timestamps = new HashMap<String, Integer>();
    private Map<String, List<String>> disallows = new HashMap<String, List<String>>();
    /**
     * This array list has all the current url string ids (url db keys) that a
     * a thread is currently working on.
     */
    private List<String> current = new ArrayList<String>();
    /**
     * This array list has all the urls seen thus far, in this instance of the
     * crawler
     */
    private List<String> history = new ArrayList<String>();
    private int noOfTrans = 0;


    /**
     * Pulls the url from the shared queue.
     *
     * @return a URL containing the link information
     * @throws InterruptedException
     */
    public synchronized Message pull() throws InterruptedException {
        // Wait until a request is available.
        while (empty) {
            try {
                logger.info("Waiting");
                logger.info("History: " + history.size());
//                for (int i = 0; i<history.size(); i++) {
//                    logger.info("\t[" + i + "]  "+history.get(i));
//                }
                wait();
            } catch (InterruptedException e) {
                // We are being interrupted for shutdown
                logger.info("Thread " + Thread.currentThread().getName() + ": Queue interrupted");
                throw new InterruptedException();
            }
        }
        // pull from the queue
        Message message = poll();
        noOfTrans--;
        // On a status change notify threads that we have a new request.
        if ((!empty) && noOfTrans == 0) {
            notifyAll(); // why do we notify here?
            empty = true;
        }

        logger.info("Pull: " + message.toString() + " | Size: " + future.size() + "\t actual: " + noOfTrans + "-" + empty);

        return message;
    }

    /**
     * Pushes the URL to the shared queue.
     *
     * @param message a java URL object containing the all url information
     */
    public synchronized void push(Message message) {
        // Now it is not empty -- notify threads that status has changed.
        if ((empty) && ((message.getType() == Message.Type.NEW))) { // even if UPD or NEW it is not empty now
            logger.debug("Queue is empty -- notifyAll");
            empty = false;
            notifyAll();
        }
        // Store message.
        add(message);
        // We don't add UPD in to the queue, we just use their info
        if (message.getType() == Message.Type.NEW) noOfTrans++;
        // prepare shadowing
        // TODO: check url type if UPDATE WE NEED TO unshadow and cache
        // TODO: robots.txt
        logger.info("[Push-dine!]: " + message.getURL().getFile() + " | Size: " + future.size() + "\t actual: " + noOfTrans + "-" + empty);
    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }

    public boolean isSeen(String url) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * When a thread pushes a URL to the URLFrontier, there are two things that
     * could happen:
     * 1.  The URL is new, so it needs to be added in the (FIFO, for start)
     * queue.
     * 2.  The URL is an update, so it probably has information on the crawl
     * delay and the robots.txt directives.
     *
     * @param url the augmented url to be inserted
     */
    private void add(Message url) {
        logger.info("[push]" + url.getURL().getFile());
        logState();
        // crawl update should be never null
        if (url.getType() == Message.Type.UPD) {
            // remove from current, shadow->future,
            // update timestamp, directives and delay
            // add history
            if (current.contains(url.getURL().getFile())) { // but first make sure it is a real update
                current.remove(url.getURL().getFile());
                future.put(url.getURL().getHost(), shadowed.get(url.getURL().getHost()));
                shadowed.remove(url.getURL().getHost());

                // NOTE: we handle only crawl-delay less than a day!
                int secOfDay = Calendar.getInstance().get(Calendar.SECOND) +
                        Calendar.getInstance().get(Calendar.MINUTE) * 60 +
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60;
                timestamps.put(url.getURL().getHost(), secOfDay);
                delays.put(url.getURL().getHost(), url.getCrawlDelay());
                if (url.getDisallow() != null)
                    disallows.put(url.getURL().getHost(), url.getDisallow());

            }

        } else if (url.getType() == Message.Type.NEW) {
            // if currently another thread is working on this, just discard
            if (current.contains(url.getURL().getFile())) return;
            // if it is in future or shadowed again discard
            //if (history.contains(url.getUrl().getFile())) return; no we just update
            // check if we dont have ANY record at all, then check future then shadowed
            if ((!future.containsKey(url.getURL().getHost())) && (!shadowed.containsKey(url.getURL().getHost()))) {
                Queue<Message> urlList = new LinkedList<Message>();
                urlList.add(url);
                future.put(url.getURL().getHost(), urlList);
            } else if (future.containsKey(url.getURL().getHost())) {
                logger.info("why?");
                future.get(url.getURL().getHost()).add(url);
            } else if (shadowed.containsKey(url.getURL().getHost())) {
                shadowed.get(url.getURL().getHost()).add(url);
            }
            // add the ID to history
            history.add(url.getURL().getFile());
        }
        logState();
    }

    /**
     * Implements a custom priority queue -- in fact, the algorithm that selects
     * which URL to provide to a thread requesting one. The algorithm mainly
     * checks the following things:
     * *   TODO Describe ALL DETAILS
     * *   TODO TEST WITH ONE THREAD
     *
     * @return a URL
     */
    private Message poll() {
        logger.info("[pull]");
        logState();

        // step 1: favor new entry.
        Message send = null;
        boolean found = false;
        String key = "";
        Iterator<Map.Entry<String, Queue<Message>>> iterator = future.entrySet().iterator();
        while (iterator.hasNext()) {
            // if there is no such entry in delays, then we found a new entry
            key = iterator.next().getKey();
            if (!delays.containsKey(key)) {
                found = true;
                break;
            }
        }

        // step 2: grab the actual record
        if (found) {
            send = future.get(key).poll();
        } else {
            send = future.entrySet().iterator().next().getValue().poll();
            key = send.getURL().getHost();
        }

        // step3: shadow record -- BUT If it is the last record of this key,
        // remove the whole record??  NO BECAUSE WHEN A NEXT PUSH FOR THE SAME DOMAIN COMES IN,
        // WHILE A THREAD IS WORKING ON THE SAME DOMAIN, IT DOES NOT FIND ANYTHING AND GOES TO FUTURE STATE
//        if (future.get(key).size() == 0) {
//            future.remove(key);
//        } else {
        shadowed.put(key, future.get(key));
        future.remove(key);
//        }

        // step 4: finally push "current"
        current.add(send.getURL().getFile());

        // step 5: Augment url with data from
        // timestamp, delay and disallows before sending
        send.setCrawlDelay(delays.containsKey(key) ? delays.get(key) : 0);
        send.setDisallow(disallows.containsKey(key) ? disallows.get(key) : null);
        send.setTimestamp(timestamps.containsKey(key) ? timestamps.get(key) : 0); // UPDATED TIMESTAMP
        send.setType(Message.Type.UPD);

        logState();

        return send;

    }

    public void logState() {
        logger.info("=======URLFrontier State=======");

        logger.info("= <future>");
        for (String key : future.keySet()) {
            logger.info("= Key: " + key + "\tqueue size: " + ((future.get(key) == null) ? "NULL!" : future.get(key).size()) + "\tdelay:" +
                    delays.get(key) + "\ttimestamp" + timestamps.get(key) + "\tdisallows:" +
                    ((disallows.containsKey(key)) ? disallows.get(key).size() : "none"));
            if (disallows.containsKey(key)) {
                for (String dis : disallows.get(key)) {
                    logger.info(dis);
                }
            }
        }
        logger.info("= </future>");

        logger.info("= <shadowed>");
        for (String key : shadowed.keySet()) {
            logger.info("= Queue size: " + ((shadowed.get(key) == null) ? "NULL!" : shadowed.get(key).size()) + "\tdelay:" +
                    delays.get(key) + "\ttimestamp" + timestamps.get(key) + "\tdisallows:" +
                    ((disallows.containsKey(key)) ? disallows.get(key).size() : "none"));
            if (disallows.containsKey(key)) {
                for (String dis : disallows.get(key)) {
                    logger.info(dis);
                }
            }
        }
        logger.info("= </shadowed>");

        logger.info("= </current>");
        for (String cur : current) logger.info(cur);
        logger.info("= </current>");
        logger.info("=======URLFrontier State=======");
    }


}
