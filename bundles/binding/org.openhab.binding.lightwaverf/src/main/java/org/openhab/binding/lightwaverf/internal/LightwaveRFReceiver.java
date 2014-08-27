package org.openhab.binding.lightwaverf.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFReceiver implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
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
			logger.error("Error initalising socket", e);
		}
	}

 	public void stop(){
		running = false;
	}

	public void run() {
		while(running) {
			String message = receiveUDP();
			logger.info("Message received: " + message);
		}
		logger.info("Stopping LightwaveRFReceiver");
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
			logger.error("Error receiving message", e);
		}
		return receivedMessage;
	}
}
