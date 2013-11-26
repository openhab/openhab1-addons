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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
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
    
    // list of consumers (grouped by broker)
    private Map<String, List<MqttitudeConsumer>> consumers = new HashMap<String, List<MqttitudeConsumer>>();
    
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
		super.activate();
		registerConsumers();
	}

	/**
	 * Shut down the binding service.
	 */
	@Override
	public void deactivate() {	
		logger.debug("Deactivating Mqtt binding");
		super.deactivate();
		unregisterConsumers();
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

		// home lat/lon is mandatory
		float homeLat = Float.parseFloat(getMandatoryProperty(properties, "home.lat"));
		float homeLon = Float.parseFloat(getMandatoryProperty(properties, "home.lon"));
        
		if (homeLat == 0)
            throw new ConfigurationException("mqttitude:home.lat", "No latitude specified for 'home'");
        if (homeLon == 0)
            throw new ConfigurationException("mqttitude:home.lon", "No longitude specified for 'home'");
        
        homeLocation = new Location(homeLat, homeLon);
		
        // geofence is optional - default to 100m
        geoFence = Float.parseFloat(getOptionalProperty(properties, "geofence", "100"));
        
		logger.debug("Configuration updated for Mqttitude binding.");

		unregisterConsumers();
		registerConsumers();
	}
	
	private String getMandatoryProperty(Dictionary<String, ?> properties, String name) throws ConfigurationException {
		String value = (String) properties.get(name);
		
		if (StringUtils.isBlank(value))
			throw new ConfigurationException("mqttitude:" + name, "Missing or invalid property '" + name + "'");

		return value.trim();
	}
	
	private String getOptionalProperty(Dictionary<String, ?> properties, String name, String defaultValue) {
		String value = (String) properties.get(name);
		
		if (StringUtils.isBlank(value))
			return defaultValue;

		return value.trim();
	}
	
	private boolean isConfigured() {
		return homeLocation != null;
	}
	
	private List<MqttitudeConsumer> getConsumersForBroker(String broker) {
		if (!consumers.containsKey(broker))
			return new ArrayList<MqttitudeConsumer>();
		return new ArrayList<MqttitudeConsumer>(consumers.get(broker));
	}
	    
	private void registerConsumers() {
		for (BindingProvider provider : providers) {
			if (provider instanceof MqttitudeBindingProvider) {
				MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;	
				for (String itemName : mqttitudeProvider.getItemNames()) {
					registerConsumer(mqttitudeProvider.getItemConfig(itemName));
				}
			}
		}		
	}
	
	private void unregisterConsumers() {
		for (String broker : consumers.keySet()) {
			for (MqttitudeConsumer consumer : getConsumersForBroker(broker)) {
				unregisterConsumer(broker, consumer);			
			}
		}		
	}

	private void registerConsumer(MqttitudeItemConfig itemConfig) {	
		if (!isConfigured() || itemConfig == null)
			return;
		
		String itemName = itemConfig.getItemName();
		String broker = itemConfig.getBroker();
		String topic = itemConfig.getTopic();

		// if we already have a consumer for this item then un-register first
		for (MqttitudeConsumer consumer : getConsumersForBroker(broker)) {
			if (consumer.getItemName().equals(itemName))
				unregisterConsumer(broker, consumer);
		}
		
		MqttitudeConsumer consumer = new MqttitudeConsumer(itemName, homeLocation, geoFence);
		consumer.setTopic(topic);

		logger.debug("Registering Mqttitude consumer for " + topic);
		mqttService.registerMessageConsumer(broker, consumer);
		
		if (!consumers.containsKey(broker)) 
			consumers.put(broker, new ArrayList<MqttitudeConsumer>());
		
		consumers.get(broker).add(consumer);
	}	

	private void unregisterConsumer(String broker, MqttitudeConsumer consumer) {
		logger.debug("Unregistering Mqttitude consumer for " + consumer.getTopic());
		mqttService.unregisterMessageConsumer(broker, consumer);

		if (consumers.containsKey(broker))
			consumers.get(broker).remove(consumer);
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
