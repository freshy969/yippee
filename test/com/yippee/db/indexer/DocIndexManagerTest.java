package com.yippee.db.indexer;

import com.yippee.db.indexer.model.AnchorHit;
import com.yippee.db.indexer.model.DocEntry;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DocIndexManagerTest {
	DocIndexManager docIndexManager;
	DocEntry doc1, doc2, doc3;
	
	@BeforeClass
	public static void setUpBeforeClass(){
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}
	
	@Before
    public void setUp(){    
       docIndexManager = new DocIndexManager();
       doc1 = new DocEntry("http://www.facebook.com/tjdu22", "TJ Du", "www.facebook.com", new Date());
       doc2 = new DocEntry("http://www.cis.upenn.edu/~cis455", "CIS 455 / 555: Internet and Web Systems (Spring 2012)", "www.cis.upenn.edu", new Date());
       doc3 = new DocEntry("https://github.com/nvasilakis/yippee", "nvasilakis/yippee", "www.github.com", new Date());
    }

    
    @Test
    public void testAddDoc(){
        assertTrue(docIndexManager.addDocEntry(doc1));
        assertTrue(docIndexManager.addDocEntry(doc2));
        assertTrue(docIndexManager.addDocEntry(doc3));
    }
   
    @Test
    public void testGetDoc(){
        assertTrue("TJ Du".equals(docIndexManager.getDocEntry("http://www.facebook.com/tjdu22").getTitle()));
        assertTrue("www.cis.upenn.edu".equals(docIndexManager.getDocEntry("http://www.cis.upenn.edu/~cis455").getHost()));
        assertTrue("nvasilakis/yippee".equals(docIndexManager.getDocEntry("https://github.com/nvasilakis/yippee").getTitle()));
    }
    
    @Test 
    public void testUpdateDoc() {
    	doc1.setTitle("Margarita Miranda");
    	assertTrue(docIndexManager.addDocEntry(doc1));
    	assertTrue("Margarita Miranda".equals(docIndexManager.getDocEntry("http://www.facebook.com/tjdu22").getTitle()));
    }
}
