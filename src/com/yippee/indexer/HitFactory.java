package com.yippee.indexer;

import com.yippee.db.model.Hit;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class HitFactory {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(HitFactory.class);
	Lexicon lexicon;
	
	/**
	 * Constructor takes the lexicon object
	 * @param l
	 */
	public HitFactory(Lexicon l) {
		lexicon = l;
	}
	
	/**
	 * Parser passes in the list of words from the document and createHits will
	 * turn the words into hits, creating a hitlist
	 * @param args
	 * @param docId
	 * @return
	 */
	public ArrayList<Hit> createHits(String[] args, String docId) {
		ArrayList<Hit> hitlist = new ArrayList<Hit>();
		for(int i=0; i<args.length; i++) {
			String word = args[i];
			byte[] wordID = lexicon.getWordId(word.toLowerCase());
			if(wordID!=null) {
				Hit h = new Hit(docId, wordID,i);
				setCap(h, word);
				hitlist.add(h);
			}
		}
		return hitlist;		
	}
	
	/**
	 * checks whether the first character of word is capitalized and sets yes or no
	 * in the Hit object
	 * @param h
	 * @param w
	 */
	public void setCap(Hit h, String w) {
		char[] chs = w.toCharArray();
		if(Character.isUpperCase(chs[0]))
			h.setCaps(true);
	}
	
	
}
