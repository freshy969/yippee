package com.yippee.crawler;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpModuleTest extends TestCase{
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HttpModuleTest.class);


    /**
     * This is malformed -- the content should be nothing
     */
    @Test
    public void testFalse() {
        String content = "";
        try {
            HttpModule httpModule = new HttpModule(new URL("https://www.tumblr.com/../../blog/nvas"));
            content = httpModule.getContent();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        assertTrue(content == null);
    }

    @Test
    public void testFetch() {

    }

}
