package org.openhab.binding.mqttitude.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.transport.mqtt.MqttMessageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	private Location parseLocation(byte[] payload) {
		InputStream inputStream = new ByteArrayInputStream(payload);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
	    // parse the response to build our location object
	    try {
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject)parser.parse(reader);		    
		    float latitude = Float.parseFloat(obj.get("lat").toString());
		    float longitude = Float.parseFloat(obj.get("lon").toString());
		    return new Location(latitude, longitude);
	    } catch (IOException e) {
	    	logger.error("Failed to read the Mqttitude response: " + new String(payload), e);
	    	return null;
	    } catch (ParseException e) {
	    	logger.error("Failed to parse the Mqttitude response: " + new String(payload), e);
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
