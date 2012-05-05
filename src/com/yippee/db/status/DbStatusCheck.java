package com.yippee.db.status;


import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.crawler.CrawlerDBEnv;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.crawler.model.FrontierSavedState;
import com.yippee.db.crawler.model.RobotsTxt;
import com.yippee.db.indexer.IndexerDBEnv;
import com.yippee.db.util.DAL;
import com.yippee.util.Configuration;

public class DbStatusCheck {
	
	/**
     * The default constructor
     *
	 * @param args the database environment
	 */
	public DbStatusCheck(String[] args) {
		String dbRootPath = args[0];
		if(dbRootPath.equals("db/prod") || dbRootPath.equals("db/test"))
			Configuration.getInstance().setBerkeleyDBRoot(dbRootPath);
		else
			die("Invalid Root Path");
		
		System.out.println("Checking db: " + dbRootPath);
		
		printCrawlerInfo();
		printIndexerInfo();

		
	}

	private static  void printCrawlerInfo() {
		System.out.println("===== Crawler Info =====");
		
		CrawlerDBEnv crawlerEnv = CrawlerDBEnv.getInstance(true);
		DAL dao = new DAL(crawlerEnv.getCrawlerStore());
		
		//Check number of DocAugs
		PrimaryIndex<String, DocAug> docAugs = dao.getDocById();
		System.out.println("DocAugs Count: " + docAugs.count());
		
		//Check number of RobotsManager
		PrimaryIndex<String, RobotsTxt> robots = dao.getRobotsById();
		System.out.println("RobotsTxt Count: " + robots.count());
		
		//Check number of URLFrontierStore
		PrimaryIndex<Integer, FrontierSavedState> frontierStates = dao.getFrontierStateByVersion();
		EntityCursor<FrontierSavedState> stateCursor = frontierStates.entities();
		FrontierSavedState lastState = stateCursor.last();
		System.out.println("Last Saved Frontier State: " + lastState);
		
		if(lastState != null){
			System.out.println("\tVersion Number" + lastState.getVersion());
			for(Integer queueIndex : lastState.getPrioritySets().keySet()){
				System.out.println("\t# urls in queue " + queueIndex + ": " + lastState.getPrioritySets().get(queueIndex).size());
			}	
		}
		stateCursor.close();
		System.out.println("\n");
	}

	private static void printIndexerInfo(){
		System.out.println("===== Indexer Info =====");

		IndexerDBEnv indexerEnv = IndexerDBEnv.getInstance(true);
		DAL dao = new DAL(indexerEnv.getIndexerStore());
		
		//Check Number of DocArchives
		PrimaryIndex<String, DocAug> docArchives = dao.getDocArcByURL();
		System.out.println("Doc Archive Count: " + docArchives.count());

		System.out.println("\n");
	}

    /**
     * A vicious little method!
     *
     * @param msg The message to output before playing dead!
     */
	private void die(String msg) {
		System.out.println(msg);
		System.exit(1);
	}
	
	public static void main(String[] args){
		DbStatusCheck statusCheck = new DbStatusCheck(args);
	}

}
