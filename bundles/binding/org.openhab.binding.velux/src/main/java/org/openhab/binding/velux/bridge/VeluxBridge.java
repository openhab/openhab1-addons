/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.velux.bridge.comm.BClogin;
import org.openhab.binding.velux.bridge.comm.BClogout;
import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

/**
 * Basic I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides two methods for pre- and postcommunication,
 * {@link VeluxBridge#bridgeLogin bridgeLogin} and
 * {@link VeluxBridge#bridgeLogout bridgeLogout} as well
 * as a method for the real communication
 * {@link VeluxBridge#bridgeCommunicate bridgeCommunicate}.
 * The behaviour can, in addition of the general configuration,
 * be changed by {@link VeluxBridge#bridgeOverwriteConfig bridgeOverwriteConfig}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class VeluxBridge {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridge.class);

    /**
     * Configuration options for this {@link VeluxBridge} are handled (esp. defaults) via
     * {@link VeluxBridgeConfiguration}.
     */
    private VeluxBridgeConfiguration configuration = null;

    /** BridgeCommunicationProtocol authentication token for Velux Bridge. */
    private String authenticationToken = "";

    /**
     * Constructor. Initializes the Velux bridge connectivity settings.
     */
    public VeluxBridge() {
        logger.trace("VeluxBridge(constructor) called.");
        configuration = new VeluxBridgeConfiguration();
        logger.trace("VeluxBridge(constructor) done.");
    }

    /**
     * Modifies the communication parameters of {@link VeluxBridge}
     * for interacting with the {@link VeluxBridge <b>Velux</b> bridge}.
     * If any of the passed parameters is negative, the default configuration
     * will be set again.
     *
     * @param retries             as int defining the number of retries before throwing an I/O error.
     * @param waitIntervalInMSecs as long defining the initial time wait interval in milliseconds for the Binary
     *                                Exponential Backoff
     *                                (BEB) Algorithm for handling of I/O failures.
     */
    public void bridgeOverwriteConfig(int retries, int waitIntervalInMSecs) {
        logger.trace(
                "bridgeOverwriteConfig(): parameters requested: retries = {} times, initial wait interval = {} msecs.",
                retries, waitIntervalInMSecs);

        if ((retries < 0) || (waitIntervalInMSecs < 0)) {
            logger.trace("bridgeOverwriteConfig(): resetting to defaults.");
            configuration = new VeluxBridgeConfiguration();
        } else {
            configuration.retries = retries;
            configuration.timeoutMsecs = waitIntervalInMSecs;
        }
        logger.trace("bridgeOverwriteConfig(): config set to retries = {} times, initial wait interval = {} msecs.",
                retries, waitIntervalInMSecs);

    }

    /**
     * Base level communication with the {@link VeluxBridge <b>Velux</b> bridge}.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param authentication  as String providing the Authentication token to be passed with the request header.
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @param                 <T> generic response based on classOfResponse.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o. Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws                     java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    private <T> T io(String url, String authentication, Object Request, Class<T> classOfResponse)
            throws JsonSyntaxException, IOException {
        /** Local handles */
        int retryCount = 0;
        IOException lastIOE;

        do {
            try {
                com.google.gson.Gson gson = new com.google.gson.Gson();
                String jsonRequest = gson.toJson(Request);
                logger.trace("io() to {} using request {}.", url, jsonRequest);

                Properties headerItems = new Properties();
                if (authentication.length() > 0) {
                    headerItems.setProperty("Authorization", String.format("Bearer %s", authentication));
                }
                InputStream content = IOUtils.toInputStream(jsonRequest, "UTF-8");

                String jsonResponse = org.openhab.io.net.http.HttpUtil.executeUrl("PUT", url, headerItems, content,
                        "application/json", configuration.timeoutMsecs);
                if (jsonResponse == null) {
                    throw new IOException("transport error");
                }
                logger.trace("io(): wait time {} msecs.", configuration.timeoutMsecs);
                // Give the bridge some time to breathe
                try {
                    Thread.sleep(configuration.timeoutMsecs);
                } catch (InterruptedException ie) {
                    logger.trace("io() wait interrupted.");
                }

                logger.trace("io() got response {}.", jsonResponse.replaceAll("\\p{C}", "."));
                jsonResponse = jsonResponse.replaceAll("^.+,\n", ""); // "()]}',"
                logger.trace("io() cleaned response {}.", jsonResponse);
                try {
                    T response = gson.fromJson(jsonResponse, classOfResponse);
                    return response;
                } catch (JsonSyntaxException jse) {
                    logger.info("io(): Exception occurred on deserialization: {}, aborting.", jse.getMessage());
                    throw jse;
                }
            } catch (IOException ioe) {
                logger.trace("io(): Exception occurred during I/O: {}.", ioe.getMessage());
                lastIOE = ioe;
                // Error Retries with Exponential Backoff
                long waitTime = ((long) Math.pow(2, retryCount) * configuration.timeoutMsecs);
                logger.trace("io(): wait time {} msecs.", waitTime);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    logger.trace("io() wait interrupted.");
                }
            }
        } while (retryCount++ < configuration.retries);
        logger.info("io(): socket I/O failed continuously ({} times).", configuration.retries);
        throw lastIOE;
    }

    /**
     * Initializes an authenticated communication with the {@link VeluxBridge <b>Velux</b> bridge}.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param authentication  as String providing the Authentication token to be passed with the request header.
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @param                 <T> generic response based on classOfResponse.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o. Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws                     java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    private <T> T ioAuthenticated(String url, String authentication, Object Request, Class<T> classOfResponse)
            throws JsonSyntaxException, IOException {
        return io(url, authentication, Request, classOfResponse);
    }

    /**
     * Initializes an unauthenticated communication with the {@link VeluxBridge <b>Velux</b> bridge}.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @param                 <T> generic response based on classOfResponse.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o. Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws                     java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    private <T> T ioUnauthenticated(String url, Object Request, Class<T> classOfResponse)
            throws JsonSyntaxException, IOException {
        return io(url, "", Request, classOfResponse);
    }

    /**
     * Prepares an authorization request and communicate it with the <b>Velux</b> veluxBridge.
     * In the positive case, the return authorization token will be stored within this class
     * for any further communication via {@link#bridgeCommunicate} up
     * to a deauthorization with method {@link VeluxBridgeProvider#bridgeLogout}
     *
     * @return <b>boolean</b>
     *         whether the logout operation according to the request was successful.
     */
    public synchronized boolean bridgeLogin() {
        logger.trace("bridgeLogin() called.");

        BClogin.Response loginResponse = bridgeCommunicate(new BClogin(configuration.bridgePassword), false);
        if (loginResponse != null) {
            logger.trace("bridgeLogin(): communication succeeded.");
            if (loginResponse.getResult()) {
                logger.trace("bridgeLogin(): storing authentication token for further access.");
                authenticationToken = loginResponse.getToken();
                return true;
            }
        }
        return false;
    }

    /**
     * Prepares an (authenticated!) deauthorization request and communicate it with the <b>Velux</b> veluxBridge.
     * In any case, the authorization token stored in this class will be destroyed, so that the
     * next communication has to start with {@link VeluxBridgeProvider#bridgeLogin}.
     *
     * @return <b>boolean</b>
     *         whether the logout operation according to the request was successful.
     */
    public synchronized boolean bridgeLogout() {
        logger.trace("bridgeLogout() called.");

        BClogout.Response logoutResponse = bridgeCommunicate(new BClogout());

        logger.trace("bridgeLogout(): emptying authentication token.");
        authenticationToken = "";

        if (logoutResponse != null) {
            logger.trace("bridgeLogout(): communication succeeded.");
            if (logoutResponse.getResult()) {
                logger.trace("bridgeLogout(): logout successful.");
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes a client/server communication towards <b>Velux</b> veluxBridge
     * based on the Basic I/O interface {@link VeluxBridge} and parameters
     * passed as arguments (see below) and provided by {@link VeluxBridgeConfiguration}.
     *
     * @param communication     Structure of interface type {@link BridgeCommunicationProtocol} describing the
     *                              intended
     *                              communication,
     *                              that is request and response interactions as well as appropriate URL definition.
     * @param                   <T> generic response based on details within communication.
     * @param useAuthentication boolean flag to decide whether to use authenticated communication.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o.
     *         Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     */
    public synchronized <T> T bridgeCommunicate(BridgeCommunicationProtocol<T> communication,
            boolean useAuthentication) {
        logger.trace("bridgeCommunicate({},{}authenticated) called.", communication.name(),
                useAuthentication ? "" : "un");

        if (!isAuthenticated()) {
            if (useAuthentication) {
                logger.trace("bridgeCommunicate(): no auth token available, aborting.");
                return null;
            } else {
                logger.trace("bridgeCommunicate(): no auth token available, continuing.");
            }
        }

        String sapURL = configuration.bridgeProtocol.concat("://").concat(configuration.bridgeIPAddress).concat(":")
                .concat(Integer.toString(configuration.bridgeTCPPort)).concat(communication.getURL());
        logger.trace("bridgeCommunicate(): using SAP {}.", sapURL);
        Object getRequest = communication.getObjectOfRequest();
        Class<T> classOfResponse = communication.getClassOfResponse();
        T response;

        try {
            if (useAuthentication) {
                response = ioAuthenticated(sapURL, authenticationToken, getRequest, classOfResponse);
            } else {
                response = ioUnauthenticated(sapURL, getRequest, classOfResponse);
            }
            logger.trace("bridgeCommunicate(): communication result is {}, returning details.",
                    communication.isCommunicationSuccessful(response));
            return response;
        } catch (IOException ioe) {
            logger.info("bridgeCommunicate(): Exception occurred on accessing {}: {}.", sapURL, ioe.getMessage());
            return null;
        } catch (JsonSyntaxException jse) {
            logger.info("bridgeCommunicate(): Exception occurred on (de-)serialization during accessing {}: {}.",
                    sapURL, jse.getMessage());
            return null;
        }
    }

    /**
     * Initializes a client/server communication towards <b>Velux</b> veluxBridge
     * based on the Basic I/O interface {@link VeluxBridge} and parameters
     * passed as arguments (see below) and provided by {@link VeluxBridgeConfiguration}.
     * This method automatically decides to invoke a login communication before the
     * intended request if there has not been an authentication before.
     *
     * @param communication Structure of interface type {@link BridgeCommunicationProtocol} describing the intended
     *                          communication,
     *                          that is request and response interactions as well as appropriate URL definition.
     * @param               <T> generic response based on details within communication.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o.
     *         Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     */
    public synchronized <T> T bridgeCommunicate(BridgeCommunicationProtocol<T> communication) {
        logger.trace("bridgeCommunicate({}) called.", communication.name());
        if (!isAuthenticated()) {
            bridgeLogin();
        }
        return bridgeCommunicate(communication, true);
    }

    private boolean isAuthenticated() {
        return (this.authenticationToken != null) && (authenticationToken.length() > 0);
    }
}

/**
 * end-of-bridge/VeluxBridge.java
 */
