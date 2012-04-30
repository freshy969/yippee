package com.yippee.indexer;

public class Indexer {
	NodeIndex nodeIndex;
	//WordIndex wordIndex;
	final int NO_THREADS = 10;
	Lexicon lexicon;
	
	public Indexer() {
		lexicon = new Lexicon("doc/lexicon.txt");
		nodeIndex = new NodeIndex();
		//wordIndex = new WordIndex();
	}
	
	public void makeThreads(){
		for(int i=0; i<NO_THREADS; i++){
			(new IndexWorker(nodeIndex)).start();
		}
	}
	
	
	
}
