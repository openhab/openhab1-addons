package org.openhab.binding.lightwaverf.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFConnector {

	private static final Logger logger = LoggerFactory.getLogger(LightwaveRFConnector.class);

	private Thread receiveThread = null;
	private Thread transmitThread = null;
	private DatagramSocket transmitSocket;
	private DatagramSocket receiveSocket;

	public LightwaveRFConnector() {}

	public void connect(String address, int portIn, int portOut){
		try{
			receiveSocket = new DatagramSocket(portIn);
			transmitSocket = new DatagramSocket();

			receiveThread = new LightwaveRFReceiver();
			transmitThread = new LightwaveRFTransmitter();

			transmitThread.start();
			receiveThread.start();

		}
		catch (IOException e){
			logger.error("Error initalising socket", e);
		 }
	}

	public void disconnect(){
		//TODO stop threads, tidy up sockets
	}

	public void sendMessage() {
		//TODO send message
	}

	public class LightwaveRFReceiver extends Thread {
		public void run(){
			//TODO implement receiver thread
		}
	}

	public class LightwaveRFTransmitter extends Thread {
		public void run() {
			//TODO Transmitter thread
		}
	}

}
