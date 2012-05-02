package com.yippee.pastry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.yippee.util.SocketQueue;

/**
 * @author tdu2 Daemon thread class that listens for incoming connections
 *
 */
public class DaemonListener implements Runnable {
	int port;
	SocketQueue queue;
	
	public DaemonListener(int port, SocketQueue queue) {
		this.port = port;
		this.queue = queue;
		System.out.println("Listening on port: " + port);
	}
	
    public void run() {
       
        ServerSocket server;
        try {
        	server = new ServerSocket(port);
        	
            while(true) {
            	Socket client = server.accept();
            	System.out.println("Daemon received connection from: " + client.getInetAddress().getHostAddress());
            	queue.put(client);
            }   
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
       
    }
}