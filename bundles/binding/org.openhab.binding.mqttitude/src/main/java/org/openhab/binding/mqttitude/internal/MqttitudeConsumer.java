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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.transport.mqtt.MqttMessageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An MQTT consumer which subscribes to an MQTT topic and listens for Mqttitude 
 * location publishes. 
 * 
 * Depending on the item binding configuration it will either manually calculate the 
 * distance from 'home', or wait for enter/leave events sent from the mobile apps.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeConsumer implements MqttMessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MqttitudeConsumer.class);
	
	// home location - optionally set for the binding if using non-region based item bindings
	private final Location homeLocation;
	private final float geoFence;
	
	// the topic this consumer is subscribed to
	private String topic;
	
	// the list of items we are monitoring on this topic 
	private Map<String, MqttitudeItemConfig> itemConfigs = new HashMap<String, MqttitudeItemConfig>();
		
	private EventPublisher eventPublisher;
	
	public MqttitudeConsumer(Location homeLocation, float geoFence) {		
		this.homeLocation = homeLocation;
		this.geoFence = geoFence;
	}

	public void addItemConfig(MqttitudeItemConfig itemConfig) {
		// check this item config is for this topic
		if (!itemConfig.getTopic().equals(topic)) {
			logger.error("Attempting to add an item config with topic '{}', to a consumer with topic '{}'", itemConfig.getTopic(), topic);
			return;
		}
		
		// update our internal map
		itemConfigs.put(itemConfig.getItemName(), itemConfig);
	}
	
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public String getTopic() {
		return topic;
	}

	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void processMessage(String topic, byte[] payload) {
		// convert the response to a string
		String decoded = new String(payload); 
		logger.trace("Message received on topic {}: {}", topic, decoded);
		
		// read the payload into a JSON param/value map
		Map<String, String> jsonPayload = readJsonPayload(decoded);
		if (jsonPayload == null)
			return;

		// only interested in 'location' publishes
		String type = jsonPayload.get("_type");
		if (StringUtils.isEmpty(type) || !type.equals("location"))
			return;

		// sanity check
		if (itemConfigs.size() == 0) {
			logger.trace("No items configured for this topic, ignoring");
			return;
		}

		// process all items being monitored on this topic
		for (MqttitudeItemConfig itemConfig : itemConfigs.values()) {
			logger.trace("Checking item {}...", itemConfig.getItemName());
			
			// if no region specified then we must be manually calculating distance from 'home'
			if (StringUtils.isEmpty(itemConfig.getRegion())) {
				// we must have a home location configured for the binding
				if (homeLocation == null) {
					logger.error("Unable to calculate relative location for {} as there is no lat/lon configured for 'home'", itemConfig.getItemName());
					continue;
				}
				
				// parse the published location
			    float latitude = Float.parseFloat(jsonPayload.get("lat"));
			    float longitude = Float.parseFloat(jsonPayload.get("lon"));
			    Location location = new Location(latitude, longitude);
		        logger.trace("Location received for {}: {}", itemConfig.getItemName(), location.toString());
				
			    // calculate the distance from 'home'
				double distance = calculateDistance(location, homeLocation);
				
		        // update the item state based on the location relative to the geofence
				if (distance > geoFence) {
		            logger.debug("{} is outside the 'home' geofence ({}m)", itemConfig.getItemName(), distance);
					eventPublisher.postUpdate(itemConfig.getItemName(), OnOffType.OFF);
				} else {
		            logger.debug("{} is inside the 'home' geofence ({}m)", itemConfig.getItemName(), distance);
					eventPublisher.postUpdate(itemConfig.getItemName(), OnOffType.ON);
				}			    
			} else {
				// we are only interested in location updates with an 'event' (i.e. enter/leave)
				String event = jsonPayload.get("event");
				if (StringUtils.isEmpty(event)) {
			        logger.trace("Not a location enter/leave event, ignoring");
					continue;
				}
				
				// check this event is for the region we are monitoring
				String desc = jsonPayload.get("desc");
				if (StringUtils.isEmpty(desc)) {
			        logger.trace("Location {} event has no region (missing or empty 'desc'), ignoring", event);
					continue;
				}
				if (!itemConfig.getRegion().equals(desc)) {
			        logger.trace("Location {} event is for region '{}', ignoring", event, desc);
					continue;
				}

				if (event.equals("leave")) {
		            logger.debug("{} has left region {}", itemConfig.getItemName(), itemConfig.getRegion());
					eventPublisher.postUpdate(itemConfig.getItemName(), OnOffType.OFF);
				} else {
		            logger.debug("{} has entered region {}", itemConfig.getItemName(), itemConfig.getRegion());
					eventPublisher.postUpdate(itemConfig.getItemName(), OnOffType.ON);
				}		
			}			
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> readJsonPayload(String payload) {
		// parse the response to build our location object
		ObjectMapper jsonReader = new ObjectMapper();
		try {
			return jsonReader.readValue(payload, Map.class);
		} catch (JsonParseException e) {
			logger.error("Error parsing JSON:\n" + payload);
			return null;
		} catch (JsonMappingException e) {
			logger.error("Error mapping JSON:\n" + payload);
			return null;
		} catch (IOException e) {
			logger.error("An I/O error occured while decoding JSON:\n" + payload);
			return null;
		}
	}

	private double calculateDistance(Location location1, Location location2) {
        float lat1 = location1.getLatitude();
        float lng1 = location1.getLongitude();
        float lat2 = location2.getLatitude();
        float lng2 = location2.getLongitude();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                        * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double earthRadiusKm = 6369;
        double distKm = earthRadiusKm * c;

        // return the distance in meters
        return distKm * 1000;
    }	
}
