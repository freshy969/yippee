package com.yippee.crawler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Include all tests of the Crawler Package
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({	MessageTest.class,
						HttpModuleTest.class,
						RobotsModuleTest.class})
public class CrawlerTestSuite { }