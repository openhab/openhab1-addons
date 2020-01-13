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
package org.openhab.binding.velux.bridge;

import java.util.Set;

import org.openhab.binding.velux.bridge.common.BridgeAPI;
import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.common.Login;
import org.openhab.binding.velux.bridge.common.Logout;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2nd Level I/O interface towards the <B>Velux</B> bridge.
 * It provides methods for pre- and post-communication
 * as well as a common method for the real communication.
 * The following class access methods exist:
 * <UL>
 * <LI>{@link VeluxBridge#bridgeLogin} for pre-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeLogout} for post-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeCommunicate} as method for the common communication.</LI>
 * </UL>
 * The behavior of the communication can, in addition of the general configuration,
 * be changed by {@link VeluxBridge#bridgeOverwriteConfig} for modifying the
 * communication parameters.
 * Each protocol-specific implementation provides a publicly visible
 * set of supported protocols as variable {@link #supportedProtocols}.
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
public class VeluxBridge {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridge.class);

    // Type definitions, variables

    /**
     * Support protocols for the concrete implementation.
     * {@link VeluxBridgeConfiguration}.
     */
    public Set<String> supportedProtocols;

    /** BridgeCommunicationProtocol authentication token for Velux Bridge. */
    protected String authenticationToken = "";

    /**
     * Handler to access global bridge instance methods
     *
     */
    protected VeluxBridgeInstance bridgeInstance = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the binding-wide instance for dealing with common informations and
     * the Velux bridge connectivity settings by preparing the configuration settings with help
     * by {@link VeluxBridgeConfiguration}.
     *
     * @param bridgeInstance refers to the binding-wide instance for dealing for common informations
     *            like existing actuators and predefined scenes.
     */
    public VeluxBridge(VeluxBridgeInstance bridgeInstance) {
        logger.trace("VeluxBridge(constructor) called.");
        this.bridgeInstance = bridgeInstance;
    }

    // Destructor methods

    /**
     * Destructor.
     * <P>
     * Deinitializes the binding-wide instance.
     *
     */
    public void shutdown() {
        logger.trace("shutdown() called.");
    }

    // Class access methods

    /**
     * Determines whether the binding is already authenticated against the bridge so that
     * any other communication can occur without an additional care about authentication.
     * <P>
     * This method automatically decides on availability of the stored authentication
     * information {@link VeluxBridge#authenticationToken} whether a (re-)authentication is possible.
     *
     * @return true if the bridge is authenticated; false otherwise.
     */
    private boolean isAuthenticated() {
        boolean success = (authenticationToken != null) && (authenticationToken.length() > 0);
        logger.trace("isAuthenticated() returns {}.", success);
        return success;
    }

    /**
     * Modifies the communication parameters of {@link VeluxBridge}.
     * If any of the passed parameters is negative, the default configuration
     * will be set again.
     *
     * @param retries the number of retries before throwing an I/O error.
     * @param waitIntervalInMSecs the initial time wait interval in milliseconds for the Binary
     *            Exponential Backoff (BEB) Algorithm for handling of I/O failures.
     */
    public void bridgeOverwriteConfig(int retries, int waitIntervalInMSecs) {
        logger.trace(
                "bridgeOverwriteConfig(): parameters requested: retries = {} times, initial wait interval = {} msecs.",
                retries, waitIntervalInMSecs);

        if ((retries < 0) || (waitIntervalInMSecs < 0)) {
            logger.info("bridgeOverwriteConfig(): settings ignored due to invalid values.");
            VeluxBridgeConfiguration defaultConfiguration = new VeluxBridgeConfiguration();
            bridgeInstance.veluxBridgeConfiguration().retries = defaultConfiguration.retries;
            bridgeInstance.veluxBridgeConfiguration().timeoutMsecs = defaultConfiguration.timeoutMsecs;
        } else {
            bridgeInstance.veluxBridgeConfiguration().retries = retries;
            bridgeInstance.veluxBridgeConfiguration().timeoutMsecs = waitIntervalInMSecs;
        }
        logger.trace("bridgeOverwriteConfig(): config set to retries = {} times, initial wait interval = {} ms.",
                bridgeInstance.veluxBridgeConfiguration().retries,
                bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
    }

    /**
     * Prepare an authorization request and communicate it with the <b>Velux</b> veluxBridge.
     * If login is successful, the returned authorization token will be stored within this class
     * for any further communication via {@link#bridgeCommunicate} up
     * to an authorization with method {@link VeluxBridge#bridgeLogout}.
     *
     * @return true if the login was successful, and false otherwise.
     */
    public synchronized boolean bridgeLogin() {
        logger.trace("bridgeLogin() called.");

        Login bcp = bridgeAPI().login();
        bcp.setPassword(bridgeInstance.veluxBridgeConfiguration().bridgePassword);
        if (bridgeCommunicate(bcp, false)) {
            logger.trace("bridgeLogin(): communication succeeded.");
            if (bcp.isCommunicationSuccessful()) {
                logger.trace("bridgeLogin(): storing authentication token for further access.");
                authenticationToken = bcp.getAuthToken();
                return true;
            }
        }
        return false;
    }

    /**
     * Prepare an authenticated deauthorization request and communicate it with the <b>Velux</b> veluxBridge.
     * The authorization token stored in this class will be destroyed, so that the
     * next communication has to start with {@link VeluxBridge#bridgeLogin}.
     *
     * @return true if the logout was successful, and false otherwise.
     */
    public synchronized boolean bridgeLogout() {
        logger.trace("bridgeLogout() called: emptying authentication token.");
        authenticationToken = "";

        Logout bcp = bridgeAPI().logout();
        if (bridgeCommunicate(bcp, false)) {
            logger.trace("bridgeLogout(): communication succeeded.");
            if (bcp.isCommunicationSuccessful()) {
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
     * @param communication the intended communication,
     *            that is request and response interactions as well as appropriate URL definition.
     * @param useAuthentication whether to use authenticated communication.
     * @return true if communication was successful, and false otherwise.
     */
    public synchronized boolean bridgeCommunicate(BridgeCommunicationProtocol communication,
            boolean useAuthentication) {
        logger.trace("bridgeCommunicate({},{}authenticated) called.", communication.name(),
                useAuthentication ? "" : "un");

        if (!isAuthenticated()) {
            if (useAuthentication) {
                logger.trace("bridgeCommunicate(): no auth token available, aborting.");
                return false;
            } else {
                logger.trace("bridgeCommunicate(): no auth token available, continuing.");
            }
        }
        return bridgeAPI().bridgeDirectCommunicate(communication, useAuthentication);
    }

    /**
     * Initializes a client/server communication towards <b>Velux</b> Bridge
     * based on the Basic I/O interface {@link VeluxBridge} and parameters
     * passed as arguments (see below) and provided by {@link VeluxBridgeConfiguration}.
     * This method automatically decides to invoke a login communication before the
     * intended request if there has not been an authentication before.
     *
     * @param communication the intended communication, that is request and response interactions as well as appropriate
     *            URL definition.
     * @return true if communication was successful, and false otherwise.
     */
    public synchronized boolean bridgeCommunicate(BridgeCommunicationProtocol communication) {
        logger.trace("bridgeCommunicate({}) called.", communication.name());
        if (!isAuthenticated()) {
            bridgeLogin();
        }
        return bridgeCommunicate(communication, true);
    }

    /**
     * Provides information about the base-level communication method and
     * any kind of available gateway interaction.
     * <P>
     * <B>Note:</B> <B>this</B> implementation within the class {@link VeluxBridge} will return <B>null</B>.
     * For protocol-specific implementations this method has to be overwritten along the inheritance i.e.
     * with the protocol-specific class implementations.
     *
     * @return null.
     */
    public BridgeAPI bridgeAPI() {
        logger.debug(
                "bridgeAPI() called. Should NEVER occur as it seems to be a lack of protocol-specific implementation of this bridge API.");
        return null;
    }

    /**
     * Just a Deprecated constructur
     */
    public VeluxBridge() {
    }

    @Deprecated
    public synchronized <T> T bridgeCommunicate(
            org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol<T> communication,
            boolean useAuthentication) {
        return null;
    }

    @Deprecated
    public synchronized <T> T bridgeCommunicate(
            org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol<T> communication) {
        return null;
    }

}
