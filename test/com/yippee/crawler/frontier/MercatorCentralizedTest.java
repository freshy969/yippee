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
	}

	@Before
	public void setUp() throws Exception {
		frontier = new MercatorCentralized();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
