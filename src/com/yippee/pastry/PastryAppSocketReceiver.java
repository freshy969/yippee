package com.yippee.pastry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.yippee.db.indexer.model.Hit;

import rice.p2p.commonapi.*;
import rice.p2p.commonapi.appsocket.AppSocket;
import rice.p2p.commonapi.appsocket.AppSocketReceiver;

public class PastryAppSocketReceiver implements AppSocketReceiver {
	private Endpoint endpoint;
	private ByteBuffer in;
	private Node node;
	private int MSG_LENGTH;
	private ArrayList<Hit> hitList;
	
	public PastryAppSocketReceiver(Node node, Endpoint endpoint){
		hitList = new ArrayList<Hit>();
		this.node = node;
		this.endpoint = endpoint;
        //setting up for AppSocket
        MSG_LENGTH = node.getLocalNodeHandle().getId().toByteArray().length; 
        in = ByteBuffer.allocate(MSG_LENGTH);
	}

	@Override
	public void receiveException(AppSocket arg0, Exception e) {
		e.printStackTrace();
	}

	@Override
	public void receiveSelectResult(AppSocket socket, boolean canRead, boolean canWrite)
			throws IOException {
		in.clear();
 	        try {
 	          // read from the socket into ins
 	          long ret = socket.read(in);   
 	         
 	          if (ret != MSG_LENGTH) {
 	            // if you sent any kind of long message, you would need to handle this case better
 	            System.out.println("Error, we only received part of a message."+ret+" from "+socket);
 	            return;
 	          }
 	           
 	          System.out.println(node.getLocalNodeHandle()+" Received message from "+node.getIdFactory().buildId(in.array()));       
 	        } catch (IOException ioe) {
 	          ioe.printStackTrace();
 	        }
 	        
 	        //parse what read in
			try {
	 	        ByteArrayInputStream bis = new ByteArrayInputStream(in.array());
				ObjectInput objectInput = new ObjectInputStream(bis);
				hitList = (ArrayList<Hit>)objectInput.readObject();
				bis.close();
				objectInput.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	@Override
	public void receiveSocket(AppSocket socket) throws IOException {
        socket.register(true, false, 30000, this);
	    endpoint.accept(this);
	}
	
	public void saveInBarrels(){
		//Do stuff with hitList
	}

}
