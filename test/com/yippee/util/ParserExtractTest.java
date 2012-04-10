package com.yippee.util;

import com.yippee.db.model.DocAug;
import com.yippee.indexer.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ParserExtractTest {
    /*
    Please do not remove urls, since the tests are going to break;
    if you want to add a url, just add it below, and update assertUrls table!
     */
    String testHTML = "<HTML><HEAD><TITLE>CSE455/CIS555 HW2 Grading Data</TITLE></HEAD><BODY>" +
            "<H3>XML to be crawled</H3>" +
            "<UL>" +
            "<LI><A HREF=\"rss/cnnp.xml\">CNN politics - MATCHED</A></LI>" +
            "<LI><A HREF=\"rss/cnnt.xml\">CNN top stories - MATCHED</A></LI>" +
            "<LI><A HREF=\"rss/cnnl.xml\">CNN Laws - NOT MATCHED</A></LI>" +
            "</UL>" +
            "<H3>Other XML data</H3>" +
            "<UL>" +
            "<LI><A HREF=\"restrict/frontpage.xml\">BBC frontpage - restricted</A></LI>" +
            "<LI><A HREF=\"eurofxref-hist.xml\">Historical Euro exchange rate data - too large</A></LI>" +

            // these will probably fail (level 3)
            "      <li><a href=\"/~nvas/something/Africa.html\">Africa</a></li>\n" +
            "      <li><a href=\"./Americas.html\">Americas</a></li>\n" +
            "      <li><a href=\"../AsiaPacific.html\">Asia</a></li>\n" +

            //these should pass, are nothing special (level 0)
            "<li><a href=\"http://n.v.a.s.i.ak.is/~nvas/something/Africa.html\">dots</a></li>\n" +
            "<li><a href=\"http://domain:8080/./Americas.html\">port</a></li>\n" +
            "<li><a href=\"http://www.seas.upenn/../AsiaPacific.html\">..</a></li>\n" +
            "<li><a href=\"http://we.com/index.php\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index/\">domain</a></li>\n" +

            // these are a bit advanced (level 2)
            "      <li><a href=\"http://www.cis.upenn.edu/~nvas/crawltest/index/./ea.html\">Business</a></li>\n" +
            "      <li><a href=\"http://www.cis.upenn.edu/~nvas/crawltest/index/\">Europe</a></li>\n" +
            "      <li><a href=\"http://www.cis.upenn.edu/~nvas/crawltest/index\">Front Page</a></li>\n" +
            "      <li><a href=\"http://www.cis.upenn.edu/~nvas/crawltest/index/whatevah/../ea.html\">Middle East</a></li>\n" +
            "      <li><a href=\"http://www.cis.upenn.edu/~nvas/crawltest/index/ea.html\">Movie Reviews</a></li>" +
            "</UL>" +
            "<H3>NON XML files</H3>" +
            "<UL>" +
            "<LI><A HREF=\"1.txt\">1.txt</A></LI>" +
            "<LI><A HREF=\"2.png\">2.png</A></LI>" +
            "</UL>" +
            "</BODY></HTML>";

    // This array contains the correct assertions
    String[] assertUrls = {

    };

    DocAug docAug;

    @Before
    public void setUp() throws Exception {
        docAug = new DocAug();
        docAug.setDoc(testHTML);
        docAug.setUrl("http://crawltest.cis.upenn.edu/index.html");
    }

    @Test
    public void testParser() {
        Parser parser = new Parser();
        Document doc = null;

        try {
            doc = parser.parseDoc(docAug);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertNotNull(doc);
    }

    @Test
    public void testHref() {
        Parser parser = new Parser();
        Document doc = null;

        try {
            doc = parser.parseDoc(docAug);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();

        linkEx.extract(docAug.getUrl(), doc);

//		System.out.println(linkEx.getLinks());
        ArrayList<String> links = linkEx.getLinks();
        assertEquals(7, links.size()); // change this to
        assertTrue("http://crawltest.cis.upenn.edu/rss/cnnp.xml".equals(links.get(0)));
    }


    @Test
    public void testHead() {
        Parser parser = new Parser();
        Document doc = null;

        try {
            doc = parser.parseDoc(docAug);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();

        linkEx.extract(docAug.getUrl(), doc);

        //		System.out.println(linkEx.getText());
        ArrayList<String> text = linkEx.getText();
        assertEquals(11, text.size());
        assertTrue("CSE455/CIS555 HW2 Grading Data".equals(text.get(0)));
    }
}
