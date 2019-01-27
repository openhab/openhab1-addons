/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
