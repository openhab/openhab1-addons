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
package org.openhab.binding.velux.bridge.slip;

import org.openhab.binding.velux.bridge.VeluxBridge;
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
 * SLIP-based 3rd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides the one-and-only protocol specific 1st-level communication class.
 * Additionally it provides all methods for different gateway interactions.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link SlipBridgeAPI#bridgeDirectCommunicate} as base-level SLIP-specific communication with the bridge,</LI>
 * <LI></LI>
 * <LI>{@link SlipBridgeAPI#checkLostNodes} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#detectProducts} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getDeviceStatus} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getFirmware} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getLANConfig} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getProduct} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getProducts} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getScenes} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#getWLANConfig} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#identifyProduct} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#login} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#logout} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#modifyHouseStatusMonitor} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#runScene} as method for ...</LI>
 * <LI>{@link SlipBridgeAPI#setSilentMode} as method for ...</LI>
 * </UL>
 * <P>
 * As most derived class of the several inheritance levels it defines an
 * interfacing method {@link VeluxBridge#bridgeAPI} which
 * returns the SLIP-protocol-specific communication for gateway interaction.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SlipBridgeAPI extends SlipVeluxBridge implements BridgeAPI {
    private final Logger logger = LoggerFactory.getLogger(SlipBridgeAPI.class);

    private static final CheckLostNodes CHECKLOSTNODES = new SCcheckLostNodes();
    private static final DetectProducts DETECTPRODUCTS = new SCdetectProducts();
    private static final GetDeviceStatus GETDEVICESTATUS = new SCgetDeviceStatus();
    private static final GetFirmware GETFIRMWARE = new SCgetFirmware();
    private static final GetLANConfig GETLANCONFIG = new SCgetLANConfig();
    private static final GetProduct GETPRODUCT = new SCgetProduct();
    private static final GetProducts GETPRODUCTS = new SCgetProducts();
    private static final GetScenes GETSCENES = new SCgetScenes();
    private static final GetWLANConfig GETWLANCONFIG = new SCgetWLANConfig();
    private static final IdentifyProduct IDENTIFYPRODUCT = new SCidentifyProduct();
    private static final Login LOGIN = new SClogin();
    private static final Logout LOGOUT = new SClogout();
    private static final ModifyHouseStatusMonitor MODIFYHOUSESTATUSMONITOR = new SCmodifyHouseStatusMonitor();
    private static final RunScene RUNSCENE = new SCrunScene();
    private static final SendCommand SENDCOMMAND = new SCsendCommand();
    private static final SetSilentMode SETSILENTMODE = new SCsetSilentMode();

    /**
     * Constructor.
     * <P>
     * Inherits the initialization of the binding-wide instance for dealing for common information and
     * initializes the handler {@link org.openhab.binding.velux.bridge.slip.SlipVeluxBridge#bridgeAPI
     * SlipVeluxBridge.bridgeAPI}
     * to pass the interface methods.
     *
     * @param bridgeInstance refers to the binding-wide instance for dealing for common informations.
     */
    public SlipBridgeAPI(VeluxBridgeInstance bridgeInstance) {
        super(bridgeInstance);
        logger.trace("SlipBridgeAPI(constructor) called.");
        this.bridgeAPI = this;
    }

    @Override
    public boolean bridgeDirectCommunicate(BridgeCommunicationProtocol communication, boolean useAuthentication) {
        logger.trace("bridgeDirectCommunicate({},{}) called.", communication, useAuthentication);
        return this.bridgeDirectCommunicate((SlipBridgeCommunicationProtocol) communication, useAuthentication);
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
    public GetProduct getProduct() {
        return GETPRODUCT;
    }

    @Override
    public GetProducts getProducts() {
        return GETPRODUCTS;
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
        return MODIFYHOUSESTATUSMONITOR;
    }

    @Override
    public RunScene runScene() {
        return RUNSCENE;
    }

    @Override
    public SendCommand sendCommand() {
        return SENDCOMMAND;
    }

    @Override
    public SetSilentMode setSilentMode() {
        return SETSILENTMODE;
    }

}
