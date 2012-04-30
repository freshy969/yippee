package com.yippee.pastry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.yippee.db.indexer.model.Hit;

import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.appsocket.AppSocket;
import rice.p2p.commonapi.appsocket.AppSocketReceiver;

public class PastryAppSocketSender implements AppSocketReceiver {

	private Endpoint endpoint;
	private ByteBuffer out;
	private Node node;
	private int MSG_LENGTH;
	private ArrayList<Hit> hitList;
	
	public PastryAppSocketSender(Node node, Endpoint endpoint){
		hitList = new ArrayList<Hit>();
		this.node = node;
		this.endpoint = endpoint;
        //setting up for AppSocket
		out = ByteBuffer.wrap(node.getLocalNodeHandle().getId().toByteArray());  

	}

	@Override
	public void receiveException(AppSocket arg0, Exception e) {
		e.printStackTrace();
	}

	@Override
	public void receiveSelectResult(AppSocket socket, boolean canRead, boolean canWrite)
			throws IOException {
 	        try {
 	           long ret = socket.write(out);       
 	           	          // see if we are done
 	           if (!out.hasRemaining()) {
 	        	   socket.close();           
 	               out.clear();
 	           } else {
 	           	            // keep writing
 	           	   socket.register(false, true, 30000, this);
 	           }
 	        } catch (IOException ioe) {
 	           ioe.printStackTrace();
 	        }

	}

	@Override
	public void receiveSocket(AppSocket socket) throws IOException {
        socket.register(false, true, 30000, this);
	}
}
