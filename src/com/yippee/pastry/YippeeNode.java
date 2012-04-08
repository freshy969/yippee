package com.yippee.pastry;

/**
 * YippeeNode makes use of the YippeePastryApp to create an application instance
 * on this node. It is back-end entry point (front-end being the servlet/JSP)
 * which runs on multiple nodes.
 */
public class YippeeNode {


    public static void main(String[] args){
        System.out.println("Start application");
        for (int i=0; i<args.length; i++) {
            //TODO: Init loggers, log application arguments
            System.out.println(args[i]);
        }


    }
}
