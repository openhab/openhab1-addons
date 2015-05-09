/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.octoller.internal;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Dictionary;

import org.openhab.binding.octoller.octollerBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.octoller.devicecom.*;	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * For openHAB binding for octoller (www.octoller.com)
 * 
 * @author JPlenert
 * @since 1.5.1
 */
public class octollerBinding extends AbstractActiveBinding<octollerBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(octollerBinding.class);
		
	/** 
	 * the refresh interval which is used to poll values from the octoller
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	public octollerBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
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
		return "octoller Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
		for (octollerBindingProvider provider : providers) 
		{
			for (String itemName : provider.getItemNames()) 
			{
				octollerBindingConfig config = (octollerBindingConfig) provider.getConfig(itemName);
				if (config == null)
					continue;
					
				//eventPublisher.postUpdate(itemName, (State)new PercentType(100));
				
				try
				{
					Connection con = new Connection(config.GatewayHost);
					String result = con.DoCommand(con.BuildCommandString(config, "", ""));
					con.ProcessResultToPublisher(eventPublisher, itemName, result);
					con.Close();
					logger.debug(itemName + " returned " + result);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) 
	{
		for (octollerBindingProvider provider : providers) 
		{
			octollerBindingConfig config = (octollerBindingConfig) provider.getConfig(itemName);
			if (config == null)
				continue;
			
			try
			{
				Connection con = new Connection(config.GatewayHost);
				String result = con.DoCommand(con.BuildCommandString(config, command.getClass().getSimpleName(), command.toString()));
				con.ProcessResultToPublisher(eventPublisher, itemName, result);
				con.Close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			
			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}
	

}
