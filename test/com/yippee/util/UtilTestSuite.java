package com.yippee.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FancyExtractorTest.class, 
				LinkTextExtractorTest.class
				})
public class UtilTestSuite {

}
