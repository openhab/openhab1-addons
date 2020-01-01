/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo.internal.camera;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
import org.openhab.binding.netatmo.internal.NetatmoException;
import org.openhab.binding.netatmo.internal.authentication.OAuthCredentials;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Camera;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Event;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Home;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Person;
import org.openhab.binding.netatmo.internal.messages.NetatmoError;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author Ing. Peter Weiss
 * @since 1.9.0
 */
public class NetatmoCameraBinding {

    private static final Logger logger = LoggerFactory.getLogger(NetatmoCameraBinding.class);

    private final Map<String, Map<String, Person>> configuredHomeKnownPersons = new HashMap<String, Map<String, Person>>();
    private final Map<String, Map<String, Person>> configuredHomeUnKnownPersons = new HashMap<String, Map<String, Person>>();
    private final Map<String, Home> configuredHomeMap = new HashMap<String, Home>();
    private final Map<String, Map<String, Camera>> configuredhomeCameras = new HashMap<String, Map<String, Camera>>();

    /**
     * Execute the weather binding from Netatmo Binding Class
     *
     * @param oauthCredentials
     * @param providers
     * @param eventPublisher
     */
    public void execute(OAuthCredentials oauthCredentials, Collection<NetatmoBindingProvider> providers,
            EventPublisher eventPublisher) {

        logger.debug("Querying Netatmo camera API");
        try {
            oauthCredentials.refreshAccessToken();
            configuredHomeKnownPersons.clear();
            configuredHomeUnKnownPersons.clear();
            configuredHomeMap.clear();
            configuredhomeCameras.clear();

            processGetHomeData(oauthCredentials, providers, eventPublisher);

            for (final NetatmoBindingProvider provider : providers) {
                for (final String itemName : provider.getItemNames()) {
                    final String homeId = provider.getHomeId(itemName);
                    final NetatmoCameraAttributes attribute = provider.getAttribute(itemName);

                    State state = null;

                    if (homeId != null) {
                        final String personId = provider.getPersonId(itemName);
                        final String cameraId = provider.getCameraId(itemName);

                        if (personId != null) {
                            if ("UNKNOWN".equals(personId)) {
                                final StringBuilder message = new StringBuilder();
                                int i = 0;

                                switch (attribute) {
                                    case HOME_UNKNWOWN_HOME_COUNT:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (!person.getOut_of_sight()) {
                                                    i++;
                                                }
                                            }
                                        }
                                        state = new DecimalType(i);
                                        break;

                                    case HOME_UNKNWOWN_AWAY_COUNT:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (person.getOut_of_sight()) {
                                                    i++;
                                                }
                                            }
                                        }
                                        state = new DecimalType(i);
                                        break;

                                    case HOME_UNKNWOWN_OUTOFSIGHT_LIST:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (message.length() > 1) {
                                                    message.append("#");
                                                }
                                                message.append(person.getOut_of_sight());
                                            }
                                        }
                                        state = new StringType(message.toString());
                                        break;

                                    case HOME_UNKNWOWN_LASTSEEN_LIST:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (message.length() > 1) {
                                                    message.append("#");
                                                }

                                                final Calendar calendar = Calendar.getInstance();
                                                calendar.setTimeInMillis(person.getLastSeen().getTime() * 1000);
                                                message.append(calendar);
                                            }
                                        }
                                        state = new StringType(message.toString());
                                        break;

