package com.yippee.indexer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.DocAug;
import com.yippee.indexer.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LexiconTest {
	Lexicon mLexicon;
	ArrayList<String> correctLexicon;
	String[] words = {"cat","dog","cat","mouse","farm","rabbit", "dog"};	
	
    @Before
    public void setUp(){
        mLexicon = new Lexicon();
    	correctLexicon = new ArrayList<String>();
    	correctLexicon.add("cat");
    	correctLexicon.add("dog");
    	correctLexicon.add("mouse");
    	correctLexicon.add("farm");
    	correctLexicon.add("rabbit");
    }
	
    @Test
    public void testAddListToLexicon(){
    	mLexicon.addListToLexicon(words);
    	ArrayList<String> actualLexicon = mLexicon.getLexicon();
        assertTrue(correctLexicon.equals(actualLexicon));
    }
    
    @Test
    public void testIsWordInLexicon(){
    	mLexicon.addListToLexicon(words);
    	ArrayList<String> actualLexicon = mLexicon.getLexicon();
        assertTrue(actualLexicon.contains("cat"));
    }
    
    @Test
    public void testAddWord(){
    	mLexicon.addWord("monkey");
    	ArrayList<String> actualLexicon = mLexicon.getLexicon();
        assertTrue(actualLexicon.contains("monkey"));
    }

}
