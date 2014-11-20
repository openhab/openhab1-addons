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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.mios.internal.config.DeviceBindingConfig;
import org.openhab.binding.mios.internal.config.MiosBindingConfig;
import org.openhab.binding.mios.internal.config.SceneBindingConfig;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;

/**
 * Manages the HTTP Connection for a single MiOS Unit.
 * 
 * This code has two functions to perform:
 * <ul>
 * <li>Inbound <i>changes</i> from the MiOS Unit.<br>
 * Manages an internal thread to periodically poll the configured MiOS Unit and
 * timeout at the specified interval. All changes are internally transformed
 * from their original (JSON) form, and dispatched to the relevant parts of
 * openHAB.
 * <li>Outbound <i>commands</i> to the MiOS Unit.<br>
 * Exposes a method to transform & transmit openHAB Commands to the configured
 * MiOS Unit, in a non-blocking manner.<br>
 * Callers wishing to utilize this functionality use the
 * <code>invokeCommand</code> method.
 * </ul>
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosUnitConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(MiosUnitConnector.class);

	private static final String ENCODING_CHARSET = "utf-8";
	private static int FORCE_FULL_POLL = 10;

	private static final String BIND_COMMAND_VALUE = "??";
	private static final String BIND_ITEM_INCREMENT = "?++";
	private static final String BIND_ITEM_DECREMENT = "?--";
	private static final String BIND_ITEM_VALUE = "?";

	private static final String SCENE_URL = "http://%s:%d/data_request?id=action&serviceId=urn:micasaverde-com:serviceId:HomeAutomationGateway1&action=RunScene&SceneNum=%d";

	private static final String DEVICE_URL = "http://%s:%d/data_request?id=action&DeviceNum=%d&serviceId=%s&action=%s";
	private static final String DEVICE_URL_PARAMS = DEVICE_URL + "&%s";

	private static final Pattern DEVICE_PATTERN = Pattern
			.compile("(?<serviceName>.+)/"
					+ "(?<serviceAction>.+)"
					+ "\\(((?<serviceParam>[a-zA-Z]+[a-zA-Z0-9]*)(=(?<serviceValue>.+))?)?\\)");

	// Certain properties are really "dates" in one-form-or-another. We keep a
	// list of them in this properties file so they can be fixed up on the way
	// into the system.
	//
	// In some really special cases, the value inbound might be represented by
	// either a String OR an Integer in the JSON, and you can't tell which
	// you're going to get ahead of time.
	private static Properties datetimeMap = new Properties();
	private static String DATETIME_FIXES = "org/openhab/binding/mios/internal/DatetimeVariables.properties";

	static {
		InputStream input = DeviceBindingConfig.class.getClassLoader()
				.getResourceAsStream(DATETIME_FIXES);

		try {
			datetimeMap.load(input);
			logger.debug(
					"Successfully loaded Datetime fixes from '{}', entries '{}'",
					DATETIME_FIXES, datetimeMap.size());
		} catch (Exception e) {
			// Pre-shipped with the Binding, so it should never error out.
			logger.error("Failed to load Datetime fixes file '{}', Exception",
					DATETIME_FIXES, e);
		}
	}

	// the MiOS instance and openHAB event publisher handles
	private final MiosUnit unit;
	private final MiosBinding binding;

	private final AsyncHttpClient client;
	private LongPoll pollCall;
	private Thread pollThread;
	boolean running;

	/**
	 * @param unit
	 *            The host to connect to. Give a reachable hostname or IP
	 *            address, without protocol or port
	 */
	public MiosUnitConnector(MiosUnit unit, MiosBinding binding) {
		logger.debug("Constructor: unit '{}', binding '{}'", unit, binding);

		this.unit = unit;
		this.binding = binding;

		Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setRequestTimeoutInMs(unit.getTimeout());

		// Use the JDK Provider for now, we're not looking for server-level
		// scalability, and we'd like to lighten the load for folks wanting to
		// run atop RPi units.
		this.client = new AsyncHttpClient(new JDKAsyncHttpProvider(
				builder.build()));

		pollCall = new LongPoll();
		pollThread = new Thread(pollCall);
	}

	/***
	 * Check if the connection to the MiOS instance is active
	 * 
	 * @return true if an active connection to the MiOS instance exists, false
	 *         otherwise
	 */
	public boolean isConnected() {
		return isRunning() && pollCall.isConnected();
	}

	/**
	 * Attempts to create a connection to the MiOS host and begin listening for
	 * updates over the async HTTP connection.
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void open() throws IOException, InterruptedException,
			ExecutionException {
		running = true;
		pollThread.start();
	}

	/***
	 * Close this connection to the MiOS instance
	 */
	public void close() {
		running = false;
	}

	/**
	 * Is the underlying Polling thread running?
	 * 
	 * @return true if it's running.
	 */

	public boolean isRunning() {
		return running;
	}

	private static String toBindValue(String value, Command command, State state) {
		// TODO: Allow for more complex Bind expressions, to allow for different
		// increment/decrement values, and various other transformations that
		// may be required.

		// Perform a simple item-value substitution on the resulting string.
		if (value == null) {
			return state.toString();
		} else if (value.contains(BIND_COMMAND_VALUE)) {
			return value.replace(BIND_COMMAND_VALUE, command.toString());
		} else if (value.contains(BIND_ITEM_INCREMENT)) {
			String tmp = String.valueOf(Integer.parseInt(state.toString()) + 1);
			return value.replace(BIND_ITEM_INCREMENT, tmp);
		} else if (value.contains(BIND_ITEM_DECREMENT)) {
			String tmp = String.valueOf(Integer.parseInt(state.toString()) - 1);
			return value.replace(BIND_ITEM_DECREMENT, tmp);
		} else if (value.contains(BIND_ITEM_VALUE)) {
			return value.replace(BIND_ITEM_VALUE, state.toString());
		} else {
			return value;
		}
	}

	private void callDevice(DeviceBindingConfig config, Command command,
			State state) throws TransformationException {

		logger.debug(
				"callDevice: Need to remote-invoke Device '{}' action '{}' and current state '{}')",
				new Object[] { config.toProperty(), command, state });

		String newCommand = config.transformCommand(command);
		if (newCommand == null) {
			logger.warn(
					"callDevice: Command '{}' not supported, or missing command: mapping, for Item '{}'",
					command.toString(), config.getItemName());
			return;
		} else if (newCommand.equals("")) {
			logger.trace(
					"callDevice: Item '{}' has disabled the use of Command '{}' via it's configuration '{}'",
					new Object[] { config.getItemName(), command.toString(),
							config.toProperty() });
			return;
		}

		Matcher matcher = DEVICE_PATTERN.matcher(newCommand);

		if (matcher.matches()) {
			try {
				MiosUnit u = getMiosUnit();

				String serviceName = matcher.group("serviceName");
				String serviceAction = matcher.group("serviceAction");
				String serviceParam = matcher.group("serviceParam");
				String serviceValue = matcher.group("serviceValue");

				logger.debug(
						"callDevice: decoded as serviceName '{}' serviceAction '{}' serviceParam '{}' serviceValue '{}'",
						new Object[] { serviceName, serviceAction,
								serviceParam, serviceValue });

				// Perform any necessary bind-variable style transformations on
				// the value, before we put it into the URL.
				serviceValue = toBindValue(serviceValue, command, state);

				// If the parameters to the URL are specified, then we need to
				// build the parameter section of the URL, encoding parameter
				// names and values... trust no-one 8)
				if (serviceParam != null) {
					String p = URLEncoder
							.encode(serviceParam, ENCODING_CHARSET)
							+ '='
							+ URLEncoder.encode(serviceValue, ENCODING_CHARSET);
					callMios(String.format(DEVICE_URL_PARAMS, u.getHostname(),
							u.getPort(), config.getMiosId(),
							URLEncoder.encode(serviceName, ENCODING_CHARSET),
							URLEncoder.encode(serviceAction, ENCODING_CHARSET),
							p));
				} else {
					callMios(String.format(DEVICE_URL, u.getHostname(),
							u.getPort(), config.getMiosId(),
							URLEncoder.encode(serviceName, ENCODING_CHARSET),
							URLEncoder.encode(serviceAction, ENCODING_CHARSET)));
				}

			} catch (UnsupportedEncodingException uee) {
				logger.debug(
						"Really, trust me, this won't happen ;)   exception='{}'",
						uee);
			}
		} else {
			logger.error(
					"callDevice: The parameter is in the wrong format.  BindingConfig '{}', UPnP Action '{}'",
					config, newCommand);
		}

	}

	private void callScene(SceneBindingConfig config, Command command,
			State state) throws TransformationException {
		logger.debug("callScene: Need to remote-invoke Scene '{}'",
				config.toProperty());

		String newCommand = config.transformCommand(command);

		if (newCommand != null) {
			MiosUnit u = getMiosUnit();
			callMios(String.format(SCENE_URL, u.getHostname(), u.getPort(),
					config.getMiosId()));
		} else {
			logger.warn(
					"callScene: Command '{}' not supported, or missing command: declaration, for Item '{}'",
					command.toString(), config.getItemName());
		}

	}

	private void callMios(String url) {
		logger.debug("callMios: Would like to fire off the URL '{}'", url);

		try {
			getAsyncHttpClient().prepareGet(url).execute();

			// TODO: Run it and walk away?
			//
			// Work out a better model to gather information about the
			// success/fail of the call, log details (etc) so things can be
			// diagnosed.
		} catch (Exception e) {
			logger.debug(
					"callMios: Exception Error occurred fetching content: {}",
					e.getMessage(), e);
		}
	}

	/**
	 * Request a Command be delivered to the MiOS Unit under control.
	 * <p>
	 * The MiOS Binding uses this call to deliver "single valued" openHAB
	 * Commands to the target MiOS Unit.
	 * <p>
	 * Configurable transformations, defined at the BindingConfig level, are
	 * used to transform this openHAB Command into the specific form required by
	 * the MiOS Unit.
	 * <p>
	 * openHAB Commands can only be targeted to Device and Scene Bindings, all
	 * other requests will cause a warning to be logged.
	 */
	public void invokeCommand(MiosBindingConfig config, Command command,
			State state) throws Exception {
		if (config instanceof SceneBindingConfig) {
			callScene((SceneBindingConfig) config, command, state);
		} else if (config instanceof DeviceBindingConfig) {
			callDevice((DeviceBindingConfig) config, command, state);
		} else {
			logger.warn(
					"Unhandled command execution for Command ('{}') on binding '{}'",
					command, config);
		}
	}

	private MiosUnit getMiosUnit() {
		return unit;
	}

	private MiosBinding getMiosBinding() {
		return binding;
	}

	private AsyncHttpClient getAsyncHttpClient() {
		return client;
	}

	/**
	 * MiOS Poll code.
	 * 
	 * This code will stand up a thread to serially poll the target MiOS Unit.
	 * The initial poll request will return the full content from the MiOS unit.
	 * Subsequent calls are requested to only return the incremental/changed
	 * contents.
	 * 
	 * If a processing error (Timeout, etc) is detected, then a full content
	 * poll is again initiated (since things can change during the interval)
	 * 
	 * Upon successful polling, a series of calls are made to openHAB to push
	 * the data to the respective bindings, so that data values will change
	 * within the user's Rules (etc).
	 * 
	 * @author Mark Clark
	 * @since 1.6.0
	 */
	private class LongPoll implements Runnable {
		private boolean connected = false;

		private Integer loadTime = null;
		private Integer dataVersion = null;
		private int failures = 0;

		private final ObjectMapper mapper = new ObjectMapper();

		private static final String BASE_URL = "http://%s:%d/data_request";
		private static final String STATUS2_URL = BASE_URL + "?id=status2";
		private static final String STATUS2_INCREMENTAL_URL = STATUS2_URL
				+ "&LoadTime=%d&DataVersion=%d&Timeout=%d";

		private static final int MAX_FULL_REFRESH_COUNT = 5000;

		public LongPoll() {
		}

		@SuppressWarnings("unchecked")
		private List<Object> getList(Map<String, Object> data, String param) {
			if (!data.containsKey(param))
				return new ArrayList<Object>();

			return (List<Object>) data.get(param);
		}

		private String getUri(boolean full) {
			// Force a full poll of the dataSet every MAX_FAILURES,
			// "just in case".
			boolean force = full || (failures != 0)
					&& ((failures % FORCE_FULL_POLL) == 0);

			MiosUnit unit = getMiosUnit();

			if (!force && loadTime != null && dataVersion != null) {
				AsyncHttpClientConfig c = getAsyncHttpClient().getConfig();

				// Use a timeout on the MiOS URL call that's about 1/3 of what
				// the
				// connection timeout is.
				int t = Math.min(c.getIdleConnectionTimeoutInMs(),
						c.getRequestTimeoutInMs()) / 1000 / 3;

				return String.format(Locale.US, STATUS2_INCREMENTAL_URL,
						unit.getHostname(), unit.getPort(), loadTime,
						dataVersion, new Integer(t));
			} else {
				return String.format(Locale.US, STATUS2_URL,
						unit.getHostname(), unit.getPort());
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
			String p = getMiosUnit().formatProperty(property);

			try {
				getMiosBinding().postPropertyUpdate(p, value);
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
							|| value instanceof Double
							|| value instanceof Boolean) {
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
						// No need to bring these into openHAB, they'll only
						// bulk up
						// the system.
						if (key.equals("StartupCode") || key.equals("startup")) {
							continue;
						}

						logger.debug(
								"processSystem: skipping key={}, class={}",
								key, value.getClass());
					}
				}
			}
		}

		private void processDevices(List<Object> devices) {
			for (Object d : devices) {
				@SuppressWarnings("unchecked")
				Map<String, Object> device = (Map<String, Object>) d;

				// Note that the "name" field is not present in status2
				// responses,
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
							|| value instanceof Double
							|| value instanceof Boolean) {
						if (key.equals("time_created")) {
							// fix [known] date-time values on the way through,
							// so
							// they use the native type.
							value = fixTimestamp(value);
						}

						// TODO: Consider putting the Device's ID attribute last
						// in
						// the list. When multiple value changes are being made,
						// people may want something to indicate
						// "these are the last" [bundle] of changes for this
						// device.
						// Otherwise they'll come out in JSON order
						// (unpredictable)
						// which may not be the order they're normally changed
						// at
						// the MiOS end. Making this last would be like having
						// an
						// "end of [device] transaction" marker.

						String property = "device:" + deviceId + '/'
								+ da.getKey();
						publish(property, value);
					} else {
						// No need to bring these into openHAB, they'll only
						// bulk up
						// the system.
						if (key.equals("states") || key.equals("ControlURLs")
								|| key.equals("Jobs") || key.equals("tooltip")) {
							continue;
						}

						logger.debug(
								"processDevices: skipping key={}, class={}",
								key, value.getClass());
					}
				}

				// Enumerate Device State Variables
				List<Object> deviceStates = getList(device, "states");

				for (Object ds : deviceStates) {
					@SuppressWarnings("unchecked")
					Map<String, Object> state = (Map<String, Object>) ds;
					String var = (String) state.get("service") + "/"
							+ (String) state.get("variable");
					String property = "device:" + deviceId + "/service/" + var;

					// Can be String or Integer
					Object value = (Object) state.get("value");

					// TODO: Externalize this, make it configurable
					if (datetimeMap.containsKey(var)) {
						value = fixTimestamp(value);
					}

					publish(property, value);
				}
			}
		}

		private void processScenes(List<Object> scenes) {
			for (Object s : scenes) {
				@SuppressWarnings("unchecked")
				Map<String, Object> scene = (Map<String, Object>) s;

				// Note that the "name" field is not present in status2
				// responses,
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
							|| value instanceof Double
							|| value instanceof Boolean) {
						if (key.equals("Timestamp")) {
							// fix [known] date-time values on the way through,
							// so
							// they use the native type.
							value = fixTimestamp(value);

							// Fix the key so it's consistent with the one used
							// at
							// the System level.
							// TODO: Document this.
							key = "TimeStamp";
						}

						String property = "scene:" + sceneId + "/" + key;
						publish(property, value);
					} else {
						// No need to bring these into openHAB, they'll only
						// bulk up
						// the system.
						if (key.equals("timers") || key.equals("triggers")
								|| key.equals("groups")
								|| key.equals("tooltip") || key.equals("Jobs")
								|| key.equals("lua")) {
							continue;
						}

						logger.debug(
								"processScenes: skipping key={}, class={}",
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
						new Object[] { lt, dv,
								Integer.toString(devices.size()),
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
		// entire (large) MiOS JSON string in memory at the same time as the
		// parsed
		// JSON result.
		@SuppressWarnings("unchecked")
		private Map<String, Object> readJson(String json) {
			if (json == null)
				return new HashMap<String, Object>();

			try {
				return mapper.readValue(json, Map.class);
			} catch (JsonParseException e) {
				// TODO: Replace RuntimeException with a more specialized
				// exception.
				throw new RuntimeException("Failed to parse JSON", e);
			} catch (JsonMappingException e) {
				// TODO: Replace RuntimeException with a more specialized
				// exception.
				throw new RuntimeException("Failed to map JSON", e);
			} catch (IOException e) {
				// TODO: Replace RuntimeException with a more specialized
				// exception.
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

					Future<Response> f = getAsyncHttpClient().prepareGet(uri)
							.execute();
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
			} while (isRunning());

			logger.info("run: Shutting down MiOS Polling thread.  Unit={}",
					getMiosUnit().getName());
			connected = false;
		}
	}
}
