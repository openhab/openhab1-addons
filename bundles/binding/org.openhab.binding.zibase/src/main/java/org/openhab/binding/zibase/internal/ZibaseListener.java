package org.openhab.binding.zibase.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.openhab.binding.zibase.internal.zibaseBindingConfig;
import org.openhab.core.events.EventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;
import fr.zapi.utils.XmlSimpleParse;

/**
 * Zibase Listener Thread class
 * @author Julien Tiphaine
 *
 */
public class ZibaseListener extends Thread {
	
	/**
	 * generic logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(zibaseBinding.class);
	
	/**
	 * zibase instance to listen to
	 */
	private Zibase zibase = null;
	
	/**
	 * eventpubisher to publish update on
	 */
	private EventPublisher eventPubisher = null;
	
	/**
	 * define wether the thread is running
	 */
	private boolean running = false;
	
	/**
	 * ip address sent to Zibase for registering
	 */
	private String listenerHost = "127.0.0.1";
	
	/**
	 * ip address sent to Zibase for registering
	 */
	private int listenerPort = 9876;
	
	
	/**
	 * Constructor 
	 */
	public ZibaseListener() {
		logger.debug("Init ZibaseListener");
	}
	
	
	/**
	 * set zibase to listen to
	 * @param pZibase
	 */
	public void setZibase (Zibase pZibase) {
		this.zibase = pZibase;
	}
	
	
	/**
	 * set host to send to zibase for registering
	 * @param pListenerHost
	 */
	public void setListenerHost(String pListenerHost) {
		this.listenerHost = pListenerHost;
	}
	
	
	/**
	 * set host to send to zibase for registering
	 * @param pListenerPort
	 */
	public void setListenerPort(int pListenerPort) {
		this.listenerPort = pListenerPort;
	}
	
	
	/**
	 * set eventpublisher to post update on
	 * @param pEventPublisher
	 */
	public void setEventPubisher(EventPublisher pEventPublisher) {
		this.eventPubisher = pEventPublisher;
	}
	
	
	/**
	 * allow to shutdown the listener thread
	 */
	public void shutdown() {
		this.running = false;
	}
	
	
	/**
	 * Thread main method.
	 * register to the zibase system and start listening to every zibase messages 
	 */
	public void run() {
		
		if(zibase != null && eventPubisher !=null) {       
			try {
	        	zibase.hostRegistering(listenerHost, listenerPort);
				DatagramSocket serverSocket = new DatagramSocket(listenerPort);  // bind
	        
				byte[] receiveData = new byte[470];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				running=true;
				
				// the real thread work is their : read message and analyse it 
				while (running) {
	            	serverSocket.receive(receivePacket);	
	            	ZbResponse zbResponse = new ZbResponse(receivePacket.getData());
	            	publishEvents(zbResponse);
	            	logger.debug("ZIBASE MESSAGE: " + zbResponse.getMessage());
				}
			
			} catch(SocketException ex) {
				logger.error("Could not open socket to zibase : " + ex);
			} catch (UnknownHostException ex) {
				logger.error("Given Zibase host not reachable : " + ex);
			} catch (IOException ex) {
				logger.error("IO eror reading Zibase soket : " + ex);
			}
			
		} else {
			logger.error("Zibase listener thread launched with no associated zibase and/or eventPublisher !");
		}
	}
	
	
	/**
	 * publish configured zibase item messages on openhab bus
	 * @param zbResponse
	 */
	protected void publishEvents(ZbResponse zbResponse) {
	
		// first get item id from Zibase message...
		String id = XmlSimpleParse.getTagValue("id", zbResponse.getMessage());
		if(id == null) return;
		
		// ...retreive all itemNames that use this id...
		Vector<String> listOfItemNames = zibaseBinding.getBindingProvider().getItemNamesById(id);
		if (listOfItemNames == null) return;
		
		logger.debug("trying to publish events for " + id);
		
		// then post update for all items that use this id
		for(String itemName : listOfItemNames) {
			zibaseBindingConfig config = zibaseBinding.getBindingProvider().getItemConfig(itemName);
			logger.debug("Getting config for " + itemName);
			
			if (config != null) {
				org.openhab.core.types.State value = config.getOpenhabStateFromZibaseValue(zbResponse);
				logger.debug("publishing update for " + itemName + " : " + value);
				eventPubisher.postUpdate(itemName, value);
			}
		}
	}
}
