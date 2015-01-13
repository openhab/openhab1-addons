/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.zibase.internal.ZibaseBindingConfig;
import org.openhab.core.events.EventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;

/**
 * Zibase Listener Thread class
 * 
 * This class is used to connect to the zibase as a "listener" so it receive 
 * every details about the zibase activity (RF orders, scenarios execution...).
 * Each supported activities is then sent on openHab bus as events.
 * 
 * @author Julien Tiphaine
 * @since 1.7.0
 *
 */
public class ZibaseListener extends Thread {
	
	/**
	 * generic logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ZibaseBinding.class);

	/**
	 * Regex pattern to extact X10 / Chacon RfId from zibase log
	 */
	private static final Pattern X10CHACONPATTERN 	= Pattern.compile(": ([A-Z][0-9]{1,2})(_)");
	/**
	 * Regex pattern to extact Radio ID from zibase log
	 */
	private static final Pattern RADIODIDPATTERM 	= Pattern.compile(": (<id>)([A-Z]{2}[0-9]*)");
	/**
	 * Regex pattern to extact Scenario id from zibase log
	 */
	private static final Pattern SCENARIOPATTERN 	= Pattern.compile(": ([0-9]{1,3})");
	
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
				// register to zibase for listening
	        	zibase.hostRegistering(listenerHost, listenerPort);
				DatagramSocket serverSocket = new DatagramSocket(listenerPort);  // bind
	        
				byte[] receiveData = new byte[470];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				running=true;
				
				// the real thread work is their : read message and analyse it to publish events on openhab bus 
				while (running) {
	            	serverSocket.receive(receivePacket);	
	            	ZbResponse zbResponse = new ZbResponse(receivePacket.getData());
	            	logger.debug("ZIBASE MESSAGE: " + zbResponse.getMessage());
	            	publishEvents(zbResponse);
				}
				zibase.hostUnregistering(listenerHost, listenerPort);
			
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
	 * Get Item's id from zibase log
	 * @param zbResponseStr
	 * @return
	 */
	protected String extractIdFromZbResponse(String zbResponseStr) {		
		
		Matcher matcher = ZibaseListener.X10CHACONPATTERN.matcher(zbResponseStr);
		if(matcher.find()) {
			return matcher.group(1);
		}
		
		matcher = ZibaseListener.SCENARIOPATTERN.matcher(zbResponseStr);
		if(matcher.find()) {
			return matcher.group(1);
		}
		
		matcher = ZibaseListener.RADIODIDPATTERM.matcher(zbResponseStr);
		if(matcher.find()) {
			return matcher.group(2);
		}
		
		return null;
	}
	
	/**
	 * publish configured zibase item messages on openhab bus
	 * @param zbResponse
	 */
	protected void publishEvents(ZbResponse zbResponse) {
	
		String zbResponseStr = zbResponse.getMessage();
		String id = this.extractIdFromZbResponse(zbResponseStr);
		logger.debug("Found event from ID " + id);
		
		if(id == null) return;
		
		// ...retreive all itemNames that use this id...
		Vector<String> listOfItemNames = ZibaseBinding.getBindingProvider().getItemNamesById(id);
		if (listOfItemNames == null) return;
		
		logger.debug("trying to publish events for " + id);
		
		// then post update for all items that use this id
		for(String itemName : listOfItemNames) {
			ZibaseBindingConfig config = ZibaseBinding.getBindingProvider().getItemConfigByUniqueId(itemName+"_"+id);
			logger.debug("Getting config for " + itemName + " (id = "+ id +") ");
			
			if (config != null) {
				org.openhab.core.types.State value = config.getOpenhabStateFromZibaseValue(zibase, zbResponseStr);
				logger.debug("publishing update for " + itemName + " : " + value);
				eventPubisher.postUpdate(itemName, value);
			}
		}
	}
}
