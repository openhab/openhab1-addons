/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 
package org.openhab.binding.sunnywebbox.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.sunnywebbox.SunnyWebBoxBindingProvider;
import org.openhab.binding.sunnywebbox.requests.GetPlantOverviewRequest;
import org.openhab.binding.sunnywebbox.requests.GetDevicesRequest;
import org.openhab.binding.sunnywebbox.requests.GetParameterRequest;
import org.openhab.binding.sunnywebbox.requests.GetProcessDataRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Manolis Nikiforakis
 * @author Yiannis Gkoufas
 * @since 1.5.0
 */
public class SunnyWebBoxBinding extends
		AbstractActiveBinding<SunnyWebBoxBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(SunnyWebBoxBinding.class);

	/**
	 * the refresh interval which is used to poll values from the SunnyWebBox
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 1000; //temp fix
	private HashMap<String, String> URLs = new HashMap<String, String>();
	private HashMap<String, List<Object>> urlToDevicesMap = new HashMap<String, List<Object>>();

	 private HashSet<CacheConfig> cache = new HashSet<CacheConfig>();

	private List<Object> deviceIds = new ArrayList<Object>();

	private HashMap<String, HashMap<String, Object>> plantOverviewValues = new HashMap<String, HashMap<String, Object>>();
	private HashMap<String, HashMap<String, Object>> processDataPerDevice = new HashMap<String, HashMap<String, Object>>();
	private HashMap<String, HashMap<String, Object>> parametersPerDevice = new HashMap<String, HashMap<String, Object>>();

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "SunnyWebBox Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method of sunnywebbox is called!");
		
		

		Gson gson = new GsonBuilder().create();

		if (deviceIds.isEmpty()) {
			GetDevicesRequest getDevicesRequest = new GetDevicesRequest();
			List<Object> _deviceIds = new ArrayList<Object>();
			Iterator<String> keyIterator = URLs.keySet().iterator();
			while (keyIterator.hasNext()) {
				String urlID = keyIterator.next();
				String urlValue = URLs.get(urlID);
				List<Object> deviceIds1 = getDevicesRequest
						.getDevices(urlValue);
				if (deviceIds1 != null) {
					_deviceIds.addAll(deviceIds1);
					urlToDevicesMap.put(urlID, deviceIds1);
				}
			}

			deviceIds = _deviceIds;

			logger.info("devices are {}", deviceIds.toString());

		}

		GetPlantOverviewRequest getPlantOverviewRequest = new GetPlantOverviewRequest();
		List<String> urlValues = new ArrayList<String>(URLs.values());
		HashMap<String, HashMap<String, Object>> localPlantOverviewValues = new HashMap<String, HashMap<String, Object>>();

		Iterator<String> keyIterator0 = URLs.keySet().iterator();
		while (keyIterator0.hasNext()) {
			String urlID = keyIterator0.next();
			String urlValue = URLs.get(urlID);
			/* logger.info("URL iterated {}", url); */
			localPlantOverviewValues.put(urlID,
					getPlantOverviewRequest.getValues(urlValue));
		}

		plantOverviewValues = localPlantOverviewValues;
		String json = gson.toJson(plantOverviewValues);
		logger.info("plants overview {}", json);

		// if (plantOverviewValues != null) {
		// Gson gson = new GsonBuilder().create();
		// String json = gson.toJson(plantOverviewValues);
		//
		// DBCollection plantOverviewCol = db.getCollection("plantoverview");
		// plantOverviewCol.insert(new BasicDBObject(plantOverviewValues));
		//
		// logger.info("plants overview {}", json);
		// }

		GetProcessDataRequest getProcessDataRequest = new GetProcessDataRequest();

		HashMap<String, HashMap<String, Object>> localProcessDataPerDevice = new HashMap<String, HashMap<String, Object>>();

		Iterator<String> keyIterator = URLs.keySet().iterator();
		while (keyIterator.hasNext()) {
			String urlID = keyIterator.next();
			String urlValue = URLs.get(urlID);
			if(urlToDevicesMap.get(urlID)==null) continue;
			getProcessDataRequest.setDeviceIds(urlToDevicesMap.get(urlID));
			localProcessDataPerDevice.putAll(getProcessDataRequest
					.getProcessDataPerDevice(urlValue));
		}
		processDataPerDevice = localProcessDataPerDevice;
		String json1 = gson.toJson(processDataPerDevice);
		logger.info("data per device {}", json1);

		// for(String url : urlValues)
		// {
		// localPlantOverviewValues.putAll(getPlantOverviewRequest.getValues(url));
		// }
		//
		// getProcessDataRequest.setDeviceIds(deviceIds);
		// processDataPerDevice = getProcessDataRequest
		// .getProcessDataPerDevice(URL);

		// if (processDataPerDevice != null) {
		// Gson gson = new GsonBuilder().create();
		// String json = gson.toJson(processDataPerDevice);
		//
		// DBCollection processDataCol = db.getCollection("processdata");
		// processDataCol.insert(new BasicDBObject(processDataPerDevice));
		//
		// logger.info("data per device {}", json);
		// }

		GetParameterRequest getParameterRequest = new GetParameterRequest();

		HashMap<String, HashMap<String, Object>> localParametersPerDevice = new HashMap<String, HashMap<String, Object>>();

		Iterator<String> keyIterator1 = URLs.keySet().iterator();
		while (keyIterator1.hasNext()) {
			String urlID = keyIterator1.next();
			String urlValue = URLs.get(urlID);
			if(urlToDevicesMap.get(urlID)==null) continue;
			getParameterRequest.setDeviceIds(urlToDevicesMap.get(urlID));
			localParametersPerDevice.putAll(getParameterRequest
					.getParametersDevice(urlValue));
		}
		parametersPerDevice = localParametersPerDevice;
		String json2 = gson.toJson(parametersPerDevice);
		logger.info("parameters per device {}", json2);

		// getParameterRequest.setDeviceIds(deviceIds);
		// parametersPerDevice = getParameterRequest.getParametersDevice(URL);

		// if (parametersPerDevice != null) {
		// Gson gson = new GsonBuilder().create();
		// String json = gson.toJson(parametersPerDevice);
		//
		// DBCollection parametersCol = db.getCollection("parameters");
		// parametersCol.insert(new BasicDBObject(parametersPerDevice));
		//
		// logger.info("parameters per device {}", json);
		// }

		for (SunnyWebBoxBindingProvider provider : providers) {

			for (String itemName : provider.getItemNames()) {

				String deviceId = provider.getDeviceId(itemName);
				String meta = provider.getMeta(itemName);
				String urlId = provider.getUrlKey(itemName);

				logger.info("device id {}", deviceId);
				logger.info("meta {}", meta);

				Object value;

				value = getValueForDevice(deviceId, meta, urlId);

				if (value != null) {
					if (NumberUtils.isNumber(value.toString())) {
						logger.debug("value isNumber:{}", value);
						eventPublisher.postUpdate(itemName,
								DecimalType.valueOf(value.toString()));
					} else
						logger.debug("value is String:{}", value);
					eventPublisher.postUpdate(itemName,
							StringType.valueOf(value.toString()));
				}

				// try {
				// GenericRequest genericRequest = new GenericRequest();
				// genericRequest.setProc(provider.getRPC(itemName));
				// String value = genericRequest.getValue(URL,
				// provider.getMeta(itemName));

				// if (value != null && !value.isEmpty()) {
				//
				// if (NumberUtils.isNumber(value)) {
				// logger.debug("value isNumber:{}", value);
				// eventPublisher.postUpdate(itemName,
				// DecimalType.valueOf(value));
				// } else
				// logger.debug("value is String:{}", value);
				// eventPublisher.postUpdate(itemName,
				// StringType.valueOf(value));
				// } else {
				// logger.warn("{} value is null",itemName);
				// eventPublisher.postUpdate(itemName,
				// StringType.valueOf("null"));
				// }
				// } catch (Exception e) {
				// //e.printStackTrace();
				// logger.error("Execute WS error", e);
				// }
			}
		}
	}

	private Object getValueForDevice(String deviceId, String meta, String urlID) {
		if (plantOverviewValues.containsKey(urlID)) {
			HashMap<String, Object> map = plantOverviewValues.get(urlID);
			if (map.containsKey(meta))
				return map.get(meta);
		}

		if (processDataPerDevice.containsKey(deviceId)) {
			HashMap<String, Object> map = processDataPerDevice.get(deviceId);
			if (map.containsKey(meta))
				return map.get(meta);
		}
		if (parametersPerDevice.containsKey(deviceId)) {
			HashMap<String, Object> map = parametersPerDevice.get(deviceId);
			if (map.containsKey(meta))
				return map.get(meta);
		}

		return null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				logger.debug("refresh is set to "+refreshIntervalString);
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			// String URLConfig = (String) config.get("URL");
			Enumeration<String> configElementsKeys = config.keys();
			while (configElementsKeys.hasMoreElements()) {
				String configElementKey = configElementsKeys.nextElement();
				logger.debug("config element: " + configElementKey);
				if (configElementKey.startsWith("URL")) {
					URLs.put(configElementKey,
							(String) config.get(configElementKey));
				}
			}
			// System.exit(0);
			// config.
			// if (StringUtils.isNotBlank(URLConfig)) {
			// URL = URLConfig;
			// }

			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}

	class CacheConfig {

		String item;
		String value;
		long lastUpdated;
		int interval;

		public boolean needsUpdate(long time) {
			if (time - lastUpdated > interval)
				return true;
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheConfig other = (CacheConfig) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (item == null) {
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			return true;
		}

		private SunnyWebBoxBinding getOuterType() {
			return SunnyWebBoxBinding.this;
		}

	}

}
