package com.yippee.util;

import com.yippee.db.model.DocAug;
import com.yippee.indexer.Parser;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LinkTextExtractorTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LinkTextExtractorTest.class);
    /*
    Please do not remove urls, since the tests are going to break;
    if you want to add a url, just add it below, and update assertUrls table!
    Current Number: *24* (update also url assertion arrays)
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
            "<li><a href=\"http://d.o.t.y/~nvas/something/Africa.html\">dots</a></li>\n" +
            "<li><a href=\"http://domain:8080/./Americas.html\">port</a></li>\n" +
            "<li><a href=\"http://www.seas.upenn/../AsiaPacific.html\">..</a></li>\n" +

            "<li><a href=\"http://we.com/index.php\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/index/\">domain</a></li>\n" +
            "<li><a href=\"http://we.com\">domain</a></li>\n" +
            "<li><a href=\"http://we.com/\">domain</a></li>\n" +

            // these are a bit advanced (level 2) -- these should have different semantics depending whether we are in a dir or page!
            "      <li><a href=\"nothingSpecial/./ea.html\">Business</a></li>\n" +
            "      <li><a href=\"nothingSpecial/\">Europe</a></li>\n" +
            "      <li><a href=\"nothingSpecial\">Front Page</a></li>\n" +
            "      <li><a href=\"nothingSpecial/whatevah/../ea.html\">Middle East</a></li>\n" +

            "      <li><a href=\"?who=me\">params</a></li>" +
            "      <li><a href=\"#tag\">tags</a></li>" +
            "</UL>" +
            "<H3>NON XML files</H3>" +
            "<UL>" +
            "<LI><A HREF=\"1.txt\">1.txt</A></LI>" +
            "<LI><A HREF=\"2.png\">2.png</A></LI>" +
            "</UL>" +
            "</BODY></HTML>";

    // 0. for http://crawltest.cis.upenn.edu/index/index.html
    // 1. for http://crawltest.cis.upenn.edu/index.html
    // 2. for http://crawltest.cis.upenn.edu/index
    // 3. for http://crawltest.cis.upenn.edu/index/
    // 4. for http://crawltest.cis.upenn.edu/
    // 5. for http://crawltest.cis.upenn.edu:8080
    // 6. for "http://crawltest.cis.upenn.edu"
    String[] baseUrls = {"http://crawltest.cis.upenn.edu/index/index.html",
            "http://crawltest.cis.upenn.edu/index.html",
            "http://crawltest.cis.upenn.edu/index",
            "http://crawltest.cis.upenn.edu/index/",
            "http://crawltest.cis.upenn.edu/",
            "http://crawltest.cis.upenn.edu:8080",
            "http://crawltest.cis.upenn.edu"
    };

    // This array contains the correct assertions *24*
    // 0. for http://crawltest.cis.upenn.edu/index/index.html
    String[] caseZeroExpectedUrls = {
            "http://crawltest.cis.upenn.edu/index/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/index/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/index/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/index/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/index/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/index/./Americas.html",
            "http://crawltest.cis.upenn.edu/index/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/index/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu/index/index.html?who=me",
            "http://crawltest.cis.upenn.edu/index/index.html#tag",

            "http://crawltest.cis.upenn.edu/index/1.txt",
            "http://crawltest.cis.upenn.edu/index/2.png",
    };

    // This array contains the correct assertions *24*
    // 1. for http://crawltest.cis.upenn.edu/index.html
    String[] caseOneExpectedUrls = {
            "http://crawltest.cis.upenn.edu/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/./Americas.html",
            "http://crawltest.cis.upenn.edu/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/nothingSpecial",
            "http://crawltest.cis.upenn.edu/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu/index.html?who=me",
            "http://crawltest.cis.upenn.edu/index.html#tag",

            "http://crawltest.cis.upenn.edu/1.txt",
            "http://crawltest.cis.upenn.edu/2.png",
    };

    // 2. for http://crawltest.cis.upenn.edu/index
    String[] caseTwoExpectedUrls = {
            "http://crawltest.cis.upenn.edu/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/./Americas.html",
            "http://crawltest.cis.upenn.edu/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/nothingSpecial",
            "http://crawltest.cis.upenn.edu/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu/index?who=me",
            "http://crawltest.cis.upenn.edu/index#tag",

            "http://crawltest.cis.upenn.edu/1.txt",
            "http://crawltest.cis.upenn.edu/2.png",
    };

    // 3. for http://crawltest.cis.upenn.edu/index/
    String[] caseThreeExpectedUrls = {
            "http://crawltest.cis.upenn.edu/index/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/index/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/index/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/index/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/index/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/index/./Americas.html",
            "http://crawltest.cis.upenn.edu/index/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/index/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial",
            "http://crawltest.cis.upenn.edu/index/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu/index/?who=me",
            "http://crawltest.cis.upenn.edu/index/#tag",

            "http://crawltest.cis.upenn.edu/index/1.txt",
            "http://crawltest.cis.upenn.edu/index/2.png",
    };

    // 4. for http://crawltest.cis.upenn.edu/
    String[] caseFourExpectedUrls = {
            "http://crawltest.cis.upenn.edu/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/./Americas.html",
            "http://crawltest.cis.upenn.edu/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/nothingSpecial",
            "http://crawltest.cis.upenn.edu/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu/?who=me",
            "http://crawltest.cis.upenn.edu/#tag",

            "http://crawltest.cis.upenn.edu/1.txt",
            "http://crawltest.cis.upenn.edu/2.png",
    };

    // 5. for http://crawltest.cis.upenn.edu:8080
    String[] caseFiveExpectedUrls = {
            "http://crawltest.cis.upenn.edu:8080/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu:8080/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu:8080/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu:8080/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu:8080/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu:8080/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu:8080/./Americas.html",
            "http://crawltest.cis.upenn.edu:8080/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu:8080/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu:8080/nothingSpecial/",
            "http://crawltest.cis.upenn.edu:8080/nothingSpecial",
            "http://crawltest.cis.upenn.edu:8080/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu:8080?who=me",
            "http://crawltest.cis.upenn.edu:8080#tag",

            "http://crawltest.cis.upenn.edu:8080/1.txt",
            "http://crawltest.cis.upenn.edu:8080/2.png",
    };

    // 6. "http://crawltest.cis.upenn.edu"
    String[] caseSixExpectedUrls = {
            "http://crawltest.cis.upenn.edu/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnt.xml",
            "http://crawltest.cis.upenn.edu/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/./Americas.html",
            "http://crawltest.cis.upenn.edu/../AsiaPacific.html",

            "http://d.o.t.y/~nvas/something/Africa.html",
            "http://domain:8080/./Americas.html",
            "http://www.seas.upenn/../AsiaPacific.html",

            "http://we.com/index.php",
            "http://we.com/index",
            "http://we.com/index/",
            "http://we.com",
            "http://we.com/",

            "http://crawltest.cis.upenn.edu/nothingSpecial/./ea.html",
            "http://crawltest.cis.upenn.edu/nothingSpecial/",
            "http://crawltest.cis.upenn.edu/nothingSpecial",
            "http://crawltest.cis.upenn.edu/nothingSpecial/whatevah/../ea.html",

            "http://crawltest.cis.upenn.edu?who=me",
            "http://crawltest.cis.upenn.edu#tag",

            "http://crawltest.cis.upenn.edu/1.txt",
            "http://crawltest.cis.upenn.edu/2.png",
    };

    DocAug[] docAugs;

    Parser parser;
    Document doc;

    @Before
    public void setUp() throws Exception {
        docAugs = new DocAug[baseUrls.length];
        for (int i = 0; i < baseUrls.length; i++) {
            docAugs[i] = new DocAug();
            docAugs[i].setDoc(testHTML);
            docAugs[i].setUrl(baseUrls[i]);
        }
        parser = new Parser();
        doc = null;
    }

    @Test
    public void testParser() {
        try {
            doc = parser.parseDoc(docAugs[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(doc);
    }

    /**
     * Testing as current page being
     * http://crawltest.cis.upenn.edu/index/index.html case (0)
     */
    @Test
    public void testCaseZeroHref() {
    	
    	System.out.println("Testing Case 0: " + baseUrls[0]);
        try {
            doc = parser.parseDoc(docAugs[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[0].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseZeroExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseZeroExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseZeroExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseZeroExpectedUrls[i], links.get(i));
        }
    }

    /*
     * Testing as current page being
     * http://crawltest.cis.upenn.edu/index.html case (1)
     *
     */
    @Test
    public void testCaseOneHref() {
    	System.out.println("Testing Case 1: " + baseUrls[1]);

        try {
            doc = parser.parseDoc(docAugs[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[1].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseOneExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseOneExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseOneExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseOneExpectedUrls[i], links.get(i));
        }
    }

    /*
     * Testing as current page being
     * http://crawltest.cis.upenn.edu/index (2)
     *
     */
    @Test
    public void testCaseTwoHref() {
    	System.out.println("Testing Case 2: " + baseUrls[2]);

        try {
            doc = parser.parseDoc(docAugs[2]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[2].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseTwoExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseTwoExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseTwoExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseTwoExpectedUrls[i], links.get(i));
        }
    }

    /**
     * Testing for current page being
     * http://crawltest.cis.upenn.edu/index/ (3)
     */
    @Test
    public void testCaseThreeHref() {
    	System.out.println("Testing Case 3: " + baseUrls[3]);

        try {
            doc = parser.parseDoc(docAugs[3]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[3].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseThreeExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseThreeExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseThreeExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseThreeExpectedUrls[i], links.get(i));
        }
    }

    /**
     * Testing for current page being
     * http://crawltest.cis.upenn.edu/ (4)
     */
    @Test
    public void testCaseFourHref() {
    	System.out.println("Testing Case 4: " + baseUrls[4]);

        try {
            doc = parser.parseDoc(docAugs[4]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[4].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseFourExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseFourExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseFourExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseFourExpectedUrls[i], links.get(i));
        }
    }

    /**
     * Testing for current page being
     * http://crawltest.cis.upenn.edu:8080 (5)
     */
    @Test
    public void testCaseFiveHref() {
    	System.out.println("Testing Case 5: " + baseUrls[5]);

        try {
            doc = parser.parseDoc(docAugs[5]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[5].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseFiveExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseFiveExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseFiveExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseFiveExpectedUrls[i], links.get(i));
        }
    }

    /**
     * Testing for current page being
     * "http://crawltest.cis.upenn.edu" (6)
     */
    @Test
    public void testCaseSixHref() {
    	System.out.println("Testing Case 6: " + baseUrls[6]);

        try {
            doc = parser.parseDoc(docAugs[6]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[6].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseSixExpectedUrls.length);
        for (int i = 0; i < links.size(); i++) {
            String expected = caseSixExpectedUrls[i];
            String actual = links.get(i);
            if (!expected.equals(actual))
                System.out.println(caseSixExpectedUrls[i] + "\t\t" + links.get(i));
            assertEquals(caseSixExpectedUrls[i], links.get(i));
        }
    }

    @Test
    public void testNumber() {
        try {
            doc = parser.parseDoc(docAugs[0]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[0].getUrl(), doc);
        ArrayList<String> links = linkEx.getLinks();
        assertEquals(caseOneExpectedUrls.length, links.size());
    }

//    @Test
//    public void testHead() {
//        try {
//            doc = parser.parseDoc(docAugs[0]);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        LinkTextExtractor linkEx = new LinkTextExtractor();
//        linkEx.extract(docAugs[0].getUrl(), doc);
//        ArrayList<String> text = linkEx.getText();
//        assertEquals(11, text.size());  // not sure what text means
//        assertTrue("CSE455/CIS555 HW2 Grading Data".equals(text.get(0)));
//    }
}
