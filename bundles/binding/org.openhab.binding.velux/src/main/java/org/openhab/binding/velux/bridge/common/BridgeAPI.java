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
package org.openhab.binding.velux.bridge.common;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of Bridge configuration.
 * <P>
 *
 * It defines information how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the BridgeCommunicationProtocol.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public interface BridgeAPI {
    public boolean bridgeDirectCommunicate(BridgeCommunicationProtocol communication, boolean useAuthentication);

    public long bridgeLastSuccessfullCommunication();

    public CheckLostNodes checkLostNodes();

    public DetectProducts detectProducts();

    public GetDeviceStatus getDeviceStatus();

    public GetFirmware getFirmware();

    public GetLANConfig getLANConfig();

    public GetProducts getProducts();

    public GetProduct getProduct();

    public GetScenes getScenes();

    public GetWLANConfig getWLANConfig();

    public IdentifyProduct identifyProduct();

    public Login login();

    public Logout logout();

    public ModifyHouseStatusMonitor modifyHouseStatusMonitor();

    public RunScene runScene();

    public SendCommand sendCommand();

    public SetSilentMode setSilentMode();
}
