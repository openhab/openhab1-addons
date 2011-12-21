/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.asterisk.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
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
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
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
public class AsteriskBinding implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(AsteriskBinding.class);
	
	/** to keep track of all binding providers */
	final static protected Collection<AsteriskBindingProvider> providers = new HashSet<AsteriskBindingProvider>();
	
	protected static ItemRegistry itemRegistry;
	
	protected static EventPublisher eventPublisher;

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
	
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		AsteriskBinding.itemRegistry = itemRegistry;
	}
	
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		AsteriskBinding.itemRegistry = null;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		AsteriskBinding.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		AsteriskBinding.eventPublisher = null;
	}

	public void addBindingProvider(AsteriskBindingProvider provider) {
		AsteriskBinding.providers.add(provider);
	}

	public void removeBindingProvider(AsteriskBindingProvider provider) {
		AsteriskBinding.providers.remove(provider);		
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
	private static class AsteriskEventManager implements ManagerEventListener {

		/** holds call details of the currently active calls */
		protected static Map<String, CallType> eventCache;
		
		public AsteriskEventManager() {
			eventCache = new HashMap<String, CallType>();
		}

		/**
		 * @{inheritDoc}
		 */
		public void onManagerEvent(ManagerEvent managerEvent) {
			if (itemRegistry != null) {
				for (AsteriskBindingProvider provider : providers) {
					for (AsteriskBindingTypes type : AsteriskBindingTypes.values()) {
						for (String itemName : provider.getItemNamesForType(type)) {
							try {
								Item item = itemRegistry.getItem(itemName);
								handleManagerEvent(item, managerEvent);								
							}
							catch (ItemNotFoundException e) {
							}
							catch (ItemNotUniqueException e) {
							}
						}
					}
				}
			}
		}

		/**
		 * Dispatches the given <code>managerEvent</code> to the specialized
		 * handler methods.
		 * 
		 * @param item the corresponding item
		 * @param managerEvent the {@link ManagerEvent} to dispatch
		 */
		private void handleManagerEvent(Item item, ManagerEvent managerEvent) {
			if (managerEvent instanceof NewChannelEvent) {
				handleNewCall(item, (NewChannelEvent) managerEvent);
			}
			else if (managerEvent instanceof HangupEvent) {
				handleHangupCall(item, (HangupEvent) managerEvent);
			}
		}

		private void handleNewCall(Item item, NewChannelEvent event) {
			CallType call = new CallType(
					new StringType(event.getCallerIdNum()),
					new StringType(event.getExten()));
			eventCache.put(event.getUniqueId(), call);
			
			if (item instanceof SwitchItem) {
				eventPublisher.postUpdate(item.getName(), OnOffType.ON);
			}
			else if (item instanceof CallItem) {
				eventPublisher.postUpdate(item.getName(), call);
			}
			else {
				logger.warn("handleCall - postUpdate for itemType '{}' is undefined", item);
			}
		}
		
		/**
		 * Removes <code>event</code> from the <code>eventCache</code> and posts
		 * updates according to the content of the <code>eventCache</code>. If
		 * there is no active call left we send an OFF-State (resp. empty 
		 * {@link CallType} and ON-State (one of the remaining active calls)
		 * in all other cases. 
		 * 
		 * @param item
		 * @param event
		 */
		private void handleHangupCall(Item item, HangupEvent event) {
			eventCache.remove(event.getUniqueId());
			if (item instanceof SwitchItem) {
				OnOffType activeState = 
					(eventCache.size() == 0 ? OnOffType.OFF : OnOffType.ON); 
				eventPublisher.postUpdate(item.getName(), activeState);
			}
			else if (item instanceof CallItem) {
				CallType call = (CallType)
					(eventCache.size() == 0 ? CallType.EMPTY : eventCache.values().toArray()[0]);
				eventPublisher.postUpdate(item.getName(), call);
			}
			else {
				logger.warn("handleHangupCall - postUpdate for itemType '{}' is undefined", item);
			}
		}
		
	}
	
	
}
