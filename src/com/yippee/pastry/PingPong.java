package com.yippee.pastry;

import com.yippee.util.Configuration;
import org.apache.log4j.Logger;

/**
 * This class implements the ping-pong thread, necessary to
 * make sure that all nodes work together.
 *
 * boot-up 10 seconds, ping pong every 5 seconds
 */
public class PingPong implements Runnable {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(PingPong.class);
    /**
     * Toggle whether the node is booting or not.
     */
    private boolean booting;

    /**
     * Constructor -- when object is constructed, starts on the booting process
     */
    public PingPong() {
        booting = true;
    }

    /**
     * The overridden thread run method
     */
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                if (booting) {
                    Thread.sleep(10000);
                    booting = false;
                } else {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();  // No need to reach here
            }
            // generate random
            Configuration.getInstance().getPastryEngine().sendPing();
        }

    }
}
