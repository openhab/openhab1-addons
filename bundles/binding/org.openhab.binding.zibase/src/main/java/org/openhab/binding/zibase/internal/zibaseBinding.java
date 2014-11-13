/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.util.Dictionary;
import org.openhab.binding.zibase.zibaseBindingProvider;
import org.openhab.binding.zibase.internal.zibaseGenericBindingProvider;
import org.openhab.binding.zibase.internal.zibaseBindingConfig;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.ZbAction;
import fr.zapi.ZbProtocol;
import fr.zapi.Zibase;


/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Julien Tiphaine
 * @since 1.6.0
 */
public class zibaseBinding extends AbstractActiveBinding<zibaseBindingProvider> implements ManagedService {

	/**
	 * generic logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(zibaseBinding.class);	
	
	/**
	 * zibase to which we are connected
	 */
	private static Zibase zibase;
	
	/**
	 * zibase binding provider
	 */
	private static zibaseGenericBindingProvider bindingProvider = new zibaseGenericBindingProvider();
	
	/**
	 * Associated Zibase listener instance
	 */
	private static ZibaseListener zibaseListener = null; 
	
	/** 
	 * the refresh interval which is used to poll values from the zibase
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/**
	 * ip adresse of zibase to connect to
	 */
	private String zibaseIp = "192.168.1.1";
	
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
	public zibaseBinding() {

	}
	
	/**
	 * get the associated binding provider
	 * @return
	 */
	public static zibaseGenericBindingProvider getBindingProvider() {
		return bindingProvider;
	}
		
	/**
	 * @{inheritDoc}
	 * TODO: read ip address from config file
	 * TODO: start listener depending on param from config file
	 */
	public void activate() {
		try {
			zibase = new Zibase(zibaseIp);
			logger.info("connected to zibase for command sending");
						
		} catch(Throwable th)	{
			logger.info("Error connecting to zibase using specified ip address");
		}
		
		try {
			logger.info("Starting zibase listener thread...");
			zibaseListener = new ZibaseListener();
			zibaseListener.setZibase(zibase);
			zibaseListener.setEventPubisher(eventPublisher);
			zibaseListener.setListenerHost(listenerHost);
			zibaseListener.setListenerPort(listenerPort);
			zibaseListener.start();
		} catch(Throwable th)	{
			logger.info("Error connecting to zibase for listening");
		}
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
		logger.info("Shutting down zibase connection and/or thread...");
		zibaseListener.shutdown();
		zibase = null;
		logger.info("Zibase binding desactivated");
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "zibase Refresh Service";
	}
	
	
	/**
	 * @{inheritDoc}
	 * TODO : read XML config from Zibase and update bus
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	
	/**
	 * @{inheritDoc}
	 * TODO : check how dim values work
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called with ITEM = " + itemName + " / COMMAND = " + command.toString());
		zibaseBindingConfig config = bindingProvider.getItemConfig(itemName);
		config.sendCommand(zibase, command, -1);
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			String ip = (String) config.get("ip");
			if (StringUtils.isNotBlank(ip)) {
				zibaseIp = ip;
			}

			String tmpListenerIp = (String) config.get("listenerHost");
			if (StringUtils.isNotBlank(tmpListenerIp)) {
				listenerHost = tmpListenerIp;
			}
			
			String tmpListenerPort = (String) config.get("listenerPort");
			if (StringUtils.isNotBlank(tmpListenerPort)) {
				listenerPort = Integer.parseInt(tmpListenerPort);
			}
			
			// read further config parameters here ...
			setProperlyConfigured(true);
		}
	}
}