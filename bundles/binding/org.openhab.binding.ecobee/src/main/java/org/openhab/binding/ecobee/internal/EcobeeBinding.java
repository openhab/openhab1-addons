/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.openhab.binding.ecobee.EcobeeBindingProvider;
import org.openhab.binding.ecobee.internal.messages.AbstractFunction;
import org.openhab.binding.ecobee.internal.messages.ApiResponse;
import org.openhab.binding.ecobee.internal.messages.AuthorizeRequest;
import org.openhab.binding.ecobee.internal.messages.AuthorizeResponse;
import org.openhab.binding.ecobee.internal.messages.Request;
import org.openhab.binding.ecobee.internal.messages.Selection;
import org.openhab.binding.ecobee.internal.messages.Selection.SelectionType;
import org.openhab.binding.ecobee.internal.messages.Status;
import org.openhab.binding.ecobee.internal.messages.Temperature;
import org.openhab.binding.ecobee.internal.messages.Thermostat;
import org.openhab.binding.ecobee.internal.messages.Thermostat.Climate;
import org.openhab.binding.ecobee.internal.messages.Thermostat.EquipmentSetting;
import org.openhab.binding.ecobee.internal.messages.Thermostat.GeneralSetting;
import org.openhab.binding.ecobee.internal.messages.Thermostat.HouseDetails;
import org.openhab.binding.ecobee.internal.messages.Thermostat.LimitSetting;
import org.openhab.binding.ecobee.internal.messages.Thermostat.Location;
import org.openhab.binding.ecobee.internal.messages.Thermostat.NotificationSettings;
import org.openhab.binding.ecobee.internal.messages.Thermostat.Program;
import org.openhab.binding.ecobee.internal.messages.Thermostat.Settings;
import org.openhab.binding.ecobee.internal.messages.ThermostatRequest;
import org.openhab.binding.ecobee.internal.messages.ThermostatResponse;
import org.openhab.binding.ecobee.internal.messages.ThermostatSummaryRequest;
import org.openhab.binding.ecobee.internal.messages.ThermostatSummaryResponse;
import org.openhab.binding.ecobee.internal.messages.ThermostatSummaryResponse.Revision;
import org.openhab.binding.ecobee.internal.messages.RefreshTokenRequest;
import org.openhab.binding.ecobee.internal.messages.TokenRequest;
import org.openhab.binding.ecobee.internal.messages.TokenResponse;
import org.openhab.binding.ecobee.internal.messages.UpdateThermostatRequest;

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
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that retrieves information about thermostats we're interested in
 * every few minutes, and sends updates and commands to Ecobee as they are made.
 * Reviewed lots of other binding implementations, particularly Netatmo and
 * XBMC.
 * 
 * @author John Cocula
 * @since 1.6.0
 */
