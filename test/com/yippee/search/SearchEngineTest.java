package com.yippee.search;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.yippee.db.indexer.model.HitList;

import static org.junit.Assert.assertTrue;

public class SearchEngineTest {
	HitList h1;
	HitList h2;
	
	HashMap<String, Float> h1TfMap;
	HashMap<String, Float> h2TfMap;
	
	HashMap<String, Float> h1AtfMap;
	HashMap<String, Float> h2AtfMap;
	
	public SearchEngineTest() {	
		
		h1 = new HitList();
		h2 = new HitList();
		
		h1TfMap = new HashMap<String, Float>();
		h2TfMap = new HashMap<String, Float>();
		
		h1TfMap.put("water.com", (float) 0.707106);
		
		
	}
	
	@Test
	public void testCalculateTfIDF() {
		
	}
	
	
}
