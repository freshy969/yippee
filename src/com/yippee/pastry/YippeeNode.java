package com.yippee.pastry;

/**
 * YippeeNode makes use of the YippeePastryApp to create an application instance
 * on this node. It is back-end entry point (front-end being the servlet/JSP)
 * which runs on multiple nodes.
 */
public class YippeeNode {

    public static void main(String[] args) {
        System.out.println("Yippee!");

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                //TODO: Init loggers, log application arguments
                try {
                    Thread.sleep(10000); //10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(args[i]);
            }
        }

    }
}
