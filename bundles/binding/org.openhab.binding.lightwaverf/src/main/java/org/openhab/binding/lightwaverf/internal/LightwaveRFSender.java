package org.openhab.binding.lightwaverf.internal;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFSender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LightwaveRFSender.class);
    private static final LightwaveRFCommand STOP_MESSAGE = LightwaveRFCommand.STOP_MESSAGE;
    
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    // Queue of messages to send
    private final BlockingDeque<LightwaveRFCommand> queue = new LinkedBlockingDeque<LightwaveRFCommand>();
    
    // Time between commands so we don't flood the LightwaveRF hub
    private final int timeBetweenCommand;
    // LightwaveRF WIFI hub port.
    private final int lightwaveWifiLinkPortIn;
    // LightwaveRF WIFI hub IP Address or broadcast address to send messages to
    private final InetAddress ipAddress;
    // Socket to transmit messages
    private final DatagramSocket transmitSocket;

    // Boolean to indicate if we are running
    private boolean running = false;

    public LightwaveRFSender(String lightwaveWifiLinkIp, int lightwaveWifiLinkPortIn, int timeBetweenCommand) throws UnknownHostException, SocketException {
    	this.lightwaveWifiLinkPortIn = lightwaveWifiLinkPortIn;
    	this.timeBetweenCommand = timeBetweenCommand;
        ipAddress =  InetAddress.getByName(lightwaveWifiLinkIp);
        transmitSocket = new DatagramSocket();
    }
    /**
     * Start the LightwaveRFSender
     * Will set running to true, initalise the socket and start the sender thread
     */
    public synchronized void start() {
        logger.info("Starting LightwaveRFSender");
        running = true;
        executor.scheduleWithFixedDelay(this, 0, timeBetweenCommand, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the LightwaveRFReseiver
     * Will set running to false, add a stop message to the queue so that it stops when empty,
     * close and set the socket to null
     */
    public synchronized void stop() {
        logger.info("Stopping LightwaveRFSender");
        running = false;
        executor.shutdownNow();
        transmitSocket.close();
        logger.info("LightwaveRFSender Stopped");
    }

    /**
     * Run thread, pulling off any items from the UDP commands buffer, then send across network
     */
    @Override
    public void run() {
	    logger.info("LightwaveRFSender Started");
	        try {
	            LightwaveRFCommand commandToSend = queue.take();
	            if(!commandToSend.equals(STOP_MESSAGE)) {
	                netsendUDP(commandToSend);
	            } else {
	                logger.info("Stop message received");
	            }
	        } catch(InterruptedException e) {
	            logger.error("Error waiting on queue", e);
	        }
    }

    /**
    * Add LightwaveRFCommand command to queue to send.
    */
    public void sendLightwaveCommand(LightwaveRFCommand command) {
        try {
            if(running) {
                queue.put(command);
            } else {
                logger.info("Message not added to queue as we are shutting down Message[" + command + "]");
            }
        } catch(InterruptedException e) {
            logger.error("Error adding command[" + command + "] to queue", e);
        }
    }

    /**
    * Send the UDP commands
     */
    private void netsendUDP(LightwaveRFCommand command) {
        try {
            logger.debug("Sending command[" + command.getLightwaveRfCommandString() + "]");
            byte[] sendData = new byte[1024];
            sendData = command.getLightwaveRfCommandString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, lightwaveWifiLinkPortIn);
            transmitSocket.send(sendPacket);
        } 		catch (IOException e) {
            logger.error("Error sending command[" + command + "]", e);
        }
    }
}
