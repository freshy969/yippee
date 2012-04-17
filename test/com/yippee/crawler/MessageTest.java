package com.yippee.crawler;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageTest {
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

    private String[] malformed = {
            "https://we.com/index",
            //"http://we.com///index",  // this is not malformed
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
             if (!url.equals(new Message(url).getUrl().getPath())){
                 test = false;
                 System.out.println(new Message(url).getUrl().getPath());
                 System.out.println(new Message(url).getUrl().getFile());
             }
        }
        assertTrue(test);
    }

    /**
     * Test malformed urls; these should throw exception
     *
     * @throws MalformedURLException
     */
    @Test(expected = MalformedURLException.class)
    public void testMalformed() throws MalformedURLException {
        boolean test = true;
        for (String url : malformed) {
            if ((new Message(url).getType() != Message.Type.NOX)) {
                test = false;
                System.out.println(new Message(url).getUrl().getPath());
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
                if (!(new URL(url)).getHost().equals((new Message(url).getUrl().getHost()))) {
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
        assertEquals(new URL(url), m.getUrl());
    }

}
