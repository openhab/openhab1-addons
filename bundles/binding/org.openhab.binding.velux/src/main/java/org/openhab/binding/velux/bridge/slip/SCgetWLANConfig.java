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

import org.openhab.binding.velux.bridge.common.GetLANConfig;
import org.openhab.binding.velux.bridge.common.GetWLANConfig;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxGwWLAN;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Retrieve WLAN configuration</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class
 * itself.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface {@link org.openhab.binding.velux.bridge.json.JsonBridgeCommunicationProtocol
 * SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link #getWLANConfig} to retrieve the current WLAN configuration.</LI>
 * </UL>
 *
 * @see GetLANConfig
 * @see SlipBridgeCommunicationProtocol
 *
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCgetWLANConfig extends GetWLANConfig implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCgetWLANConfig.class);

    private static final String DESCRIPTION = "Retrieve WLAN configuration";
    private static final Command COMMAND = Command.GW_GET_NETWORK_SETUP_REQ;

    /*
     * Message Objects
     */

    private byte[] requestData;
    private short responseCommand;
    @SuppressWarnings("unused")
    private byte[] responseData;

    /*
     * ===========================================================
     * Constructor Method
     */

    public SCgetWLANConfig() {
        logger.trace("SCgetWLANConfig(constructor) called.");
        requestData = new byte[1];
    }

    /*
     * ===========================================================
     * Methods required for interface {@link SlipBridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return DESCRIPTION;
    }

    @Override
    public CommandNumber getRequestCommand() {
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        return requestData;
    }

    @Override
    public void setResponse(short thisResponseCommand, byte[] thisResponseData) {
        logger.trace("setResponseCommand({}, {}) called.", thisResponseCommand, new Packet(thisResponseData));
        responseCommand = thisResponseCommand;
        responseData = thisResponseData;
    }

    @Override
    public boolean isCommunicationFinished() {
        return true;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return (responseCommand == Command.GW_GET_NETWORK_SETUP_CFM.getShort());
    }

    /*
     * ===========================================================
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public VeluxGwWLAN getWLANConfig() {
        logger.trace("getWLANConfig() called.");
        // ToDo: Velux will provide an API for retrieving the SSID.
        return new VeluxGwWLAN("***yet-unsupported-by-firmware**", "***yet-unsupported-by-firmware**");
    }

}
