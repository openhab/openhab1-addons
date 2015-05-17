/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class implement a Mochad X10 host simulator. It can be used for testing the binding.
 * When the main class is executed, an instance of this class is created and started. The
 * server will wait for a connection of the binding. When it is connected, the console window
 * will show all communication from the binding to the host.
 * 
 * Furthermore, it is possible to type commands that mimic the reception of events by the 
 * Mochad X10 host. When <enter> is pressed, the command is sent to the binding and the 
 * binding will handle it via the MochadX10ReceiveThread.
 * 
 * Example messages:
 * 
 * 03/22 20:53:45 Rx RF HouseUnit: A2 Func: On
 * 03/22 20:53:55 Rx RF House: A Func: Dim
 * 
 * @author Jack Sleuters
 * @since 1.7.0
 *
 */
public class MochadX10Simulator {
	/** 
	 * The server port on which to connect to the binding. Use the port that you specified in the
	 * openhab configuration file 'mochadx10:hostPort'
	 */
	private static final int SERVER_PORT = 1099;
	
	/**
	 * The server socket used by the simulator
	 */
	private ServerSocket serverSocket;
	
	/**
	 * The writer to send received commands (typed in the consoled window) to the binding
	 */
	private PrintWriter out;
	
	/**
	 * The reader that receives commands from the binding
	 */
	private BufferedReader in;
	
	/**
	 * The thread that deals with commands send by the binding
	 */
	private MochadX10SimReceiveThread receiveThread;
	
	/**
	 * The socket providing the input and output streams to communicate with the binding
	 */
	private Socket server;
	
	/**
	 * The reader that accepts input from the console window.
	 */
	private BufferedReader br;

	/**
	 * This thread takes care of asynchronously receiving commands from the binding.
	 *  
	 * @author Jack Sleuters
	 * @since 1.7.0
	 *
	 */
	private class MochadX10SimReceiveThread extends Thread {
		/**
		 * The input stream to read from
		 */
		BufferedReader in = null;
		
		/**
		 * Used to indicate whether or not the thread should be stopped
		 */
		private boolean stopThread = false;

		/**
		 * Constructor
		 * 
		 * @param in 	the input stream to read from
		 */
		public MochadX10SimReceiveThread(BufferedReader in) {
			this.in = in;
		}

		/**
		 * Terminates the thread
		 */
		public void terminate() {
			stopThread  = true;
		}
		
		/**
		 * This thread will read incoming messages from the binding for as long as
		 * it is not terminated.
		 */
		public void run() {
			System.out.println("Receive Thread is started");
			try {
				while (!interrupted() && !stopThread) {
					String incoming = in.readLine();
					if (incoming != null) {
						System.out.println("In: " + incoming);
					}
				}
			} catch (IOException e) {
				System.out.println("Exception in receive thread");
			}
			System.out.println("Receive Thread is stopped");
		}
	}
	
	
	/**
	 * Connects with the Mochad X10 Binding
	 * 
	 * @throws IOException
	 */
	private void connectToBinding() throws IOException {
		serverSocket = new ServerSocket(SERVER_PORT);	
		System.out.println("Waiting for connection...");
		server = serverSocket.accept();
		System.out.println("Connection established");
		out = new PrintWriter(server.getOutputStream(), true);
		in = new BufferedReader( new InputStreamReader( server.getInputStream() ) );

		receiveThread = new MochadX10SimReceiveThread(in);
		receiveThread.start();
		
	}
	
	/**
	 * Disconnects from the Mochad X10 Binding
	 * 
	 * @throws IOException
	 */
	private void disconnectFromBinding() throws IOException {
		server.close();
		serverSocket.close();
		in.close();
		out.close();

		receiveThread.terminate();
		try {
			receiveThread.join();
		} catch (InterruptedException e) {
			System.out.println("Exception in disconnectFromBinding");
		}
	}

	/**
	 * Starts the simulator.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void start() throws IOException, InterruptedException {
        br = new BufferedReader(new InputStreamReader(System.in));
		String command = "";

        connectToBinding();
        
		while (!command.equals("exit")) {
	        command = br.readLine();
	        if (command.equals("break pipe")) {
	        	disconnectFromBinding();
	        	System.out.println("Broke pipe");
	        	connectToBinding();
	        }
	        else {
				out.write(command + "\r\n");
				out.flush();
	        }
		}
		disconnectFromBinding();
	}
}
