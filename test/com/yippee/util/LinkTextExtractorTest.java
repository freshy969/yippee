package com.yippee.util;

import com.yippee.db.model.DocAug;
import com.yippee.indexer.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LinkTextExtractorTest {
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

    // TODO: there must be 5 tables, depending of the url of the current page, this is only for (1):
    // 1. for http://crawltest.cis.upenn.edu/index.html
    // 2. for http://crawltest.cis.upenn.edu/index
    // 3. for http://crawltest.cis.upenn.edu/index/
    // 4. for http://crawltest.cis.upenn.edu/
    // 5. for http://crawltest.cis.upenn.edu:8080
    String[] baseUrls = { 	"http://crawltest.cis.upenn.edu/index/index.html",
    						"http://crawltest.cis.upenn.edu/index.html", 
    		 				"http://crawltest.cis.upenn.edu/index",
    		 				"http://crawltest.cis.upenn.edu/index/",
    		 				"http://crawltest.cis.upenn.edu/",
    		 				"http://crawltest.cis.upenn.edu:8080",
    		 				"http://crawltest.cis.upenn.edu"
    		 				};
        
    // This array contains the correct assertions *24*
    String[] caseOneExpectedUrls = {
            "http://crawltest.cis.upenn.edu/index/rss/cnnp.xml",
            "http://crawltest.cis.upenn.edu/index/rss/cnnt.xml", 
            "http://crawltest.cis.upenn.edu/index/rss/cnnl.xml",

            "http://crawltest.cis.upenn.edu/index/restrict/frontpage.xml",
            "http://crawltest.cis.upenn.edu/index/eurofxref-hist.xml", //

            "http://crawltest.cis.upenn.edu/~nvas/something/Africa.html",
            "http://crawltest.cis.upenn.edu/index/Americas.html",
            "http://crawltest.cis.upenn.edu/AsiaPacific.html",

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
    String[] caseTwoExpectedUrls = {};
    //DocAug docAug;
    
    DocAug[] docAugs;
    
    Parser parser;
    Document doc;

    @Before
    public void setUp() throws Exception {
    	docAugs = new DocAug[baseUrls.length];
    	for(int i = 0; i < baseUrls.length; i++){
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertNotNull(doc);
    }

    @Test
    public void testCaseOneHref() {
        try {
            doc = parser.parseDoc(docAugs[0]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();
        linkEx.extract(docAugs[0].getUrl(), doc);

        ArrayList<String> links = linkEx.getLinks();
        assertEquals(links.size(), caseOneExpectedUrls.length);
        for(int i =  0; i < links.size(); i++){
        	String expected = caseOneExpectedUrls[i];
        	String actual = links.get(i);
        	if(!expected.equals(actual)) System.out.println(caseOneExpectedUrls[i] + "\t\t" + links.get(i));
        	//assertTrue(caseOneExpectedUrls[i].equals(links.get(i)));
        	
        }
        
    }
    
    @Test
    public void testCaseTwoHref(){
    	
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

//		System.out.println(linkEx.getLinks());
        ArrayList<String> links = linkEx.getLinks();
        assertEquals(caseOneExpectedUrls.length, links.size());
    }

    @Test
    public void testHead() {
        try {
            doc = parser.parseDoc(docAugs[0]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LinkTextExtractor linkEx = new LinkTextExtractor();

        linkEx.extract(docAugs[0].getUrl(), doc);

        //		System.out.println(linkEx.getText());
        ArrayList<String> text = linkEx.getText();        
        assertEquals(11, text.size());  // not sure what text means
        assertTrue("CSE455/CIS555 HW2 Grading Data".equals(text.get(0)));
    }
}
