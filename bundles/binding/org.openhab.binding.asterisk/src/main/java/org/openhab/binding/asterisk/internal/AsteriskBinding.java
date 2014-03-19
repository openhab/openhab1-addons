/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.asterisk.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.openhab.binding.asterisk.AsteriskBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Asterisk binding connects to a Manager Interface of an Asterisk VOIP PBX
 * and listens to event notifications from this box. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class AsteriskBinding extends AbstractBinding<AsteriskBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(AsteriskBinding.class);

	protected static ManagerConnection managerConnection;
	
	/** The hostname of the Asterisk Manager Interface to connect to */
	protected static String host;
	/** The username to connect to the Manager Interface */
	protected static String username;
	/** The password to connect to the Manager Interface */
	protected static String password;

	
	public void activate() {
	}
	
	public void deactivate() {
		disconnect();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			disconnect();
			
			AsteriskBinding.host = (String) config.get("host");
			AsteriskBinding.username = (String) config.get("username");
			AsteriskBinding.password = (String) config.get("password");
			
			if (StringUtils.isNotBlank(AsteriskBinding.host) && StringUtils.isNotBlank(AsteriskBinding.username)) {
				connect(AsteriskBinding.host, AsteriskBinding.username, AsteriskBinding.password);
			}
			else {
				logger.warn("cannot connect to asterisk manager interface because of missing " +
					"parameters (host={}, username={})", AsteriskBinding.host, AsteriskBinding.username);
			}
		}
	}
	
	/**
	 * Connects to <code>host</code> and opens a ManagerConnection by using the
	 * given <code>username</code> and <code>password</code>. Note: The Asterisk
	 * ManagerInterface on your Asterisk PBX is deactivated by default. Please
	 * refer to the documentation how to activate the ManagerInterface (AMI).
	 * 
	 * @param host the where to find the Asterisk PBX
	 * @param username username to login to Asterisk ManagerInterface
	 * @param password password to login to Asterisk ManagerInterface
	 */
	private void connect(String host, String username, String password) {
		ManagerConnectionFactory factory = 
			new ManagerConnectionFactory(host, username, password);

		AsteriskBinding.managerConnection = factory.createManagerConnection();
		AsteriskBinding.managerConnection.addEventListener(new AsteriskEventManager());
		
		try {
			AsteriskBinding.managerConnection.login();
		} catch (AuthenticationFailedException afe) {
			logger.error("authentication failed, please verify username and password");
		} catch (IOException ioe) {
			logger.error("Could not connect to ManagerInterface on {}: {}", host, ioe.toString());
		} catch (Exception e) {
			logger.error("Login to Asterisk Manager-Interface on {} throws exception: {}", host, e.toString());
		}
		
		try {
			AsteriskBinding.managerConnection.sendAction(new StatusAction());
		} catch (Exception e) {
			logger.error("registering for status update throws exception: {}", e.toString());
		}
	}
	
	/**
	 * Disconnects from the current Asterisk ManagerInterface.
	 */
	private void disconnect() {
		if (AsteriskBinding.managerConnection != null) {
			AsteriskBinding.managerConnection.logoff();
			AsteriskBinding.managerConnection = null;
		}
	}
	
	
	/**
	 * @author Thomas.Eichstaedt-Engelen
	 */
	private class AsteriskEventManager implements ManagerEventListener {

		/** holds call details of the currently active calls */
		protected Map<String, CallType> eventCache;
		
		public AsteriskEventManager() {
			eventCache = new HashMap<String, CallType>();
		}

		/**
		 * @{inheritDoc}
		 */
		public void onManagerEvent(ManagerEvent managerEvent) {
			for (AsteriskBindingProvider provider : providers) {
				for (AsteriskBindingTypes type : AsteriskBindingTypes.values()) {
					for (String itemName : provider.getItemNamesByType(type)) {
						Class<? extends Item> itemType = provider.getItemType(itemName);
						handleManagerEvent(itemName, itemType, managerEvent);								
					}
				}
			}
		}

		/**
		 * Dispatches the given <code>managerEvent</code> to the specialized
		 * handler methods.
		 * 
		 * @param itemName the corresponding item
		 * @param itemType the Type of the corresponding item
		 * @param managerEvent the {@link ManagerEvent} to dispatch
		 */
		private void handleManagerEvent(String itemName, Class<? extends Item> itemType, ManagerEvent managerEvent) {
			if (managerEvent instanceof NewChannelEvent) {
				handleNewCall(itemName, itemType, (NewChannelEvent) managerEvent);
			}
			else if (managerEvent instanceof HangupEvent) {
				handleHangupCall(itemName, itemType, (HangupEvent) managerEvent);
			}
		}

		private void handleNewCall(String itemName, Class<? extends Item> itemType, NewChannelEvent event) {
			if (event.getCallerIdNum() == null || event.getExten() == null) {
				logger.debug("calleridnum or exten is null -> handle new call aborted!");
				return;
			}
			
			CallType call = new CallType(
					new StringType(event.getCallerIdNum()),
					new StringType(event.getExten()));
			eventCache.put(event.getUniqueId(), call);
			
			if (itemType.isAssignableFrom(SwitchItem.class)) {
				eventPublisher.postUpdate(itemName, OnOffType.ON);
			}
			else if (itemType.isAssignableFrom(CallItem.class)) {
				eventPublisher.postUpdate(itemName, call);
			}
			else {
				logger.warn("handle call for item type '{}' is undefined", itemName);
			}
		}
		
		/**
		 * Removes <code>event</code> from the <code>eventCache</code> and posts
		 * updates according to the content of the <code>eventCache</code>. If
		 * there is no active call left we send an OFF-State (resp. empty 
		 * {@link CallType} and ON-State (one of the remaining active calls)
		 * in all other cases. 
		 * 
		 * @param itemName
		 * @param itemType 
		 * @param event
		 */
		private void handleHangupCall(String itemName, Class<? extends Item> itemType, HangupEvent event) {
			eventCache.remove(event.getUniqueId());
			if (itemType.isAssignableFrom(SwitchItem.class)) {
				OnOffType activeState = 
					(eventCache.size() == 0 ? OnOffType.OFF : OnOffType.ON); 
				eventPublisher.postUpdate(itemName, activeState);
			}
			else if (itemType.isAssignableFrom(CallItem.class)) {
				CallType call = (CallType)
					(eventCache.size() == 0 ? CallType.EMPTY : eventCache.values().toArray()[0]);
				eventPublisher.postUpdate(itemName, call);
			}
			else {
				logger.warn("handleHangupCall - postUpdate for itemType '{}' is undefined", itemName);
			}
		}
		
	}
	
	
}
