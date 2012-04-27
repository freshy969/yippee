package com.yippee.db;

import com.yippee.db.crawler.*;
import com.yippee.db.crawler.model.*;
import com.yippee.db.indexer.*;
import com.yippee.db.indexer.model.*;
import com.yippee.db.pastry.*;
import com.yippee.db.pastry.model.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({	FrontierSavedStateTest.class,
				RobotsTxtTest.class,
				RobotsManagerTest.class,
				DocAugManagerTest.class,
				URLFrontierManagerTest.class,
				
				HitTest.class,
				AnchorManagerTest.class,
				BarrelManagerTest.class,
				LexiconManagerTest.class,
				
				NodeStateTest.class,
				PastryManagerTest.class
				})
public class DBTestSuite { }