/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.followmemusic.internal;

import java.sql.SQLException;
import java.util.Dictionary;

import javax.swing.JFrame;

import org.openhab.binding.followmemusic.FollowMeMusicBindingProvider;
import org.openhab.binding.followmemusic.internal.FollowMeMusicGenericBindingProvider.FollowMeMusicBindingConfig;
import org.openhab.binding.followmemusic.internal.FollowMeMusicGenericBindingProvider.FollowMeMusicItemType;

import org.apache.commons.lang.StringUtils;
import org.followmemusic.io.MySQLDatabaseConnection;
import org.followmemusic.location.CommandFormatException;
import org.followmemusic.location.DatabaseLocationManager;
import org.followmemusic.location.FollowMeMusicLocationManager;
import org.followmemusic.location.ObjectNotFoundException;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author FollowMe
 * @since 0.1.0.dev
 */
public class FollowMeMusicBinding extends AbstractActiveBinding<FollowMeMusicBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(FollowMeMusicBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the FollowMeMusic
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	private FollowMeMusicLocationManager locationManager;
	
	
	public FollowMeMusicBinding() {
		try {			
			MySQLDatabaseConnection connection = new MySQLDatabaseConnection("root", "raspberry", "localhost/followme_web");
			this.locationManager = new FollowMeMusicLocationManager(connection);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return "FollowMeMusic Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		logger.debug(itemName+" = "+command.toString());
		
		if(this.locationManager != null) {
			// Find item's binding config
			for(FollowMeMusicBindingProvider provider : providers) {
				if(provider.providesBindingFor(itemName)) {
					
					// Get config
					FollowMeMusicBindingConfig config = provider.getConfig(itemName);
					if(config.getItemType() == FollowMeMusicItemType.SENSOR) {
						try {
							logger.info("Handling update request ...");
							this.locationManager.handleUpdateLocationRequest(config, command);
						} catch (ObjectNotFoundException | CommandFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						logger.error("Item isn't a sensor");
					}
				}
			}
		}
		else {
			logger.error("No location manager !!!");
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
