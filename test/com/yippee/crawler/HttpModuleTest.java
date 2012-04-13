package com.yippee.crawler;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpModuleTest extends TestCase{

    @Before
    public void setUp() throws Exception {

    }

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
