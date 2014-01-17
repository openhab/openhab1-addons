/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * and listen for location updates (to a specified topic).
 * 
 * There are two types of binding, one just listens for any location updates and 
 * calculates the distance relative to 'home' (specified in binding config).
 * The other listens for enter/leave events published by the Mqttitude app for a 
 * specific region meaning we can detect presence in any number of areas.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeBinding extends AbstractBinding<MqttitudeBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MqttitudeBinding.class);
		
	private MqttService mqttService;
	
    // optional home location and geofence (only used if no 'regions' defined in the Mqttitude app) 
    private Location homeLocation = null;
    private float geoFence = 0;
    
    // list of consumers (grouped by broker)
    private Map<String, List<MqttitudeConsumer>> consumers = new HashMap<String, List<MqttitudeConsumer>>();
    
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof MqttitudeBindingProvider) {
			MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;
			registerItemConfig(mqttitudeProvider.getItemConfig(itemName));
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
				registerItemConfig(mqttitudeProvider.getItemConfig(itemName));
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
		registerAll();
	}

	/**
	 * Shut down the binding service.
	 */
	@Override
	public void deactivate() {	
		logger.debug("Deactivating Mqtt binding");
		super.deactivate();
		unregisterAll();
	}
    
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		// no mandatory binding properties - so fine to get nothing here
		if (properties == null || properties.size() == 0)
			return;
		
		float homeLat = Float.parseFloat(getOptionalProperty(properties, "home.lat", "0"));
		float homeLon = Float.parseFloat(getOptionalProperty(properties, "home.lon", "0"));
        
		if (homeLat == 0 || homeLon == 0) {
			homeLocation = null;
			geoFence = 0;
			logger.debug("Mqttitude binding configuration updated, no 'home' location specified. All item bindings must be configured with a <region>.");
        } else {        
			homeLocation = new Location(homeLat, homeLon);
	        geoFence = Float.parseFloat(getOptionalProperty(properties, "geofence", "100"));
			logger.debug("Mqttitude binding configuration updated, 'home' location specified ({}) with a geofence of {}m.", homeLocation.toString(), geoFence);
        }
		
		// need to re-register all the consumers/topics if the home location has changed
		unregisterAll();
		registerAll();
	}
	
	private String getOptionalProperty(Dictionary<String, ?> properties, String name, String defaultValue) {
		if (properties == null)
			return defaultValue;
		
		String value = (String) properties.get(name);
		
		if (StringUtils.isBlank(value))
			return defaultValue;

		return value.trim();
	}
	
	private void registerAll() {
		for (BindingProvider provider : providers) {
			if (provider instanceof MqttitudeBindingProvider) {
				MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;	
				for (String itemName : mqttitudeProvider.getItemNames()) {
					registerItemConfig(mqttitudeProvider.getItemConfig(itemName));
				}
			}
		}		
	}
	
	private void unregisterAll() {
		for (String broker : consumers.keySet()) {
			for (MqttitudeConsumer consumer : consumers.get(broker)) {
				logger.debug("Unregistering Mqttitude consumer for {} (on {})", consumer.getTopic(), broker);
				mqttService.unregisterMessageConsumer(broker, consumer);
			}
		}
		consumers.clear();
	}

	private void registerItemConfig(MqttitudeItemConfig itemConfig) {	
		if (itemConfig == null)
			return;
		
		String broker = itemConfig.getBroker();
		String topic = itemConfig.getTopic();

		// get the consumer for this broker/topic (might not exist)
		MqttitudeConsumer consumer = getConsumer(broker, topic);
		
		// NOTE: we only create a single consumer for each topic, but a topic may
		// 		 have multiple item bindings - i.e. monitoring multiple regions
		if (consumer == null) {
			// create a new consumer for this topic
			consumer = new MqttitudeConsumer(homeLocation, geoFence);
			consumer.setTopic(topic);
			
			// register the new consumer
			logger.debug("Registering Mqttitude consumer for {} (on {})", topic, broker );
			mqttService.registerMessageConsumer(broker, consumer);
			
			if (!consumers.containsKey(broker)) 
				consumers.put(broker, new ArrayList<MqttitudeConsumer>());
			
			consumers.get(broker).add(consumer);
		} 
		
		// add this item to our consumer (will replace any existing config for the same item name)
		consumer.addItemConfig(itemConfig);
	}	

	private MqttitudeConsumer getConsumer(String broker, String topic) {
		if (consumers.containsKey(broker)) {
			for (MqttitudeConsumer consumer : consumers.get(broker)) {
				if (consumer.getTopic().equals(topic))
					return consumer;
			}
		}
		return null;
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
