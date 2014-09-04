package org.openhab.binding.lightwaverf.internal;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFSender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LightwaveRFSender.class);
    private static final String STOP_MESSAGE = "StopMessage";
    // Poll time so we don't flood the LightwaveRF hub
    private static final int POLL_TIME = 250;
    // LightwaveRF WIFI hub port.
    private static final int LIGHTWAVE_PORT_IN = 9760;
    // LightwaveRF WIFI hub IP Address or broadcast address
    private static final String LIGHTWAVE_IP_ADDRESS = "255.255.255.255";
    // Used to send messages
    private final InetAddress ipAddress;
    // Latch used to ensure we shutdown.
    private CountDownLatch latch = new CountDownLatch(0);
    // Simple queue of UDP transmission
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    // Boolean to indicate if we are running
    private boolean running = false;
    // LightwaveRF messageId
    private int messageId = 0;
    private DatagramSocket transmitSocket = null;

    public LightwaveRFSender() throws UnknownHostException {
        ipAddress =  InetAddress.getByName(LIGHTWAVE_IP_ADDRESS);
    }
    /**
     * Start the LightwaveRFSender
     * Will set running to true, initalise the socket and start the sender thread
     */
    public synchronized void start() {
        logger.info("Starting LightwaveRFSender");
        running = true;
        latch = new CountDownLatch(1);
        initialiseSockets();
        new Thread(this).start();
    }

    /**
     * Stop the LightwaveRFReseiver
     * Will set running to false, add a stop message to the queue so that it stops when empty,
     * close and set the sockect to null
     */
    public synchronized void stop() {
        logger.info("Stopping LightwaveRFSender");
        running = false;
        addStopMessage();
        try {
            latch.await();
        } catch(InterruptedException e) {
            logger.error("Error waiting for shutdown to complete", e);
        }
        transmitSocket.close();
        transmitSocket = null;
        logger.info("LightwaveRFSender Stopped");
    }

    /**
      * Initialise transmit sockets for UDP publishing
      */
    private void initialiseSockets() {
        try {
            transmitSocket = new DatagramSocket();
        } catch (IOException e) {
            logger.error("Error initalising socket", e);
        }
    }

    /**
     * Run thread, pulling off any items from the UDP commands buffer, then send across network
     */
    @Override
    public void run() {
        logger.info("LightwaveRFSender Started");
        while(running) {
            try {
                String commandToSend = queue.take();
                if(!commandToSend.equals(STOP_MESSAGE)) {
                    netsendUDP(commandToSend);
                } else {
                    logger.info("Stop message received");
                    break;
                }
                Thread.sleep(POLL_TIME);
            } catch(InterruptedException e) {
                logger.error("Error waiting on queue", e);
            }
        }
        latch.countDown();
    }

    /**
    * Add UDP commands to queue.
    */
    public void sendUDP(String command) {
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
     * Add a stop message so we start shutting down
     */
    private void addStopMessage() {
        try {
            queue.put(STOP_MESSAGE);
        } catch (InterruptedException e) {
            logger.error("Error stoping LightwaveRFSender", e);
        }
    }

    /**
    * Send the UDP commands
     */
    private void netsendUDP(String command) {
        command = messageId + command;
        incrementMessageId();
        try {
            logger.info("Sending command[" + command + "]");
            byte[] sendData = new byte[1024];
            sendData = command.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, LIGHTWAVE_PORT_IN);
            transmitSocket.send(sendPacket);
        } 		catch (IOException e) {
            logger.error("Error sending command[" + command + "]", e);
        }
    }

    /**
     * Increment message counter, so different messages have different IDs
     * Important for getting corresponding OK acknowledgements from port 9761 tagged with the same counter value
     */
    private void incrementMessageId() {
        if (messageId <=998) messageId++;
        else messageId = 000;
    }
}
