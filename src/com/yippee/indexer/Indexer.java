package com.yippee.indexer;

public class Indexer {
	NodeIndex nodeIndex;
	final int NO_THREADS = 10;
	Lexicon lexicon;
	
	public Indexer() {
		lexicon = new Lexicon();
	}
	
	public void makeThreads(){
		for(int i=0; i<NO_THREADS; i++){
			(new IndexWorker(nodeIndex)).start();
		}
	}
	
	
	
}
