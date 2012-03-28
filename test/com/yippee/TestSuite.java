package com.yippee;

import com.yippee.crawler.CrawlerTestSuite;
import com.yippee.db.DBTestSuite;
import com.yippee.indexer.IndexerTestSuite;
import com.yippee.pagerank.PRTestSuite;
import com.yippee.search.SearchTestSuite;
import com.yippee.web.WebTestSuite;
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
        WebTestSuite.class
})
public class TestSuite { }
