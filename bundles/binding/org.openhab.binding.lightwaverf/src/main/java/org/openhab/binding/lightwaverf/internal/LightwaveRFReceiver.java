package org.openhab.binding.lightwaverf.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFReceiver implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
	private static final int PORT = 9760;
	
	private final CopyOnWriteArrayList<LightwaveRFMessageListener> listeners = new CopyOnWriteArrayList<LightwaveRFMessageListener>();

	private boolean running = true;
	private DatagramSocket receiveSocket;

 	public LightwaveRFReceiver(){}

 	public synchronized void stop(){
		running = false;
		receiveSocket.close();
	}
 	
 	public synchronized void start(){
 		running = true;
 		initialiseSockets();
 		new Thread(this).start();
 	}

   	/**
   	 * Initialise receive sockets for UDP
   	 */
	private void initialiseSockets(){
		try{
			receiveSocket = new DatagramSocket(PORT);
   		} catch (IOException e){
			logger.error("Error initalising socket", e);
		}
	}
 	
	/**
	 * Run method, this will listen to the socket and receive messages
	 */
	@Override
	public void run() {
		logger.info("Starting LightwaveRFReceiver");
		while(running) {
			String message = receiveUDP();
			logger.debug("Message received: " + message);
			notifyListeners(message);
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
			if(!(running == false && receiveSocket.isClosed())){
				// If running isn't false and the socket isn't closed log the error
				logger.error("Error receiving message", e);
			}
		}
		return receivedMessage;
	}
	
	public void addListener(LightwaveRFMessageListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(LightwaveRFMessageListener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(String message){
		for(LightwaveRFMessageListener listener : listeners){
			listener.messageRecevied(message);
		}
	}
}
