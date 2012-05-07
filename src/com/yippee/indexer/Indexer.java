package com.yippee.indexer;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;


import com.sleepycat.persist.EntityCursor;
import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.indexer.BarrelManager;
import com.yippee.search.QueryDaemon;
import com.yippee.util.Configuration;

public class Indexer extends Thread {
	/**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Indexer.class);
    
	NodeIndex nodeIndex;
	//WordIndex wordIndex;
	final int NO_THREADS = 3;
	static int count = 0;
	static long time = 0;
	Lexicon lexicon;
	static HashMap<String, byte[]> lexiconMap;
	static HashMap<String, String> stopWords;
	static Iterator<String> cursor;
	static DocAugManager dam;
	static Vector<DocAug> queue;
	BarrelManager bm;
	
	public Indexer() {
//		Configuration.getInstance().setBerkeleyDBRoot("db/prod");
		lexicon = new Lexicon("doc/lexicon.txt");
		lexiconMap = lexicon.getLexiconMap();
		stopWords = lexicon.getStopList(); 
		
		nodeIndex = new NodeIndex();
		queue = new Vector<DocAug>();
		
		bm = new BarrelManager();
		bm.invalidate();
		
//		nodeIndex.setArchiveMode(true);
		//wordIndex = new WordIndex();
	}
	
	public void makeThreads(){
		for(int i=0; i<NO_THREADS; i++){
			(new IndexWorker(nodeIndex)).start();
		}
	}
	
	public static boolean isInLexicon(String word){
		return lexiconMap.containsKey(word);
	}
	
	public static boolean isStopWork(String word) {
		return stopWords.containsKey(word);
	}
	
	public synchronized static void fillQueue() {
		if(queue.size() < 100) {
			System.out.println("---------------");
			System.out.println("Time (m): " + (float)time/60000);
			System.out.println("Indexed count: " + count);
//			System.out.println("Rate: " + ((double) count / time ) + "docs/min");
			int tmp = 0;
			while(cursor.hasNext() && tmp < 1000) {
				String next = cursor.next();
//				System.out.println(next);
				queue.add(dam.read(next));
				tmp++;
			}
			count += tmp;
		}
		
	}
	
	public synchronized static DocAug poll() {
		if (!queue.isEmpty())
			return queue.remove(0);
		else
			return null;
	}
	
	public void run() {
		dam = new DocAugManager();
		long stop = dam.getSize();
		System.out.println("Barrels size: " + bm.getBarrelSize());
		System.out.println("Total docs: " + dam.getSize());
		
		cursor = dam.getKeys();
				
		while(count < stop) {
			fillQueue();
			try {
				this.sleep(60000);
				time += 60000;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Successfully Indexed!");
		System.out.println("---------------------");
		System.out.println("Barrels size: " + bm.getBarrelSize());
		System.out.println("Total docs:   " + count);
		System.out.println("---------------------");
	}
}
