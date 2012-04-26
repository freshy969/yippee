package com.yippee.indexer;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class IndexerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(IndexerTest.class);
	
//	 Threading tester
	public static void main(String[] args) {
		// Start putting test documents into the queues
		DocCreator doccreate = new DocCreator();
		doccreate.start();
		
		NodeIndex nodeIndex = new NodeIndex();
		
		Indexer indexer = new Indexer(nodeIndex);
		indexer.start();
	}
}

class DocCreator extends Thread {
	
    String testHTML = "<HTML><HEAD><TITLE>CSE455/CIS555 HW2 Grading Data</TITLE></HEAD>" +
            "<H3>XML to be crawled</H3>" +
            "<UL>" +
            "<LI><A HREF=\"rss/cnnp.xml\"><B>CNN's politics - MATCHED</A></B></LI>" +
            "<LI><I><A HREF=\"rss/cnnt.xml\">CNN top stories - MATCHED</A></I></LI>" +
            "<LI><A HREF=\"rss/cnnl.xml\">CNN Laws - N-O-T MATCHED</A></LI>" +
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

            "      <li><a href=\"?who=me\">params</a></li> " +
            "      <li><a href=\"#tag\">tags</a></li>" +
            "</UL>" +
            "<H3>NON XML files</H3>" +
            "<UL>" +
            "<B><LI><A HREF=\"1.txt\">1.txt</A></LI></B>" +
            "<LI><A HREF=\"2.png\">2.png</A></LI>" +
            "</UL>" +
            "</BODY></HTML>";
	
	DocAugManager dam;
	int counter;
	
	public void run() {
		dam = new DocAugManager("db/test/indexer");
		counter = 0;
		
		while (true) {
			DocAug doc = new DocAug();
			doc.setId(String.valueOf(counter) + "ID");
			doc.setUrl(String.valueOf(counter) + ".com");
			doc.setDoc(testHTML);
			System.out.println("Creating doc: " + counter);
						
			dam.create(doc);
			
			counter++;
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			DocAug newDoc = dam.poll();
//			
//			System.out.println("Retrieved ID: " + newDoc.getId());
//			System.out.println("Retrieved URL: " + newDoc.getUrl());
//			
//			System.out.println(dam.peek());
			
		}
		
		
	}
	
	@Test
	public void testIndexer() {
		fail("not yet implemented");
	}
}
