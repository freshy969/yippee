package com.yippee.db.managers;

import com.yippee.db.model.DocAug;
import com.yippee.db.model.Hit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BarrelManagerTest {
	//test addDocHit
	//test getHitList
	BarrelManager barrelManager;
	byte[] wordid1 = {1,2,3};
	byte[] wordid2 = {1,0,7};
	byte[] wordid3 = {7,9,0,7,7};
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
       /* expect make:
       		wordid1) h1, h3, h4 -sort-> h3, h4, h1
       		wordid2) h2
       		wordid3) h4      
       */
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
    	ArrayList<Hit> hits = barrelManager.getHitList(wordid1);  	
    	assertTrue(hits.size()==3);
    	ArrayList<Hit> hits2 = barrelManager.getHitList(wordid2);  	
    	assertTrue(hits2.size()==1);
    	ArrayList<Hit> hits3 = barrelManager.getHitList(wordid3);  	
    	assertTrue(hits3.size()==1);
    }
    
}
