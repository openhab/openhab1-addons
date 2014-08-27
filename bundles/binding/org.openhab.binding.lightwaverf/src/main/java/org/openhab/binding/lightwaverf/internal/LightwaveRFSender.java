package org.openhab.binding.lightwaverf.internal;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFSender implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(LightwaveRFSender.class);
	private static final int LIGHTWAVE_PORT_IN = 9760; // Port into Lightwave Wifi hub.
	private static final String BROADCAST_ADDRESS = "255.255.255.255";  // Broadcast UDP address.
	private final ScheduledExecutorService scheduler;
	private static final long POLL_TIME = 1;
	private static final long INITIAL_POLL_DELAY = 0;

	private int messageCount = 0;
	private DatagramSocket transmitSocket; // Socket for UDP transmission to LWRF port 9760
	private BlockingQueue<String> queue; // Simple queue to queue up UDP transmission that could be from a polling thread, or direct commands through API  	
	private boolean running = false;
	/*
	 * Constructor defaults to logging every 30 seconds
	 */
	public LightwaveRFSender() {
		queue = new LinkedBlockingQueue<String>();
		initialiseSockets();
		scheduler = Executors.newScheduledThreadPool(1);
	}

	/*
	 * Initialise transmit sockets for UDP
	 */
	public void initialiseSockets(){
		try{
			transmitSocket = new DatagramSocket();
		} catch (IOException e){
			logger.error("Error initalising socket", e);
		}
	}

 	public void start(){
 		running = true;
		scheduler.scheduleWithFixedDelay(this, INITIAL_POLL_DELAY, POLL_TIME, TimeUnit.SECONDS);
 	}

	public void stop() {
		running = false;
	} 	
	/*
	 * Run thread, pulling off any items from the UDP commands buffer, then send across network
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		logger.info("Running");
		try{
			netsendUDP(queue.take());
		}
		catch(InterruptedException e){
			logger.error("Error waiting on queue", e);
		}
	}

     	/*
 	 * Add UDP commands to a buffer.
  	 */
	public void sendUDP(String command){
		try{
			queue.put(command);
		}
		catch(InterruptedException e){
			logger.error("Error adding command[" + command + "] to queue", e);
		}
	}

	/*
	 * Send the UDP commands from the buffer, waiting a period of time before sending next, so as not to flood UDP socket on LWRF 9760 port
	 */
	public void netsendUDP(String command){
		command = messageCount + command;
		incrementMessageCount();
		try {
			logger.info("Sending command[" + command + "]");
			byte[] sendData = new byte[1024];
			sendData = command.getBytes();
			InetAddress ipAddress =  InetAddress.getByName(BROADCAST_ADDRESS);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, LIGHTWAVE_PORT_IN); //Send broadcast UDP to 9760 port
			transmitSocket.send(sendPacket);
		}
		catch (IOException e) {
			logger.error("Error sending command[" + command + "]", e);
		}
	}

 	/*
	 * Increment message counter, so different messages have different IDs
	 * Important for getting corresponding OK acknowledgements from port 9761 tagged with the same counter value
	 */
	private void incrementMessageCount(){
		if (messageCount <=998) messageCount++;
		else messageCount = 000;
	}
}
