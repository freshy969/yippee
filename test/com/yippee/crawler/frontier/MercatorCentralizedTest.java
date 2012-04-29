package com.yippee.crawler.frontier;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yippee.util.Configuration;

public class MercatorCentralizedTest {
	MercatorCentralized frontier;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Configuration.getInstance().setBerkeleyDBRoot("db/test");
		Configuration.getInstance().setCrawlerThreadNumber(20);
	}

	@Before
	public void setUp() throws Exception {
		int numCrawlingThreads = Configuration.getInstance().getCrawlerThreadNumber();
		int numPriorityLevels = 10;
		frontier = new MercatorCentralized(numPriorityLevels, numCrawlingThreads);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
