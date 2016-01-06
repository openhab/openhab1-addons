/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import org.openhab.binding.nest.NestBindingProvider;
import org.openhab.binding.nest.internal.messages.AbstractRequest;
import org.openhab.binding.nest.internal.messages.AccessTokenRequest;
import org.openhab.binding.nest.internal.messages.AccessTokenResponse;
import org.openhab.binding.nest.internal.messages.DataModel;
import org.openhab.binding.nest.internal.messages.DataModelRequest;
import org.openhab.binding.nest.internal.messages.DataModelResponse;
import org.openhab.binding.nest.internal.messages.UpdateDataModelRequest;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that retrieves information about objects we're interested in every few minutes, and sends updates and
 * commands to Nest as they are made.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public class NestBinding extends AbstractActiveBinding<NestBindingProvider> implements ManagedService {

	private static final String DEFAULT_USER_ID = "DEFAULT_USER";

	private static final Logger logger = LoggerFactory.getLogger(NestBinding.class);

	protected static final String CONFIG_REFRESH = "refresh";
	protected static final String CONFIG_CLIENT_ID = "client_id";
	protected static final String CONFIG_CLIENT_SECRET = "client_secret";
	protected static final String CONFIG_PIN_CODE = "pin_code";
	protected static final String CONFIG_TIMEOUT = "timeout";

	/**
	 * the refresh interval which is used to poll values from the Nest server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * A map of userids from the openhab.cfg file to OAuth credentials used to communicate with each app instance.
	 * Multiple accounts is not implemented in the Nest binding due to the current prohibition 3 in the Terms of
	 * Service, but some code is here for if/when that restriction is lifted.
	 */
	private Map<String, OAuthCredentials> credentialsCache = new HashMap<String, OAuthCredentials>();

	/**
	 * used to store events that we have sent ourselves; we need to remember them for not reacting to them
	 */
	private static class Update {
		private String itemName;
		private State state;

		Update(final String itemName, final State state) {
			this.itemName = itemName;
			this.state = state;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Update)) {
				return false;
			}
			return (this.itemName == null ? ((Update) o).itemName == null : this.itemName.equals(((Update) o).itemName))
					&& (this.state == null ? ((Update) o).state == null : this.state.equals(((Update) o).state));
		}

		@Override
		public int hashCode() {
			return (this.itemName == null ? 0 : this.itemName.hashCode())
					^ (this.state == null ? 0 : this.state.hashCode());
		}
	}

	private List<Update> ignoreEventList = Collections.synchronizedList(new ArrayList<Update>());

	/**
	 * The most recently received data model, or <code>null</code> if none have been retrieved yet.
	 */
	private DataModel oldDataModel = null;

	public NestBinding() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		super.activate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Nest Refresh Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.trace("Querying Nest API");

		try {
			for (String userid : credentialsCache.keySet()) {
				OAuthCredentials oauthCredentials = getOAuthCredentials(userid);

				if (oauthCredentials.noAccessToken()) {
					if (!oauthCredentials.retrieveAccessToken()) {
						logger.warn("Periodic poll skipped.");
						continue;
					}
				}

				readNest(oauthCredentials);
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.warn("Exception reading from Nest.", e);
			} else {
				logger.warn("Exception reading from Nest: {}", e.getMessage());
			}
		}
	}

	/**
	 * Given the credentials to use and what to select from the Nest API, read any changed information from Nest and
	 * update the affected items.
	 * 
	 * @param oauthCredentials
	 *            the credentials to use
	 */
	private void readNest(OAuthCredentials oauthCredentials) throws Exception {

		DataModelRequest dmreq = new DataModelRequest(oauthCredentials.accessToken);
		DataModelResponse dmres = dmreq.execute();

		if (dmres.isError()) {
			logger.error("Error retrieving data model: {}", dmres.getError());
			return;
		} else {
			logger.trace("Retrieved data model: {}", dmres);
		}

		DataModel newDataModel = dmres;
		this.oldDataModel = newDataModel;

		// Iterate through bindings and update all inbound values.
		for (final NestBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				if (provider.isInBound(itemName)) {
					final String property = provider.getProperty(itemName);
					final State newState = getState(newDataModel, property);

					logger.trace("Updating itemName '{}' with newState '{}'", itemName, newState);

					/*
					 * we need to make sure that we won't send out this event to Nest again, when receiving it on the
					 * openHAB bus
					 */
					ignoreEventList.add(new Update(itemName, newState));
					logger.trace("Added event (item='{}', newState='{}') to the ignore event list (size={})",
							itemName, newState, ignoreEventList.size());
					this.eventPublisher.postUpdate(itemName, newState);
				}
			}
		}
	}

	/**
	 * Give a binding provider, a data model, and an item name, return the corresponding state object.
	 * 
	 * @param provider
	 *            the Nest binding provider
	 * @param dataModel
	 *            a data model from which to retrieve the value
	 * @param itemName
	 *            the item name from the items file.
	 * @return the State object for the named item
	 */
	private State getState(final DataModel dataModel, final String property) {

		if (dataModel != null) {
			try {
				return createState(dataModel.getProperty(property));
			} catch (Exception e) {
				logger.error("Unable to get state from data model", e);
			}
		}
		return UnDefType.NULL;
	}

	/**
	 * Creates an openHAB {@link State} in accordance to the class of the given {@code propertyValue}. Currently
	 * {@link Date}, {@link BigDecimal}, {@link Temperature} and {@link Boolean} are handled explicitly. All other
	 * {@code dataTypes} are mapped to {@link StringType}.
	 * <p>
	 * If {@code propertyValue} is {@code null}, {@link UnDefType#NULL} will be returned.
	 * 
	 * Copied/adapted from the Koubachi binding.
	 * 
	 * @param propertyValue
	 * 
	 * @return the new {@link State} in accordance with {@code dataType}. Will never be {@code null}.
	 */
	private State createState(Object propertyValue) {
		if (propertyValue == null) {
			return UnDefType.NULL;
		}

		Class<?> dataType = propertyValue.getClass();

		if (Date.class.isAssignableFrom(dataType)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) propertyValue);
			return new DateTimeType(calendar);
		} else if (Integer.class.isAssignableFrom(dataType)) {
			return new DecimalType((Integer) propertyValue);
		} else if (BigDecimal.class.isAssignableFrom(dataType)) {
			return new DecimalType((BigDecimal) propertyValue);
		} else if (Boolean.class.isAssignableFrom(dataType)) {
			if ((Boolean) propertyValue) {
				return OnOffType.ON;
			} else {
				return OnOffType.OFF;
			}
		} else {
			return new StringType(propertyValue.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.trace("internalReceiveCommand(item='{}', command='{}')", itemName, command);
		commandNest(itemName, command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(final String itemName, final State newState) {
		logger.trace("Received update (item='{}', state='{}')", itemName, newState);
		if (!isEcho(itemName, newState)) {
			updateNest(itemName, newState);
		}
	}

	/**
	 * Perform the given {@code command} against all targets referenced in {@code itemName}.
	 * 
	 * @param command
	 *            the command to execute
	 * @param the
	 *            target(s) against which to execute this command
	 */
	private void commandNest(final String itemName, final Command command) {
		if (command instanceof State) {
			updateNest(itemName, (State) command);
		}
	}

	private boolean isEcho(String itemName, State state) {
		if (ignoreEventList.remove(new Update(itemName, state))) {
			logger.debug(
					"We received this event (item='{}', state='{}') from Nest, so we don't send it back again -> ignore!",
					itemName, state);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Send the {@code newState} for the given {@code itemName} to Nest.
	 * 
	 * @param itemName
	 * @param newState
	 */
	private void updateNest(final String itemName, final State newState) {

		// Find the first binding provider for this itemName.
		NestBindingProvider provider = null;
		String property = null;
		for (NestBindingProvider p : this.providers) {
			property = p.getProperty(itemName);
			if (property != null) {
				provider = p;
				break;
			}
		}

		if (provider == null) {
			logger.warn("no matching binding provider found [itemName={}, newState={}]", itemName, newState);
			return;
		}

		if (!provider.isOutBound(itemName)) {
			logger.warn("attempt to update non-outbound item skipped [itemName={}, newState={}]", itemName, newState);
			return;
		}

		try {
			logger.debug("About to set property '{}' to '{}'", property, newState);

			// Ask the old DataModel to generate a new DataModel that only contains the update we want to send
			DataModel updateDataModel = oldDataModel.updateDataModel(property, newState);

			logger.trace("Data model for update: {}", updateDataModel);

			if (updateDataModel == null) {
				return;
			}

			OAuthCredentials oauthCredentials = getOAuthCredentials(DEFAULT_USER_ID);

			if (oauthCredentials == null) {
				logger.warn("Unable to locate credentials for item {}; aborting update.", itemName);
				return;
			}

			// If we don't have an access token yet, retrieve one.
			if (oauthCredentials.noAccessToken()) {
				if (!oauthCredentials.retrieveAccessToken()) {
					logger.warn("Sending update skipped.");
					return;
				}
			}

			UpdateDataModelRequest request = new UpdateDataModelRequest(oauthCredentials.accessToken, updateDataModel);
			DataModelResponse response = request.execute();
			if (response.isError()) {
				logger.error("Error updating data model: {}", response);
			}
		} catch (Exception e) {
			logger.error("Unable to update data model", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {

		if (provider instanceof NestBindingProvider) {
			this.oldDataModel = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {

		if (provider instanceof NestBindingProvider) {
			this.oldDataModel = null;
		}
	}

	/**
	 * Returns the cached {@link OAuthCredentials} for the given {@code userid}. If their is no such cached
	 * {@link OAuthCredentials} element, the cache is searched with the {@code DEFAULT_USER_ID}. If there is still no
	 * cached element found {@code NULL} is returned.
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
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like nest:refresh=120000
			String refreshIntervalString = (String) config.get(CONFIG_REFRESH);
			if (isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// to override the default HTTP request timeout one has to add a
			// parameter to openhab.cfg like nest:timeout=20000
			String timeoutString = (String) config.get(CONFIG_TIMEOUT);
			if (isNotBlank(timeoutString)) {
				AbstractRequest.setHttpRequestTimeout(Integer.parseInt(timeoutString));
			}

			Enumeration<String> configKeys = config.keys();
			while (configKeys.hasMoreElements()) {
				String configKey = (String) configKeys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if (CONFIG_REFRESH.equals(configKey) || CONFIG_TIMEOUT.equals(configKey) || "service.pid".equals(configKey)) {
					continue;
				}

				String userid = DEFAULT_USER_ID;
				String configKeyTail = configKey;

				OAuthCredentials credentials = credentialsCache.get(userid);
				if (credentials == null) {
					credentials = new OAuthCredentials(userid);
					credentialsCache.put(userid, credentials);
				}

				String value = (String) config.get(configKey);

				if (CONFIG_CLIENT_ID.equals(configKeyTail)) {
					credentials.clientId = value;
				} else if (CONFIG_CLIENT_SECRET.equals(configKeyTail)) {
					credentials.clientSecret = value;
				} else if (CONFIG_PIN_CODE.equals(configKeyTail)) {
					credentials.pinCode = value;
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}

			// Verify the completeness of each OAuthCredentials entry
			// to make sure we can get started.

			boolean properlyConfigured = true;

			for (String userid : credentialsCache.keySet()) {
				OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
				String userString = (DEFAULT_USER_ID.equals(userid)) ? "" : (userid + ".");
				if (oauthCredentials.clientId == null) {
					logger.error("Required nest:{}{} is missing.", userString, CONFIG_CLIENT_ID);
					properlyConfigured = false;
					break;
				}
				if (oauthCredentials.clientSecret == null) {
					logger.error("Required nest:{}{} is missing.", userString, CONFIG_CLIENT_SECRET);
					properlyConfigured = false;
					break;
				}
				if (oauthCredentials.pinCode == null) {
					logger.error("Required nest:{}{} is missing.", userString, CONFIG_PIN_CODE);
					properlyConfigured = false;
					break;
				}
				// Load persistently stored values for this credential set
				oauthCredentials.load();
			}

			setProperlyConfigured(properlyConfigured);
		}
	}

	/**
	 * This internal class holds the credentials necessary for the OAuth2 flow to work. It also provides basic methods
	 * to retrieve an access token.
	 * 
	 * @author John Cocula
	 * @since 1.7.0
	 */
	static class OAuthCredentials {

		private static final String ACCESS_TOKEN = "accessToken";
		private static final String PIN_CODE = "pinCode";

		private String userid;

		/**
		 * The private client_id needed in order to interact with the Nest API. This must be provided in the
		 * <code>openhab.cfg</code> file.
		 */
		private String clientId;

		/**
		 * The client_secret needed when authorizing this client to the Nest API.
		 * 
		 * @see AccessTokenRequest
		 */
		private String clientSecret;

		/**
		 * The pincode needed when authorizing this client to the Nest API.
		 * 
		 * @see AccessTokenRequest
		 */
		private String pinCode;

		/**
		 * The access token to access the Nest API. Automatically renewed from the API using the refresh token and
		 * persisted for use across activations.
		 * 
		 * @see #refreshTokens()
		 */
		private String accessToken;

		public OAuthCredentials(String userid) {

			try {
				this.userid = userid;
			} catch (Exception e) {
				throw new NestException("Cannot create OAuthCredentials.", e);
			}
		}

		private Preferences getPrefsNode() {
			return Preferences.userRoot().node("org.openhab.nest." + userid);
		}

		/**
		 * Only load the accessToken if the pinCode that was saved with it matches the current pinCode. Otherwise, we
		 * could continue to try to use an accessToken that does not match the credentials in openhab.cfg.
		 */
		private void load() {
			Preferences prefs = getPrefsNode();
			String pinCode = prefs.get(PIN_CODE, null);
			if (this.pinCode.equals(pinCode)) {
				this.accessToken = prefs.get(ACCESS_TOKEN, null);
			}
		}

		private void save() {
			Preferences prefs = getPrefsNode();
			if (this.accessToken != null) {
				prefs.put(ACCESS_TOKEN, this.accessToken);
			} else {
				prefs.remove(ACCESS_TOKEN);
			}
			if (this.pinCode != null) {
				prefs.put(PIN_CODE, this.pinCode);
			} else {
				prefs.remove(PIN_CODE);
			}
		}

		/**
		 * Determine if we have an access token.
		 * 
		 * @return <code>true</code> if we have an access token; <code>false</code> otherwise.
		 */
		public boolean noAccessToken() {
			return this.accessToken == null;
		}

		/**
		 * Retrieve an access token from the Nest API.
		 * 
		 * @return <code>true</code> if we were successful, <code>false</code> otherwise
		 */
		public boolean retrieveAccessToken() {
			logger.trace("Retrieving access token in order to access the Nest API.");

			final AccessTokenRequest request = new AccessTokenRequest(clientId, clientSecret, pinCode);
			logger.trace("Request: {}", request);

			final AccessTokenResponse response = request.execute();
			logger.trace("Response: {}", response);

			if (response.isError()) {
				logger.error("Error retrieving access token: {}'", response);
			}

			this.accessToken = response.getAccessToken();
			save();

			return !noAccessToken();
		}
	}
}
