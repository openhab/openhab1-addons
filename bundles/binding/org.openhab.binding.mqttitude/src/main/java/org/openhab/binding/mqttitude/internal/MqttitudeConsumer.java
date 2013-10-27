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
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.transport.mqtt.MqttMessageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An MQTT consumer which listens for Mqttitude location publishes, calculates
 * the distance from 'home', and updates the openHAB event bus if this distance
 * is inside/outside the specified 'geofence'.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeConsumer implements MqttMessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MqttitudeConsumer.class);
	
	private EventPublisher eventPublisher;
	private String topic;
	
	private final String itemName;
	private final Location homeLocation;
	private final float geoFence;
		
	public MqttitudeConsumer(String itemName, Location homeLocation, float geoFence) {		
		this.itemName = itemName;
		this.homeLocation = homeLocation;
		this.geoFence = geoFence;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	/**
	 * @{inheritDoc}
	 */	
	@Override
	public void processMessage(String topic, byte[] payload) {
		Location location = parseLocation(payload);
		if (location == null)
			return;

        logger.debug("Location received for '{}': {}", itemName, location.toString());
		double distance = calculateDistance(location, homeLocation);
        logger.debug("Distance from 'home' calculated as {} for '{}'", distance, itemName);
		
		if (distance > geoFence) {
            logger.debug("Detected that '{}' is outside the geofence ({}m)", itemName, geoFence);
			eventPublisher.postUpdate(itemName, OnOffType.OFF);
		} else {
            logger.debug("Detected that '{}' is inside the geofence ({}m)", itemName, geoFence);
			eventPublisher.postUpdate(itemName, OnOffType.ON);
		}		
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

	@SuppressWarnings("unchecked")
	private Location parseLocation(byte[] payload) {
		// convert the response to a string
		String decoded = new String(payload); 

		// parse the response to build our location object
		ObjectMapper jsonReader = new ObjectMapper();
		Map<String, String> locationData;
		try {
			locationData = jsonReader.readValue(decoded, Map.class);
		} catch (JsonParseException e) {
			logger.error("Error parsing JSON:\n" + decoded);
			return null;
		} catch (JsonMappingException e) {
			logger.error("Error mapping JSON:\n" + decoded);
			return null;
		} catch (IOException e) {
			logger.error("An I/O error occured while decoding JSON:\n" + decoded);
			return null;
		}

	    float latitude = Float.parseFloat(locationData.get("lat"));
	    float longitude = Float.parseFloat(locationData.get("lon"));
	    return new Location(latitude, longitude);
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
