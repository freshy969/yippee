package com.yippee.db.indexer;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yippee.db.indexer.model.DocEntry;
import com.yippee.util.Configuration;

public class DocEntryManagerTest {
	
	DocEntryManager man;

	@BeforeClass
	public static void setUpBeforeClass(){
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}

	@Before
	public void setUp() throws Exception {
		man = new DocEntryManager();
	}

	@Test
	public void testUpdatePageRank() {
		DocEntry entry = new DocEntry("http://upenn.edu", "Upenn", "upenn.edu", new Date());
		man.addDocEntry(entry);
		
		DocEntry fromDB = man.getDocEntry("http://upenn.edu");
		assertTrue(1 == fromDB.getPagerank());

		man.updatePageRank("http://upenn.edu", 4);
		DocEntry updatedFromDB = man.getDocEntry("http://upenn.edu");
		assertTrue(4 == updatedFromDB.getPagerank());
	}

}
