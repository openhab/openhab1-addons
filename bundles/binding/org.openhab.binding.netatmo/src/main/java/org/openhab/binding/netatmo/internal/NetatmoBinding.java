/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequest.createKey;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
import org.openhab.binding.netatmo.internal.messages.DeviceListRequest;
import org.openhab.binding.netatmo.internal.messages.DeviceListResponse;
import org.openhab.binding.netatmo.internal.messages.DeviceListResponse.Device;
import org.openhab.binding.netatmo.internal.messages.DeviceListResponse.Module;
import org.openhab.binding.netatmo.internal.messages.MeasurementRequest;
import org.openhab.binding.netatmo.internal.messages.MeasurementResponse;
import org.openhab.binding.netatmo.internal.messages.NetatmoError;
import org.openhab.binding.netatmo.internal.messages.RefreshTokenRequest;
import org.openhab.binding.netatmo.internal.messages.RefreshTokenResponse;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that gets measurements from the Netatmo API every couple of minutes.
 *
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @since 1.4.0
 */
public class NetatmoBinding extends
		AbstractActiveBinding<NetatmoBindingProvider> implements ManagedService {

	private static final String DEFAULT_USER_ID = "DEFAULT_USER";

	private static final Logger logger = LoggerFactory
			.getLogger(NetatmoBinding.class);

	protected static final String CONFIG_CLIENT_ID = "clientid";
	protected static final String CONFIG_CLIENT_SECRET = "clientsecret";
	protected static final String CONFIG_REFRESH = "refresh";
	protected static final String CONFIG_REFRESH_TOKEN = "refreshtoken";
	protected static final String CONFIG_PRESSURE_UNIT = "pressureunit";
	protected static final String CONFIG_UNIT_SYSTEM = "unitsystem";

	/**
	 * The refresh interval which is used to poll values from the Netatmo server
	 * (optional, defaults to 300000ms)
	 */
	private long refreshInterval = 300000;

	private PointType stationPosition = null;

	private Map<String, OAuthCredentials> credentialsCache = new HashMap<String, OAuthCredentials>();

	private NetatmoPressureUnit pressureUnit = NetatmoPressureUnit.DEFAULT_PRESSURE_UNIT;

	private NetatmoUnitSystem unitSystem = NetatmoUnitSystem.DEFAULT_UNIT_SYSTEM;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Netatmo Refresh Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return this.refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void execute() {
		logger.debug("Querying Netatmo API");
		for (String userid : credentialsCache.keySet()) {

			OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
			if (oauthCredentials.noAccessToken()) {
				// initial run after a restart, so get an access token first
				oauthCredentials.refreshAccessToken();
			}

			try {
				if (oauthCredentials.firstExecution) {
					processDeviceList(oauthCredentials);
				}

				DeviceMeasureValueMap deviceMeasureValueMap = processMeasurements(oauthCredentials);
				for (final NetatmoBindingProvider provider : this.providers) {
					for (final String itemName : provider.getItemNames()) {
						final String deviceId = provider.getDeviceId(itemName);
						final String moduleId = provider.getModuleId(itemName);
						final NetatmoMeasureType measureType = provider
								.getMeasureType(itemName);

						State state = null;
						switch (measureType) {
						case MODULENAME:
							if (moduleId == null) // we're on the main device
								for (Device device : oauthCredentials.deviceListResponse
										.getDevices()) {
									if (device.getId().equals(deviceId)) {
										state = new StringType(
												device.getModuleName());
										break;
									}
								}
							else {
								for (Module module : oauthCredentials.deviceListResponse
										.getModules()) {
									if (module.getId().equals(moduleId)) {
										state = new StringType(
												module.getModuleName());
										break;
									}
								}
							}
							break;
						case TIMESTAMP:
							state = deviceMeasureValueMap.timeStamp;
							break;
						case TEMPERATURE:
						case CO2:
						case HUMIDITY:
						case NOISE:
						case PRESSURE:
						case RAIN:
							final String requestKey = createKey(deviceId,
									moduleId);
							BigDecimal value = deviceMeasureValueMap.get(
									requestKey).get(measureType.getMeasure());
							// Protect that sometimes Netatmo returns null where
							// numeric value is awaited (issue #1848)
							if (value != null) {
								if (measureType == NetatmoMeasureType.TEMPERATURE) {
									value = unitSystem.convertTemp(value);
								} else if (measureType == NetatmoMeasureType.RAIN) {
									value = unitSystem.convertRain(value);
								} else if (measureType == NetatmoMeasureType.PRESSURE) {
									value = pressureUnit.convertPressure(value);
								}

								state = new DecimalType(value);
							}
							break;
						case BATTERYVP:
						case RFSTATUS:
							for (Module module : oauthCredentials.deviceListResponse
									.getModules()) {
								if (module.getId().equals(moduleId)) {
									switch (measureType) {
									case BATTERYVP:
										state = new DecimalType(
												module.getBatteryLevel());
										break;
									case RFSTATUS:
										state = new DecimalType(
												module.getRfLevel());
										break;
									case MODULENAME:
										state = new StringType(
												module.getModuleName());
										break;
									}
								}
							}
							break;
						case ALTITUDE:
						case LATITUDE:
						case LONGITUDE:
						case WIFISTATUS:
						case COORDINATE:
						case STATIONNAME:
							for (Device device : oauthCredentials.deviceListResponse
									.getDevices()) {
								if (stationPosition == null) {
									stationPosition = new PointType(
											new DecimalType(
													device.getLatitude()),
											new DecimalType(device
													.getLongitude()),
											new DecimalType(Math.round(unitSystem.
													convertAltitude(device.getAltitude()))));
								}
								if (device.getId().equals(deviceId)) {
									switch (measureType) {
									case LATITUDE:
										state = stationPosition.getLatitude();
										break;
									case LONGITUDE:
										state = stationPosition.getLongitude();
										break;
									case ALTITUDE:
										state = stationPosition.getAltitude();
										break;
									case WIFISTATUS:
										state = new DecimalType(
												device.getWifiLevel());
										break;
									case COORDINATE:
										state = stationPosition;
										break;
									case STATIONNAME:
										state = new StringType(
												device.getStationName());
										break;
									}
								}
							}
							break;
						}

						if (state != null) {
							this.eventPublisher.postUpdate(itemName, state);
						}
					}
				}
			} catch (NetatmoException ne) {
				logger.error(ne.getMessage());
			}
		}
	}

	static class DeviceMeasureValueMap extends
			HashMap<String, Map<String, BigDecimal>> {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		DateTimeType timeStamp = null;
	}

	private DeviceMeasureValueMap processMeasurements(
			OAuthCredentials oauthCredentials) {
		DeviceMeasureValueMap deviceMeasureValueMap = new DeviceMeasureValueMap();

		for (final MeasurementRequest request : createMeasurementRequests()) {
			final MeasurementResponse response = request.execute();

			logger.debug("Request: {}", request);
			logger.debug("Response: {}", response);

			if (response.isError()) {
				final NetatmoError error = response.getError();

				if (error.isAccessTokenExpired()) {
					oauthCredentials.refreshAccessToken();
					execute();
				} else {
					throw new NetatmoException(error.getMessage());
				}

				break; // abort processing measurement requests
			} else {
				processMeasurementResponse(request, response,
						deviceMeasureValueMap);
			}
		}

		return deviceMeasureValueMap;
	}

	private void processDeviceList(OAuthCredentials oauthCredentials) {
		logger.debug("Request: {}", oauthCredentials.deviceListRequest);
		logger.debug("Response: {}", oauthCredentials.deviceListResponse);

		if (oauthCredentials.deviceListResponse.isError()) {
			final NetatmoError error = oauthCredentials.deviceListResponse
					.getError();

			if (error.isAccessTokenExpired()) {
				oauthCredentials.refreshAccessToken();
				execute();
			} else {
				throw new NetatmoException(error.getMessage());
			}

			return; // abort processing
		} else {
			processDeviceListResponse(oauthCredentials.deviceListResponse);
			oauthCredentials.firstExecution = false;
		}
	}

	/**
	 * Processes an incoming {@link DeviceListResponse}.
	 * <p>
	 */
	private void processDeviceListResponse(final DeviceListResponse response) {
		// Prepare a map of all known device measurements
		final Map<String, Device> deviceMap = new HashMap<String, Device>();
		final Map<String, Set<String>> deviceMeasurements = new HashMap<String, Set<String>>();

		for (final Device device : response.getDevices()) {
			final String deviceId = device.getId();
			deviceMap.put(deviceId, device);

			for (final String measurement : device.getMeasurements()) {
				if (!deviceMeasurements.containsKey(deviceId)) {
					deviceMeasurements.put(deviceId, new HashSet<String>());
				}

				deviceMeasurements.get(deviceId).add(measurement);
			}
		}

		// Prepare a map of all known module measurements
		final Map<String, Module> moduleMap = new HashMap<String, Module>();
		final Map<String, Set<String>> moduleMeasurements = new HashMap<String, Set<String>>();

		for (final Module module : response.getModules()) {
			final String moduleId = module.getId();
			moduleMap.put(moduleId, module);

			for (final String measurement : module.getMeasurements()) {
				if (!moduleMeasurements.containsKey(moduleId)) {
					moduleMeasurements.put(moduleId, new HashSet<String>());
				}

				moduleMeasurements.get(moduleId).add(measurement);
			}
		}

		// Remove all configured items from the maps
		for (final NetatmoBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final String deviceId = provider.getDeviceId(itemName);
				final String moduleId = provider.getModuleId(itemName);
				final NetatmoMeasureType measureType = provider
						.getMeasureType(itemName);

				final Set<String> measurements;

				if (moduleId != null) {
					measurements = moduleMeasurements.get(moduleId);
				} else {
					measurements = deviceMeasurements.get(deviceId);
				}

				if (measurements != null) {
					measurements.remove(measureType.getMeasure());
				}
			}
		}

		// Log all unconfigured measurements
		final StringBuilder message = new StringBuilder();
		for (Entry<String, Set<String>> entry : deviceMeasurements.entrySet()) {
			final String deviceId = entry.getKey();
			final Device device = deviceMap.get(deviceId);

			for (String measurement : entry.getValue()) {
				message.append("\t" + deviceId + "#" + measurement + " ("
						+ device.getModuleName() + ")\n");
			}
		}

		for (Entry<String, Set<String>> entry : moduleMeasurements.entrySet()) {
			final String moduleId = entry.getKey();
			final Module module = moduleMap.get(moduleId);

			for (String measurement : entry.getValue()) {
				message.append("\t" + module.getMainDevice() + "#" + moduleId
						+ "#" + measurement + " (" + module.getModuleName()
						+ ")\n");
			}
		}
		if (message.length() > 0) {
			message.insert(0,
					"The following Netatmo measurements are not yet configured:\n");
			logger.info(message.toString());
		}
	}

	/**
	 * Creates the necessary requests to query the Netatmo API for all measures
	 * that have a binding. One request can query all measures of a single
	 * device or module.
	 */
	private Collection<MeasurementRequest> createMeasurementRequests() {
		final Map<String, MeasurementRequest> requests = new HashMap<String, MeasurementRequest>();

		for (final NetatmoBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {

				final String userid = provider.getUserid(itemName);
				final String deviceId = provider.getDeviceId(itemName);
				final String moduleId = provider.getModuleId(itemName);
				final NetatmoMeasureType measureType = provider
						.getMeasureType(itemName);

				final String requestKey = createKey(deviceId, moduleId);

				switch (measureType) {
				case TEMPERATURE:
				case CO2:
				case HUMIDITY:
				case NOISE:
				case PRESSURE:
				case RAIN:
					OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
					if (oauthCredentials != null) {
						if (!requests.containsKey(requestKey)) {
							requests.put(requestKey, new MeasurementRequest(
									oauthCredentials.accessToken, deviceId,
									moduleId));
						}
						requests.get(requestKey).addMeasure(measureType);
						break;
					}
				default:
					break;
				}
			}
		}

		return requests.values();
	}

	private void processMeasurementResponse(final MeasurementRequest request,
			final MeasurementResponse response,
			DeviceMeasureValueMap deviceMeasureValueMap) {
		final List<BigDecimal> values = response.getBody().get(0).getValues()
				.get(0);
		final Map<String, BigDecimal> valueMap = new HashMap<String, BigDecimal>();

		int index = 0;
		for (final String measure : request.getMeasures()) {
			final BigDecimal value = values.get(index);
			valueMap.put(measure, value);
			index++;
		}

		deviceMeasureValueMap.put(request.getKey(), valueMap);
		deviceMeasureValueMap.timeStamp = new DateTimeType(
				response.getTimeStamp());
	}

	/**
	 * Returns the cached {@link OAuthCredentials} for the given {@code userid}.
	 * If their is no such cached {@link OAuthCredentials} element, the cache is
	 * searched with the {@code DEFAULT_USER}. If there is still no cached
	 * element found {@code NULL} is returned.
	 * 
	 * @param userid
	 *            the userid to find the {@link OAuthCredentials}
	 * @return the cached {@link OAuthCredentials} or {@code NULL}
	 */
	private OAuthCredentials getOAuthCredentials(String userid) {
		if (credentialsCache.containsKey(userid)) {
			return credentialsCache.get(userid);
		} else {
			return credentialsCache.get(DEFAULT_USER_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(final Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			final String refreshIntervalString = (String) config
					.get(CONFIG_REFRESH);
			if (isNotBlank(refreshIntervalString)) {
				this.refreshInterval = Long.parseLong(refreshIntervalString);
			}

			Enumeration<String> configKeys = config.keys();
			while (configKeys.hasMoreElements()) {
				String configKey = configKeys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if (CONFIG_REFRESH.equals(configKey)
						|| "service.pid".equals(configKey)) {
					continue;
				}

				String userid;
				String configKeyTail;

				if (configKey.contains(".")) {
					String[] keyElements = configKey.split("\\.");
					userid = keyElements[0];
					configKeyTail = keyElements[1];

				} else {
					userid = DEFAULT_USER_ID;
					configKeyTail = configKey;
				}

				OAuthCredentials credentials = credentialsCache.get(userid);
				if (credentials == null) {
					credentials = new OAuthCredentials();
					credentialsCache.put(userid, credentials);
				}

				String value = (String) config.get(configKeyTail);

				if (CONFIG_CLIENT_ID.equals(configKeyTail)) {
					credentials.clientId = value;
				} else if (CONFIG_CLIENT_SECRET.equals(configKeyTail)) {
					credentials.clientSecret = value;
				} else if (CONFIG_REFRESH_TOKEN.equals(configKeyTail)) {
					credentials.refreshToken = value;
				} else if (CONFIG_PRESSURE_UNIT.equals(configKeyTail)) {
					try {
						pressureUnit = NetatmoPressureUnit.fromString(value);
					} catch (IllegalArgumentException e) {
						throw new ConfigurationException(configKey,
								"the value '" + value
										+ "' is not valid for the configKey '"
										+ configKey +"'");
					}
				} else if (CONFIG_UNIT_SYSTEM.equals(configKeyTail)) {
					try {
						unitSystem = NetatmoUnitSystem.fromString(value);
					} catch (IllegalArgumentException e) {
						throw new ConfigurationException(configKey,
								"the value '" + value
										+ "' is not valid for the configKey '"
										+ configKey +"'");
					}
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey
									+ "' is unknown");
				}
			}

			setProperlyConfigured(true);
		}
	}

	/**
	 * This internal class holds the different crendentials necessary for the
	 * OAuth2 flow to work. It also provides basic methods to refresh the access
	 * token.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.6.0
	 */
	static class OAuthCredentials {

		/**
		 * The client id to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client
		 *      Credentials</a>
		 */
		String clientId;

		/**
		 * The client secret to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client
		 *      Credentials</a>
		 */
		String clientSecret;

		/**
		 * The refresh token to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client&nbsp;Credentials</a>
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh&nbsp;Token</a>
		 */
		String refreshToken;

		/**
		 * The access token to access the Netatmo API. Automatically renewed
		 * from the API using the refresh token.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh
		 *      Token</a>
		 * @see #refreshAccessToken()
		 */
		String accessToken;

		DeviceListResponse deviceListResponse = null;
		DeviceListRequest deviceListRequest = null;

		boolean firstExecution = true;

		public boolean noAccessToken() {
			return this.accessToken == null;
		}

		public void refreshAccessToken() {
			logger.debug("Refreshing access token.");

			final RefreshTokenRequest request = new RefreshTokenRequest(
					this.clientId, this.clientSecret, this.refreshToken);
			logger.debug("Request: {}", request);

			final RefreshTokenResponse response = request.execute();
			logger.debug("Response: {}", response);

			this.accessToken = response.getAccessToken();

			deviceListRequest = new DeviceListRequest(this.accessToken);
			deviceListResponse = deviceListRequest.execute();
		}

	}

}
