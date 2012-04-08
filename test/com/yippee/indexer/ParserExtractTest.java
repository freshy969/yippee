package com.yippee.indexer;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.DocAug;

public class ParserExtractTest {
	// Test input
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
			"</UL>" +
			"<H3>NON XML files</H3>" +
			"<UL>" +
			"<LI><A HREF=\"1.txt\">1.txt</A></LI>" +
			"<LI><A HREF=\"2.png\">2.png</A></LI>" +
			"</UL>" +
			"</BODY></HTML>";
	
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
	public void testLinkExtractor(){
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
		assertEquals(7, links.size());
		assertTrue("http://crawltest.cis.upenn.edu/rss/cnnp.xml".equals(links.get(0)));
		
//		System.out.println(linkEx.getText());
		ArrayList<String> text = linkEx.getText();
		assertEquals(11, text.size());
		assertTrue("CSE455/CIS555 HW2 Grading Data".equals(text.get(0)));
		
	}
}
