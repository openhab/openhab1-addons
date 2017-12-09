/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openpaths.internal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.openpaths.OpenPathsBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for OpenPaths location detection.
 *
 * When signing up to the OpenPaths service (at https://openpaths.cc/) you will
 * be issued with an ACCESS_KEY and SECRET_KEY. Using these keys to configure
 * this binding you can periodically get openHAB to check the location of one or
 * more users, and check against a predefined 'home' location to see if a user
 * is inside the 'geofence'.
 *
 * @author Ben Jones, Robert Bausdorf
 * @since 1.4.0
 */
public class OpenPathsBinding extends AbstractActiveBinding<OpenPathsBindingProvider>implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(OpenPathsBinding.class);

    // default refresh interval (defaults to 5 minutes)
    private long refreshInterval = 300000L;

    // default fallback geo fence distance (defaults to 100m)
    private float geoFence = 100;

    // list of configured locations in openhab.conf
    private Map<String, Location> locations;

    // list of OpenPaths openpaths users configured in openhab.conf
    private Map<String, OpenPathsUser> openPathsUsers;

    @Override
    protected String getName() {
        return "OpenPaths Refresh Service";
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    public Map<String, OpenPathsBinding.Location> getLocations() {
        return this.locations;
    }

    public Map<String, OpenPathsBinding.OpenPathsUser> getUsers() {
        return this.openPathsUsers;
    }

    enum LocationBindingType {
        on,
        distance
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void execute() {
        if (!bindingsExist()) {
            logger.debug("There is no existing OpenPaths binding configuration => refresh cycle aborted!");
            return;
        }

        for (OpenPathsBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                logger.trace("try binding provider item: " + itemName);
                OpenPathsBindingConfig bindingConfig = provider.getItemConfig(itemName);
                String bindingConfigName = bindingConfig.getName();
                String[] bindingParts = bindingConfigName.split("\\:");
                if (bindingParts.length < 1) {
                    logger.error("Empty OpenPaths binding config");
                    continue;
                }
                String name = bindingParts[0];
                if (!openPathsUsers.containsKey(name)) {
                    logger.warn("There is no OpenPaths user configured for '" + name
                            + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
                    continue;
                }

                Location location = null;
                OpenPathsUser openPathsUser = openPathsUsers.get(name);
                if (openPathsUser.lastUpdateTS + this.refreshInterval < System.currentTimeMillis()) {
                    String accessKey = openPathsUser.getAccessKey();
                    String secretKey = openPathsUser.getSecretKey();

                    if (StringUtils.isEmpty(accessKey)) {
                        logger.warn("There is no ACCESS_KEY configured for '" + name
                                + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
                        continue;
                    }
                    if (StringUtils.isEmpty(secretKey)) {
                        logger.warn("There is no SECRET_KEY configured for '" + name
                                + "'. Please add this user to the binding configuration, including both the ACCESS_KEY and SECRET_KEY from the OpenPaths profile.");
                        continue;
                    }

                    logger.debug("Requesting location for '{}'...", name);
                    location = getUserLocation(accessKey, secretKey);
                    if (location != null) {
                        openPathsUsers.get(name).setLastLocation(location);
                        logger.debug("New location received for '{}': {}", name, location.toString());
                    } else {
                        logger.warn("Unable to determine location for '{}'. Skipping.", name);
                        continue;
                    }
                } else {
                    location = openPathsUsers.get(name).getLastLocation();
                    logger.trace("Using cached location for '{}'", openPathsUser.toString());
                }

                String bindingLocationName = bindingParts.length > 1 ? bindingParts[1] : "";
                if (bindingLocationName.startsWith("current")) {
                    if (bindingLocationName.equals("currentLocation")) {
                        eventPublisher.postUpdate(itemName,
                                new StringType("" + location.getLatitude() + ", " + location.getLongitude()));
                    } else if (bindingLocationName.equals("currentLatitude")) {
                        eventPublisher.postUpdate(itemName, new DecimalType(new BigDecimal(location.getLatitude())));
                    } else if (bindingLocationName.equals("currentLongitude")) {
                        eventPublisher.postUpdate(itemName, new DecimalType(new BigDecimal(location.getLongitude())));
                    } else {
                        logger.warn("unsupported Binding: " + bindingLocationName);
                    }
                    continue;
                }

                if (!locations.containsKey(bindingLocationName)) {
                    logger.warn("location name " + bindingLocationName + " not configured, falling back to 'home'");
                    bindingLocationName = "home";
                }
                logger.debug("OpenPathsUser: " + name + "@" + bindingLocationName);

                LocationBindingType bindingType = LocationBindingType.on;
                if (bindingParts.length == 3) {
                    if (bindingParts[2].equals("distance")) {
                        bindingType = LocationBindingType.distance;
                    }
                }

                Location bindingLocation = locations.get(bindingLocationName);
                logger.trace("Calculating distance between home ({}) and user location ({}) for '{}'...",
                        new Object[] { bindingLocation.toString(), location.toString(), name });
                double distance = calculateDistance(bindingLocation, location);
                bindingLocation.setDistance(distance);
                logger.trace("Distance calculated as {} for '{}'@'{}'", distance, name, bindingLocationName);

                if (bindingType.equals(LocationBindingType.on)) {
                    float fence = bindingLocation.getGeofence() == 0.0 ? geoFence : bindingLocation.getGeofence();
                    if (distance <= fence) {
                        logger.trace("Detected that '{}'@'{}' is inside the geofence ({}m)", name, bindingLocationName,
                                fence);
                        eventPublisher.postUpdate(itemName, OnOffType.ON);
                    } else {
                        logger.trace("Detected that '{}'@'{}' is outside the geofence ({}m)", name, bindingLocationName,
                                fence);
                        eventPublisher.postUpdate(itemName, OnOffType.OFF);
                    }
                } else if (bindingType.equals(LocationBindingType.distance)) {
                    eventPublisher.postUpdate(itemName, new DecimalType(new BigDecimal(distance / 1000)));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Location getUserLocation(String accessKey, String secretKey) {
        // build the OAuth service using the access/secret keys
        OAuthService service = new ServiceBuilder().provider(new OpenPathsApi()).apiKey(accessKey).apiSecret(secretKey)
                .build();

        // build the request
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://openpaths.cc/api/1");
        service.signRequest(Token.empty(), request);
        request.addQuerystringParameter("num_points", "1");

        // send the request and check we got a successful response
        Response response = request.send();
        if (!response.isSuccessful()) {
            logger.error("Failed to request the OpenPaths location, response code: " + response.getCode());
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
            logger.error("An I/O error occurred while decoding JSON:\n" + response.getBody());
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
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double earthRadiusKm = 6369;
        double distKm = earthRadiusKm * c;

        // return the distance in meters
        return distKm * 1000;
    }

    protected void addBindingProvider(OpenPathsBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(OpenPathsBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        openPathsUsers = new HashMap<String, OpenPathsUser>();
        locations = new HashMap<String, Location>();
        if (config != null) {
            Enumeration<String> keys = config.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = (String) config.get(key);

                // the config-key enumeration contains additional keys that we
                // don't want to process here ...
                if ("service.pid".equals(key)) {
                    continue;
                }

                if ("refresh".equals(key)) {
                    if (StringUtils.isNotBlank(value)) {
                        refreshInterval = Long.parseLong(value);
                        logger.trace("Config: refresh=" + this.refreshInterval);
                    }
                } else if ("geofence".equals(key)) {
                    // only for backward compatibility / as fallback
                    if (StringUtils.isNotBlank(value)) {
                        geoFence = Float.parseFloat(value);
                        logger.trace("Config: geofence=" + this.geoFence);
                    }
                } else if (key.endsWith("lat")) {
                    String[] keyParts = key.split("\\.");
                    if (keyParts.length != 2) {
                        throw new ConfigurationException(key, "Invalid OpenPaths user location lattitude: " + key);
                    }
                    if (StringUtils.isNotBlank(value)) {
                        float lat = Float.parseFloat(value);

                        String name = keyParts[0];

                        if (locations.containsKey(name)) {
                            locations.get(name).setLatitude(lat);
                            logger.trace("Config: new Location " + name + "(" + locations.get(name) + ")");
                        } else {
                            Location loc = new Location();
                            loc.setLatitude(lat);
                            logger.trace("Config: update Location " + name + "(" + locations.get(name) + ")");
                            locations.put(name, loc);
                        }
                    }
                } else if (key.endsWith("long")) {
                    String[] keyParts = key.split("\\.");
                    if (keyParts.length != 2) {
                        throw new ConfigurationException(key, "Invalid OpenPaths user location longitude: " + key);
                    }
                    if (StringUtils.isNotBlank(value)) {
                        float lon = Float.parseFloat(value);

                        String name = keyParts[0];

                        if (locations.containsKey(name)) {
                            locations.get(name).setLongitude(lon);
                            logger.trace("Config: new Location " + name + "(" + locations.get(name) + ")");
                        } else {
                            Location loc = new Location();
                            loc.setLongitude(lon);
                            logger.trace("Config: update Location " + name + "(" + locations.get(name) + ")");
                            locations.put(name, loc);
                        }
                    }
                } else if (key.endsWith("geofence") && !key.equals("geofence")) {
                    String[] keyParts = key.split("\\.");
                    if (keyParts.length != 2) {
                        throw new ConfigurationException(key, "Invalid OpenPaths user location geofence: " + key);
                    }
                    if (StringUtils.isNotBlank(value)) {
                        float fence = Float.parseFloat(value);

                        String name = keyParts[0];

                        if (locations.containsKey(name)) {
                            locations.get(name).setGeofence(fence);
                            logger.trace("Config: new Location " + name + "(" + locations.get(name) + ")");
                        } else {
                            Location loc = new Location();
                            loc.setGeofence(fence);
                            logger.trace("Config: update Location " + name + "(" + locations.get(name) + ")");
                            locations.put(name, loc);
                        }
                    }
                } else if (key.endsWith("key")) {
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

            if (!locations.containsKey("home")) {
                throw new ConfigurationException("home.lat", "No location specified for 'home'");
            }

            setProperlyConfigured(true);
        }
    }

    public class OpenPathsUser {
        private final String name;
        private String accessKey;
        private String secretKey;
        private Location lastLocation = null;
        private long lastUpdateTS = 0;

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

        public Location getLastLocation() {
            return lastLocation;
        }

        public void setLastLocation(Location lastLocation) {
            this.lastLocation = lastLocation;
            this.lastUpdateTS = System.currentTimeMillis();
        }

        public long getLastUpdateTS() {
            return lastUpdateTS;
        }

        @Override
        public String toString() {
            StringBuilder out = new StringBuilder();
            out.append(name).append(", last updated ");
            out.append(new Date(this.lastUpdateTS).toString());
            out.append(": ").append(this.lastLocation.toString());

            return out.toString();
        }
    }

    public class Location {
        private float latitude;
        private float longitude;
        private float geofence;
        private String device;
        private double distance;

        public Location(float latitude, float longitude, String device) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.device = device != null ? device : "";
            this.geofence = 0.0f;
            this.distance = Float.MAX_VALUE;
        }

        public Location() {
            this.latitude = 0.0f;
            this.longitude = 0.0f;
            this.device = "";
            this.geofence = 0.0f;
            this.distance = Float.MAX_VALUE;
        }

        public float getLatitude() {
            return latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public float getGeofence() {
            return geofence;
        }

        public String getDevice() {
            return device;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public void setGeofence(float geofence) {
            this.geofence = geofence;
        }

        public void setDevice(String device) {
            this.device = device != null ? device : "";
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            StringBuilder out = new StringBuilder();
            out.append(latitude).append(", ").append(longitude);
            if (geofence > 0) {
                out.append(", Fence: ").append(geofence);
            }
            if (!device.isEmpty()) {
                out.append("(from ").append(device).append(')');
            }
            return out.toString();
        }
    }
}
