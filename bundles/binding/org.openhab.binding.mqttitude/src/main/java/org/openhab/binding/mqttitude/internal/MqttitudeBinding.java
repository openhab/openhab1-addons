/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.mqttitude.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mqttitude.MqttitudeBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.io.transport.mqtt.MqttService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for Mqttitude location detection. 
 * 
 * Install the Mqttitude app on your phone and configure it to publish location
 * updates to a specified broker. This binding will subscribe to that broker
 * and listen for location updates (to a specified topic), calculate the distance
 * relative to 'home' and update a switch if the user is within a specified geofence.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeBinding extends AbstractBinding<MqttitudeBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MqttitudeBinding.class);
		
	private MqttService mqttService;
	
    // home location
    private Location homeLocation;

    // geo fence distance
    private float geoFence;
    
    // list of consumers
    private Map<String, MqttitudeConsumer> consumers = new HashMap<String, MqttitudeConsumer>();
    
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof MqttitudeBindingProvider) {
			MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;
			registerConsumer(mqttitudeProvider.getItemConfig(itemName));
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		if (provider instanceof MqttitudeBindingProvider) {
			MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;
			for (String itemName : mqttitudeProvider.getItemNames()) {
				registerConsumer(mqttitudeProvider.getItemConfig(itemName));
			}
		}
	}
	
	/**
	 * Start the binding service.
	 */
	@Override
	public void activate() {
		logger.debug("Activating Mqttitude binding");

		for (MqttitudeBindingProvider mqttitudeProvider : providers) {
			for (String itemName : mqttitudeProvider.getItemNames()) {
				registerConsumer(mqttitudeProvider.getItemConfig(itemName));
			}
		}
	}

	/**
	 * Shut down the binding service.
	 */
	@Override
	public void deactivate() {
		logger.debug("Deactivating Mqtt binding");
		
		for (MqttitudeBindingProvider mqttitudeProvider : providers) {
			for (String itemName : mqttitudeProvider.getItemNames()) {
				unregisterConsumer(mqttitudeProvider.getItemConfig(itemName));
			}
		}
	}
    
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties == null || properties.isEmpty()) {
			logger.trace("No config properties available.");
			return;
		}

		float homeLat = Float.parseFloat(getProperty(properties, "home.lat"));
		float homeLon = Float.parseFloat(getProperty(properties, "home.lon"));
        
		if (homeLat == 0)
            throw new ConfigurationException("home.lat", "No latitude specified for 'home'");
        if (homeLon == 0)
            throw new ConfigurationException("home.lon", "No longitude specified for 'home'");
        
        homeLocation = new Location(homeLat, homeLon);
		geoFence = Float.parseFloat(getProperty(properties, "geofence"));
        
		logger.debug("Configuration updated for Mqttitude binding.");

		deactivate();
        activate();
	}
	
	private String getProperty(Dictionary<String, ?> properties, String name) throws ConfigurationException {

		String value = (String) properties.get(name);
		if (StringUtils.isNotBlank(value)) {
			return value.trim();
		} else {
			throw new ConfigurationException("mqttitude:" + name, "Missing or invalid property '" + name + "'");
		}
	}
	
	private void registerConsumer(MqttitudeItemConfig itemConfig) {	
		if (itemConfig == null)
			return;
		
		unregisterConsumer(itemConfig);
	
		String itemName = itemConfig.getItemName();

		MqttitudeConsumer consumer = new MqttitudeConsumer(itemName, homeLocation, geoFence);
		consumer.setTopic(itemConfig.getTopic());

		logger.debug("Registering Mqttitude consumer for " + itemName);
		mqttService.registerMessageConsumer(itemConfig.getBroker(), consumer);
		
		consumers.put(itemName, consumer);
	}	

	private void unregisterConsumer(MqttitudeItemConfig itemConfig) {
		if (itemConfig == null)
			return;
		
		String itemName = itemConfig.getItemName();
		
		if (!consumers.containsKey(itemName))
			return;
		
		logger.debug("Unregistering Mqttitude consumer for " + itemName);
		mqttService.unregisterMessageConsumer(itemConfig.getBroker(), consumers.get(itemName));

		consumers.remove(itemName);
	}
	
	/**
	 * Setter for Declarative Services. Adds the MqttService instance.
	 * 
	 * @param mqttService to set.
	 */
	public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param mqttService to remove.
	 */
	public void unsetMqttService(MqttService mqttService) {
		this.mqttService = null;
	}
}


