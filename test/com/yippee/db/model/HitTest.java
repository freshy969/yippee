package com.yippee.db.model;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HitTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HitTest.class);
	Hit hit;
	String docID = "doc123";
	byte[] wordID = {1,0,1,1};
	int pos = 0;
	
    @Before
    public void setUp(){
    	hit = new Hit(docID,wordID,pos);
    }
	
    @Test
    public void testSetGetItalisize(){
    	assertTrue(!hit.getItalicize());
    	hit.setItalicize(true);
    	assertTrue(hit.getItalicize());
    }
    
    @Test
    public void testSetGetCaps(){
    	assertTrue(!hit.getCaps());
    	hit.setCaps(true);
    	assertTrue(hit.getCaps());
    }
    
    @Test
    public void testSetGetBold(){
    	assertTrue(!hit.getBold());
    	hit.setBold(true);
    	assertTrue(hit.getBold());
    }
    
    @Test
    public void testGetWordID(){
    	assertTrue(hit.getWordId()==wordID);
    }

    @Test
    public void testGetDocID(){
    	assertTrue(hit.getDocId().equals(docID));
    }
    
    @Test
    public void testGetPosition(){
    	assertTrue(hit.getPostion()==pos);
    }
}
