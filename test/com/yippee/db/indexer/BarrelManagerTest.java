package com.yippee.db.indexer;

import com.yippee.db.indexer.BarrelManager;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class BarrelManagerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(BarrelManagerTest.class);
	//test addDocHit
	//test getHitList
	BarrelManager barrelManager;
	String wordid1 = "word1";
	String wordid2 = "word2";
	String wordid3 = "word3";
	Hit h1;
	Hit h2;
	Hit h3;
	Hit h4;
	Hit h5;
	
	@BeforeClass
	public static void setUpBeforeClass(){
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}
	
	@Before
    public void setUp(){    
       barrelManager = new BarrelManager();
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
    	ArrayList<Hit> hl = new ArrayList<Hit>();
    	hl.add(h1);
        assertTrue(barrelManager.addDocHits(hl));
        hl = new ArrayList<Hit>();
    	hl.add(h2);
        assertTrue(barrelManager.addDocHits(hl));
        hl = new ArrayList<Hit>();
    	hl.add(h3);
        assertTrue(barrelManager.addDocHits(hl));
        hl = new ArrayList<Hit>();
    	hl.add(h4);
        assertTrue(barrelManager.addDocHits(hl));
        hl = new ArrayList<Hit>();
    	hl.add(h5);
        assertTrue(barrelManager.addDocHits(hl));
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
    
	@Test
	public void testSize(){
	//	ArrayList<Hit> hits = barrelManager.getHitList("channel").getHitList();  
    	assertTrue(barrelManager.getBarrelSize()>0);
	//	assertTrue(true);
	}
    
}
