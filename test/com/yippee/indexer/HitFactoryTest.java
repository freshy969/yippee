package com.yippee.indexer;

import com.yippee.db.model.Hit;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class HitFactoryTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HitFactoryTest.class);
	Lexicon mLexicon;
	String[] words = {"The","fox","jumped","over","the","lazy","dog"};
	String docID = "doc123";
	HitFactory hitFactory;
	
    @Before
    public void setUp(){
        mLexicon = new Lexicon("db/test","doc/en-common.txt"); 
        hitFactory = new HitFactory(mLexicon);
    }
	
    @Test
    public void testCreateHits(){
    	ArrayList<Hit> hitList = hitFactory.createHits(words, docID);
        assertTrue(hitList.size()==7);
    }

    @Test
    public void testHitContentUpperThe(){
    	ArrayList<Hit> hitList = hitFactory.createHits(words, docID);
    	Hit h = hitList.get(0); //The
    	String docid = h.getDocId();
    	byte[] wordid = h.getWordId();
    	int position = h.getPostion();
    	byte[] lexiconWordID = mLexicon.getWordId("the");
        assertTrue(docid.equals(docID)); 
        assertTrue((new String(wordid)).equals(new String(lexiconWordID)));
        assertTrue(position==0); 
        assertTrue(h.getCaps());
    }
    
    @Test
    public void testHitContentLowerThe(){
    	ArrayList<Hit> hitList = hitFactory.createHits(words, docID);
    	Hit h = hitList.get(4); //the
    	String docid = h.getDocId();
    	byte[] wordid = h.getWordId();
    	int position = h.getPostion();
    	byte[] lexiconWordID = mLexicon.getWordId("the");
        assertTrue(docid.equals(docID)); 
        assertTrue((new String(wordid)).equals(new String(lexiconWordID)));
        assertTrue(position==4); 
        assertTrue(!h.getCaps());
    }
    
    @After
    public void tearDown(){
        mLexicon.close();
    }
}
