package com.yippee.crawler.prioritizer;

import java.net.URL;
import java.util.Random;

public class RandomPrioritizer implements Prioritizer {
	
	private Random rand = new Random(System.nanoTime());
	private int max;
	
	public RandomPrioritizer(int maxPriority){
		max = maxPriority;
	}

	/**
	 * Returns a random int between 0 (inclusive) and maxPriority (exclusive)
	 */
	public int getPriority(URL url) {
		return rand.nextInt(max);
	}

}
