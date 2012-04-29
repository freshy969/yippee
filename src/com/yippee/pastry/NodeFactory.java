package com.yippee.pastry;

import org.apache.log4j.Logger;

import com.yippee.db.pastry.PastryManager;
import com.yippee.db.pastry.model.NodeState;

import rice.environment.Environment;
import rice.p2p.commonapi.Node;

import rice.pastry.Id;
import rice.pastry.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple class for creating multiple Pastry nodes in the same
 * ring
 * 
 * 
 * @author Nick Taylor
 *
 */
public class NodeFactory {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(NodeFactory.class);
	Environment env;
	NodeIdFactory nidFactory;
	SocketPastryNodeFactory factory;
	NodeHandle bootHandle;
	int createdCount = 0;
	int port;
	
	NodeFactory(int port) {
		this(new Environment(), port);
	}	
	
	NodeFactory(int port, InetSocketAddress bootstrap) {
		this(port);
		bootHandle = factory.getNodeHandle(bootstrap);
	}
	
	NodeFactory(Environment env, int port) {
		this.env = env;
		this.port = port;
		nidFactory = new RandomNodeIdFactory(env);
		try {
			this.env.getParameters().setString("pastry_socket_allow_loopback","true" );
			factory = new SocketPastryNodeFactory(nidFactory, port, env);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}
		
	}
	
	public Node getNode() {
		try {
			synchronized (this) {
				if (bootHandle == null && createdCount > 0) {
					InetAddress localhost = InetAddress.getLocalHost();
					InetSocketAddress bootaddress = new InetSocketAddress(localhost, port);
					bootHandle = factory.getNodeHandle(bootaddress);
				}
			}
			
			PastryNode node = null;
			//PastryManager pm = new PastryManager();
			NodeState state = null; 
					//pm.loadState();
			//If there's a stored NodeState, use it to get NodeID
			if(state != null){
				/*
				 * Commenting this out for now.  We should return to this once
				 * 	all project components are functional
				 */
				
//				PastryIdFactory idFactory = new PastryIdFactory(env);
//				Id id = idFactory.buildId(state.getNodeIdString());
//				
//				node = factory.newNode(bootHandle, state.getNodeIdString());
//				logger.debug("Node Id Loaded: " + node.getId());
				
			} else{
				node =  factory.newNode(bootHandle);
				logger.debug("Node Id Generated: " + node.getId());
				//Store the generated nodeID
				//Id id = node.getNodeId();
				//pm.storeState(id);
			}
			
			
			
			/*
			while (!node.isReady()) {
				Thread.sleep(100);
			}*/
			synchronized (node) {
				while (!node.isReady() && ! node.joinFailed()) {
					node.wait(500);
					if (node.joinFailed()) {
						throw new IOException("Could join the FreePastry ring. Reason:"+node.joinFailedReason());
					}	
				}
			}
			
			synchronized (this) {
				++createdCount;
			}
			return node;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	
	public void shutdownNode(Node n) {
		((PastryNode) n).destroy();
		
	}
	
//	public Id getIdFromBytes(byte[] material) {
//		return Id.build(material);
//	}
	
	public Id getIdFromString(String keyString) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] content = keyString.getBytes();
		md.update(content);
		byte shaDigest[] = md.digest();
		//rice.pastry.Id keyId = new rice.pastry.Id(shaDigest);
		return Id.build(shaDigest);
	}
}