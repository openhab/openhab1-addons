/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openpaths.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.scribe.builder.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.openpaths.OpenPathsBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for OpenPaths location detection. 
 * 
 * When signing up to the OpenPaths service (at https://openpaths.cc/) 
 * you will be issued with an ACCESS_KEY and SECRET_KEY. Using these
 * keys to configure this binding you can periodically get openHAB
 * to check the location of one or more users, and check against a 
 * predefined 'home' location to see if a user is inside the 'geofence'. 
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class OpenPathsBinding extends AbstractActiveBinding<OpenPathsBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(OpenPathsBinding.class);
		
    // default refresh interval (defaults to 5 minutes)
    private long refreshInterval = 300000L;

    // default geo fence distance (defaults to 100m)
    private float geoFence = 100;
    
    // home location
    private Location homeLocation;
    
    // list of OpenPaths users
    private Map<String, OpenPathsUser> openPathsUsers;
    
	@Override
	protected String getName() {
		return "OpenPaths Refresh Service";
	}
	
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing OpenPaths binding configuration => refresh cycle aborted!");
			return;
		}
		
		for (OpenPathsBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				OpenPathsBindingConfig bindingConfig = provider.getItemConfig(itemName);				
				String name = bindingConfig.getName();
				
				if (!openPathsUsers.containsKey(name)) {
					logger.warn("There is no OpenPaths user configured for '" + name + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
					continue;
				}

				OpenPathsUser openPathsUser = openPathsUsers.get(name);
				String accessKey = openPathsUser.getAccessKey();
				String secretKey = openPathsUser.getSecretKey();
				
				if (StringUtils.isEmpty(accessKey)) {
					logger.warn("There is no ACCESS_KEY configured for '" + name + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
					continue;
				}
				if (StringUtils.isEmpty(secretKey)) {
					logger.warn("There is no SECRET_KEY configured for '" + name + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
					continue;
				}
				
				logger.debug("Requesting location for '{}'...", name);
				Location location = getUserLocation(accessKey, secretKey);
                if (location == null) {
                    logger.warn("Unable to determine location for '{}'. Skipping.", name);
                    continue;
                }
                logger.debug("Location received for '{}': {}", name, location.toString());
				
                logger.debug("Calculating distance between home ({}) and user location ({}) for '{}'...", new Object[] { homeLocation.toString(), location.toString(), name });                
                double distance = calculateDistance(homeLocation, location);
                logger.debug("Distance calculated as {} for '{}'", distance, name);

                if (distance <= geoFence) {
                    logger.debug("Detected that '{}' is inside the geofence ({}m)", name, geoFence);
                    eventPublisher.postUpdate(itemName, OnOffType.ON);
                } else {
                    logger.debug("Detected that '{}' is outside the geofence ({}m)", name, geoFence);
                    eventPublisher.postUpdate(itemName, OnOffType.OFF);
                }
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Location getUserLocation(String accessKey, String secretKey) {
		// build the OAuth service using the access/secret keys
	    OAuthService service = new ServiceBuilder()
        	.provider(new OpenPathsApi())
        	.apiKey(accessKey)
        	.apiSecret(secretKey)
        	.build();

	    // build the request
	    OAuthRequest request = new OAuthRequest(Verb.GET, "https://openpaths.cc/api/1");
	    service.signRequest(Token.empty(), request);
	    request.addQuerystringParameter("num_points", "1");	    
	    
	    // send the request and check we got a successful response
	    Response response = request.send();
	    if (!response.isSuccessful()) {
	    	logger.error("Failed to request the OpenPaths location: " + response.getCode());
	    	return null;
	    }
	    
        // parse the response to build our location object
        Map<String, Object> locationData;
        String toParse = "{}";
        try {
            ObjectMapper jsonReader = new ObjectMapper();
            toParse = response.getBody();
            toParse = toParse.substring(1, toParse.length() - 2);
        	locationData = jsonReader.readValue(toParse, Map.class);
        } catch (JsonParseException e) {
            logger.error("Error parsing JSON:\n" + toParse, e);
            return null;
        } catch (JsonMappingException e) {
            logger.error("Error mapping JSON:\n" + toParse, e);
            return null;
        } catch (IOException e) {
            logger.error("An I/O error occured while decoding JSON:\n" + response.getBody());
            return null;
        }
	    
	    float latitude = Float.parseFloat(locationData.get("lat").toString());
	    float longitude = Float.parseFloat(locationData.get("lon").toString());
	    String device = locationData.get("device").toString();
	    return new Location(latitude, longitude, device);
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
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		openPathsUsers = new HashMap<String, OpenPathsUser>();
		if (config != null) {
            // mandatory configs
            float homeLat = 0;
            float homeLong = 0;

            Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) config.get(key);

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}
				
				if ("home.lat".equals(key)) {
		            if (StringUtils.isNotBlank(value)) {
		                homeLat = Float.parseFloat(value);
		            }
				} else if ("home.long".equals(key)) {
		            if (StringUtils.isNotBlank(value)) {
		                homeLong = Float.parseFloat(value);
		            }
				} else if ("refresh".equals(key)) {
		            if (StringUtils.isNotBlank(value)) {
		                refreshInterval = Long.parseLong(value);
		            }
				} else if ("geofence".equals(key)) {
		            if (StringUtils.isNotBlank(value)) {
		                geoFence = Float.parseFloat(value);
		            }
				} else {
					String[] keyParts = key.split("\\.");
					if (keyParts.length != 2) {
						throw new ConfigurationException(key, "Invalid OpenPaths user key: " + key);
					}

					String name = keyParts[0];
					String configKey = keyParts[1];

					if (!openPathsUsers.containsKey(name)) {
						openPathsUsers.put(name, new OpenPathsUser(name));
					}

					OpenPathsUser openPathsUser = openPathsUsers.get(name);
					
					if (configKey.equalsIgnoreCase("accesskey")) {
						openPathsUser.setAccessKey(value);
					} else if (configKey.equalsIgnoreCase("secretkey")) {
						openPathsUser.setSecretKey(value);
					} else {
						throw new ConfigurationException(key, "Unrecognised configuration parameter: " + configKey);
					}
				}
			}

            if (homeLat == 0)
                throw new ConfigurationException("home.lat", "No latitude specified for 'home'");
            if (homeLong == 0)
                throw new ConfigurationException("home.long", "No longitude specified for 'home'");

            homeLocation = new Location(homeLat, homeLong, "home");
            
			setProperlyConfigured(true);            
        }
	}
		
	class OpenPathsUser {
		private final String name;
		private String accessKey;
		private String secretKey;
		
		public OpenPathsUser(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public String getAccessKey() {
			return accessKey;
		}
		
		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}
		
		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}
	}
	
	class Location {
		private final float latitude;
		private final float longitude;
		private final String device;
		
		public Location(float latitude, float longitude, String device) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.device = device;
		}
		
		public float getLatitude() {
			return latitude;
		}
		
		public float getLongitude() {
			return longitude;
		}
		
		public String getDevice() {
			return device;
		}
		
		@Override
		public String toString() {
			return "Lat: " + latitude + ", Long: " + longitude + " (from " + device + ")";
		}
	}
}
