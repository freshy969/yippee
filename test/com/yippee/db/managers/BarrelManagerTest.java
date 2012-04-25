package com.yippee.db.managers;

import com.yippee.db.model.Hit;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class BarrelManagerTest {
/*    *//**
     * Create logger in the Log4j hierarchy named by by software component
     *//*
    static Logger logger = Logger.getLogger(BarrelManagerTest.class);
	//test addDocHit
	//test getHitList
	BarrelManager barrelManager;
	String wordid1 = "";
	String wordid2 = "";
	String wordid3 = "";
	
	Hit h1;
	Hit h2;
	Hit h3;
	Hit h4;
	Hit h5;
	
	@Before
    public void setUp(){    
       barrelManager = new BarrelManager("db/test");
       h1 = new Hit("doc5",wordid1,2);
       h2 = new Hit("doc2",wordid2,67);
       h3 = new Hit("doc1",wordid1,6);
       h4 = new Hit("doc1",wordid1,7);
       h5 = new Hit("doc1",wordid3,5);
        expect make:
       		wordid1) h1, h3, h4 -sort-> h3, h4, h1
       		wordid2) h2
       		wordid3) h4      
       
    }

    
    @Test
    public void testAddDoc(){
        assertTrue(barrelManager.addDocHit(h1));
        assertTrue(barrelManager.addDocHit(h2));
        assertTrue(barrelManager.addDocHit(h3));
        assertTrue(barrelManager.addDocHit(h4));
        assertTrue(barrelManager.addDocHit(h5));
    }
    
    @Test
    public void testGetHitList(){
    	ArrayList<Hit> hits = barrelManager.getHitList(wordid1).getHitList();  
    	assertTrue(hits.size()==3);
    	ArrayList<Hit> hits2 = barrelManager.getHitList(wordid2).getHitList();  	
    	assertTrue(hits2.size()==1);
    	ArrayList<Hit> hits3 = barrelManager.getHitList(wordid3).getHitList();  	
    	assertTrue(hits3.size()==1);
    }
    
    @Test 
    public void testDelete() {
    	barrelManager.deleteWordEntry(wordid1);
    	barrelManager.deleteWordEntry(wordid2);
    	barrelManager.deleteWordEntry(wordid3);
    	assertTrue(barrelManager.getHitList(wordid1)==null);
    	assertTrue(barrelManager.getHitList(wordid2)==null);
    	assertTrue(barrelManager.getHitList(wordid3)==null);
    }
    
   */
}
