package com.yippee.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.yippee.indexer.Lexicon;
import com.yippee.pastry.YippeePastryApp;
import com.yippee.pastry.message.QueryMessage;
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
	
	public QueryDaemon(YippeePastryApp yippeeApp, SocketQueue queue) {
		this.yippeeApp = yippeeApp;
		this.queue = queue;
		lexicon = new Lexicon();
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
	
				// Encode the socket and store it
				UUID queryID = UUID.randomUUID();
				yippeeApp.putSocket(queryID, request);
				yippeeApp.putQuery(queryID, keywords);
				
				logger.info("Received query: " + keywords);
				
				String[] wordArray = keywords.split("\\s+");

				// Split query into keywords and send out to ring
				for (int i = 0; i < wordArray.length; i++) {
					// Send query to the ring
					QueryMessage qm = new QueryMessage(yippeeApp.getNode().getLocalNodeHandle(), wordArray[i], queryID, wordArray.length);
					
					Id nodeID = yippeeApp.getNodeFactory().getIdFromString(wordArray[i]);			
					yippeeApp.sendQuery(nodeID, qm);	
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
