package com.yippee;

import com.yippee.crawler.CrawlerTestSuite;
import com.yippee.db.DBTestSuite;
import com.yippee.indexer.IndexerTestSuite;
import com.yippee.pagerank.PRTestSuite;
import com.yippee.pastry.YippeePastryAppTest;
import com.yippee.search.SearchTestSuite;
import com.yippee.web.WebTestSuite;
import com.yippee.util.UtilTestSuite;
import com.yippee.util.Configuration;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Include all tests possible
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CrawlerTestSuite.class,
        DBTestSuite.class,
        IndexerTestSuite.class,
        PRTestSuite.class,
        SearchTestSuite.class,
        WebTestSuite.class,
        YippeePastryAppTest.class,
        UtilTestSuite.class
})
public class TestSuite {
	
    @BeforeClass
    public static void setUp() {
        System.out.println("setting up before running test class");
        Configuration.getInstance().setBerkeleyDBRoot("db/test");
    }
    
    @AfterClass
    public static void tearDown(){
    	//TODO delete test db files here?
    	System.out.println("tear down after running test class");
    }

}
