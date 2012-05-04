package com.yippee.indexer;


import com.yippee.indexer.Lexicon;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class LexiconTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LexiconTest.class);
	Lexicon mLexicon;
	String test = "word";
	HashMap<String, byte[]> lexiconMap;
	
    @Before
    public void setUp(){
        mLexicon = new Lexicon("doc/lexicon.txt");
        lexiconMap = mLexicon.getLexiconMap();
    }
	
    @Test
    public void testIsInLexicon(){
        assertTrue(lexiconMap.containsKey(test));
    }
    
    
    @Test
    public void testGetWordId(){
    	byte[] id = lexiconMap.get(test);
    	byte[] id2 = lexiconMap.get(test);
        assertTrue(Arrays.equals(id, id2));
    }
    
    
    
}
