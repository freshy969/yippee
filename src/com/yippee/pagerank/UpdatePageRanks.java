package com.yippee.pagerank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.yippee.db.indexer.DocEntryManager;


public class UpdatePageRanks {

	Hashtable<String, Boolean> alreadyUpdated;

	/**
	 * Create logger in the Log4j hierarchy named by by software component
	 */
	static Logger logger = Logger.getLogger(UpdatePageRanks.class);
	Scanner in;

	public UpdatePageRanks(String pathToFile){		
		alreadyUpdated = new Hashtable<String, Boolean>();

		File f = new File(pathToFile);
		try {
			in = new Scanner(f);

		} catch (FileNotFoundException e) {
			logger.error("PageRank file not found.");

		}		
	}


	public boolean update(){
		String DEL = "', '";
		DocEntryManager man = new DocEntryManager();

		String line = "";
		while(in.hasNext()){
			line = in.nextLine();

			//Remove first and last quote then split on the delimiter DEL
			String[] parts = line.substring(1, line.length() - 1).split(DEL);
			String url = parts[0];

			if(!alreadyUpdated.contains(url)){
				float pagerank = Float.parseFloat(parts[1]);
				man.updatePageRank(url, pagerank);
				
				alreadyUpdated.put(url, true);
			}
		}

		return false;
	}


}
