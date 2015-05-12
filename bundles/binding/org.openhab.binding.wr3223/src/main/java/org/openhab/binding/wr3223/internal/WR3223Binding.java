/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.IOException;
import java.util.Map;

import org.openhab.binding.wr3223.WR3223BindingProvider;
import org.openhab.binding.wr3223.WR3223Commands;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Michael Fraefel
 * @since 1.7.0
 */
public class WR3223Binding extends AbstractActiveBinding<WR3223BindingProvider> {

	private static final Logger logger = 
		LoggerFactory.getLogger(WR3223Binding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the WR3223
	 * server (optional, defaults to 15000ms)
	 */
	private long refreshInterval = 15000;
	
	/**
	 * Host if connection over IP is used.
	 */
	private String host;
	
	/**
	 * Port if connection over IP is used.
	 */	
	private int port;
	
	/**
	 * Serial port if connection over serial interface is used.
	 */
	private String serialPort;
	
	/**
	 * Controller address.
	 */
	private byte controllerAddr=1;
	
	/**
	 * WR3223 Connector
	 */
	private AbstractWR3223Connector connector;
	
	
	public WR3223Binding() {
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;		
		configure(configuration);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		configure(configuration);
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		if(connector != null){
			try{
				connector.close();
			}catch(IOException ex){
				logger.error("Error by closing connector.", ex);
			}
		}
	}
	
	/**
	 * Configure binding.
	 * @param configuration
	 */
	private void configure(final Map<String, Object> configuration) {
		//Configure refresh
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			if(refreshInterval>20000){
				logger.warn("Refresh interval should not be over 20 seconds.");
			}
		}
		
		//Controller
		String controllerAddrString = (String) configuration.get("controllerAddr");
		if (StringUtils.isNotBlank(controllerAddrString)) {
			controllerAddr = Byte.parseByte(controllerAddrString);
		}
		
		//Configure connection
		String hostString = (String) configuration.get("host");
		String portString = (String) configuration.get("port");
		String serialPortString = (String) configuration.get("serialPort");
		if (StringUtils.isNotBlank(hostString) && StringUtils.isNotBlank(portString)) {
			host = hostString;
			port = Integer.parseInt(portString);
			serialPort = null;
			logger.info("Connect over IP to host {}:{}", host, port);
			setProperlyConfigured(true);
		}else if(StringUtils.isNotBlank(serialPortString)){
			serialPort = serialPortString;
			host = null;
			logger.info("Connect over serial port {}", serialPort);
			setProperlyConfigured(true);			
		}else{
			setProperlyConfigured(false);
			logger.error("No connection configured");
		}

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
		return "WR3223 Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		
		//setup connector
		try{
			if(connector == null){
				if(host != null){
					TcpWR3223Connector tcpConnector = new TcpWR3223Connector();
					tcpConnector.connect(host, port);
					connector = tcpConnector;
				}else{
					
				}
			}
		}catch(IOException ex){
			logger.error("Couldn't establish connection to WR3223.", ex);
		}
		
		try{
			logger.info("Aussentemperatur: "+connector.read(controllerAddr, WR3223Commands.T3));
		
		}catch(IOException ex){
			logger.error("WR3223 read:", ex);
			
		}		

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

}
