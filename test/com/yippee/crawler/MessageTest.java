package com.yippee.crawler;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

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
    public void testURLs() {
        boolean test = true;
        for (String url : urls) {
            try {
                if ((new Message(url).getType() == Message.Type.NEW)) {
                    if (!url.equals(new Message(url).getURL())) {
                        System.out.println("["+new Message(url).getURL()+"]");
                        test = false;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                test = false;
            }
        }
        assertTrue(test);
    }

    /**
     * Test malformed urls; these should throw exception
     */
    @Test
    public void testMalformed() {
        boolean test = true;
        for (String url : malformed) {
            try {
                if ((new Message(url).getType() != Message.Type.NOX)) {
                    System.out.println(new Message(url).getURL());
                    test = false;
                }
            } catch (MalformedURLException e) {
                //e.printStackTrace(); // we know that it is malformed!
                test = true;
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
            try{
                if (!(new URL(url)).getHost().equals((new Message(url).getHost()))){
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
     * Test that the oldPath method can provide the getPath() equivalent of the url class
     */
    @Test
    public void testPathEq() {
        boolean test = true;
        for (String url : urls) {
            try{
                if (!(new URL(url)).getPath().equals((new Message(url).getOldPath()))){
                    System.out.println((new URL(url)).getPath() +"|"+(new Message(url)).getOldPath());
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
    public void testType(){
        Message message = null;
        try {
            message = new Message("http://www.example.com");
            message.setType(Message.Type.UPD);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertEquals(Message.Type.UPD, message.getType());
    }

    /**
     * Test a number of variations for short paths, these should work ok
     */
    @Test
    public void testGetPath() {
        boolean result = true;
        String s1 = "http://we.com/index";
        String s2 = "http://we.com/index/";
        String s3 = "http://we.com/";
        String s4 = "http://we.com";
        Message message1 = null; Message message2 = null;
        Message message3 = null; Message message4 = null;
        try {
            message1 = new Message(s1);
            message2 = new Message(s2);
            message3 = new Message(s3);
            message4 = new Message(s4);
            String path1 = message1.getPath();
            String path2 = message2.getPath();
            String path3 = message3.getPath();
            String path4 = message4.getPath();
            if (!path1.equals("/") || !path2.equals("/index/") || !path3.equals("/") || !path4.equals("/")) {
                System.out.println(path1 +"|"+path2 + "|" +path3 +"|"+path4);
                result = false;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = false;
        }
        assertTrue(result);

    }

    /**
     * Test a number of variations for short paths, these should work ok
     */
    @Test
    public void testFile() {
        boolean result = true;
        String s1 = "http://we.com/index";
        String s2 = "http://we.com/index/";
        String s3 = "http://we.com/";
        String s4 = "http://we.com";
        Message message1 = null; Message message2 = null;
        Message message3 = null; Message message4 = null;
        try {
            message1 = new Message(s1);
            message2 = new Message(s2);
            message3 = new Message(s3);
            message4 = new Message(s4);
            String path1 = message1.getFile();
            String path2 = message2.getFile();
            String path3 = message3.getFile();
            String path4 = message4.getFile();
            if (!path1.equals("index") || !path2.equals("") || !path3.equals("") || !path4.equals("")) {
                System.out.println(path1 +"|"+path2);
                result = false;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = false;
        }
        assertTrue(result);

    }

}
