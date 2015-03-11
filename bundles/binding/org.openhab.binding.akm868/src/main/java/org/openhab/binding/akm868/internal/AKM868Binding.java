/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.akm868.internal;

import java.util.Dictionary;

import java.util.List;

import org.openhab.binding.akm868.AKM868BindingProvider;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Michael Heckmann
 * @since 1.7.0
 */
public class AKM868Binding extends AbstractActiveBinding<AKM868BindingProvider> implements ManagedService, AKM868Listener {

	private static final Logger logger = 
		LoggerFactory.getLogger(AKM868Binding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the AKM868
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	private String host;
	private int port;
	private long timeout;
	private AKM868PacketReceiver packetlistener;
	
	
	public AKM868Binding() {
		packetlistener = new AKM868PacketReceiver(this);
	}
		
	
	public void activate() {
		
	}
	
	public void deactivate() {
		logger.debug("Stoppig AKM868 listener...");
		if (packetlistener != null)
			packetlistener.stopListener();
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
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
		return "AKM868 Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
								
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("AKM 868: internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("AKM 868:internalReceiveUpdate() is called!");
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
			
			String hostString = (String) config.get("host");
			if (StringUtils.isNotBlank(hostString)) {
				host = hostString;
			}
			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString)) {
				port = Integer.parseInt(portString);
			}
			
			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				timeout = Long.parseLong(timeoutString); 
			}
			
			// make sure that there is no listener running
			packetlistener.stopListener();
			// send the parsed information to the listener
			packetlistener.initializeReceiver(host,port,timeout);
			// start the listener
			new Thread(packetlistener).start();
			setProperlyConfigured(true);
		}
	}


	@Override
	public void publishUpdate(String id, boolean isPresent) {
		
		for (AKM868BindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				if (provider.getId(itemName).equals(id) && provider.getChannel(itemName).equals("0")) {
					logger.debug("Publishing state for: "+itemName+" -state: "+provider.getId(itemName));
					eventPublisher.postUpdate(itemName, isPresent==true ? OnOffType.ON : OnOffType.OFF);
				}

			}
		}
		
	}


	@Override
	public void publishKeyPressedShort(String id) {
		
		for (AKM868BindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				if (provider.getId(itemName).equals(id) && provider.getChannel(itemName).equals("1")) {
					logger.debug("Publishing state for: "+itemName+" -state: ON");
					eventPublisher.postUpdate(itemName, OnOffType.ON );
				}

			}
		}
		
	}


	@Override
	public void publishKeyPressedLong(String id) {
		for (AKM868BindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				if (provider.getId(itemName).equals(id) && provider.getChannel(itemName).equals("5")) {
					logger.debug("Publishing state for: "+itemName+" -state: ON");
					eventPublisher.postUpdate(itemName, OnOffType.ON );
				}

			}
		}
		
	}
	

}
