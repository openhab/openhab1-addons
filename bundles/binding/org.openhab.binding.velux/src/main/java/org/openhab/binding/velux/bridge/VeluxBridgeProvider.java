/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxExistingScenes;

/**
 * This interface is implemented by classes that provide general communication with the Velux bridge.
 * <P>
 * Communication
 * </P>
 * <UL>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#bridgeCommunicate bridgeCommunicate} for
 * generic communication,</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#bridgeLogin bridgeLogin} and</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#bridgeLogout bridgeLogout} for common
 * communication,</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#bridgeOverwriteConfig bridgeOverwriteConfig} for
 * adapting communication configuration,</LI>
 * </UL>
 *
 * <P>
 * Status
 * </P>
 * and, in addition, two methods for bridge-internal configuration retrieval:
 * <UL>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#getExistingsProducts getExistingsProducts} for
 * retrieving scene informations,</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.VeluxBridgeProvider#getExistingsScenes getExistingsScenes} for retrieving
 * product informations.</LI>
 * </UL>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public interface VeluxBridgeProvider {

    /**
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetProducts#getProducts}
     *
     * @return <b>response</b> of type VeluxExistingProducts containing all registered products.
     *         <P>
     *         <B>null</B> in case of any error.
     */
    public VeluxExistingProducts getExistingsProducts();

    /**
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetScenes#getScenes}
     *
     * @return <b>response</b> of type VeluxExistingScenes containing all registered scenes.
     *         <P>
     *         <B>null</B> in case of any error.
     */
    public VeluxExistingScenes getExistingsScenes();

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
    public void bridgeOverwriteConfig(int retries, int waitIntervalInMSecs);

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
    public <T> T bridgeCommunicate(BridgeCommunicationProtocol<T> communication, boolean useAuthentication);

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

    public <T> T bridgeCommunicate(BridgeCommunicationProtocol<T> communication);

    /**
     * Prepares an authorization request and communicate it with the <b>Velux</b> veluxBridge.
     * In the positive case, the return authorization token will be stored within this class
     * for any further communication via {@link#bridgeCommunicate} up
     * to a deauthorization with method {@link VeluxBridgeProvider#bridgeLogout}
     *
     * @return <b>boolean</b>
     *         whether the logout operation according to the request was successful.
     */
    public boolean bridgeLogin();

    /**
     * Prepares an authenticated deauthorization request and communicate it with the <b>Velux</b> veluxBridge.
     * In any case, the authorization token stored in this class will be destroyed, so that the
     * next communication has to start with {@link VeluxBridgeProvider#bridgeLogin}.
     *
     * @return <b>boolean</b>
     *         whether the logout operation according to the request was successful.
     */
    public boolean bridgeLogout();
}

/**
 * end-of-bridge/VeluxBridgeProvider.java
 */
