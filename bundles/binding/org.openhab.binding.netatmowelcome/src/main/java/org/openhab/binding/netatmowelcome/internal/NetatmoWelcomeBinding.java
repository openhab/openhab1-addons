/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmowelcome.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.netatmowelcome.NetatmoWelcomeBindingProvider;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataRequest;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataResponse;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataResponse.Camera;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataResponse.Event;
import org.openhab.binding.netatmowelcome.internal.messages.NetatmoWelcomeError;
import org.openhab.binding.netatmowelcome.internal.messages.RefreshTokenRequest;
import org.openhab.binding.netatmowelcome.internal.messages.RefreshTokenResponse;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataResponse.Home;
import org.openhab.binding.netatmowelcome.internal.messages.GetHomeDataResponse.Person;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
public class NetatmoWelcomeBinding extends AbstractActiveBinding<NetatmoWelcomeBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(NetatmoWelcomeBinding.class);

	private static final String DEFAULT_USER_ID = "DEFAULT_USER";

	protected static final String CONFIG_CLIENT_ID = "clientid";
	protected static final String CONFIG_CLIENT_SECRET = "clientsecret";
	protected static final String CONFIG_REFRESH = "refresh";
	protected static final String CONFIG_REFRESH_TOKEN = "refreshtoken";

	protected static final String HOME_NAME = "Name";
	protected static final String HOME_PLACE_COUNTRY = "PlaceCountry";
	protected static final String HOME_PLACE_TIMEZONE = "PlaceTimezone";

	protected static final String HOME_PERSON_OUTOFSIGHT = "OutOfSight";
	protected static final String HOME_PERSON_PSEUDO = "Pseudo";
	protected static final String HOME_PERSON_LASTSEEN = "LastSeen";
	protected static final String HOME_PERSON_FACE_ID = "FaceId";
	protected static final String HOME_PERSON_FACE_KEY = "FaceKey";

	protected static final String HOME_UNKNWOWN_HOME_COUNT = "HomeCount";
	protected static final String HOME_UNKNWOWN_AWAY_COUNT = "AwayCount";
	protected static final String HOME_UNKNWOWN_OUTOFSIGHT_LIST = "OutOfSightList";
	protected static final String HOME_UNKNWOWN_LASTSEEN_LIST = "LastSeenList";
	protected static final String HOME_UNKNWOWN_FACE_ID_LIST= "FaceIdList";
	protected static final String HOME_UNKNWOWN_FACE_KEY_LIST = "FaceKeyList";
	 
	protected static final String HOME_CAMERA_NAME = "Name";
	protected static final String HOME_CAMERA_STATUS = "Status";
	protected static final String HOME_CAMERA_SD_STATUS = "SdStatus";
	protected static final String HOME_CAMERA_ALIM_STATUS = "AlimStatus";

	
	private Map<String, OAuthCredentials> credentialsCache = new HashMap<String, OAuthCredentials>();

	private final Map<String, Map<String, Person>> configuredHomeKnownPersons = new HashMap<String, Map<String, Person>>();
	private final Map<String, Map<String, Person>> configuredHomeUnKnownPersons = new HashMap<String, Map<String, Person>>();
	private final Map<String, Home> configuredHomeMap = new HashMap<String, Home>();
	private final Map<String, Map<String, Camera>> configuredhomeCameras = new HashMap<String, Map<String, Camera>>();

	/** 
	 * the refresh interval which is used to poll values from the NetatmoWelcome
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/**
	 * 
	 */
	public NetatmoWelcomeBinding() {
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 * @throws ConfigurationException 
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) throws ConfigurationException {
		

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
		
			
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		String userid = DEFAULT_USER_ID;

		OAuthCredentials credentials = credentialsCache.get(userid);
		if (credentials == null) {
			credentials = new OAuthCredentials();
			credentialsCache.put(userid, credentials);
		}
		
		String clientId = (String) configuration.get(CONFIG_CLIENT_ID);
		if (StringUtils.isNotBlank(clientId)) {
			credentials.clientId = clientId;
		}
		
		String clientSecret = (String) configuration.get(CONFIG_CLIENT_SECRET);
		if (StringUtils.isNotBlank(clientSecret)) {
			credentials.clientSecret = clientSecret;
		}
		
		String refreshToken = (String) configuration.get(CONFIG_REFRESH_TOKEN);
		if (StringUtils.isNotBlank(refreshToken)) {
			credentials.refreshToken = refreshToken;
		}
	
		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
		logger.debug("Config modified");
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
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
	protected String getName() {
		return "NetatmoWelcome Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		
		logger.debug("Querying NetatmoWelcome API");
		for (String userid : credentialsCache.keySet()) {
			try {
				OAuthCredentials oauthCredentials = getOAuthCredentials(userid);
				
				oauthCredentials.refreshAccessToken();
				configuredHomeKnownPersons.clear();
				configuredHomeUnKnownPersons.clear();
				configuredHomeMap.clear();
				configuredhomeCameras.clear();
				
				processGetHomeData(oauthCredentials);

				
				for (final NetatmoWelcomeBindingProvider provider : this.providers) {
					for (final String itemName : provider.getItemNames()) {
						final String homeId = provider.getHomeId(itemName);
						final String attribute = provider.getAttribute(itemName);
						State state = null;
						
						if (homeId != null)
						{
							final String personId = provider.getPersonId(itemName);
							final String cameraId = provider.getCameraId(itemName);
							
							if (personId != null)
							{
								if ("UNKNOWN".equals(personId)) {																		
									final StringBuilder message = new StringBuilder();
									int i = 0;

									switch (attribute) {
									case HOME_UNKNWOWN_HOME_COUNT:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if (!person.getOut_of_sight())
													i++;
											}				
										}
										state = new DecimalType(i);
										break;

									case HOME_UNKNWOWN_AWAY_COUNT:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if (person.getOut_of_sight())
													i++;
											}				
										}
										state = new DecimalType(i);
										break;

									case HOME_UNKNWOWN_OUTOFSIGHT_LIST:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if(message.length()>1)
													message.append("#");
												message.append(person.getOut_of_sight());				
											}				
										}										
										state = new StringType(message.toString());
										break;

									case HOME_UNKNWOWN_LASTSEEN_LIST:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if(message.length()>1)
													message.append("#");

												final Calendar calendar = GregorianCalendar.getInstance();
												calendar.setTimeInMillis(person.getLastSeen().getTime() * 1000);											
												message.append(calendar);				
											}				
										}										
										state = new StringType(message.toString());
										break;

									case HOME_UNKNWOWN_FACE_ID_LIST:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if(message.length()>1)
													message.append("#");
												message.append(person.getFace().getId());				
											}				
										}										
										state = new StringType(message.toString());
										break;

									case HOME_UNKNWOWN_FACE_KEY_LIST:
										if (configuredHomeUnKnownPersons.containsKey(homeId)) {
											for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId).entrySet()) {
												final Person person = entry.getValue();
												if(message.length()>1)
													message.append("#");
												message.append(person.getFace().getKey());				
											}				
										}										
										state = new StringType(message.toString());
										break;


									default:
										break;
									}
									
								}
								else {
									Person person = null;
									if (configuredHomeKnownPersons.containsKey(homeId))
										person = configuredHomeKnownPersons.get(homeId).get(personId);

									if (person !=null){								
										switch (attribute) {
										case HOME_PERSON_OUTOFSIGHT:
											if(person.getOut_of_sight())
												state = OnOffType.OFF;
											else
												state = OnOffType.ON;							
											break;

										case HOME_PERSON_PSEUDO:
											state = new StringType(person.getPseudo());							
											break;

										case HOME_PERSON_LASTSEEN:										
											final Calendar calendar = GregorianCalendar.getInstance();
											calendar.setTimeInMillis(person.getLastSeen().getTime() * 1000);											
											state = new DateTimeType(calendar);
											break;

										case  HOME_PERSON_FACE_ID:
											state = new StringType(person.getFace().getId());
											break;

										case HOME_PERSON_FACE_KEY:
											state = new StringType(person.getFace().getKey());							
											break;

										default:
											break;
										}
									}
								}
							}
							else if (cameraId != null){
								Camera camera = null;
								if (configuredhomeCameras.containsKey(homeId))
									camera = configuredhomeCameras.get(homeId).get(cameraId);

								if (camera != null) {
									switch (attribute) {
									case HOME_CAMERA_NAME:
										state = new StringType(camera.getName());																	
										break;

									case HOME_CAMERA_STATUS:
										state = new StringType(camera.getStatus());																	
										break;

									case HOME_CAMERA_SD_STATUS:
										state = new StringType(camera.getSd_status());																	
										break;

									case HOME_CAMERA_ALIM_STATUS:
										state = new StringType(camera.getAlim_status());																	
										break;

									default:
										break;
									}
								}
							}
							
							else {
								Home home = configuredHomeMap.get(homeId);
								if (home != null) {
								
									switch (attribute) {
									case HOME_NAME:
										state = new StringType(home.getName());																	
										break;

									case HOME_PLACE_COUNTRY:
										state = new StringType(home.getPlace().getCountry());																	
										break;

									case HOME_PLACE_TIMEZONE:
										state = new StringType(home.getPlace().getTimezone());																	
										break;

									default:
										break;
									}
								}
							}
						}
							
						if (state != null) {
							this.eventPublisher.postUpdate(itemName, state);
						}
					}
					
				}

			} catch (NetatmoWelcomeException ne) {
				logger.error(ne.getMessage());
			}
		}

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

		if (properties != null) {

			final String refreshIntervalString = (String) properties
					.get(CONFIG_REFRESH);
			if (isNotBlank(refreshIntervalString)) {
				this.refreshInterval = Long.parseLong(refreshIntervalString);
			}

			Enumeration<String> configKeys = properties.keys();
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

				String value = (String) properties.get(configKeyTail);

				if (CONFIG_CLIENT_ID.equals(configKeyTail)) {
					credentials.clientId = value;
				} else if (CONFIG_CLIENT_SECRET.equals(configKeyTail)) {
					credentials.clientSecret = value;
				} else if (CONFIG_REFRESH_TOKEN.equals(configKeyTail)) {
					credentials.refreshToken = value;
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
	 * Welcome Home Implementation
	 *
	 * @param oauthCredentials
	 */
	private void processGetHomeData(OAuthCredentials oauthCredentials) {
		logger.debug("Request: {}", oauthCredentials.getHomeDataRequest);
		logger.debug("Response: {}", oauthCredentials.getHomeDataResponse);

		if (oauthCredentials.getHomeDataResponse.isError()) {
			final NetatmoWelcomeError error = oauthCredentials.getHomeDataResponse
					.getError();

			if (error.isAccessTokenExpired() || error.isTokenNotVaid()) {
				logger.debug("Token is expired or is not valid, refreshing: code = {} message = {}",
						error.getCode(), error.getMessage());

				oauthCredentials.refreshAccessToken();
				execute();
			} else {
				logger.error("Error processing device list: code = {} message = {}",
						error.getCode(), error.getMessage());

				throw new NetatmoWelcomeException(error.getMessage());
			}

			return; // abort processing
		} else {
			processGetHomeDataResponse(oauthCredentials.getHomeDataResponse);
			oauthCredentials.firstExecution = false;
		}
	}

	/**
	 * Welcome Home
	 * 
	 * 
	 * Processes an incoming {@link GetHomeDataResponse}.
	 * <p>
	 */
	private void processGetHomeDataResponse(final GetHomeDataResponse response) {
		final Map<String, Home> homeMap = new HashMap<String, Home>();
		final Map<String, Map<String, Person>> homeKnownPersons = new HashMap<String, Map<String, Person>>();
		final Map<String, Map<String, Person>> homeUnKnownPersons = new HashMap<String, Map<String, Person>>();
		final Map<String, Set<Event>> homeEvents = new HashMap<String, Set<Event>>();
		final Map<String, Map<String, Camera>> homeCameras = new HashMap<String, Map<String, Camera>>();

		// Add Homes to a HasMap
		for (final Home home : response.getHomes()) {
			final String homeId = home.getId();
			homeMap.put(homeId, home);
			logger.debug("Response Home: ID=" + home.getId() + " NAME=" + home.getName() + " PLACE=" + home.getPlace().toString());

			// Add Cameras of this home
			for (final Camera camera : home.getCameras()) {
				if (!homeCameras.containsKey(homeId)) {
					homeCameras.put(homeId, new HashMap<String, Camera>());
				}

				homeCameras.get(homeId).put(camera.getId(), camera);
				logger.debug("Response Camera: ID=" + camera.getId() + " STATUS=" + camera.getStatus() + " SDSTATUS=" + camera.getSd_status() + " ALIMSTATUS=" + camera.getAlim_status());
			}

			// Add Persons that have been seen in the house
			for (final Person person : home.getPersons()) {
				if (!homeKnownPersons.containsKey(homeId)) {
					homeKnownPersons.put(homeId, new HashMap<String, Person>());
				}
				if (!homeUnKnownPersons.containsKey(homeId)) {
					homeUnKnownPersons.put(homeId, new HashMap<String, Person>());
					homeUnKnownPersons.get(homeId).put("UNKNOWN", null);
				}
				
				String pseudo = person.getPseudo();
				if (pseudo!=null) {
					homeKnownPersons.get(homeId).put(person.getId(), person);					
					logger.debug("Response Known Person: ID=" + person.getId() + " (" + pseudo + ") AWAY=" + person.getOut_of_sight().toString() + " LASTSEEN=" + (person.getLastSeen()!=null?person.getLastSeen().toString():"unknown"));
				}
				else {
					homeUnKnownPersons.get(homeId).put(person.getId(), person);
					
					if (!configuredHomeUnKnownPersons.containsKey(homeId)) {
						configuredHomeUnKnownPersons.put(homeId, new HashMap<String, Person>());
					}
					configuredHomeUnKnownPersons.get(homeId).put(person.getId(), person);

					logger.debug("Response UnKnown Person: ID=" + person.getId() + " AWAY=" + person.getOut_of_sight().toString() + " LASTSEEN=" + (person.getLastSeen()!=null?person.getLastSeen().toString():"unknown"));
				}
					
			}
			
			// Add Events that happened in this home
			for (final Event event : home.getEvents()) {
				if (!homeEvents.containsKey(homeId)) {
					homeEvents.put(homeId, new HashSet<Event>());
				}

				homeEvents.get(homeId).add(event);
				logger.debug("Response Event: ID=" + event.getId() + " TYPE=" + event.getType() + " MESSAGE=" + event.getMessage() + " TIME=" + (event.getTime()!=null?event.getTime().toString():"unknown"));
			}
		}

		// Remove all configured items from the maps
		for (final NetatmoWelcomeBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {

				final String homeId = provider.getHomeId(itemName);

				if (homeId!=null) {
					
					final String personId = provider.getPersonId(itemName);
					final String cameraId = provider.getCameraId(itemName);
					if (personId!=null) {
						if ("UNKNOWN".equals(personId)) {
							if(homeUnKnownPersons.containsKey(homeId) && homeUnKnownPersons.get(homeId).containsKey(personId)) {
								homeUnKnownPersons.get(homeId).remove(personId);								
							}
						}
						else {
							if (!configuredHomeKnownPersons.containsKey(homeId)) {
								configuredHomeKnownPersons.put(homeId, new HashMap<String, Person>());
							}
							if(homeKnownPersons.containsKey(homeId) && homeKnownPersons.get(homeId).containsKey(personId))
							{
								configuredHomeKnownPersons.get(homeId).put(personId, homeKnownPersons.get(homeId).get(personId));
								homeKnownPersons.get(homeId).remove(personId);
							}
						}
					}
					else if (cameraId!=null)
					{
						if (!configuredhomeCameras.containsKey(homeId)) {
							configuredhomeCameras.put(homeId, new HashMap<String, Camera>());
						}
						if(homeCameras.containsKey(homeId) && homeCameras.get(homeId).containsKey(cameraId))
						{
							configuredhomeCameras.get(homeId).put(cameraId, homeCameras.get(homeId).get(cameraId));
							homeCameras.get(homeId).remove(cameraId);
						}
					}
					else
					{
						//configuredHomeMap.put(homeId, home);						
						if(homeMap.containsKey(homeId))
						{
							configuredHomeMap.put(homeId, homeMap.get(homeId));
							homeMap.remove(homeId);
						}

					}
				}

			}
		}

		// Log all unconfigured measurements
		final StringBuilder message = new StringBuilder();
				
		for (Entry<String, Home> entry : homeMap.entrySet()) {
			final String homeId = entry.getKey();
				message.append("\t HOME: " + homeId + " ("
						+ entry.getValue().getName() + ")\n");
		}
		
		for (Entry<String, Map<String, Person>> entry : homeKnownPersons.entrySet()) {
			final String homeId = entry.getKey();

			for (Entry<String, Person> entry2 : entry.getValue().entrySet()) {
				final String personId = entry2.getKey();
				message.append("\t PERSON: " + homeId + "#" + personId + " ("
						+ entry2.getValue().getPseudo() + ")\n");
			}
		}

		for (Entry<String, Map<String, Person>> entry : homeUnKnownPersons.entrySet()) {
			final String homeId = entry.getKey();
			
			if (entry.getValue().containsKey("UNKNOWN")) {
				message.append("\t PERSON: " + homeId + "#UNKNOWN\n");				
			}				
		}

		for (Entry<String, Map<String, Camera>> entry : homeCameras.entrySet()) {
			final String homeId = entry.getKey();

			for (Entry<String, Camera> entry2 : entry.getValue().entrySet()) {
				final String cameraId = entry2.getKey();
				message.append("\t CAMERA: " + homeId + "#" + cameraId + " ("
						+ entry2.getValue().getName() + ")\n");
			}
		}
		

		if (message.length() > 0) {
			message.insert(0,
					"The following Items from NetatmoWelcome are not yet configured:\n");
			logger.info(message.toString());
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

		GetHomeDataResponse getHomeDataResponse = null;
		GetHomeDataRequest getHomeDataRequest = null;

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

			if (response == null) {
				throw new NetatmoWelcomeException("Could not refresh access token! If you see "
						+ "'Fatal transport error: javax.net.ssl.SSLHandshakeException' "
						+ "above. You need to install the StartCom CA certificate and restart openHAB. "
						+ "See https://github.com/openhab/openhab/wiki/Netatmo-Binding#missing-certificate-authority "
						+ "for more information.");
			}

			this.accessToken = response.getAccessToken();

			getHomeDataRequest = new GetHomeDataRequest(this.accessToken);
			getHomeDataResponse = getHomeDataRequest.execute();
			
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

}
