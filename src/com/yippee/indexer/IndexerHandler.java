package com.yippee.indexer;

public class IndexerHandler {
	NodeIndex nodeIndex;
	final int NO_THREADS = 10;
	
	public IndexerHandler(){
		nodeIndex = new NodeIndex();		
	}
	
	public void makeThreads(){
		for(int i=0; i<NO_THREADS; i++){
			(new Indexer(nodeIndex)).start();
		}
	}
	
	

}
