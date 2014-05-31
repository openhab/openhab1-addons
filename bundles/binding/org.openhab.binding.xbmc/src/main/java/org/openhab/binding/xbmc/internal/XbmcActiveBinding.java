/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.XbmcBindingProvider;
import org.openhab.binding.xbmc.rpc.XbmcConnector;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This refresh service for the XBMC binding is used to periodically
 * check to ensure all XBMC web sockets are still open and alive.
 * 
 * All item updates are received asynchronously via the web socket
 * All item commands are sent via the web socket
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public class XbmcActiveBinding extends AbstractActiveBinding<XbmcBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(XbmcActiveBinding.class);

	private Map<String, XbmcConnector> connectors = new HashMap<String, XbmcConnector>();
	private Map<String, XbmcHost> nameHostMapper = null;

	/**
	 * the refresh interval which is used to check for lost connections
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public void activate() {
		logger.debug(getName() + " activate()");
		setProperlyConfigured(true);
	}

	public void deactivate() {
		logger.debug(getName() + " deactivate()");

		// close any open connections
		for (XbmcConnector connector : connectors.values()) {
			if (connector.isConnected()) {
				connector.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "XBMC Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof XbmcBindingProvider) {
			XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;
			registerWatch(xbmcProvider, itemName);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		if (provider instanceof XbmcBindingProvider) {
			XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;		
			for (String itemName : xbmcProvider.getItemNames()) {
				registerWatch(xbmcProvider, itemName);
			}
		}
	}
	
	private void registerAllWatches() {
		for (BindingProvider provider : providers) {
			if (provider instanceof XbmcBindingProvider) {
				XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;	
				for (String itemName : xbmcProvider.getItemNames()) {
					registerWatch(xbmcProvider, itemName);
				}
			}
		}
	}
		
	private void registerWatch(XbmcBindingProvider xbmcProvider, String itemName) {
        // only interested in watching 'inbound' items
		if (!xbmcProvider.isInBound(itemName))
			return;

		String xbmcInstance = xbmcProvider.getXbmcInstance(itemName);
		String property = xbmcProvider.getProperty(itemName);

		XbmcConnector connector = getXbmcConnector(xbmcInstance);
		if (connector != null) {
			// add the new 'watch'
			connector.addItem(itemName, property);
			
			// update the player status so any current value is initialised
			if (connector.isConnected())
				connector.updatePlayerStatus();
		}
	}
	
	private String getXbmcInstance(String itemName) {
		for (BindingProvider provider : providers) {
			if (provider instanceof XbmcBindingProvider) {
				XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;
				if (xbmcProvider.getItemNames().contains(itemName)) {
					return xbmcProvider.getXbmcInstance(itemName);
				}
			}
		}
		return null;
	}
	
	private String getProperty(String itemName) {
		for (BindingProvider provider : providers) {
			if (provider instanceof XbmcBindingProvider) {
				XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;
				if (xbmcProvider.getItemNames().contains(itemName)) {
					return xbmcProvider.getProperty(itemName);
				}
			}
		}
		return null;
	}

	private boolean isInBound(String itemName) {
		for (BindingProvider provider : providers) {
			if (provider instanceof XbmcBindingProvider) {
				XbmcBindingProvider xbmcProvider = (XbmcBindingProvider) provider;
				if (xbmcProvider.getItemNames().contains(itemName)) {
					return xbmcProvider.isInBound(itemName);
				}
			}
		}
		return false;
	}

	private XbmcConnector getXbmcConnector(String xbmcInstance) {
		// sanity check
		if (xbmcInstance == null)
			return null;
		
		// check if the connector for this instance already exists
		XbmcConnector connector = connectors.get(xbmcInstance);
		if (connector != null)
			return connector;
		
		XbmcHost xbmcHost;
		if (xbmcInstance.startsWith("#")) {
			// trim off the '#' identifier
			String instance = xbmcInstance.substring(1);

			// check if we have been initialised yet - can't process 
			// named instances until we have read the binding config
			if (nameHostMapper == null) {
				logger.trace("Attempting to access the named instance '{}' before the binding config has been loaded", instance);
				return null;
			}
			
			// check this instance name exists in our config
			if (!nameHostMapper.containsKey(instance)) {
				logger.error("Named instance '{}' does not exist in the binding config", instance);
				return null;
			}

			xbmcHost = nameHostMapper.get(instance);
		} else {
			xbmcHost = new XbmcHost();
			xbmcHost.setHostname(xbmcInstance);
		}		

		// create a new connection handler
		logger.debug("Creating new XbmcConnector for '{}' on {}", xbmcInstance, xbmcHost.getHostname());
		connector = new XbmcConnector(xbmcHost, eventPublisher);
		connectors.put(xbmcInstance, connector);
		
		// attempt to open the connection straight away
		try {
			connector.open();
		} catch (Exception e) {
			logger.error("Connection failed for '{}' on {}", xbmcInstance, xbmcHost.getHostname());
		}

		return connector;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		for (Map.Entry<String, XbmcConnector> entry : connectors.entrySet()) {
			XbmcConnector connector = entry.getValue();
			if (connector.isConnected()) {
				// we are still connected but send a ping to make sure
				connector.ping();
			} else {
				// broken connection so attempt to reconnect
				logger.debug("Broken connection found for '{}', attempting to reconnect...", entry.getKey());
				try {
					connector.open();
				} catch (Exception e) {
					logger.debug("Reconnect failed for '{}', will retry in {}s", entry.getKey(), refreshInterval / 1000);
				}
			} 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
        // only interested in 'outbound' items
		if (isInBound(itemName)) {
			logger.warn("Received command ({}) for item {} which is configured as 'in-bound', ignoring", command.toString(), itemName);
			return;
		}
		
		try {
			// lookup the XBMC instance name and property for this item
			String xbmcInstance = getXbmcInstance(itemName);
			String property = getProperty(itemName);
			
			XbmcConnector connector = getXbmcConnector(xbmcInstance);
			if (connector == null) {
				logger.warn("Received command ({}) for item {} but no XBMC connector found for {}, ignoring", command.toString(), itemName, xbmcInstance);
				return;
			}
			if (!connector.isConnected()) {
				logger.warn("Received command ({}) for item {} but the connection to the XBMC instance {} is down, ignoring", command.toString(), itemName, xbmcInstance);
				return;
			}
			
			// TODO: handle other commands
			if (property.equals("Player.PlayPause"))
				connector.playerPlayPause();
			if (property.equals("Player.Stop"))			
				connector.playerStop();
			if (property.equals("GUI.ShowNotification"))
				connector.showNotification("openHAB", command.toString());
		} catch (Exception e) {
			logger.error("Error handling command", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		try {
			// lookup the XBMC instance name and property for this item
			String xbmcInstance = getXbmcInstance(itemName);
			String property = getProperty(itemName);
			
			XbmcConnector connector = getXbmcConnector(xbmcInstance);
			if (connector == null) {
				logger.warn("Received update ({}) for item {} but no XBMC connector found for {}, ignoring", newState.toString(), itemName, xbmcInstance);
				return;
			}
			if (!connector.isConnected()) {
				logger.warn("Received update ({}) for item {} but the connection to the XBMC instance {} is down, ignoring", newState.toString(), itemName, xbmcInstance);
				return;
			}
	
			// TODO: handle other updates
			if (property.equals("GUI.ShowNotification"))
				connector.showNotification("openHAB", newState.toString());
		} catch (Exception e) {
			logger.error("Error handling update", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug(getName() + " updated()");

		Map<String, XbmcHost> hosts = new HashMap<String, XbmcHost>();

		Enumeration<String> keys = config.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			if ("service.pid".equals(key)) {
				continue;
			}

			String[] parts = key.split("\\.");
			String hostname = parts[0];

			XbmcHost host = hosts.get(hostname);
			if (host == null) {
				host = new XbmcHost();
			}

			String value = ((String) config.get(key)).trim();

			if ("host".equals(parts[1])) {
				host.setHostname(value);
			}
			if ("rsPort".equals(parts[1])) {
				host.setRsPort(Integer.valueOf(value));
			}
			if ("wsPort".equals(parts[1])) {
				host.setWsPort(Integer.valueOf(value));
			}
			if ("username".equals(parts[1])) {
				host.setUsername(value);
			}
			if ("password".equals(parts[1])) {
				host.setPassword(value);
			}

			hosts.put(hostname, host);
		}
		
		nameHostMapper = hosts;		
		registerAllWatches();
	}
}
