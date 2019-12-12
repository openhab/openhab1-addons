/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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

import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.common.BridgeAPI;
import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.common.CheckLostNodes;
import org.openhab.binding.velux.bridge.common.DetectProducts;
import org.openhab.binding.velux.bridge.common.GetDeviceStatus;
import org.openhab.binding.velux.bridge.common.GetFirmware;
import org.openhab.binding.velux.bridge.common.GetLANConfig;
import org.openhab.binding.velux.bridge.common.GetProduct;
import org.openhab.binding.velux.bridge.common.GetProducts;
import org.openhab.binding.velux.bridge.common.GetScenes;
import org.openhab.binding.velux.bridge.common.GetWLANConfig;
import org.openhab.binding.velux.bridge.common.IdentifyProduct;
import org.openhab.binding.velux.bridge.common.Login;
import org.openhab.binding.velux.bridge.common.Logout;
import org.openhab.binding.velux.bridge.common.ModifyHouseStatusMonitor;
import org.openhab.binding.velux.bridge.common.RunScene;
import org.openhab.binding.velux.bridge.common.SendCommand;
import org.openhab.binding.velux.bridge.common.SetSilentMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of Bridge configuration.
 * <P>
 *
 * It defines information how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class JsonBridgeAPI extends JsonVeluxBridge implements BridgeAPI {
    private final Logger logger = LoggerFactory.getLogger(JsonBridgeAPI.class);

    private static final CheckLostNodes CHECKLOSTNODES = new JCcheckLostNodes();
    private static final DetectProducts DETECTPRODUCTS = new JCdetectProducts();
    private static final GetDeviceStatus GETDEVICESTATUS = new JCgetDeviceStatus();
    private static final GetFirmware GETFIRMWARE = new JCgetFirmware();
    private static final GetLANConfig GETLANCONFIG = new JCgetLANConfig();
    private static final GetProducts GETPRODUCTS = new JCgetProducts();
    private static final GetScenes GETSCENES = new JCgetScenes();
    private static final GetWLANConfig GETWLANCONFIG = new JCgetWLANConfig();
    private static final IdentifyProduct IDENTIFYPRODUCT = new JCidentifyProduct();
    private static final Login LOGIN = new JClogin();
    private static final Logout LOGOUT = new JClogout();
    private static final RunScene RUNSCENE = new JCrunScene();
    private static final SetSilentMode SETSILENTMODE = new JCsetSilentMode();

    /**
     * Constructor.
     * <P>
     * Inherits the initialization of the binding-wide instance for dealing for common information and
     * initializes the handler {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge#bridgeAPI
     * JsonVeluxBridge.bridgeAPI}
     * to pass the interface methods.
     *
     * @param bridgeInstance refers to the binding-wide instance for dealing for common informations.
     */
    public JsonBridgeAPI(VeluxBridgeInstance bridgeInstance) {
        super(bridgeInstance);
        logger.trace("JsonBridgeAPI(constructor) called.");
        this.bridgeAPI = this;
    }

    @Override
    public boolean bridgeDirectCommunicate(BridgeCommunicationProtocol communication, boolean useAuthentication) {
        logger.trace("bridgeDirectCommunicate({},{}) called.", communication, useAuthentication);
        return this.bridgeDirectCommunicate((JsonBridgeCommunicationProtocol) communication, useAuthentication);
    }

    @Override
    public long bridgeLastSuccessfullCommunication() {
        return this.lastSuccessfullCommunicationMillis;
    }

    @Override
    public CheckLostNodes checkLostNodes() {
        return CHECKLOSTNODES;
    }

    @Override
    public DetectProducts detectProducts() {
        return DETECTPRODUCTS;
    }

    @Override
    public GetDeviceStatus getDeviceStatus() {
        return GETDEVICESTATUS;
    }

    @Override
    public GetFirmware getFirmware() {
        return GETFIRMWARE;
    }

    @Override
    public GetLANConfig getLANConfig() {
        return GETLANCONFIG;
    }

    @Override
    public GetProducts getProducts() {
        return GETPRODUCTS;
    }

    @Override
    public GetProduct getProduct() {
        return null;
    }

    @Override
    public GetScenes getScenes() {
        return GETSCENES;
    }

    @Override
    public GetWLANConfig getWLANConfig() {
        return GETWLANCONFIG;
    }

    @Override
    public IdentifyProduct identifyProduct() {
        return IDENTIFYPRODUCT;
    }

    @Override
    public Login login() {
        return LOGIN;
    }

    @Override
    public Logout logout() {
        return LOGOUT;
    }

    @Override
    public ModifyHouseStatusMonitor modifyHouseStatusMonitor() {
        return null;
    }

    @Override
    public RunScene runScene() {
        return RUNSCENE;
    }

    @Override
    public SendCommand sendCommand() {
        return null;
    }

    @Override
    public SetSilentMode setSilentMode() {
        return SETSILENTMODE;
    }

}
