/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
import org.openhab.core.library.types.DecimalType;
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
 * @since 1.4.0
 */
public class NetatmoBinding extends
		AbstractActiveBinding<NetatmoBindingProvider> implements ManagedService {
	
	private static final String DEFAULT_USER_ID = "DEFAULT_USER";

	private static final Logger logger = 
		LoggerFactory.getLogger(NetatmoBinding.class);

	protected static final String CONFIG_CLIENT_ID = "clientid";
	protected static final String CONFIG_CLIENT_SECRET = "clientsecret";
	protected static final String CONFIG_REFRESH = "refresh";
	protected static final String CONFIG_REFRESH_TOKEN = "refreshtoken";

	/**
	 * The refresh interval which is used to poll values from the Netatmo server
	 * (optional, defaults to 300000ms)
	 */
	private long refreshInterval = 300000;

	private Map<String, OAuthCredentials> credentialsCache = new HashMap<String, OAuthCredentials>();
	
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

				Map<String, Map<String, BigDecimal>> deviceMeasureValueMap = processMeasurements(oauthCredentials);
				for (final NetatmoBindingProvider provider : this.providers) {
					for (final String itemName : provider.getItemNames()) {
						final String deviceId = provider.getDeviceId(itemName);
						final String moduleId = provider.getModuleId(itemName);
						final String measure = provider.getMeasure(itemName);

						final String requestKey = createKey(deviceId, moduleId);

						final State state = new DecimalType(deviceMeasureValueMap.get(requestKey).get(measure));
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

	private Map<String, Map<String, BigDecimal>> processMeasurements(OAuthCredentials oauthCredentials) {
		Map<String, Map<String, BigDecimal>> deviceMeasureValueMap = new HashMap<String, Map<String,BigDecimal>>();
		
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
				processMeasurementResponse(request, response, deviceMeasureValueMap);
			}
		}
		
		return deviceMeasureValueMap;
	}
	
	private void processDeviceList(OAuthCredentials oauthCredentials) {
		final DeviceListRequest request = new DeviceListRequest(oauthCredentials.accessToken);
		final DeviceListResponse response = request.execute();

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

			return; // abort processing
		} else {
			processDeviceListResponse(response);
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
				final String measure = provider.getMeasure(itemName);

				final Set<String> measurements;

				if (moduleId != null) {
					measurements = moduleMeasurements.get(moduleId);
				} else {
					measurements = deviceMeasurements.get(deviceId);
				}

				if (measurements != null) {
					measurements.remove(measure);
				}
			}
		}

		// Log all unconfigured measurements
		final StringBuilder message = new StringBuilder();
		message.append("The following Netatmo measurements are not yet configured:\n");
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

		logger.info(message.toString());
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
				final String measure = provider.getMeasure(itemName);

				final String requestKey = createKey(deviceId, moduleId);
				
				OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
				if (oauthCredentials != null) {
					if (!requests.containsKey(requestKey)) {
						requests.put(requestKey, 
							new MeasurementRequest(oauthCredentials.accessToken, deviceId, moduleId));
					}
					requests.get(requestKey).addMeasure(measure);
				}
			}
		}

		return requests.values();
	}
	
	private void processMeasurementResponse(final MeasurementRequest request, final MeasurementResponse response, Map<String, Map<String, BigDecimal>> deviceMeasureValueMap) {
		final List<BigDecimal> values = response.getBody().get(0).getValues().get(0);
		final Map<String, BigDecimal> valueMap = new HashMap<String, BigDecimal>();

		int index = 0;
		for (final String measure : request.getMeasures()) {
			final BigDecimal value = values.get(index);
			valueMap.put(measure, value);
			index++;
		}

		deviceMeasureValueMap.put(request.getKey(), valueMap);
	}
	
	/**
	 * Returns the cached {@link OAuthCredentials} for the given {@code userid}.
	 * If their is no such cached {@link OAuthCredentials} element, the cache is
	 * searched with the {@code DEFAULT_USER}. If there is still no cached element
	 * found {@code NULL} is returned.
	 *  
	 * @param userid the userid to find the {@link OAuthCredentials}
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
	public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			final String refreshIntervalString = (String) config.get(CONFIG_REFRESH);
			if (isNotBlank(refreshIntervalString)) {
				this.refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			Enumeration<String> configKeys = config.keys();
			while (configKeys.hasMoreElements()) {
				String configKey = (String) configKeys.nextElement();
				
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if (CONFIG_REFRESH.equals(configKey) || "service.pid".equals(configKey)) {
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
				}
				else if (CONFIG_CLIENT_SECRET.equals(configKeyTail)) {
					credentials.clientSecret= value;
				}
				else if (CONFIG_REFRESH_TOKEN.equals(configKeyTail)) {
					credentials.refreshToken = value;
				}
				else {
					throw new ConfigurationException(
						configKey, "the given configKey '" + configKey + "' is unknown");
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
		 * @see <a href="http://dev.netatmo.com/doc/authentication/usercred">Client
		 *      Credentials</a>
		 */
		String clientId;

		/**
		 * The client secret to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a href="http://dev.netatmo.com/doc/authentication/usercred">Client
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
		 * The access token to access the Netatmo API. Automatically renewed from
		 * the API using the refresh token.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh
		 *      Token</a>
		 * @see #refreshAccessToken()
		 */
		String accessToken;
		
		boolean firstExecution = true;
		
		public boolean noAccessToken() {
			return this.accessToken == null;
		}
		
		public void refreshAccessToken() {
			logger.debug("Refreshing access token.");

			final RefreshTokenRequest request = 
				new RefreshTokenRequest(this.clientId, this.clientSecret, this.refreshToken);
			logger.debug("Request: {}", request);

			final RefreshTokenResponse response = request.execute();
			logger.debug("Response: {}", response);

			this.accessToken = response.getAccessToken();
		}
		
	}

}
