package com.yippee.crawler.frontier;

import org.apache.log4j.Logger;

import com.yippee.util.Configuration;

/**
 * We  use the  Factory  method design  pattern, as  described  by GoF. The
 * factory method pattern is an object-oriented design pattern to implement
 * the concept of factories. Like  other creational patterns, it deals with
 * the problem of creating objects  (products) without specifying the exact
 * class of  object that will be  created. The creation of  an object often
 * requires complex processes not appropriate to include within a composing
 * object. The object's  creation may lead to a  significant duplication of
 * code, may  require information not  accessible to the  composing object,
 * may not provide a sufficient level  of abstraction, or may otherwise not
 * be part  of the composing  object's concerns. The factory  method design
 * pattern  handles  these  problems  by defining  a  separate  method  for
 * creating the objects, which subclasses  can then override to specify the
 * derived type of product that will be created
 */
public class FrontierFactory {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(FrontierFactory.class);

    /**
     * The configuration mechanism, central component of the factory
     *
     * @param type should be one of the follows:
     *  * SIMPLE For the simplest (and probably fastest) implementation
     *  * MERC_1 For the centralized URLFrontier described in the Mercator paper
     *  * MERC_2 For an improved URLFrontier, close to the one in the Mercator
     *  * CORALI For the Corallia Design, a new, highly scalable approach
     *
     * @return The concrete URLFrontier implementation
     */
    public static URLFrontier get(FrontierType type) {
    	
    	int crawlerThreadNumber = Configuration.getInstance().getCrawlerThreadNumber();
    	
        switch (type){
            case FAST:
                FastFrontier ff = new FastFrontier();
                return ff;
            case SIMPLE:
            	SimpleQueueFrontier sqf = new SimpleQueueFrontier();
            	sqf.load();
                return sqf;
            case MERC_1:
            	/*
            	 * TODO 10 is the number of priority levels
            	 * This is specific to the mercator frontier so should probably not be in Configuration.
            	 * Where should it go? 
            	 */            
            	MercatorCentralized mc = new MercatorCentralized(10, crawlerThreadNumber);
            	mc.load();
                return mc;
            case MERC_2:
            	MercatorDistributed md = new MercatorDistributed();
            	md.load();
                return md;
            case CORALI:
            	CoralliaFrontier cf = new CoralliaFrontier();
            	cf.load();
                return cf;
                
            case POLITE_SIMPLE:
            	PoliteSimpleQueue psq = new PoliteSimpleQueue();
            	psq.load();
            	return psq;
            default:
            	SimpleQueueFrontier defaultFrontier = new SimpleQueueFrontier();
            	defaultFrontier.load();
                return defaultFrontier;
        }
    }
}
