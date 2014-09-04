package org.openhab.binding.lightwaverf.internal;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFReceiver implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
    private static final int PORT = 9760;
    private final CopyOnWriteArrayList<LightwaveRFMessageListener> listerns = new CopyOnWriteArrayList<LightwaveRFMessageListener>();
    private CountDownLatch latch = new CountDownLatch(0);
    private boolean running = true;
    private DatagramSocket receiveSocket;
    public LightwaveRFReceiver() {}
    /**
     * Start the LightwaveRFReceiver
     * Will set running true, initialise the socket and start the thread.
     */
    public synchronized void start() {
        logger.info("Starting LightwaveRFReceiver");
        running = true;
        latch = new CountDownLatch(1);
        initialiseSockets();
        new Thread(this).start();
    }
    /**
    * Stop the LightwaveRFSender
    * Will closae the sockect wait for the thread to exit and null the socket
    */
    public synchronized void stop() {
        logger.info("Stopping LightwaveRFReceiver");
        running = false;
        receiveSocket.close();
        try {
            latch.await();
        } 		catch(InterruptedException e) {
            logger.error("Error waiting for shutdown to complete", e);
        }
        receiveSocket = null;
        logger.info("LightwaveRFReceiver Stopped");
    }
    /**
    * Initialise receive sockets for UDP
    */
    private void initialiseSockets() {
        try {
            receiveSocket = new DatagramSocket(PORT);
        } catch (IOException e) {
            logger.error("Error initalising socket", e);
        }
    }
    /**
    * Run method, this will listen to the socket and receive messages.
    * The blocking is stopped when the socket is closed.
    */
    @Override
    public void run() {
        logger.info("LightwaveRFReceiver Started");
        while(running) {
            try {
                String message = receiveUDP();
                logger.debug("Message received: " + message);
                notifyListners(message);
            } 			catch (IOException e) {
                if(!(running == false && receiveSocket.isClosed())) {
                    // If running isn't false and the socket isn't closed log the error
                    logger.error("Error receiving message", e);
                }
            }
        }
        latch.countDown();
    }
    /**
     	 * Receive the next UDP packet on the socket
     	 * @return
    	 * @throws IOException
     */
    public String receiveUDP() throws IOException {
        String receivedMessage = "";
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        receiveSocket.receive(receivePacket);
        receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        return receivedMessage;
    }
    /**
    * Add listener to be notified of messages received on the socket
    * @param listener
    */
    public void addListener(LightwaveRFMessageListener listener) {
        listerns.add(listener);
    }
    /**
     * Remove listener to stop being notified of messages being received on the socket.
    * @param listener
     */
    public void removeListener(LightwaveRFMessageListener listener) {
        listerns.remove(listener);
    }
    /**
    * Notify all listeners of a message
    * @param message
    */
    private void notifyListners(String message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.messageRecevied(message);
        }
    }
}
