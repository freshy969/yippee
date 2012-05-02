package com.yippee.db.indexer.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.yippee.db.indexer.model.Hit;

import static org.junit.Assert.assertTrue;

public class HitTest {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HitTest.class);
	Hit hit;
	String docID = "doc123";
	String word = "word";
	int pos = 0;
	Hit h1;
	Hit h2; 
	Hit h3;
	
    @Before
    public void setUp(){
    	hit = new Hit(docID,word,pos);
    	h1 = new Hit("docID1", "word1", 1);
    	h2 = new Hit("docID2", "word2", 2);
    	h3 = new Hit("docID3", "word3", 3);
    	
    }
	
    @Test
    public void testSetGetItalisize(){
    	assertTrue(!hit.isItalicize());
    	hit.setItalicize(true);
    	assertTrue(hit.isItalicize());
    }
    
    @Test
    public void testSetGetCaps(){
    	assertTrue(!hit.getCaps());
    	hit.setCaps(true);
    	assertTrue(hit.getCaps());
    }
    
    @Test
    public void testSetGetBold(){
    	assertTrue(!hit.isBold());
    	hit.setBold(true);
    	assertTrue(hit.isBold());
    }
    
    @Test
    public void testGetWord(){
    	assertTrue(hit.getWord().equals(word));
    }

    @Test
    public void testGetDocID(){
    	assertTrue(hit.getDocId().equals(docID));
    }
    
    @Test
    public void testGetPosition(){
    	assertTrue(hit.getPosition()==pos);
    }
    
    @Test 
    public void testSerializability(){
    	try{
    		//SEND
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;  
			out =  new ObjectOutputStream(bos); 
			ArrayList<Hit> list = new ArrayList<Hit>();
			list.add(h1);
			list.add(h2);
			list.add(h3);
			out.writeObject(list);
			byte[] b = bos.toByteArray();
			ByteBuffer yourBytes = ByteBuffer.wrap(b);
			out.close();
			bos.close();
			
			//RECEIVE
			ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes.array());
			ObjectInput in;
			in = new ObjectInputStream(bis);
			ArrayList<Hit> o = (ArrayList<Hit>)in.readObject();
			bis.close();
			in.close();
			assertTrue(list.get(0).compareTo(o.get(0))==0);
			assertTrue(list.get(1).compareTo(o.get(1))==0);
			assertTrue(list.get(2).compareTo(o.get(2))==0);
    	} catch (IOException e) {
    		assertTrue(false);
    	} catch (ClassNotFoundException e){
    		assertTrue(false);
    	}
	
    }

}
