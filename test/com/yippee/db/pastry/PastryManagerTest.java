package com.yippee.db.pastry;

import static org.junit.Assert.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.pastry.commonapi.PastryIdFactory;

import com.yippee.util.Configuration;

public class PastryManagerTest {
	PastryManager pm;
	PastryIdFactory idFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    	Configuration.getInstance().setBerkeleyDBRoot("db/test");
	}

	@Before
	public void setUp() throws Exception {
		pm = new PastryManager();	 
		idFactory = new PastryIdFactory(new Environment());
	}

	@Test
	public void testConstructor() {
		assertTrue(pm instanceof PastryManager);
	}

	@Test
	public void testSaveNodeState(){
		Id id = idFactory.buildId("123");
		assertTrue(pm.storeState(id));
		
	}
	
	@Test
	public void testRetrieveState(){
		//Make new state
		
		//Store state
		
		//Load state and assert it matches what was put in
		fail("not implemented");
	}
	
	 static void displayInterfaceInformation(NetworkInterface netint) throws SocketException  {
       System.out.println("Display name: " 
          + netint.getDisplayName());
       System.out.println("Hardware address: " 
          + Arrays.toString(netint.getHardwareAddress()));
   }
	
}
