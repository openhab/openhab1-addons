/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.opensprinkler.internal;

import static org.openhab.binding.opensprinkler.internal.OpenSprinklerBinding.OpenSprinklerMode.HTTP;

import java.util.Dictionary;

import net.jonathangiles.opensprinkler.OpenSprinkler;
import net.jonathangiles.opensprinkler.OpenSprinklerFactory;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.opensprinkler.OpenSprinklerBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Binding for OpenSprinkler. At present this only supports the 
 * OpenSprinkler Pi connecting via GPIO, but perhaps in the future 
 * will also support HTTP connections to both OpenSprinkler and
 * OpenSprinkler Pi.
 * 
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.3.0
 */
public class OpenSprinklerBinding extends AbstractBinding<OpenSprinklerBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(OpenSprinklerBinding.class);
	
	private OpenSprinkler openSprinkler;
	
	// configuration properties
	private int numberOfStations = 8;
	private OpenSprinklerMode mode = null;
	private String url;
	private String password;
	
	/**
	 * Represents the valid modes of the binding {@code gpio} and {@code http}.
	 */
	enum OpenSprinklerMode {
		GPIO, HTTP;
	}
	
	
	public OpenSprinklerBinding() {
		// no-op
	}
	
	public void activate() {
		updateBinding();
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
		openSprinkler.closeConnection();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		
		if (! (command instanceof OnOffType)) {
			logger.debug("Provided command " + command + " is not of type 'OnOffType'");
			return;
		}
		
		final OnOffType switchCommand = (OnOffType) command;
		final OpenSprinklerBindingProvider bindingProvider = findFirstMatchingBindingProvider(itemName, command);
		final int station = bindingProvider.getStationNumber(itemName);
		
		if (station < 0 || station >= numberOfStations) {
			logger.warn("Station " + station + " is not in the valid [" + 0 + ".." + numberOfStations + "] range");
			return;
		}
		
		switch (switchCommand) {
			case ON:  openSprinkler.openStation(station); break;
			case OFF: openSprinkler.closeStation(station); break;
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			// to override the mode (gpio or http) one has to add a 
			// parameter to openhab.cfg like openSprinkler:mode=[gpio|http]
			String modeString = (String) config.get("mode");
			if (StringUtils.isNotBlank(modeString)) {
				try {
					mode = OpenSprinklerMode.valueOf(modeString.toUpperCase());
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException("openSprinkler:mode", 
							"Unknown OpenSprinkler mode " + mode + 
							"! Valid modes are 'gpio' and 'http'. Please fix your openhab.cfg.");
				}
			}
			
			// to specify the http url one has to add a 
			// parameter to openhab.cfg like openSprinkler:http_url=<url>
			url = (String) config.get("httpUrl");
			
			// to specify the http password one has to add a 
			// parameter to openhab.cfg like openSprinkler:http_password=<password>
			password = (String) config.get("httpPassword");
			
			// to override the number of stations one has to add a 
			// parameter to openhab.cfg like openSprinkler:numberOfStations=<count>
			String numberOfStationsString = (String) config.get("numberOfStations");
			if (StringUtils.isNotBlank(numberOfStationsString)) {
				numberOfStations = Integer.parseInt(numberOfStationsString);
				openSprinkler.setNumberOfStations(numberOfStations);
			}
			
			// read further config parameters here ...
			
			// then update the binding
			updateBinding();
		}
	}
	
	/**
	 * Find the first matching {@link OpenSprinklerBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>.  
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 * provider could be found
	 */
	private OpenSprinklerBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {
		OpenSprinklerBindingProvider firstMatchingProvider = null;
		
		for (OpenSprinklerBindingProvider provider : this.providers) {
			boolean match = provider.providesBindingFor(itemName);
			if (match) {
				firstMatchingProvider = provider;
				break;
			}
		}
		
		return firstMatchingProvider;
	}
	
	private void updateBinding() {
		if (openSprinkler != null) {
			openSprinkler.closeConnection();
		}
		
		if (mode == null) {
			return;
		}
		
		switch (mode) {
			case GPIO: openSprinkler = OpenSprinklerFactory.newGpioConnection();
						 break;
			case HTTP: openSprinkler = OpenSprinklerFactory.newHttpConnection(url, password);
						 break;
		    default:   throw new IllegalStateException("Unknown OpenSprinkler mode: " + mode +
		    	"! Since it is checked while initialization already, this Exception should never be thrown!");
		}
		
		openSprinkler.setNumberOfStations(numberOfStations);
		
		logger.info("OpenSprinkler binding running in " + mode + " mode" + 
				(HTTP.equals(mode) ? " with url" + url : "") + 
				". Running with " + numberOfStations + " stations enabled.");
	}
}
