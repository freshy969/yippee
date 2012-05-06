package com.yippee.indexer;

import java.util.HashMap;

public class Indexer {
	NodeIndex nodeIndex;
	//WordIndex wordIndex;
	final int NO_THREADS = 10;
	Lexicon lexicon;
	static HashMap<String, byte[]> lexiconMap;
	static HashMap<String, String> stopWords;
	
	public Indexer() {
		lexicon = new Lexicon("doc/lexicon.txt");
		lexiconMap = lexicon.getLexiconMap();
		stopWords = lexicon.getStopList(); 

		nodeIndex = new NodeIndex();
		nodeIndex.setArchiveMode(false);
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
}
