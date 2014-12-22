/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.carddav.internal;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.exceptions.VCardParseException;
import net.sourceforge.cardme.vcard.types.AdrType;
import net.sourceforge.cardme.vcard.types.TelType;
import net.sourceforge.cardme.vcard.types.params.AdrParamType;
import net.sourceforge.cardme.vcard.types.params.TelParamType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
	

/**
 * This class registers an OSGi service for the Carddav action.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class CarddavActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(CarddavActionService.class);

	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = false;
	
	private static final VCardEngine vCardEngine;
	private static final int GEOFENCE = 1000;
	
	static {
		vCardEngine = new VCardEngine();
	}
	
	private static final String SERVICE_PID = "service.pid";
	
	
	private final Map<String, CarddavConfiguration> configMap = new HashMap<String, CarddavConfiguration>();
	
	private final List<Contact> contacts = new ArrayList<Contact>();
	private Timer timer = new Timer();
	
	public CarddavActionService() {
		
	}
	
	public void activate(ComponentContext componentContext) {
		logger.debug("Carddav action service activated");
	}
	
	public void deactivate(ComponentContext componentContext) {
		logger.debug("Carddav action service deactivated");
	}

	@Override
	public String getActionClassName() {
		return Carddav.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Carddav.class;
	}

	
	
	private static String normalize(String in) {
		in = in.replace(" ", "");
		in = in.replace("-", "");
		in = in.replace("/", "");
		if (in.startsWith("0")) {
			in = "+49" + in.substring(1);
		} else if (in.startsWith("+")) {
			// do nothing
		} else {
			in = "+4930" + in;
		}
		return in;
	}
	

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Updating config");
		if (config != null) {
			logger.trace("parsing configuration...");
			
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				
				logger.trace("handling key: {}", key);
				
				if (key.equals(SERVICE_PID)) {
					continue;
				}
				
				String[] split = StringUtils.split(key, '.');
				String addressbook = split[0];
				String valueKey = split[1];
				
				logger.trace("addressbook={}, valueKey={}", addressbook, valueKey);
				
				if (!configMap.containsKey(addressbook)) {
					configMap.put(addressbook, new CarddavConfiguration());
				}
				
				CarddavConfiguration carddavConfig = this.configMap.get(addressbook);
				
				if (valueKey.equals("address")) {
					carddavConfig.setAddress((String) config.get(key));
				} else if (valueKey.equals("username")) {
					carddavConfig.setUsername((String) config.get(key));
				} else if (valueKey.equals("password")) {
					carddavConfig.setPassword((String) config.get(key));
				} else {
					logger.warn("unsupported key: " + key);
				}
				
				logger.trace("current config (temp): {}", carddavConfig);
			}
			
			this.startRefreshTimer();
			
			Carddav.setInstance(this);
			isProperlyConfigured = true;
		} else {
			logger.warn("no configuration available");
		}
	}
	
	
	
	private void startRefreshTimer() {
		this.timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				logger.debug("refreshing contacts...");
				contacts.clear();
				for (CarddavConfiguration carddavConfiguration : configMap.values()) {
					try {
						loadContacts(carddavConfiguration);
					} catch (IOException e) {
						isProperlyConfigured = false;
						logger.error("cannot start service", e);
					} catch (VCardParseException e) {
						isProperlyConfigured = false;
						logger.error("cannot start service", e);
					} catch (Throwable e) {
						logger.error("cannot start service", e);
					}
					
				}
			}
			
			private void loadContacts(CarddavConfiguration config) throws IOException, VCardParseException {
				logger.trace("starting...");
				
				Sardine sardine = SardineFactory.begin(config.getUsername(), config.getPassword());
				
				List<DavResource> list = sardine.list(config.getAddress());
				logger.trace("listing contacts...");
				for (DavResource resource : list) {
		            if (resource.isDirectory()) {
		                continue;
		            }
		            
		            URI href = resource.getHref();
		            URL url = new URL(config.getAddress());
		            url = new URL(url.getProtocol(), url.getHost(), url.getPort(), href.toString());
		            InputStream inputStream = sardine.get(url.toString());
		            byte[] bytes = IOUtils.toByteArray(inputStream);
		            VCard parse = vCardEngine.parse(new String(bytes));
		            
		            Contact contact = new Contact();
		            contact.setSurname(parse.getN().getFamilyName());
		            contact.setGivenNames(parse.getN().getGivenName());
		            if (parse.getTels() != null) {
			            for (TelType telType : parse.getTels()) {
			            	Telephone telephone = new Telephone();
			            	StringBuilder sb = new StringBuilder();
			            	if (telType.getParams() != null) {
				            	for (TelParamType telParamType : telType.getParams()) {
				            		if (sb.length() > 0) {
				            			sb.append(", ");
				            		}
				            		sb.append(telParamType.getDescription());
				            	}
			            	}
			            	telephone.setType(sb.toString());
			            	telephone.setNumber(normalize(telType.getTelephone()));
			            	contact.getTelephone().add(telephone);
			            }
		            }
		            if (parse.getAdrs() != null) {
		            	for (AdrType adrType : parse.getAdrs()) {
		            		Address address = new Address();
		            		address.setContact(contact);
		            		address.setStreet(adrType.getStreetAddress());
		            		address.setCity(adrType.getRegion());
		            		address.setPostalCode(adrType.getPostalCode());
		            		address.setCountry(adrType.getCountryName());
		            		StringBuilder sb = new StringBuilder();
		            		if (adrType.getParams() != null) {
		            			for (AdrParamType adrParamType : adrType.getParams()) {
		            				if (sb.length() > 0) {
				            			sb.append(", ");
				            		}
				            		sb.append(adrParamType.getDescription());
		            			}
		            			address.setLabel(sb.toString());
		            		}
		            		Location location = geocode(address);
		            		address.setLocation(location);
		            		contact.getAddressList().add(address);
		            	}
		            }
		            logger.trace("found contact: {}, {}: telephones: {}, addresses: {}", new Object[]{contact.getSurname(), contact.getGivenNames(), contact.getTelephone(), contact.getAddressList()});
		            contacts.add(contact);
		        }
			}
		}, 1000, 1000 * 60 * 60);
	}

	public Contact foundContactByPhone(String in) {
		String phone = normalize(in);
		logger.trace("normalizing phone '{}' -> '{}'", in, phone);
		for (Contact contact : this.contacts) {
			for (Telephone telephone : contact.getTelephone()) {
				if (telephone.getNumber().equals(phone)) {
					logger.debug("found matching contact: {}", contact);
					return contact;
				}
				logger.trace("number does not match (in={}, cur={}", phone, telephone.getNumber());
			}
		}
		return null;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public Address foundAddress(String latitude, String longitude) {
		logger.debug("checking location for latitude={} and longitude={}", latitude, longitude);
		for (Contact contact : this.contacts) {
			logger.trace("trying contact '{}'", contact.getFullName());
			for (Address address : contact.getAddressList()) {
				if (address.getLocation() == null) {
					continue;
				}
				logger.trace("trying location '{}' from address: {}", address.getLocation(), address.getFull());
				double distance = calculateDistance(address.getLocation(), new Location(new BigDecimal(latitude), new BigDecimal(longitude)));
				logger.trace("distance to '{}': {}", address.getFull(), distance);
				if (distance <= GEOFENCE) {
					logger.debug("found matching contact: {}", contact);
					return address;
				} else {
					logger.trace("location is out of range");
				}
			}
		}
		return null;
	}
	
	private static Location geocode(Address address) throws IOException {
		if (address == null) {
			return null;
		}
		String fullAddress = address.getFull();
		if (fullAddress == null) {
			return null;
		}
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(fullAddress).setLanguage("de").getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        for (GeocoderResult geocoderResult : geocoderResponse.getResults()) {
            GeocoderGeometry geometry = geocoderResult.getGeometry();
            LatLng location = geometry.getLocation();
            return new Location(location.getLat(), location.getLng());
        }
        
        return null;
    }
	
	private static double calculateDistance(Location location, Location location2) {
        float lat1 = location2.getLatitude().floatValue();
        float lng1 = location2.getLongitude().floatValue();
        float lat2 = location.getLatitude().floatValue();
        float lng2 = location.getLongitude().floatValue();

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
