package com.yippee.indexer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Include all tests of the Indexer Package
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({   HitFactoryTest.class,
						MessageTest.class,
                        IndexerTest.class,
                        LexiconTest.class,
                        MessageTest.class,
                        WordStemmerTest.class
})
public class IndexerTestSuite {}
