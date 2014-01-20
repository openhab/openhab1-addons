/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequest.createKey;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
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
 * @since 1.4.0
 */
public class NetatmoBinding extends
		AbstractActiveBinding<NetatmoBindingProvider> implements ManagedService {

	protected static final String CONFIG_CLIENT_ID = "clientid";

	protected static final String CONFIG_CLIENT_SECRET = "clientsecret";

	protected static final String CONFIG_REFRESH = "refresh";

	protected static final String CONFIG_REFRESH_TOKEN = "refreshToken";

	private static final Logger logger = LoggerFactory
			.getLogger(NetatmoBinding.class);

	/**
	 * The refresh interval which is used to poll values from the Netatmo server
	 * (optional, defaults to 300000ms)
	 */
	private long refreshInterval = 300000;

	/**
	 * The client id to access the Netatmo API. Normally set in
	 * <code>openhab.cfg</code>.
	 * 
	 * @see <a href="http://dev.netatmo.com/doc/authentication/usercred">Client
	 *      Credentials</a>
	 */
	private String clientId;

	/**
	 * The client secret to access the Netatmo API. Normally set in
	 * <code>openhab.cfg</code>.
	 * 
	 * @see <a href="http://dev.netatmo.com/doc/authentication/usercred">Client
	 *      Credentials</a>
	 */
	private String clientSecret;

	/**
	 * The refresh token to access the Netatmo API. Normally set in
	 * <code>openhab.cfg</code>.
	 * 
	 * @see <a
	 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client&nbsp;Credentials</a>
	 * @see <a
	 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh&nbsp;Token</a>
	 */
	private String refreshToken;

	/**
	 * The access token to access the Netatmo API. Automatically renewed from
	 * the API using the refresh token.
	 * 
	 * @see <a
	 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh
	 *      Token</a>
	 * @see #refreshAccessToken()
	 */
	private String accessToken;

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

			this.clientId = (String) config.get(CONFIG_CLIENT_ID);
			if (isBlank(this.clientId)) {
				throw new ConfigurationException("netatmo:clientid",
						"Parameter 'netatmo:clientid' must be set!");
			}

			this.clientSecret = (String) config.get(CONFIG_CLIENT_SECRET);
			if (isBlank(this.clientSecret)) {
				throw new ConfigurationException("netatmo:clientsecret",
						"Parameter 'netatmo:clientsecret' must be set!");
			}

			this.refreshToken = (String) config.get(CONFIG_REFRESH_TOKEN);
			if (isBlank(this.refreshToken)) {
				throw new ConfigurationException("netatmo:refreshToken",
						"Parameter 'netatmo:refreshToken' must be set!");
			}

			setProperlyConfigured(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("Querying Netatmo API");

		if (this.accessToken == null) {
			// initial run after a restart, so get an access token first
			refreshAccessToken();
		}

		final Map<String, Map<String, BigDecimal>> deviceMeasureValueMap = new HashMap<String, Map<String, BigDecimal>>();

		for (final MeasurementRequest request : createMeasurementRequests()) {
			final MeasurementResponse response = request.execute();

			logger.debug("Request: {}", request);
			logger.debug("Response: {}", response);

			if (response.isError()) {
				final NetatmoError error = response.getError();

				if (error.isAccessTokenExpired()) {
					refreshAccessToken();
					execute();
				} else {
					logger.error(error.getMessage());
				}

				return; // abort processing
			} else {
				final List<BigDecimal> values = response.getBody().get(0)
						.getValues().get(0);

				final Map<String, BigDecimal> valueMap = new HashMap<String, BigDecimal>();

				int index = 0;
				for (final String measure : request.getMeasures()) {
					final BigDecimal value = values.get(index);
					valueMap.put(measure, value);
					index++;
				}

				deviceMeasureValueMap.put(request.getKey(), valueMap);
			}
		}

		for (final NetatmoBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final String deviceId = provider.getDeviceId(itemName);
				final String moduleId = provider.getModuleId(itemName);
				final String measure = provider.getMeasure(itemName);

				final String requestKey = createKey(deviceId, moduleId);

				final State state = new DecimalType(deviceMeasureValueMap.get(
						requestKey).get(measure));
				if (state != null) {
					this.eventPublisher.postUpdate(itemName, state);
				}
			}
		}
	}

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
	 * Creates the necessary requests to query the Netatmo API for all measures
	 * that have a binding. One request can query all measures of a single
	 * device or module.
	 */
	private Collection<MeasurementRequest> createMeasurementRequests() {
		final Map<String, MeasurementRequest> requests = new HashMap<String, MeasurementRequest>();

		for (final NetatmoBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final String deviceId = provider.getDeviceId(itemName);
				final String moduleId = provider.getModuleId(itemName);
				final String measure = provider.getMeasure(itemName);

				final String requestKey = createKey(deviceId, moduleId);

				if (!requests.containsKey(requestKey)) {
					requests.put(requestKey, new MeasurementRequest(
							this.accessToken, deviceId, moduleId));
				}

				requests.get(requestKey).addMeasure(measure);
			}
		}

		return requests.values();
	}

	private void refreshAccessToken() {
		logger.debug("Refreshing access token.");

		final RefreshTokenRequest request = new RefreshTokenRequest(
				this.clientId, this.clientSecret, this.refreshToken);
		logger.debug("Request: {}", request);

		final RefreshTokenResponse response = request.execute();
		logger.debug("Response: {}", response);

		this.accessToken = response.getAccessToken();
	}

}
