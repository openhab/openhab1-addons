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
package org.openhab.binding.velux.bridge.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.common.BridgeAPI;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonSyntaxException;

/**
 * JSON-based 2nd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides methods for pre- and postcommunication
 * as well as a common method for the real communication.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link VeluxBridge#bridgeLogin} for pre-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeLogout} for post-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeCommunicate} as method for the common communication.</LI>
 * </UL>
 * The behavior of the communication can, in addition of the general configuration,
 * be changed by
 * <UL>
 * <LI>{@link VeluxBridge#bridgeOverwriteConfig} for modifying the communication parameters.</LI>
 * </UL>
 * <P>
 * As root of several inheritance levels it predefines an
 * interfacing method {@link VeluxBridge#bridgeAPI} which
 * has to be implemented by any kind of protocol-specific
 * communication returning the appropriate base (1st) level
 * communication method as well as any other gateway
 * interaction.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class JsonVeluxBridge extends VeluxBridge {
    private final Logger logger = LoggerFactory.getLogger(JsonVeluxBridge.class);

    /**
     * Support protocols for the concrete implementation.
     * {@link VeluxBridgeConfiguration}.
     */
    public Set<String> supportedProtocols;

    /**
     * Timestamp of last successfull communication in milliseconds.
     *
     */
    protected long lastSuccessfullCommunicationMillis = 0;

    /**
     * Handler passing the interface methods to other classes.
     * Can be accessed via method {@link org.openhab.binding.velux.bridge.common.json.bridgeAPI bridgeAPI}.
     *
     */
    protected BridgeAPI bridgeAPI = null;

    /**
     * Constructor.
     * <P>
     * Inherits the initialization of the binding-wide instance for dealing for common informations and
     * initializes the Velux bridge connectivity settings.
     *
     * @param bridgeInstance refers to the binding-wide instance for dealing for common informations.
     */
    public JsonVeluxBridge(VeluxBridgeInstance bridgeInstance) {
        super(bridgeInstance);
        logger.trace("JsonVeluxBridge(constructor) called.");
        supportedProtocols = new TreeSet<String>();
        supportedProtocols.add("http");
        supportedProtocols.add("https");
    }

    /**
     * Provide access to interface methods contained within {@link org.openhab.binding.velux.bridge.common.BridgeAPI
     * BridgeAPI}level communication with the {@link JsonVeluxBridge <b>Velux</b> bridge}.
     *
     * @return <b>bridgeAPI</b> of type {@link BridgeAPI} containing all methods being implemented for this type of
     *         bridge.
     */
    @Override
    public BridgeAPI bridgeAPI() {
        logger.trace("bridgeAPI() called.");
        return bridgeAPI;
    }

    /**
     * Base level communication with the <b>Velux</b> bridge.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param authentication  as String providing the Authentication token to be passed with the request header.
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @return <b>response</b> of type Object containing all resulting informations, i.e. device status, errors a.s.o.
     *         Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object io(String url, String authentication, Object Request, Class classOfResponse)
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
                        "application/json", this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
                if (jsonResponse == null) {
                    throw new IOException("transport error");
                }
                logger.trace("io(): wait time {} msecs.", this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
                // Give the bridge some time to breathe
                try {
                    Thread.sleep(this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
                } catch (InterruptedException ie) {
                    logger.trace("io() wait interrupted.");
                }

                logger.trace("io() got response {}.", jsonResponse.replaceAll("\\p{C}", "."));
                jsonResponse = jsonResponse.replaceAll("^.+,\n", "");
                logger.trace("io() cleaned response {}.", jsonResponse);
                try {
                    Object response = gson.fromJson(jsonResponse, classOfResponse);
                    lastSuccessfullCommunicationMillis = System.currentTimeMillis();
                    return response;
                } catch (JsonSyntaxException jse) {
                    logger.info("io(): Exception occurred on deserialization: {}, aborting.", jse.getMessage());
                    throw jse;
                }
            } catch (IOException ioe) {
                logger.trace("io(): Exception occurred during I/O: {}.", ioe.getMessage());
                lastIOE = ioe;
                // Error Retries with Exponential Backoff
                long waitTime = ((long) Math.pow(2, retryCount)
                        * this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
                logger.trace("io(): wait time {} msecs.", waitTime);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    logger.trace("io() wait interrupted.");
                }
            }
        } while (retryCount++ < this.bridgeInstance.veluxBridgeConfiguration().retries);
        logger.info("io(): socket I/O failed ({} times).", this.bridgeInstance.veluxBridgeConfiguration().retries);
        throw lastIOE;
    }

    /**
     * Initializes an authenticated communication with the {@link JsonVeluxBridge <b>Velux</b> bridge}.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param authentication  as String providing the Authentication token to be passed with the request header.
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @return <b>response</b> of type T containing all resulting informations, i.e. device status, errors a.s.o. Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    @SuppressWarnings("rawtypes")
    private Object ioAuthenticated(String url, String authentication, Object Request, Class classOfResponse)
            throws JsonSyntaxException, IOException {
        return io(url, authentication, Request, classOfResponse);
    }

    /**
     * Initializes an unauthenticated communication with the {@link JsonVeluxBridge <b>Velux</b> bridge}.
     *
     * @param url             as String describing the Service Access Point location i.e. http://localhost/api .
     * @param Request         as Object representing the structure of the message request body to be converted into
     *                            JSON.
     * @param classOfResponse as Class representing the expected structure of the message response body to be converted
     *                            from JSON.
     * @param <T>             generic response based on classOfResponse.
     * @return <b>response</b> of type Object containing all resulting informations, i.e. device status, errors a.s.o.
     *         Will
     *         return
     *         <B>null</B> in case of communication or decoding error.
     * @throws java.io.IOException in case of continuous communication I/O failures.
     * @throws JsonSyntaxException in case of unusual communication failures.
     */
    @SuppressWarnings("rawtypes")
    private Object ioUnauthenticated(String url, Object Request, Class classOfResponse)
            throws JsonSyntaxException, IOException {
        return io(url, "", Request, classOfResponse);
    }

    /**
     * Initializes a client/server communication towards <b>Velux</b> veluxBridge
     * based on the Basic I/O interface {@link VeluxBridge} and parameters
     * passed as arguments (see below) and provided by {@link VeluxBridgeConfiguration}.
     *
     * @param communication     Structure of interface type {@link JsonBridgeCommunicationProtocol} describing the
     *                              intended
     *                              communication,
     *                              that is request and response interactions as well as appropriate URL definition.
     * @param useAuthentication boolean flag to decide whether to use authenticated communication.
     * @return <b>response</b> of type boolean will indicate the success of the communication.
     */
    public synchronized boolean bridgeDirectCommunicate(JsonBridgeCommunicationProtocol communication,
            boolean useAuthentication) {
        logger.trace("bridgeDirectCommunicate({},{}authenticated) called.", communication.name(),
                useAuthentication ? "" : "un");

        String sapURL = this.bridgeInstance.veluxBridgeConfiguration().bridgeProtocol.concat("://")
                .concat(this.bridgeInstance.veluxBridgeConfiguration().bridgeIPAddress).concat(":")
                .concat(Integer.toString(this.bridgeInstance.veluxBridgeConfiguration().bridgeTCPPort))
                .concat(communication.getURL());
        logger.trace("bridgeCommunicate(): using SAP {}.", sapURL);
        Object getRequest = communication.getObjectOfRequest();
        if (getRequest == null) {
            logger.warn("bridgeCommunicate(): Internal error: Empty object structure ({}), should never occur.",
                    communication.name());
            return false;
        }
        Class<?> classOfResponse = communication.getClassOfResponse();
        Object response;

        try {
            if (useAuthentication) {
                response = ioAuthenticated(sapURL, authenticationToken, getRequest, classOfResponse);
            } else {
                response = ioUnauthenticated(sapURL, getRequest, classOfResponse);
            }
            communication.setResponse(response);
            logger.trace("bridgeCommunicate(): communication result is {}, returning details.",
                    communication.isCommunicationSuccessful());
            return true;
        } catch (IOException ioe) {
            logger.warn("bridgeCommunicate(): Exception occurred on accessing {}: {}.", sapURL, ioe.getMessage());
            return false;
        } catch (JsonSyntaxException jse) {
            logger.warn("bridgeCommunicate(): Exception occurred on (de-)serialization during accessing {}: {}.",
                    sapURL, jse.getMessage());
            return false;
        }
    }

}
