package com.yippee.pastry;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class YippeePastryApp implements Application {

    /**
     * Called when the Pastry application receives a message. It pushes the url
     * to the URLFrontier (maybe through a duplicate URL eliminator).
     *
     *
     */
	public void deliver(Id id, Message message) {
		// TODO Auto-generated method stub

	}

    /**
     * This is always true in our application.
     *
     * @param routeMessage a message
     * @return true always
     */
	public boolean forward(RouteMessage routeMessage) {
		return true;
	}

    /**
     * Called when we hear about a new neighbor.
     * We do not make use of this method for now.
     */
	public void update(NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

}
