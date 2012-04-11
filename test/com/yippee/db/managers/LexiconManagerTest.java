package com.yippee.db.managers;

import com.yippee.db.model.DocAug;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LexiconManagerTest {
	LexiconManager lexicon;
	
	@Before
    public void setUp(){    
       lexicon = new LexiconManager("db/test","doc/en-common.txt");
    }

    
    @Test
    public void testPeek(){
        assertTrue(lexicon.peek("Capt").equals("Capt"));
    }
    
 
    @After
    public void tearDown(){
        lexicon.close();
    }
}
