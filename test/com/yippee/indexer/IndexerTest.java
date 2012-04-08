package com.yippee.indexer;

import com.yippee.db.managers.DocAugManager;
import com.yippee.db.model.DocAug;

public class IndexerTest {

	public static void main(String[] args) {
		// Start putting test documents into the queues
		DocCreator doccreate = new DocCreator();
		doccreate.start();
		
		Indexer indexer = new Indexer();
		indexer.start();
	}
}

class DocCreator extends Thread {
	DocAugManager dam;
	int counter;
	
	public void run() {
		dam = new DocAugManager();
		counter = 0;
		
		while (true) {
			DocAug doc = new DocAug();
			doc.setId(counter + "");
			doc.setUrl(counter + "");
			System.out.println("Creating doc: " + counter);
			
			
			dam.create(doc);
			dam.close();
			
			counter++;
			try {
				this.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