public class EcobeeBinding extends AbstractActiveBinding<EcobeeBindingProvider>
		implements ManagedService {

	private static final String DEFAULT_USER_ID = "DEFAULT_USER";

	private static final Logger logger = LoggerFactory
			.getLogger(EcobeeBinding.class);

	protected static final String CONFIG_REFRESH = "refresh";
	protected static final String CONFIG_APP_KEY = "appkey";
	protected static final String CONFIG_SCOPE = "scope";
	protected static final String CONFIG_TEMP_SCALE = "tempscale";

	private ConfigurationAdmin configAdmin;

	public void addConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	public void removeConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = null;
	}

	/**
	 * the refresh interval which is used to poll values from the Ecobee server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * A map of userids from the openhab.cfg file to OAuth credentials used to
	 * communicate with each app instance.
	 */
	private Map<String, OAuthCredentials> credentialsCache = new HashMap<String, OAuthCredentials>();

	/**
	 * used to store events that we have sent ourselves; we need to remember
	 * them for not reacting to them
	 */
	private List<String> ignoreEventList = new ArrayList<String>();

	/**
	 * The most recently received list of revisions, or an empty Map if none
	 * have been retrieved yet.
	 * 
	 * @see <a href="http://dev.netatmo.com/doc/authentication/usercred">Client
	 *      Credentials</a>
	 */
	private Map<String, Revision> lastRevisionMap = new HashMap<String, Revision>();

	public EcobeeBinding() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		super.activate();
		setProperlyConfigured(true);
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
		return "Ecobee Refresh Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("Querying Ecobee API");

		for (String userid : credentialsCache.keySet()) {
			OAuthCredentials oauthCredentials = getOAuthCredentials(userid);

			Selection selection = createSelection(oauthCredentials);
			if (selection == null) {
				logger.debug(
						"Nothing to retrieve for '{}'; skipping thermostat retrieval.",
						oauthCredentials.userid);
				continue;
			}

			if (oauthCredentials.noAccessToken()) {
				if (!oauthCredentials.refreshTokens()) {
					logger.warn("Periodic poll skipped for '{}'.",
							oauthCredentials.userid);
					continue;
				}
			}

			readEcobee(oauthCredentials, selection);
		}
	}

	/**
	 * Given the credentials to use and what to select from the Ecobee API, read
	 * any changed information from Ecobee and update the affected items.
	 * 
	 * @param oauthCredentials
	 *            the credentials to use
	 * @param selection
	 *            the selection of data to retrieve
	 */
	private void readEcobee(OAuthCredentials oauthCredentials,
			Selection selection) {

		logger.debug("Requesting summaries for {}", selection);

		ThermostatSummaryRequest request = new ThermostatSummaryRequest(
				oauthCredentials.accessToken, selection);
		ThermostatSummaryResponse response = request.execute();
		if (response.isError()) {
			final Status status = response.getStatus();

			if (status.isAccessTokenExpired()) {
				logger.debug("Access token has expired: {}", status);
				if (oauthCredentials.refreshTokens())
					readEcobee(oauthCredentials, selection);
			} else {
				logger.error(status.getMessage());
			}

			return; // abort processing
		}

		// Identify which thermostats have changed since the last fetch

		Map<String, Revision> newRevisionMap = new HashMap<String, Revision>();
		for (Revision r : response.getRevisionList()) {
			newRevisionMap.put(r.getThermostatIdentifier(), r);
		}

		// Accumulate the thermostat IDs for thermostats that have updated
		// since the last fetch.
		Set<String> thermostatIdentifiers = new HashSet<String>();

		for (Revision newRevision : newRevisionMap.values()) {
			Revision lastRevision = this.lastRevisionMap.get(newRevision
					.getThermostatIdentifier());

			// TODO: choose more carefully which criteria to examine
			// than just hasRuntimeChanged. If an in binding exists
			// for settings, for example, we should OR in another test
			// for hasThermostatChanged. This needs careful research.

			// If this thermostat's runtime values have changed,
			// add it to the list for full retrieval

			if (newRevision.hasRuntimeChanged(lastRevision)) {
				thermostatIdentifiers
						.add(newRevision.getThermostatIdentifier());
			}
		}

		// Remember the new revisions for the next execute() call.
		this.lastRevisionMap = newRevisionMap;

		if (0 == thermostatIdentifiers.size()) {
			logger.debug("No changes detected.");
		} else {
			logger.debug("Requesting full retrieval for {} thermostat(s).",
					thermostatIdentifiers.size());

			// Potentially decrease the number of thermostats for the full
			// retrieval.

			selection.setSelectionMatch(thermostatIdentifiers);

			// TODO loop through possibly multiple pages
			ThermostatRequest treq = new ThermostatRequest(
					oauthCredentials.accessToken, selection, null);
			ThermostatResponse tres = treq.execute();

			if (tres.isError()) {
				logger.error("Error retrieving thermostats: {}",
						tres.getStatus());
			} else {

				// Create a ID-based map of the thermostats we retrieved.
				Map<String, Thermostat> thermostats = new HashMap<String, Thermostat>();

				for (Thermostat t : tres.getThermostatList()) {
					thermostats.put(t.getIdentifier(), t);
				}

				// Iterate through bindings and update all inbound values.
				for (final EcobeeBindingProvider provider : this.providers) {
					for (final String itemName : provider.getItemNames()) {
						if (provider.isInBound(itemName)) {
							final State state = getState(provider, thermostats,
									itemName);
							// we need to make sure that we won't send out
							// this event to
							// Ecobee again, when receiving it on the
							// openHAB bus
							ignoreEventList.add(itemName + state.toString());
							logger.trace(
									"Added event (item='{}', type='{}') to the ignore event list",
									itemName, state.toString());
							if (state != null) {
								this.eventPublisher.postUpdate(itemName, state);
							}
						}
					}
				}
			} // end if there was no error retrieving the thermostats
		} // end if there were any changed thermostats to fetch
	}

	private JXPathContext sharedContext = null;

	/**
	 * Used a shared JXPath context when creating new ones for different beans
	 * in order to re-use a shared configuration.
	 * 
	 * @return the shared JXPathContext
	 * @see <a
	 *      href="http://commons.apache.org/proper/commons-jxpath/users-guide.html#Nested_Contexts">Nested
	 *      Contexts</a>
	 */
	private JXPathContext getSharedContext() {
		if (this.sharedContext == null) {
			this.sharedContext = JXPathContext.newContext(null);
			this.sharedContext.setLenient(true);
			this.sharedContext.setFactory(new AbstractFactory() {
				public boolean createObject(JXPathContext context,
						Pointer pointer, Object parent, String name, int index) {
					if (parent instanceof Thermostat) {
						if (name.equals("settings")) {
							((Thermostat) parent).setSettings(new Settings());
							return true;
						} else if (name.equals("location")) {
							((Thermostat) parent).setLocation(new Location());
							return true;
						} else if (name.equals("program")) {
							((Thermostat) parent).setProgram(new Program());
							return true;
						} else if (name.equals("houseDetails")) {
							((Thermostat) parent)
									.setHouseDetails(new HouseDetails());
							return true;
						} else if (name.equals("notificationSettings")) {
							((Thermostat) parent)
									.setNotificationSettings(new NotificationSettings());
							return true;
						}
					} else if (parent instanceof Program) {
						if (name.equals("schedule")) {
							((Program) parent)
									.setSchedule(new ArrayList<List<String>>());
							return true;
						}
						if (name.equals("climates")) {
							((Program) parent)
									.setClimates(new ArrayList<Climate>());
							return true;
						}
					} else if (parent instanceof NotificationSettings) {
						if (name.equals("emailAddresses")) {
							((NotificationSettings) parent)
									.setEmailAddresses(new ArrayList<String>());
							return true;
						} else if (name.equals("equipment")) {
							((NotificationSettings) parent)
									.setEquipment(new ArrayList<EquipmentSetting>());
							return true;
						} else if (name.equals("general")) {
							((NotificationSettings) parent)
									.setGeneral(new ArrayList<GeneralSetting>());
							return true;
						} else if (name.equals("limit")) {
							((NotificationSettings) parent)
									.setLimit(new ArrayList<LimitSetting>());
							return true;
						}
					}
					return false;
				}
			});
		}
		return this.sharedContext;
	}

	/**
	 * Give a binding provider, a map of thermostats, and an item name, return
	 * the corresponding state object.
	 * 
	 * @param provider
	 *            the Ecobee binding provider
	 * @param thermostats
	 *            a map of thermostat identifiers to {@link Thermostat} objects
	 * @param itemName
	 *            the item name from the items file.
	 * @return
	 */
	private State getState(EcobeeBindingProvider provider,
			Map<String, Thermostat> thermostats, String itemName) {

		final String thermostatIdentifier = provider
				.getThermostatIdentifier(itemName);
		final String property = provider.getProperty(itemName);
		final Thermostat t = thermostats.get(thermostatIdentifier);

		JXPathContext context = JXPathContext.newContext(getSharedContext(), t);
		Object o = context.getValue(property);

		if (o instanceof String) {
			return StringType.valueOf((String) o);
		} else if (o instanceof Integer) {
			return new DecimalType((Integer) o);
		} else if (o instanceof Boolean) {
			return o.equals(Boolean.TRUE) ? OnOffType.ON : OnOffType.OFF;
		} else if (o instanceof Date) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date) o);
			return new DateTimeType(c);
		} else if (o instanceof Temperature) {
			return new DecimalType(((Temperature) o).toLocalTemperature());
		} else if (o != null) {
			return StringType.valueOf(o.toString());
		} else {
			return UnDefType.NULL;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.trace("internalReceiveCommand() is called!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(final String itemName,
			final State newState) {
		logger.trace("Received update (item='{}', state='{}')", itemName,
				newState.toString());
		if (!isEcho(itemName, newState)) {
			writeToEcobee(itemName, newState);
		}
	}

	private boolean isEcho(String itemName, State state) {
		String ignoreEventListKey = itemName + state.toString();
		if (ignoreEventList.contains(ignoreEventListKey)) {
			ignoreEventList.remove(ignoreEventListKey);
			logger.trace(
					"We received this event (item='{}', state='{}') from Ecobee, so we don't send it back again -> ignore!",
					itemName, state.toString());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Send the {@code newState} for the given {@code itemName} to Ecobee.
	 * 
	 * @param itemName
	 * @param newState
	 */
	private void writeToEcobee(String itemName, State newState) {

		// Find the first binding provider for this itemName.
		EcobeeBindingProvider provider = null;
		String selectionMatch = null;
		for (EcobeeBindingProvider p : this.providers) {
			selectionMatch = p.getThermostatIdentifier(itemName);
			if (selectionMatch != null) {
				provider = p;
				break;
			}
		}
		if (provider == null) {
			logger.warn(
					"no matching binding provider found [itemName={}, newState={}]",
					itemName, newState);
			return;
		} else {
			final Selection selection = new Selection(selectionMatch);
			List<AbstractFunction> functions = null;
			logger.debug("Selection for update: {}", selection);

			String property = provider.getProperty(itemName);

			final Thermostat thermostat = new Thermostat(null);
			JXPathContext context = JXPathContext.newContext(getSharedContext(), thermostat);
			context.createPathAndSetValue(property, newState.toString()); //FIXME: figure out types
			logger.debug("Thermostat for update: {}", thermostat);

			OAuthCredentials oauthCredentials = getOAuthCredentials(provider
					.getUserid(itemName));

			if (oauthCredentials == null) {
				logger.warn(
						"Unable to locate credentials for item {}; aborting update.",
						itemName);
				return;
			}

			if (oauthCredentials.noAccessToken()) {
				if (!oauthCredentials.refreshTokens()) {
					logger.warn("Sending updated skipped.");
					return;
				}
			}

			UpdateThermostatRequest request = new UpdateThermostatRequest(
					oauthCredentials.accessToken, selection, functions,
					thermostat);
			ApiResponse response = request.execute();
			if (response.isError()) {
				final Status status = response.getStatus();
				if (status.isAccessTokenExpired()) {
					oauthCredentials.refreshTokens();
				} else {
					logger.error("Error updating thermostat(s): {}", response);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof EcobeeBindingProvider) {

			// Forget prior revisions because we may be concerned with
			// different thermostats or properties than before.
			this.lastRevisionMap.clear();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {

		// Forget prior revisions because we may be concerned with
		// different thermostats or properties than before.
		if (provider instanceof EcobeeBindingProvider) {
			this.lastRevisionMap.clear();
		}
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
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like ecobee:refresh=120000
			String refreshIntervalString = (String) config.get(CONFIG_REFRESH);
			if (isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			// to override the default usage of Fahrenheit one has to add a
			// parameter to openhab.cfg, as in ecobee:tempscale=C
			String tempScaleString = (String) config.get(CONFIG_TEMP_SCALE);
			if (isNotBlank(tempScaleString)) {
				try {
					Temperature.setLocalScale(Temperature.Scale
							.forValue(tempScaleString));
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException(CONFIG_TEMP_SCALE,
							"Unsupported temperature scale '" + tempScaleString
									+ "'.");
				}
			}

			Enumeration<String> configKeys = config.keys();
			while (configKeys.hasMoreElements()) {
				String configKey = (String) configKeys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if (CONFIG_REFRESH.equals(configKey)
						|| CONFIG_TEMP_SCALE.equals(configKey)
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
					credentials = new OAuthCredentials(configAdmin, userid);
					credentialsCache.put(userid, credentials);
				}

				String value = (String) config.get(configKey);

				if (CONFIG_APP_KEY.equals(configKeyTail)) {
					credentials.appKey = value;
				} else if (CONFIG_SCOPE.equals(configKeyTail)) {
					credentials.scope = value;
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey
									+ "' is unknown");
				}
			}

			// Verify the completeness of each OAuthCredentials entry
			// to make sure we can get started.

			boolean properlyConfigured = true;

			for (String userid : credentialsCache.keySet()) {
				OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
				String userString = (DEFAULT_USER_ID.equals(userid)) ? ""
						: (userid + ".");
				if (oauthCredentials.appKey == null) {
					logger.error("Required ecobee:{}{} is missing.",
							userString, CONFIG_APP_KEY);
					properlyConfigured = false;
					break;
				}
				if (oauthCredentials.scope == null) {
					logger.error("Required ecobee:{}{} is missing.",
							userString, CONFIG_SCOPE);
					properlyConfigured = false;
					break;
				}
			}

			setProperlyConfigured(properlyConfigured);
		}
	}

	/**
	 * Creates the necessary {@link Selection} object to request all information
	 * required from the Ecobee API for all thermostats and sub-objects that
	 * have a binding, per set of credentials configured in openhab.cfg. One
	 * {@link ThermostatRequest} can then query all information in one go.
	 * 
	 * @param oauthCredentials
	 *            constrain the resulting Selection object to only select the
	 *            thermostats which the configuration indicates can be reached
	 *            using these credentials.
	 * @returns the Selection object, or <code>null</code> if only an unsuitable
	 *          Selection is possible.
	 */
	private Selection createSelection(OAuthCredentials oauthCredentials) {
		final Selection selection = new Selection(SelectionType.THERMOSTATS,
				null);
		final Set<String> thermostatIdentifiers = new HashSet<String>();

		for (final EcobeeBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {

				final String thermostatIdentifier = provider
						.getThermostatIdentifier(itemName);
				final String property = provider.getProperty(itemName);

				/*
				 * We are only concerned with inbound items, so there would be
				 * no point to including the criteria for this item.
				 * 
				 * We are also only concerned with items that can be reached by
				 * the given credentials.
				 */

				if (!provider.isInBound(itemName)
						|| oauthCredentials != getOAuthCredentials(provider
								.getUserid(itemName))) {
					continue;
				}

				thermostatIdentifiers.add(thermostatIdentifier);

				if (property.startsWith("settings")) {
					selection.setIncludeSettings(true);
				} else if (property.startsWith("runtime")) {
					selection.setIncludeRuntime(true);
				} else if (property.startsWith("extendedRuntime")) {
					selection.setIncludeExtendedRuntime(true);
				} else if (property.startsWith("electricity")) {
					selection.setIncludeElectricity(true);
				} else if (property.startsWith("devices")) {
					selection.setIncludeDevice(true);
				} else if (property.startsWith("electricity")) {
					selection.setIncludeElectricity(true);
				} else if (property.startsWith("location")) {
					selection.setIncludeLocation(true);
				} else if (property.startsWith("technician")) {
					selection.setIncludeTechnician(true);
				} else if (property.startsWith("utility")) {
					selection.setIncludeUtility(true);
				} else if (property.startsWith("management")) {
					selection.setIncludeManagement(true);
				} else if (property.startsWith("weather")) {
					selection.setIncludeWeather(true);
				} else if (property.startsWith("events")) {
					selection.setIncludeEvents(true);
				} else if (property.startsWith("program")) {
					selection.setIncludeProgram(true);
				} else if (property.startsWith("houseDetails")) {
					selection.setIncludeHouseDetails(true);
				} else if (property.startsWith("oemCfg")) {
					selection.setIncludeOemCfg(true);
				} else if (property.startsWith("equipmentStatus")) {
					selection.setIncludeEquipmentStatus(true);
				} else if (property.startsWith("notificationSettings")) {
					selection.setIncludeNotificationSettings(true);
				} else if (property.startsWith("privacy")) {
					selection.setIncludePrivacy(true);
				} else if (property.startsWith("version")) {
					selection.setIncludeVersion(true);
				}
			}
		}

		if (thermostatIdentifiers.isEmpty()) {
			logger.info("No Ecobee in-bindings have been found for selection.");
			return null;
		}

		// include all the thermostats we found in the bindings
		selection.setSelectionMatch(thermostatIdentifiers);

		return selection;
	}

	/**
	 * This internal class holds the different credentials necessary for the
	 * OAuth2 flow to work. It also provides basic methods to refresh the
	 * tokens.
	 * 
	 * <p>
	 * OAuth States
	 * <table>
	 * <thead>
	 * <tr>
	 * <th>authToken</th>
	 * <th>refreshToken</th>
	 * <th>accessToken</th>
	 * <th>State</th>
	 * </tr>
	 * <thead> <tbody>
	 * <tr>
	 * <td>null</td>
	 * <td></td>
	 * <td></td>
	 * <td>authorize</td>
	 * </tr>
	 * <tr>
	 * <td>non-null</td>
	 * <td>null</td>
	 * <td></td>
	 * <td>request tokens</td>
	 * </tr>
	 * <tr>
	 * <td>non-null</td>
	 * <td>non-null</td>
	 * <td>null</td>
	 * <td>refresh tokens</td>
	 * </tr>
	 * <tr>
	 * <td>non-null</td>
	 * <td>non-null</td>
	 * <td>non-null</td>
	 * <td>if expired, refresh if any error, authorize</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @author John Cocula
	 * @since 1.6.0
	 */
	static class OAuthCredentials {

		private static final String AUTH_TOKEN = "authToken";
		private static final String REFRESH_TOKEN = "refreshToken";
		private static final String ACCESS_TOKEN = "accessToken";

		private String userid;
		private Configuration config;

		/**
		 * The private app key needed in order to interact with the Ecobee API.
		 * This must be provided in the <code>openhab.cfg</code> file.
		 */
		private String appKey;

		/**
		 * The scope needed when authorizing this client to the Ecobee API.
		 * 
		 * @see AuthorizeRequest
		 */
		private String scope;

		/**
		 * The authorization token needed to request the refresh and access
		 * tokens. Obtained and persisted when {@code authorize()} is called.
		 * 
		 * @see AuthorizeRequest
		 * @see #authorize()
		 */
		private String authToken;

		/**
		 * The refresh token to access the Ecobee API. Initial token is received
		 * using the <code>authToken</code>, periodically refreshed using the
		 * previous refreshToken, and saved in persistent storage so it can be
		 * used across activations.
		 * 
		 * @see TokenRequest
		 * @see RefreshTokenRequest
		 */
		private String refreshToken;

		/**
		 * The access token to access the Ecobee API. Automatically renewed from
		 * the API using the refresh token and persisted for use across
		 * activations.
		 * 
		 * @see #refreshTokens()
		 */
		private String accessToken;

		public OAuthCredentials(ConfigurationAdmin configAdmin, String userid) {

			try {
				this.userid = userid;
				this.config = configAdmin
						.getConfiguration("org.openhab.ecobee." + userid);
				load();
			} catch (Exception e) {
				throw new EcobeeException("Cannot create OAuthCredentials.", e);
			}
		}

		@SuppressWarnings("rawtypes")
		private void load() {
			Dictionary props = config.getProperties();
			if (props == null) {
				props = new Hashtable();
			}
			this.authToken = (String) props.get(AUTH_TOKEN);
			this.refreshToken = (String) props.get(REFRESH_TOKEN);
			this.accessToken = (String) props.get(ACCESS_TOKEN);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void save() {
			try {
				Dictionary props = new Hashtable();
				if (this.authToken != null) {
					props.put(AUTH_TOKEN, this.authToken);
				}
				if (this.refreshToken != null) {
					props.put(REFRESH_TOKEN, this.refreshToken);
				}
				if (this.accessToken != null) {
					props.put(ACCESS_TOKEN, this.accessToken);
				}
				config.update(props);
			} catch (Exception e) {
				throw new EcobeeException("Cannot save tokens.", e);
			}
		}

		public boolean noAccessToken() {
			return this.accessToken == null;
		}

		public void authorize() {
			logger.trace("Authorizing this binding with the Ecobee API.");

			final AuthorizeRequest request = new AuthorizeRequest(this.appKey,
					this.scope);
			logger.trace("Request: {}", request);

			final AuthorizeResponse response = request.execute();
			logger.trace("Response: {}", response);

			this.authToken = response.getAuthToken();
			this.refreshToken = null;
			this.accessToken = null;
			save();

			logger.info("#########################################################################################");
			logger.info("# Ecobee-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
			logger.info("# 1. Login to www.ecobee.com using your '{}' account",
					this.userid);
			logger.info(
					"# 2. Enter the PIN '{}' in My Apps within the next {} minutes.",
					response.getEcobeePin(), response.getExpiresIn());
			logger.info("# NOTE: Any API attempts will fail in the meantime.");
			logger.info("#########################################################################################");
		}

		/**
		 * This method attempts to advance the authorization process by
		 * retrieving the tokens needed to use the API. It returns
		 * <code>true</code> if there is reason to believe that an immediately
		 * subsequent API call would succeed.
		 * <p>
		 * This method requests access and refresh tokens to use the Ecobee API.
		 * If there is a <code>refreshToken</code>, it will be used to obtain
		 * the tokens, but if there is only an <code>authToken</code>, that will
		 * be used instead.
		 * 
		 * @return <code>true</code> if there is reason to believe that an
		 *         immediately subsequent API call would succeed.
		 */
		public boolean refreshTokens() {
			if (this.authToken == null) {
				authorize();
				return false;
			} else {
				logger.trace("Refreshing tokens.");

				Request request;

				if (this.refreshToken == null) {
					request = new TokenRequest(this.authToken, this.appKey);
				} else {
					request = new RefreshTokenRequest(this.refreshToken,
							this.appKey);
				}
				logger.trace("Request: {}", request);

				final TokenResponse response = (TokenResponse) request
						.execute();
				logger.trace("Response: {}", response);

				if (response.isError()) {
					logger.error("Error retrieving tokens: {}",
							response.getError());
					return false;
				} else {
					this.refreshToken = response.getRefreshToken();
					this.accessToken = response.getAccessToken();
					save();
					return true;
				}
			}
		}
	}
}