                                    case HOME_UNKNWOWN_FACE_ID_LIST:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (message.length() > 1) {
                                                    message.append("#");
                                                }
                                                message.append(person.getFace().getId());
                                            }
                                        }
                                        state = new StringType(message.toString());
                                        break;

                                    case HOME_UNKNWOWN_FACE_KEY_LIST:
                                        if (configuredHomeUnKnownPersons.containsKey(homeId)) {
                                            for (Entry<String, Person> entry : configuredHomeUnKnownPersons.get(homeId)
                                                    .entrySet()) {
                                                final Person person = entry.getValue();
                                                if (message.length() > 1) {
                                                    message.append("#");
                                                }
                                                message.append(person.getFace().getKey());
                                            }
                                        }
                                        state = new StringType(message.toString());
                                        break;

                                    default:
                                        break;
                                }

                            } else {
                                Person person = null;
                                if (configuredHomeKnownPersons.containsKey(homeId)) {
                                    person = configuredHomeKnownPersons.get(homeId).get(personId);
                                }

                                if (person != null) {
                                    switch (attribute) {
                                        case HOME_PERSON_OUTOFSIGHT:
                                            if (person.getOut_of_sight()) {
                                                state = OnOffType.OFF;
                                            } else {
                                                state = OnOffType.ON;
                                            }
                                            break;

                                        case HOME_PERSON_PSEUDO:
                                            state = new StringType(person.getPseudo());
                                            break;

                                        case HOME_PERSON_LASTSEEN:
                                            final Calendar calendar = Calendar.getInstance();
                                            calendar.setTimeInMillis(person.getLastSeen().getTime() * 1000);
                                            state = new DateTimeType(calendar);
                                            break;

                                        case HOME_PERSON_FACE_ID:
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
                        } else if (cameraId != null) {
                            Camera camera = null;
                            if (configuredhomeCameras.containsKey(homeId)) {
                                camera = configuredhomeCameras.get(homeId).get(cameraId);
                            }

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
                        eventPublisher.postUpdate(itemName, state);
                    }
                }

            }

        } catch (NetatmoException ne) {
            logger.error(ne.getMessage());
        }
    }

    /**
     * Camera Home Implementation
     *
     * @param oauthCredentials
     * @param providers
     */
    private void processGetHomeData(OAuthCredentials oauthCredentials, Collection<NetatmoBindingProvider> providers,
            EventPublisher eventPublisher) {

        GetHomeDataRequest homeDataRequest = new GetHomeDataRequest(oauthCredentials.getAccessToken());
        logger.debug("Request: {}", homeDataRequest);

        GetHomeDataResponse homeDataResponse = homeDataRequest.execute();
        logger.debug("Response: {}", homeDataResponse);

        if (homeDataResponse.isError()) {
            final NetatmoError error = homeDataResponse.getError();

            if (error.isAccessTokenExpired() || error.isTokenNotVaid()) {
                logger.debug("Token is expired or is not valid, refreshing: code = {} message = {}", error.getCode(),
                        error.getMessage());

                oauthCredentials.refreshAccessToken();
                execute(oauthCredentials, providers, eventPublisher);
            } else {
                logger.error("Error processing home list: code = {} message = {}", error.getCode(), error.getMessage());

                throw new NetatmoException(error.getMessage());
            }

            return; // abort processing
        } else {
            processGetHomeDataResponse(homeDataResponse, providers);
        }
    }

    /**
     * Camera Home
     *
     *
     * Processes an incoming {@link GetHomeDataResponse}.
     * <p>
     *
     * @param providers
     */
    private void processGetHomeDataResponse(final GetHomeDataResponse response,
            Collection<NetatmoBindingProvider> providers) {
        final Map<String, Home> homeMap = new HashMap<String, Home>();
        final Map<String, Map<String, Person>> homeKnownPersons = new HashMap<String, Map<String, Person>>();
        final Map<String, Map<String, Person>> homeUnKnownPersons = new HashMap<String, Map<String, Person>>();
        final Map<String, Set<Event>> homeEvents = new HashMap<String, Set<Event>>();
        final Map<String, Map<String, Camera>> homeCameras = new HashMap<String, Map<String, Camera>>();

        // Add Homes to a HasMap
        for (final Home home : response.getHomes()) {
            final String homeId = home.getId();
            homeMap.put(homeId, home);
            logger.debug("Response Home: ID=" + home.getId() + " NAME=" + home.getName() + " PLACE="
                    + home.getPlace().toString());

            // Add Cameras of this home
            for (final Camera camera : home.getCameras()) {
                if (!homeCameras.containsKey(homeId)) {
                    homeCameras.put(homeId, new HashMap<String, Camera>());
                }

                homeCameras.get(homeId).put(camera.getId(), camera);
                logger.debug("Response Camera: ID=" + camera.getId() + " STATUS=" + camera.getStatus() + " SDSTATUS="
                        + camera.getSd_status() + " ALIMSTATUS=" + camera.getAlim_status());
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
                if (pseudo != null) {
                    homeKnownPersons.get(homeId).put(person.getId(), person);
                    logger.debug("Response Known Person: ID=" + person.getId() + " (" + pseudo + ") AWAY="
                            + person.getOut_of_sight().toString() + " LASTSEEN="
                            + (person.getLastSeen() != null ? person.getLastSeen().toString() : "unknown"));
                } else {
                    homeUnKnownPersons.get(homeId).put(person.getId(), person);

                    if (!configuredHomeUnKnownPersons.containsKey(homeId)) {
                        configuredHomeUnKnownPersons.put(homeId, new HashMap<String, Person>());
                    }
                    configuredHomeUnKnownPersons.get(homeId).put(person.getId(), person);

                    logger.debug("Response UnKnown Person: ID=" + person.getId() + " AWAY="
                            + person.getOut_of_sight().toString() + " LASTSEEN="
                            + (person.getLastSeen() != null ? person.getLastSeen().toString() : "unknown"));
                }

            }

            // Add Events that happened in this home
            for (final Event event : home.getEvents()) {
                if (!homeEvents.containsKey(homeId)) {
                    homeEvents.put(homeId, new HashSet<Event>());
                }

                homeEvents.get(homeId).add(event);
                logger.debug("Response Event: ID=" + event.getId() + " TYPE=" + event.getType() + " MESSAGE="
                        + event.getMessage() + " TIME="
                        + (event.getTime() != null ? event.getTime().toString() : "unknown"));
            }
        }

        // Remove all configured items from the maps
        for (final NetatmoBindingProvider provider : providers) {
            for (final String itemName : provider.getItemNames()) {

                final String homeId = provider.getHomeId(itemName);

                if (homeId != null) {

                    final String personId = provider.getPersonId(itemName);
                    final String cameraId = provider.getCameraId(itemName);
                    if (personId != null) {
                        if ("UNKNOWN".equals(personId)) {
                            if (homeUnKnownPersons.containsKey(homeId)
                                    && homeUnKnownPersons.get(homeId).containsKey(personId)) {
                                homeUnKnownPersons.get(homeId).remove(personId);
                            }
                        } else {
                            if (!configuredHomeKnownPersons.containsKey(homeId)) {
                                configuredHomeKnownPersons.put(homeId, new HashMap<String, Person>());
                            }
                            if (homeKnownPersons.containsKey(homeId)
                                    && homeKnownPersons.get(homeId).containsKey(personId)) {
                                configuredHomeKnownPersons.get(homeId).put(personId,
                                        homeKnownPersons.get(homeId).get(personId));
                                homeKnownPersons.get(homeId).remove(personId);
                            }
                        }
                    } else if (cameraId != null) {
                        if (!configuredhomeCameras.containsKey(homeId)) {
                            configuredhomeCameras.put(homeId, new HashMap<String, Camera>());
                        }
                        if (homeCameras.containsKey(homeId) && homeCameras.get(homeId).containsKey(cameraId)) {
                            configuredhomeCameras.get(homeId).put(cameraId, homeCameras.get(homeId).get(cameraId));
                            homeCameras.get(homeId).remove(cameraId);
                        }
                    } else {
                        // configuredHomeMap.put(homeId, home);
                        if (homeMap.containsKey(homeId)) {
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
            message.append("\t HOME: " + homeId + " (" + entry.getValue().getName() + ")\n");
        }

        for (Entry<String, Map<String, Person>> entry : homeKnownPersons.entrySet()) {
            final String homeId = entry.getKey();

            for (Entry<String, Person> entry2 : entry.getValue().entrySet()) {
                final String personId = entry2.getKey();
                message.append("\t PERSON: " + homeId + "#" + personId + " (" + entry2.getValue().getPseudo() + ")\n");
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
                message.append("\t CAMERA: " + homeId + "#" + cameraId + " (" + entry2.getValue().getName() + ")\n");
            }
        }

        if (message.length() > 0) {
            message.insert(0, "The following Items from Netatmo Camera are not yet configured:\n");
            logger.info(message.toString());
        }

    }

}
