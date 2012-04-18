package com.yippee.crawler;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(MessageTest.class);
    private String[] urls = {
            "http://n.v.a.s.i.ak.is/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",
            "http://www.cis.upenn.edu/~nvas/crawltest/index/./ea.html",
            "http://www.cis.upenn.edu/~nvas/crawltest/index/",
            "http://www.cis.upenn.edu/~nvas/crawltest/index", //without trailing / !!
            "http://www.cis.upenn.edu/~nvas/crawltest/index/whatevah/../ea.html",
            "http://www.cis.upenn.edu/~nvas/crawltest/index/ea.html",
//            "http://we.com",// we don't care if it's equal to "http://we.com/",
            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
//            "http://crawltest.cis.upenn.edu/"
    };

    // We should handle gracefully
    private String[] malformed = {
            "htt://we.com/index",
            "htt://this",
            "htt://\\\\",
            "**(**)**",
            "http://we.com:what/index"
    };

    /**
     * Test well-formed urls; these should all create messages with no problem
     */
    @Test
    public void testNewTypes() {
        boolean test = true;
        for (String url : urls) {
            if ((new Message(url).getType() != Message.Type.NEW)) test = false;
        }
        assertTrue(test);
    }

    @Test
    public void testUrls(){
        boolean test = true;
        for (String url : urls) {
             if (!url.equals(new Message(url).getURL().toString())){
                 test = false;
             }
        }
        assertTrue(test);
    }

    /**
     * Test malformed urls
     *
     */
    @Test
    public void testMalformed(){
        boolean test = true;
        for (String url : malformed) {
            Message msg = new Message(url);
            if ((msg.getType() != Message.Type.NOX)) {
                test = false;
                System.out.println(msg.getURL().toString());
            }
        }
        assertTrue(test);
    }

    /**
     * Test that the host is equivalent to the getHost() method of the url class
     */
    @Test
    public void testHostEq() {
        boolean test = true;
        for (String url : urls) {
            try {
                if (!(new URL(url)).getHost().equals((new Message(url).getURL().getHost()))) {
                    test = false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                test = false;
            }
        }
        assertTrue(test);
    }

    /**
     * Make sure that the message type works
     */
    @Test
    public void testType() {
        Message message = null;
        message = new Message("http://www.example.com");
        message.setType(Message.Type.UPD);
        assertEquals(Message.Type.UPD, message.getType());
    }

    /**
     * Test URL retrieved from method is what got put in
     *
     * @throws MalformedURLException
     */
    @Test
    public void testRetrieveURL() throws MalformedURLException {
        String url = "http://crawltest.cis.upenn.edu";
        Message m = new Message(url);
        assertEquals(new URL(url), m.getURL());
    }

}
