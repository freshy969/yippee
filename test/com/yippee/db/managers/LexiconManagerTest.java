package com.yippee.db.managers;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LexiconManagerTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LexiconManagerTest.class);
	LexiconManager lexiconManager;
	String test = "Computer";
	
	@Before
    public void setUp(){    
       lexiconManager = new LexiconManager("db/test","doc/lexicon.txt");
    }

    
    @Test
    public void testGetWordId(){
    	byte[] id = lexiconManager.getWordId(test);
    	byte[] id2 = lexiconManager.getWordId(test);
        assertTrue((new String(id)).equals(new String(id2)));
    }
    
    @Test
    public void testContains(){
        assertTrue(lexiconManager.contains(test));
    }
    
    @Test
    public void testGetWordById(){
    	byte[] id = lexiconManager.getWordId(test);
    	String word = lexiconManager.getWordById(id);
        assertTrue(word.equals(test.toLowerCase()));
    }
    
    @Test
    public void testIsEmpty(){
        assertTrue(!lexiconManager.isEmpty());
    }
    
    @After
    public void tearDown(){
        lexiconManager.close();
    }
}
