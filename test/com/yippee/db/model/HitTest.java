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
	//byte[] wordID = {1,0,1,1};
	String word = "";
	int pos = 0;
	
    @Before
    public void setUp(){
    	hit = new Hit(docID,word,pos);
    }
	
    @Test
    public void testSetGetItalisize(){
    	assertTrue(!hit.isItalicize());
    	hit.setItalicize(true);
    	assertTrue(hit.isItalicize());
    }
    
    @Test
    public void testSetGetCaps(){
    	assertTrue(!hit.getCaps());
    	hit.setCaps(true);
    	assertTrue(hit.getCaps());
    }
    
    @Test
    public void testSetGetBold(){
    	assertTrue(!hit.isBold());
    	hit.setBold(true);
    	assertTrue(hit.isBold());
    }
    
    @Test
    public void testGetWordID(){
    	assertTrue(hit.getWord().equals(word));
    }

    @Test
    public void testGetDocID(){
    	assertTrue(hit.getDocId().equals(docID));
    }
    
    @Test
    public void testGetPosition(){
    	assertTrue(hit.getPosition()==pos);
    }
}
