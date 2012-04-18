package com.yippee.crawler.prioritizer;

import org.apache.log4j.Logger;

public class PrioritizerFactory {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PrioritizerFactory.class);

	public static Prioritizer get(PrioritizerType type, int numPriorityLevels){
		switch(type){
			case RANDOM:
				return new RandomPrioritizer(numPriorityLevels);
			default:
				return new RandomPrioritizer(numPriorityLevels);
		}
	}
}
