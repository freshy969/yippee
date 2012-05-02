package com.yippee.pastry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
	private int MSG_LENGTH = 1024;
	private ArrayList<Hit> hitList;
	
	public PastryAppSocketReceiver(Node node, Endpoint endpoint){
		hitList = new ArrayList<Hit>();
		this.node = node;
		this.endpoint = endpoint;
        //setting up for AppSocket
        //MSG_LENGTH = node.getLocalNodeHandle().getId().toByteArray().length; 
        in = ByteBuffer.allocate(MSG_LENGTH);
       
	}

	@Override
	public void receiveException(AppSocket arg0, Exception e) {
		e.printStackTrace();
	}

	@Override
	public void receiveSelectResult(AppSocket socket, boolean canRead, boolean canWrite)
			throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			in.clear();
 	        try { 
 	          while(socket.read(in)>0){
 	        	  bos.write(in.array());
 	        	  in.clear();
 	          }
 	          bos.close();
 	        } catch (IOException ioe) {
 	          ioe.printStackTrace();
 	        }
 	        
 	        //parse what read in
			try {
	 	        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				ObjectInput objectInput = new ObjectInputStream(bis);
				hitList = (ArrayList<Hit>)objectInput.readObject();
				bis.close();
				objectInput.close();
			} catch (ClassNotFoundException e) {
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
