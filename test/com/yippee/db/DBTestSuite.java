package com.yippee.db;

import com.yippee.db.managers.*;
import com.yippee.db.model.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({	DocAugManagerTest.class,
				RobotsManagerTest.class,
				RobotsTxtTest.class})
public class DBTestSuite { }