package com.yippee.util;

import java.net.Socket;
import java.util.Vector;

public class SocketQueue {
	private final int CAPACITY;
	private Vector<Socket> reqList;

	// private int reqNumber = 0;

	/**
	 * Creates a new webqueue of the given capacity
	 * 
	 * @param capacity
	 *            maximum size of the webqueue
	 */
	public SocketQueue(int capacity) {
		CAPACITY = capacity;
		reqList = new Vector<Socket>(capacity);
	}

	/**
	 * Puts a socket into the queue and notifies waiting threads. Waits for a
	 * thread notify if the queue is full.
	 * 
	 * @param s
	 *            Socket to be put into the queue.
	 * @throws InterruptedException
	 */
	public synchronized void put(Socket s) throws InterruptedException {
		while (reqList.size() == CAPACITY) {
			wait();
		}
		reqList.add(s);

		// Queue is stocked!
		notify();
	}

	/**
	 * Takes a socket from the queue and notifies the queue. Waits for a queue
	 * notify if the queue is empty.
	 * 
	 * @return Socket to be processed
	 * @throws InterruptedException
	 */
	public synchronized Socket take() throws InterruptedException {
		// Worker is ready!
		if (reqList.size() == CAPACITY)
			notify();

		while (reqList.isEmpty()) {
			wait();
		}

		// reqNumber++;
		// System.out.println("Handling req #: " + reqNumber);
		// System.out.println("Requests avail: " + size());
		return reqList.remove(0);
	}

	protected synchronized boolean isEmpty() {
		return reqList.isEmpty();
	}
}
