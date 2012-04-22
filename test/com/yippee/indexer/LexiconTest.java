package com.yippee.indexer;


import com.yippee.indexer.Lexicon;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LexiconTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(LexiconTest.class);
	Lexicon mLexicon;
	String test = "word";
	
    @Before
    public void setUp(){
        mLexicon = new Lexicon("db/test","doc/lexicon.txt");
    }
	
    @Test
    public void testIsInLexicon(){
        assertTrue(mLexicon.isInLexicon(test));
    }
    
    @Test
    public void testGetWord(){
    	byte[] id = mLexicon.getWordId(test);
    	String word = mLexicon.getWord(id);
        assertTrue(word.equals(test));
    }
    
    @Test
    public void testGetWordId(){
    	byte[] id = mLexicon.getWordId(test);
    	byte[] id2 = mLexicon.getWordId(test);
        assertTrue(Arrays.equals(id, id2));
    }
    
}
