package com.yippee.db.indexer;

import com.yippee.db.indexer.AnchorManager;
import com.yippee.db.indexer.model.AnchorHit;
import com.yippee.db.indexer.model.Hit;
import com.yippee.util.Configuration;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class AnchorManagerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
//    static Logger logger = Logger.getLogger(AnchorManagerTest.class);
	//test addDocHit
	//test getHitList
	AnchorManager anchorManager;
	String wordid1 = "word1";
	String wordid2 = "word2";
	String wordid3 = "word3";
	AnchorHit h1;
	AnchorHit h2;
	AnchorHit h3;
	AnchorHit h4;
	AnchorHit h5;
	String url = "page it came from?";
	
	@BeforeClass
	public static void setUpBeforeClass(){
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}
	
	@Before
    public void setUp(){    
       anchorManager = new AnchorManager();
       //String docID, byte[] wordID, int position, String URL
       h1 = new AnchorHit("doc5",wordid1,2,url);
       h2 = new AnchorHit("doc2",wordid2,67,url);
       h3 = new AnchorHit("doc1",wordid1,6,url);
       h4 = new AnchorHit("doc1",wordid1,7,url);
       h5 = new AnchorHit("doc1",wordid3,5,url);
       /* expect make:
       		wordid1) h1, h3, h4 -sort-> h3, h4, h1
       		wordid2) h2
       		wordid3) h4      
       */
    }

    
    @Test
    public void testAddDoc(){
        assertTrue(anchorManager.addAnchorHit(h1));
        assertTrue(anchorManager.addAnchorHit(h2));
        assertTrue(anchorManager.addAnchorHit(h3));
        assertTrue(anchorManager.addAnchorHit(h4));
        assertTrue(anchorManager.addAnchorHit(h5));
    }
    
//    @Test
//    public void testGetHitList(){
//    	ArrayList<Hit> hits = anchorManager.getHitList(wordid1).getHitList();  	
//    	assertTrue(hits.size()==3);
//    	ArrayList<Hit> hits2 = anchorManager.getHitList(wordid2).getHitList();  	
//    	assertTrue(hits2.size()==1);
//    	ArrayList<Hit> hits3 = anchorManager.getHitList(wordid3).getHitList();  	
//    	assertTrue(hits3.size()==1);
//    }
//    
//    @Test 
//    public void testDelete() {
//    	anchorManager.deleteWordEntry(wordid1);
//    	anchorManager.deleteWordEntry(wordid2);
//    	anchorManager.deleteWordEntry(wordid3);
//    	assertTrue(anchorManager.getHitList(wordid1)==null);
//    	assertTrue(anchorManager.getHitList(wordid2)==null);
//    	assertTrue(anchorManager.getHitList(wordid3)==null);
//    }
//    
   
}
