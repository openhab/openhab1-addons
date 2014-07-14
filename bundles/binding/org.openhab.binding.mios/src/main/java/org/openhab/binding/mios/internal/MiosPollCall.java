/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;

/**
 * MiOS Poll code.
 * 
 * This code will stand up a thread to serially poll the target MiOS Unit. The
 * initial poll request will return the full content from the MiOS unit.
 * Subsequent calls are requested to only return the incremental/changed
 * contents.
 * 
 * If a processing error (Timeout, etc) is detected, then a full content poll is
 * again initiated (since things can change during the interval)
 * 
 * Upon successful polling, a series of calls are made to openHAB to push the
 * data to the respective bindings, so that data values will change within the
 * user's Rules (etc).
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosPollCall implements Runnable {
	private boolean connected = false;

	private Integer loadTime = null;
	private Integer dataVersion = null;
	private int failures = 0;
	private static int FORCE_FULL_POLL = 10;

	private AsyncHttpClient client;
	private final ObjectMapper mapper = new ObjectMapper();
	private MiosConnector connector;

	private static final String BASE_URL = "http://%s:%d/data_request";
	private static final String STATUS2_URL = BASE_URL + "?id=status2";
	private static final String STATUS2_INCREMENTAL_URL = STATUS2_URL
			+ "&LoadTime=%d&DataVersion=%d&Timeout=%d";

	private static final int MAX_FULL_REFRESH_COUNT = 5000;

	private static final Logger logger = LoggerFactory
			.getLogger(MiosPollCall.class);

	public MiosPollCall(AsyncHttpClient client, MiosConnector connector) {
		this.client = client;
		this.connector = connector;

	}

	@SuppressWarnings("unchecked")
	private List<Object> getList(Map<String, Object> data, String param) {
		if (!data.containsKey(param))
			return new ArrayList<Object>();

		return (List<Object>) data.get(param);
	}

	private String getUri(boolean full) {
		// Force a full poll of the dataSet every MAX_FAILURES, "just in case".
		boolean force = full || (failures != 0) && ((failures % FORCE_FULL_POLL) == 0);

		MiosUnit unit = connector.getUnit();

		if (!force && loadTime != null && dataVersion != null) {
			AsyncHttpClientConfig c = client.getConfig();

			// Use a timeout on the MiOS URL call that's about 1/3 of what the
			// connection timeout is.
			int t = Math.min(c.getIdleConnectionTimeoutInMs(),
					c.getRequestTimeoutInMs()) / 1000 / 3;

			return String.format(Locale.US, STATUS2_INCREMENTAL_URL,
					unit.getHostname(), unit.getPort(), loadTime, dataVersion,
					new Integer(t));
		} else {
			return String.format(Locale.US, STATUS2_URL, unit.getHostname(),
					unit.getPort());
		}
	}

	private Object fixTimestamp(Object value) {
		if (value == null) {
			return value;
		} else if (value instanceof Integer) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(((Integer) value).intValue() * 1000l);
			return cal;
		} else if (value instanceof String && !value.equals("")) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Integer.parseInt((String) value) * 1000l);
			return cal;
		} else {
			return value;
		}
	}

	private void publish(String property, Object value) {
		String p = connector.getUnit().formatProperty(property);

		try {
			connector.getMiosBinding().postPropertyUpdate(p, value);
		} catch (Exception e) {
			logger.error(
					"Exception '{}' raised pushing property '{}' value '{}' into openHAB",
					new Object[] { e.getMessage(), p, value, e });
		}
	}

	private void processSystem(Map<String, Object> system) {
		for (Map.Entry<String, Object> entry : system.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (!key.equals("devices") && !key.equals("scenes")
					&& !key.equals("rooms") && !key.equals("sections")) {
				if (value instanceof String || value instanceof Integer
						|| value instanceof Double || value instanceof Boolean) {
					// Skip over Lua code blocks and
					// fix [known] date-time values on the way through, so
					// they use the native type.
					if (key.equals("DeviceSync") || key.equals("LoadTime")
							|| key.equals("zwave_heal")
							|| key.equals("TimeStamp")) {
						value = fixTimestamp(value);
					}

					String property = "system:/" + key;
					publish(property, value);
				} else {
					// No need to bring these into openHab, they'll only
					// bulk up
					// the system.
					if (key.equals("StartupCode") || key.equals("startup")) {
						continue;
					}

					logger.debug("processSystem: skipping key={}, class={}",
							key, value.getClass());
				}
			}
		}
	}

	private void processDevices(List<Object> devices) {
		for (Object d : devices) {
			@SuppressWarnings("unchecked")
			Map<String, Object> device = (Map<String, Object>) d;

			// Note that the "name" field is not present in status2 responses,
			// like it is in user_data2 responses.

			//
			// These can be either an Integer or a String, either way it's
			// an int. Newer ones tend to be Strings, so we'll convert them
			// all to String values.
			//
			String deviceId;
			Object v = device.get("id");

			if (v instanceof String) {
				deviceId = (String) v;
			} else if (v instanceof Integer) {
				deviceId = ((Integer) v).toString();
			} else {
				throw new IllegalArgumentException("WTF?");
			}

			// Enumerate Device Attributes
			for (Map.Entry<String, Object> da : device.entrySet()) {
				String key = da.getKey();
				Object value = da.getValue();

				if (value instanceof String || value instanceof Integer
						|| value instanceof Double || value instanceof Boolean) {
					if (key.equals("time_created")) {
						// fix [known] date-time values on the way through, so
						// they use the native type.
						value = fixTimestamp(value);
					}

					// TODO: Consider putting the Device's ID attribute last in
					// the list. When multiple value changes are being made,
					// people may want something to indicate
					// "these are the last" [bundle] of changes for this device.
					// Otherwise they'll come out in JSON order (unpredictable)
					// which may not be the order they're normally changed at
					// the MiOS end. Making this last would be like having an
					// "end of [device] transaction" marker.

					String property = "device:" + deviceId + '/' + da.getKey();
					publish(property, value);
				} else {
					// No need to bring these into openHab, they'll only bulk up
					// the system.
					if (key.equals("states") || key.equals("ControlURLs")
							|| key.equals("Jobs") || key.equals("tooltip")) {
						continue;
					}

					logger.debug("processDevices: skipping key={}, class={}",
							key, value.getClass());
				}
			}

			// Enumerate Device State Variables
			List<Object> deviceStates = getList(device, "states");

			for (Object ds : deviceStates) {
				@SuppressWarnings("unchecked")
				Map<String, Object> state = (Map<String, Object>) ds;
				String service = (String) state.get("service");
				String variable = (String) state.get("variable");

				// Can be String or Integer
				Object value = (Object) state.get("value");

				// TODO: Externalize this, make it configurable
				if ((variable.equals("LastUpdate") && service
						.equals("urn:micasaverde-com:serviceId:HaDevice1"))
						|| (variable.equals("BatteryDate") && service
								.equals("urn:micasaverde-com:serviceId:HaDevice1"))
						|| (variable.equals("LastTimeCheck") && service
								.equals("urn:micasaverde-com:serviceId:HaDevice1"))
						|| (variable.equals("FirstConfigured") && service
								.equals("urn:micasaverde-com:serviceId:HaDevice1"))
						|| (variable.equals("LastSceneTime") && service
								.equals("urn:micasaverde-com:serviceId:SceneController1"))
						|| (variable.equals("LastTrip") && service
								.equals("urn:micasaverde-com:serviceId:SecuritySensor1"))
						|| (variable.equals("HealthDate") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveDevice1"))
						|| (variable.equals("LastRouteUpdate") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveDevice1"))
						|| (variable.equals("LastReset") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveDevice1"))
						|| (variable.equals("LastRouteFailure") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveNetwork1"))
						|| (variable.equals("LastHeal") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveNetwork1"))
						|| (variable.equals("LastUpdate") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveNetwork1"))
						|| (variable.equals("LastDongleBackup") && service
								.equals("urn:micasaverde-com:serviceId:ZWaveNetwork1"))
						|| (variable.equals("LastAlarmActive") && service
								.equals("urn:micasaverde-com:serviceId:AlarmPartition2"))
						|| (variable.equals("systemLuupRestartUnix") && service
								.equals("urn:cd-jackson-com:serviceId:SystemMonitor"))
						|| (variable.equals("cmhLastRebootUnix") && service
								.equals("urn:cd-jackson-com:serviceId:SystemMonitor"))
						|| (variable.equals("cmhLastRebootUnix") && service
								.equals("urn:cd-jackson-com:serviceId:SystemMonitor"))
						|| (variable.equals("systemVeraRestartUnix") && service
								.equals("urn:cd-jackson-com:serviceId:SystemMonitor"))
						|| (variable.equals("LastUpdate") && service
								.equals("urn:upnp-micasaverde-com:serviceId:Weather1"))) {
					value = fixTimestamp(value);
				}

				// Native UPnP Service name
				String property = "device:" + deviceId + "/service/" + service
						+ '/' + variable;
				publish(property, value);
			}
		}
	}

	private void processScenes(List<Object> scenes) {
		for (Object s : scenes) {
			@SuppressWarnings("unchecked")
			Map<String, Object> scene = (Map<String, Object>) s;

			// Note that the "name" field is not present in status2 responses,
			// like it is in user_data2 responses.

			// These can be either an Integer or a String, either way it's
			// an int. Newer ones tend to be Strings, so we'll convert them
			// all to String values.
			Integer sceneId = (Integer) scene.get("id");

			// Enumerate Scene Attributes
			for (Map.Entry<String, Object> da : scene.entrySet()) {
				String key = da.getKey();

				Object value = da.getValue();

				if (value instanceof String || value instanceof Integer
						|| value instanceof Double || value instanceof Boolean) {
					if (key.equals("Timestamp")) {
						// fix [known] date-time values on the way through, so
						// they use the native type.
						value = fixTimestamp(value);

						// Fix the key so it's consistent with the one used at
						// the System level.
						// TODO: Document this.
						key = "TimeStamp";
					}

					String property = "scene:" + sceneId + "/" + key;
					publish(property, value);
				} else {
					// No need to bring these into openHab, they'll only bulk up
					// the system.
					if (key.equals("timers") || key.equals("triggers")
							|| key.equals("groups") || key.equals("tooltip")
							|| key.equals("Jobs") || key.equals("lua")) {
						continue;
					}

					logger.debug("processScenes: skipping key={}, class={}",
							key, value.getClass());
				}
			}
		}
	}

	private void processRooms(List<Object> rooms) {
		// TODO: Implement
	}

	private void processSections(List<Object> sections) {
		// TODO: Implement
	}

	private void processResponse(Map<String, Object> response) {

		Integer lt = (Integer) response.get("LoadTime");
		Integer dv = (Integer) response.get("DataVersion");

		if (lt != null && dv != null) {
			connected = true;
			List<Object> devices = getList(response, "devices");
			List<Object> scenes = getList(response, "scenes");
			List<Object> rooms = getList(response, "rooms");
			List<Object> sections = getList(response, "sections");

			logger.debug(
					"processResponse: success! loadTime={}, dataVersion={} devices({}) scenes({}) rooms({}) sections({})",
					new Object[] { lt, dv, Integer.toString(devices.size()),
							Integer.toString(scenes.size()),
							Integer.toString(rooms.size()),
							Integer.toString(sections.size()) });

			processDevices(devices);
			processScenes(scenes);
			processRooms(rooms);
			processSections(sections);
			processSystem(response);

			// Only reset these once we've successfully loaded/parsed the
			// content.
			this.loadTime = lt;
			this.dataVersion = dv;
			this.failures = 0;
		} else {
			connected = false;
			this.failures++;
			logger.debug("processResponse: failed!  Total failures ({})",
					Integer.toString(failures));
		}
	}

	public boolean isConnected() {
		return connected;
	}

	// TODO: Need to make this stream-oriented, so we don't have to keep the
	// entire (large) MiOS JSON string in memory at the same time as the parsed
	// JSON result.
	@SuppressWarnings("unchecked")
	private Map<String, Object> readJson(String json) {
		if (json == null)
			return new HashMap<String, Object>();

		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			// TODO: Replace RuntimeException with a more specialized exception.
			throw new RuntimeException("Failed to parse JSON", e);
		} catch (JsonMappingException e) {
			// TODO: Replace RuntimeException with a more specialized exception.
			throw new RuntimeException("Failed to map JSON", e);
		} catch (IOException e) {
			// TODO: Replace RuntimeException with a more specialized exception.
			throw new RuntimeException("Failed to read JSON", e);
		}
	}

	@Override
	public void run() {
		int loopCount = 0;

		do {
			try {
				// Force a full fresh every 5000 cycles.
				String uri = getUri(loopCount == 0);
				
				logger.debug("run: URI Built was '{}' loop '{}'", uri,
						loopCount);

				loopCount = ((loopCount + 1) % MAX_FULL_REFRESH_COUNT);

				Future<Response> f = client.prepareGet(uri).execute();
				Response r = (Response) f.get();

				Map<String, Object> json = readJson(r.getResponseBody());

				if (json.containsKey("error"))
					throw new IOException(json.get("error").toString());

				connected = true;
				processResponse(json);
			} catch (Exception e) {
				connected = false;
				logger.debug(
						"run: Exception Error occurred fetching content: {}",
						e.getMessage(), e);

				// TODO: Make the pause configurable and/or add a backoff
				// mechanism.
				//
				// Pause a little before restarting, to give things time to
				// recover.
				try {
					Thread.sleep(5000);
				} catch (InterruptedException tex) {
				}
			}
		} while (connector.isRunning());

		logger.info("run: Shutting down MiOS Polling thread.  Unit={}",
				connector.getUnit().getName());
		connected = false;
	}
}
