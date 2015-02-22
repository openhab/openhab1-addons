package org.openhab.binding.lightwaverf.internal;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFReceiver implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
    private static final int DELAY_BETWEEN_RECEIVES_MS = 10;
    
    private final CopyOnWriteArrayList<LightwaveRFMessageListener> listeners = new CopyOnWriteArrayList<LightwaveRFMessageListener>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    
    private final LightwaverfConvertor messageConvertor;
    private final DatagramSocket receiveSocket;
    
    private boolean running = false;
    
    public LightwaveRFReceiver(LightwaverfConvertor messageConvertor, int port) throws SocketException {
    	this.messageConvertor = messageConvertor;
    	this.receiveSocket = new DatagramSocket(port);
    }
    	
    /**
     * Start the LightwaveRFReceiver
     * Will set running true, initialise the socket and start the thread.
     */
    public synchronized void start() {
        logger.info("Starting LightwaveRFReceiver");
        running = true;
        executor.scheduleWithFixedDelay(this, 0, DELAY_BETWEEN_RECEIVES_MS, TimeUnit.MILLISECONDS);
    }
    /**
    * Stop the LightwaveRFSender
    * Will close the socket wait for the thread to exit and null the socket
    */
    public synchronized void stop() {
        logger.info("Stopping LightwaveRFReceiver");
        running = false;
        executor.shutdownNow();
        receiveSocket.close();
        logger.info("LightwaveRFReceiver Stopped");
    }

    /**
    * Run method, this will listen to the socket and receive messages.
    * The blocking is stopped when the socket is closed.
    */
    @Override
    public void run() {
    	String message = null;
        try {
        	message = receiveUDP();
            LightwaveRFCommand command = messageConvertor.convertFromLightwaveRfMessage(message);
            switch (command.getMessageType()) {
			case OK:
				notifyOkListners((LightwaveRfCommandOk) command);
				break;
			case ROOM_DEVICE:
				notifyRoomDeviceListners((LightwaveRfRoomDeviceMessage) command);
				break;
			case ROOM:
				notifyRoomListners((LightwaveRfRoomMessage) command);
				break;
			case HEAT_REQUEST:
				notifyHeatRequest((LightwaveRfHeatInfoRequest) command);
				break;
			case SERIAL:
				notifySerialListners((LightwaveRfSerialMessage) command);
				break;
			case VERSION:
				notifyVersionListners((LightwaveRfVersionMessage) command);
				break;
			default:
				break;
			}
        } 			
        catch (IOException e) {
            if(!(running == false && receiveSocket.isClosed())) {
                // If running isn't false and the socket isn't closed log the error
                logger.error("Error receiving message", e);
            }
        }
        catch (LightwaveRfMessageException e){
        	logger.error("Error converting message: " + message);
        }
    }

	/**
 	 * Receive the next UDP packet on the socket
 	 * @return
	 * @throws IOException
     */
    private String receiveUDP() throws IOException {
        String receivedMessage = "";
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        receiveSocket.receive(receivePacket);
        receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        logger.debug("Message received: " + receivedMessage);
        return receivedMessage;
    }
    
    /**
     * Add listener to be notified of messages received on the socket
     * @param listener
     */
    public void addListener(LightwaveRFMessageListener listener) {
        listeners.add(listener);
    }
    /**
     * Remove listener to stop being notified of messages being received on the socket.
     * @param listener
     */
    public void removeListener(LightwaveRFMessageListener listener) {
        listeners.remove(listener);
    }
    /**
    * Notify all listeners of a message
    * @param message
    */
    
    private void notifyRoomDeviceListners(LightwaveRfRoomDeviceMessage message) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.roomDeviceMessageReceived(message);
        }
    }

    private void notifyRoomListners(LightwaveRfRoomMessage message) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.roomMessageReceived(message);
        }
    }

    private void notifySerialListners(LightwaveRfSerialMessage message) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.serialMessageReceived(message);
        }
    }
    
    private void notifyOkListners(LightwaveRfCommandOk message) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.okMessageReceived(message);
        }
    }
    
    private void notifyVersionListners(LightwaveRfVersionMessage message) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.versionMessageReceived(message);
        }
    }
    
	private void notifyHeatRequest(LightwaveRfHeatInfoRequest command) {
        for(LightwaveRFMessageListener listener : listeners) {
            listener.heatInfoMessageReceived(command);
        }
	}
}
