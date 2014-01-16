/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.koubachi.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openhab.binding.koubachi.KoubachiBindingProvider;
import org.openhab.binding.koubachi.internal.api.Device;
import org.openhab.binding.koubachi.internal.api.KoubachiResource;
import org.openhab.binding.koubachi.internal.api.KoubachiResourceType;
import org.openhab.binding.koubachi.internal.api.Plant;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Active Binding which queries the Koubachi server frequently.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public class KoubachiBinding extends AbstractActiveBinding<KoubachiBindingProvider> implements ManagedService {

	private static final Logger logger =  LoggerFactory.getLogger(KoubachiBinding.class);
	
	/** Timeout of the HTTP Requests in ms (defaults to 10000ms) */
	private static final int HTTP_REQUEST_TIMEOUT = 10000;
	
	/** the refresh interval which is used to poll values from the Koubachi server (optional, defaults to 900000ms, 15m) */
	private static long refreshInterval = 900000;
	
	/** the URL of the Device list  (optional, defaults to 'https://api.koubachi.com/v2/user/smart_devices?user_credentials=%1$s&app_key=%2$s') */
	private static String apiDeviceListUrl = "https://api.koubachi.com/v2/user/smart_devices?user_credentials=%1$s&app_key=%2$s";
	
	/** the URL of the Plant list  (optional, defaults to 'https://api.koubachi.com/v2/user/smart_devices?user_credentials=%1$s&app_key=%2$s') */
	private static String apiPlantListUrl = "https://api.koubachi.com/v2/plants?user_credentials=%1$s&app_key=%2$s";
	
	/** The single access token configured at http://labs.kpubachi.com */
	private static String credentials;
	
	/** The personal appKey configured at http://labs.koubachi.com */
 	private static String appKey;
	
 	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Koubachi Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		List<Device> devices = getDevices(apiDeviceListUrl, credentials, appKey);
		List<Plant> plants = getPlants(apiPlantListUrl, credentials, appKey);
		
		for (KoubachiBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				KoubachiResourceType resourceType = provider.getResourceType(itemName);
				String resourceId = provider.getResourceId(itemName);
				String propertyName = provider.getPropertyName(itemName);
				
				KoubachiResource resource = null;
				if (KoubachiResourceType.DEVICE.equals(resourceType)) {
					resource = findResource(resourceId, devices);
				} else {
					resource = findResource(resourceId, plants);
				}
				
				if (resource == null) {
					logger.debug("Cannot find Koubachi resource with id '{}'", resourceId);
					continue;
				}
				
				try {
					Object propertyValue = PropertyUtils.getProperty(resource, propertyName);
					State state = createState(propertyValue.getClass(), propertyValue);
					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					}
				} catch (Exception e) {
					logger.warn("Reading value '{}' from Resource '{}' throws went wrong", propertyName, resource);
				}
			}
		}
	}
	
	/**
	 * Gets the list of all configured Devices from the Koubachi server.
	 * @param appKey
	 * @param credentials 
	 * @param apiDeviceListUrl 
	 * 
	 * @return the list of all configured Devices. Is never {@code null}.
	 */
	private List<Device> getDevices(String apiDeviceListUrl, String credentials, String appKey) {
		List<Device> devices = new ArrayList<Device>();
		
		String url = String.format(apiDeviceListUrl, credentials, appKey);
		Properties headers = new Properties();
			headers.put("Accept", "application/json");
		
		String response = HttpUtil.executeUrl("GET", url, headers, null, null, HTTP_REQUEST_TIMEOUT);

		if (response==null) {
			logger.error("No response received from '{}'", url);
		} else {
			logger.debug("Koubachi returned '{}'", response);
			
			List<Map<String, Device>> deviceList = fromJSON(new TypeReference<List<Map<String,Device>>>() {}, response);
			for (Map<String, Device> element : deviceList) {
				devices.add(element.get("device"));
			}
		}
		
		return devices;
	}
	
	/**
	 * Gets the list of all configured Plants from the Koubachi server.
	 * 
	 * @return the list of all configured Plants. Is never {@code null}.
	 */
	private List<Plant> getPlants(String apiPlantListUrl, String credentials, String appKey) {
		List<Plant> plants = new ArrayList<Plant>();
		
		String url = String.format(apiPlantListUrl, credentials, appKey);
		Properties headers = new Properties();
			headers.put("Accept", "application/json");
	
		String response = HttpUtil.executeUrl("GET", url, headers, null, null, HTTP_REQUEST_TIMEOUT);

		if (response==null) {
			logger.error("No response received from '{}'", url);
		} else {
			logger.debug("Koubachi returned '{}'", response);
			
			List<Map<String, Plant>> plantList = fromJSON(new TypeReference<List<Map<String,Plant>>>() {}, response);
			for (Map<String, Plant> element : plantList) {
				plants.add(element.get("plant"));
			}
		}
		
		return plants;
	}
	
	/**
	 * Maps the given {@code jsonString} to {@code type}.
	 * 
	 * @param type to type to map the given {@code jsonString}
	 * @param jsonString the content which should be mapped to {@code type}
	 * 
	 * @return a new instance of {@code type} with content of {@code jsonString} 
	 */
	private <T> T fromJSON(final TypeReference<T> type, final String jsonString) {
		T data = null;
		try {
			data = new ObjectMapper().readValue(jsonString, type);
		} catch (Exception e) {
			logger.error("Mapping JSON to '" + type.getType() + "' throws an exception.", e);
		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private <R extends KoubachiResource> R findResource(String id, List<R> resources) {
		for (KoubachiResource resource : resources) {
			if (resource.getId().equals(id)) {
				return (R) resource;
			}
		}
		return null;
	}
	
	/**
	 * Creates an openHAB {@link State} in accordance to the given {@code dataType}. Currently
	 * {@link Date} and {@link BigDecimal} are handled explicitly. All other {@code dataTypes}
	 * are mapped to {@link StringType}.
	 * 
	 * @param dataType
	 * @param propertyValue
	 * 
	 * @return the new {@link State} in accordance to {@code dataType}. Will never be {@code null}.
	 */
	private State createState(Class<?> dataType, Object propertyValue) {
		if (Date.class.isAssignableFrom(dataType)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) propertyValue);
			return new DateTimeType(calendar);
		} else if (BigDecimal.class.isAssignableFrom(dataType)) {
			return new DecimalType((BigDecimal) propertyValue);
		} else {
			return new StringType(propertyValue.toString());
		}
	}
	
	
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			String apiDeviceUrlString = (String) config.get("deviceurl");
			if (StringUtils.isNotBlank(apiDeviceUrlString)) {
				apiDeviceListUrl = apiDeviceUrlString;
			}
			String apiPlantUrlString = (String) config.get("planturl");
			if (StringUtils.isNotBlank(apiPlantUrlString)) {
				apiPlantListUrl = apiPlantUrlString;
			}
			
			credentials = (String) config.get("credentials");
			if (StringUtils.isBlank(credentials)) {
				throw new ConfigurationException("koubachi:credentials", "Users' credentials parameter must be set");
			}
			appKey = (String) config.get("appkey");
			if (StringUtils.isBlank(appKey)) {
				throw new ConfigurationException("koubachi:appkey", "AppKey parameter must be set");
			}
			
			setProperlyConfigured(true);
		}
	}
	
	
}
