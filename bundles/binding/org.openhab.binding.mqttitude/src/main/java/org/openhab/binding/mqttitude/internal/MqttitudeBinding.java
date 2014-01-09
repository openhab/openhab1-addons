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
    private Location homeLocation;
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
			registerRegion(mqttitudeProvider.getItemConfig(itemName));
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
				registerRegion(mqttitudeProvider.getItemConfig(itemName));
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
		// home lat/lon is optional since we can bind to the enter/leave region events from the Mqttitude app
		float homeLat = Float.parseFloat(getOptionalProperty(properties, "home.lat", "0"));
		float homeLon = Float.parseFloat(getOptionalProperty(properties, "home.lon", "0"));
        
		if (homeLat == 0 || homeLon == 0) {
			homeLocation = null;
			geoFence = 0;
			logger.trace("Home location not configured. All item bindings must be configured with a <region>.");
        } else {        
			homeLocation = new Location(homeLat, homeLon);
	        geoFence = Float.parseFloat(getOptionalProperty(properties, "geofence", "100"));
			logger.trace("Home location configured at " + homeLocation.toString());
        }
        
		logger.debug("Configuration updated for Mqttitude binding.");
		
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
	
	private List<MqttitudeConsumer> getConsumersForBroker(String broker) {
		if (!consumers.containsKey(broker))
			return new ArrayList<MqttitudeConsumer>();
		return new ArrayList<MqttitudeConsumer>(consumers.get(broker));
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
	    
	private void registerAll() {
		for (BindingProvider provider : providers) {
			if (provider instanceof MqttitudeBindingProvider) {
				MqttitudeBindingProvider mqttitudeProvider = (MqttitudeBindingProvider) provider;	
				for (String itemName : mqttitudeProvider.getItemNames()) {
					registerRegion(mqttitudeProvider.getItemConfig(itemName));
				}
			}
		}		
	}
	
	private void unregisterAll() {
		for (String broker : consumers.keySet()) {
			for (MqttitudeConsumer consumer : getConsumersForBroker(broker)) {
				logger.debug("Unregistering Mqttitude consumer for " + consumer.getTopic());
				mqttService.unregisterMessageConsumer(broker, consumer);

				if (consumers.containsKey(broker))
					consumers.get(broker).remove(consumer);
			}
		}		
	}

	private void registerRegion(MqttitudeItemConfig itemConfig) {	
		if (itemConfig == null)
			return;
		
		String itemName = itemConfig.getItemName();
		String broker = itemConfig.getBroker();
		String topic = itemConfig.getTopic();
		String region = itemConfig.getRegion();

		// get the consumer for this broker/topic (might not exist)
		MqttitudeConsumer consumer = getConsumer(broker, topic);
		
		if (consumer == null) {
			// create a new consumer for this topic
			consumer = new MqttitudeConsumer(homeLocation, geoFence);
			consumer.setTopic(topic);
			
			// register the new consumer
			logger.debug("Registering Mqttitude consumer for " + topic);
			mqttService.registerMessageConsumer(broker, consumer);
			
			if (!consumers.containsKey(broker)) 
				consumers.put(broker, new ArrayList<MqttitudeConsumer>());
			
			consumers.get(broker).add(consumer);
		} 
		
		// remove this region if it has already been loaded
		for (MqttitudeRegion mqttitudeRegion : consumer.getRegions()) {
			if (mqttitudeRegion.getItemName().equals(itemName)) {
				consumer.removeRegion(mqttitudeRegion);
			}
		}

		// add this region to our consumer
		consumer.addRegion(new MqttitudeRegion(itemName, region));
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
