package com.yippee.db;

import com.yippee.db.managers.*;
import com.yippee.db.model.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({	BarrelManagerTest.class,
				LexiconManagerTest.class,
				DocAugManagerTest.class,
				RobotsManagerTest.class,
				RobotsTxtTest.class,
				HitTest.class
				})
public class DBTestSuite { }