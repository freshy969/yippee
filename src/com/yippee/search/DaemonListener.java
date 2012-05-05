package com.yippee.search;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.yippee.pastry.message.PastryMessage;
import com.yippee.util.SocketQueue;

/**
 * @author tdu2 Daemon thread class that listens for incoming connections
 *
 */
public class DaemonListener implements Runnable {
	 /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(DaemonListener.class);
	
    int port;
	SocketQueue queue;
	
	public DaemonListener(int port, SocketQueue queue) {
		this.port = port;
		this.queue = queue;
		logger.info("Listening on port: " + port);
	}
	
    public void run() {
       
        ServerSocket server;
        try {
        	server = new ServerSocket(port);
        	
            while(true) {
            	Socket client = server.accept();
            	logger.info("Daemon received connection from: " + client.getInetAddress().getHostAddress());
            	queue.put(client);
            }   
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
       
    }
}