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
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxGwLAN;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Retrieve LAN configuration</B>
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
 * <LI>{@link #getLANConfig} to retrieve the current LAN configuration.</LI>
 * </UL>
 *
 * @see GetLANConfig
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCgetLANConfig extends GetLANConfig implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCgetLANConfig.class);

    private static final String DESCRIPTION = "Retrieve LAN configuration";
    private static final Command COMMAND = Command.GW_GET_NETWORK_SETUP_REQ;

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private int cfmIpAddress;
    private int cfmMask;
    private int cfmDefGW;
    private boolean cfmDHCP;

    /*
     * ===========================================================
     * Message Objects
     */

    private byte[] requestData;

    /*
     * ===========================================================
     * Result Objects
     */

    private boolean success = false;
    private boolean finished = false;

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
        success = false;
        finished = false;
        logger.debug("getRequestCommand() returns {} ({}).", COMMAND.name(), COMMAND.getCommand());
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        requestData = new byte[1];
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = false;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_GET_NETWORK_SETUP_CFM:
                finished = true;
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 13)) {
                    break;
                }
                cfmIpAddress = responseData.getFourByteValue(0);
                cfmMask = responseData.getFourByteValue(4);
                cfmDefGW = responseData.getFourByteValue(8);
                cfmDHCP = responseData.getOneByteValue(12) == 0 ? false : true;
                success = true;
                break;

            default:
                KLF200Response.errorLogging(logger, responseCommand);
                finished = true;
        }
        KLF200Response.outroLogging(logger, success, finished);
    }

    @Override
    public boolean isCommunicationFinished() {
        return finished;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return success;
    }

    /*
     * ===========================================================
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public VeluxGwLAN getLANConfig() {
        logger.trace("getLANConfig() called.");
        VeluxGwLAN result = new VeluxGwLAN(Packet.intToIPAddressString(cfmIpAddress),
                Packet.intToIPAddressString(cfmMask), Packet.intToIPAddressString(cfmDefGW), cfmDHCP);
        logger.debug("getLANConfig() returns {}.", result);
        return result;
    }

}
