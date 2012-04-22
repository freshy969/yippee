package com.yippee.db;

import com.yippee.db.crawler.DocAugManagerTest;
import com.yippee.db.crawler.RobotsManagerTest;
import com.yippee.db.crawler.model.RobotsTxtTest;
import com.yippee.db.indexer.BarrelManagerTest;
import com.yippee.db.indexer.LexiconManagerTest;
import com.yippee.db.indexer.model.HitTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({	RobotsManagerTest.class,
				RobotsTxtTest.class,
				BarrelManagerTest.class,
				DocAugManagerTest.class,
				LexiconManagerTest.class,
				HitTest.class
				})
public class DBTestSuite { }