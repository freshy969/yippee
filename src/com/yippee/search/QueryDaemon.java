package com.yippee.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.yippee.indexer.Lexicon;
import com.yippee.indexer.WordStemmer;
import com.yippee.pastry.YippeePastryApp;
import com.yippee.pastry.message.QueryMessage;
import com.yippee.pastry.message.ResultMessage;
import com.yippee.util.SocketQueue;

import rice.p2p.commonapi.Id;

public class QueryDaemon implements Runnable  {
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(QueryDaemon.class);
	
	YippeePastryApp yippeeApp;
	SocketQueue queue;
	Lexicon lexicon;
	private HashMap<String, byte[]> lexiconMap;
	private WordStemmer stemmer;
	
	public QueryDaemon(YippeePastryApp yippeeApp, SocketQueue queue) {
		this.yippeeApp = yippeeApp;
		this.queue = queue;
		lexicon = new Lexicon("doc/lexicon.txt");
		lexiconMap = lexicon.getLexiconMap();
		stemmer = new WordStemmer();
		logger.info("Awaiting queries: ...");
	}
	
	public void run () {
		while(true) {
			try {
				Socket request = queue.take();
	
				String keywords = "";
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
					String tmp;
					while ((tmp = reader.readLine()) != null) {
						keywords += tmp;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
	
				logger.info("Received query: " + keywords);
				
				String[] tmp = keywords.split(",");
				
				keywords = tmp[0];
//				int page = Integer.parseInt(tmp[1]);
				
				// Encode the socket and store it
				UUID queryID = UUID.randomUUID();
				yippeeApp.putSocket(queryID, request);
				yippeeApp.putQuery(queryID, keywords);
				
				
				yippeeApp.setStartTime();
//				yippeeApp.setPage(page);
				
				String[] wordArray = keywords.split("\\s+");

				wordArray = stemmer.stemList(wordArray);
				
				// Split query into keywords and send out to ring
				for (int i = 0; i < wordArray.length; i++) {
				
					if (lexiconMap.containsKey(wordArray[i])) {
						// Send query to the ring
						QueryMessage qm = new QueryMessage(yippeeApp.getNode().getLocalNodeHandle(), wordArray[i], queryID, wordArray.length);
						
						Id nodeID = yippeeApp.getNodeFactory().getIdFromString(wordArray[i]);			
						yippeeApp.sendQuery(nodeID, qm);	
					} else {
						ResultMessage rm = new ResultMessage(yippeeApp.getNode().getLocalNodeHandle(), null, wordArray[i], queryID, wordArray.length);
						
						Id nodeID = yippeeApp.getNode().getLocalNodeHandle().getId();			
						
						yippeeApp.sendResult(nodeID, rm);
					}
						
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
