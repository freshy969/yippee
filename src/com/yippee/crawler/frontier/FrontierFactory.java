package com.yippee.crawler.frontier;

import org.apache.log4j.Logger;

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
    public static URLFrontier get(Frontier type) {
        switch (type){
            case SIMPLE:
                return new SimpleQueueFrontier();
            case MERC_1:
                return new MercatorCentralized();
            case MERC_2:
                return new MercatorDistributed();
            case CORALI:
                return new CoralliaFrontier();
            default:
                return new SimpleQueueFrontier();
        }
    }
}
