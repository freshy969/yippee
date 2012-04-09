package com.yippee.indexer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.DocAug;

public class IndexerTest {

	@Test 
	public void testParseAndCleanText() {
		String test = "Google easy123, ";
		String test1 = "fruit-ninja?"; 
		String test2 = "So...long."; 
		String test3 = "I've h@d bett3r.";
		
		ArrayList<String> testList = new ArrayList<String>();
		testList.add(test);
		testList.add(test1);
		testList.add(test2);
		testList.add(test3);
		
		Indexer index = new Indexer();
//		System.out.println(i.createLexicon(testList));
		
		ArrayList<String> results = index.createLexicon(testList);
		
		for (int i = 0; i < results.size(); i++) {
			if (i == 0) {
				assertTrue(results.get(i).equals("Google"));
			} else if (i == 1) {
				assertTrue(results.get(i).equals("easy123"));
			} else if (i == 2) {
				assertTrue(results.get(i).equals("fruit"));
			} else if (i == 3) {
				assertTrue(results.get(i).equals("ninja"));
			} else if (i == 4) {
				assertTrue(results.get(i).equals("So"));
			} else if (i == 5) {
				assertTrue(results.get(i).equals("long"));
			} else if (i == 6) {
				assertTrue(results.get(i).equals("I"));
			} else if (i == 7) {
				//TODO This should be fixed! Contractions are two words.
				assertTrue(results.get(i).equals("ve"));
			} else if (i == 8) {
				assertTrue(results.get(i).equals("h"));
			} else if (i == 9) {
				assertTrue(results.get(i).equals("d"));
			} else if (i == 10) {
				assertTrue(results.get(i).equals("bett3r"));
			} 
		}
	}
	
	
	// Threading tester
//	public static void main(String[] args) {
//		// Start putting test documents into the queues
//		DocCreator doccreate = new DocCreator();
//		doccreate.start();
//		
//		Indexer indexer = new Indexer();
//		indexer.start();
//	}
}

//class DocCreator extends Thread {
//	DocAugManager dam;
//	int counter;
//	
//	public void run() {
//		dam = new DocAugManager("db");
//		counter = 0;
//		
//		while (true) {
//			DocAug doc = new DocAug();
//			doc.setId(counter + "");
//			doc.setUrl(counter + "");
//			System.out.println("Creating doc: " + counter);
//			
//			
//			dam.create(doc);
//			dam.close();
//			
//			counter++;
//			try {
//				this.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//}
