package com.yippee.crawler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.yippee.crawler.frontier.SimpleQueueFrontierTest;

/**
 * Include all tests of the Crawler Package
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({	MessageTest.class,
						HttpModuleTest.class,
						RobotsModuleTest.class,
						SimpleQueueFrontierTest.class})
public class CrawlerTestSuite { }