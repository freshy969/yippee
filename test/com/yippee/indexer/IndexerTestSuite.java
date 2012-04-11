package com.yippee.indexer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Include all tests of the Indexer Package
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({   MessageTest.class,
                        IndexerTest.class,
                        LexiconTest.class,
                        WordStemmer.class
})
public class IndexerTestSuite {}
