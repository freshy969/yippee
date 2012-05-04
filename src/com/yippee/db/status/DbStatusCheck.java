package com.yippee.db.status;

import com.sleepycat.persist.PrimaryIndex;
import com.yippee.db.crawler.CrawlerDBEnv;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.db.util.DAL;
import com.yippee.util.Configuration;

public class DbStatusCheck {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dbRootPath = args[0];
		if(dbRootPath.equals("db/prod") || dbRootPath.equals("db/test"))
			Configuration.getInstance().setBerkeleyDBRoot(dbRootPath);
		else
			die("Invalid Root Path");
		
		CrawlerDBEnv crawlerEnv = CrawlerDBEnv.getInstance(true);
		DAL dao = new DAL(crawlerEnv.getCrawlerStore());
		PrimaryIndex<String, DocAug> docAugs = dao.getDocById();
		System.out.println("Checking db: " + dbRootPath);
		System.out.println("DocAugs Count: " + docAugs.count());
	}

	private static void die(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

}
