package org.openhab.binding.lightwaverf.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;

public class LightwaveRFReceiver implements Runnable {
	private static final int port = 9761;
	private boolean running = true;
	private DatagramSocket receiveSocket;

 	public LightwaveRFReceiver(){
		initialiseSockets();
 	}

   	// Initialise receive sockets for UDP
	public void initialiseSockets(){
		try{
			receiveSocket = new DatagramSocket(port);
   		} catch (IOException e){
			e.printStackTrace();
		}
	}

 	public void stop(){
		running = true;
	}

	public void run() {
		while(running) {
			receiveUDP();
		}
		System.out.println("Stopping LightwaveRFServer");
 	}

 	//Network sockets to deal with receiving UDP responses from LightwaveRF box.  UDP response is specific to LightwaveRF
	public String receiveUDP(){
 		String receivedMessage = "";
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			receiveSocket.receive(receivePacket);
			receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
 		}
		catch (IOException e) {
			e.printStackTrace(); // Display if something went wrong
		}
		return receivedMessage;
	}
}
