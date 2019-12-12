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

import org.openhab.binding.velux.bridge.common.CheckLostNodes;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxGwState;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Check for lost Nodes</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class
 * itself.
 * <P>
 * Implementing the protocol-independent class {@link CheckLostNodes}.
 * <P>
 * As 3rd level class it defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface {@link SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link SCcheckLostNodes#getState} to retrieve the Velux gateway status.</LI>
 * </UL>
 *
 * @see CheckLostNodes
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCcheckLostNodes extends CheckLostNodes implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCcheckLostNodes.class);

    private static final String DESCRIPTION = "Check for lost Nodes";
    private static final Command COMMAND = Command.GW_GET_STATE_REQ;

    /*
     * Message Objects
     */

    private byte[] requestData;
    private short responseCommand;
    private byte[] responseData;

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
        requestData = new byte[0];
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
        return (responseCommand == Command.GW_GET_STATE_CFM.getShort());
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return (responseCommand == Command.GW_GET_STATE_CFM.getShort());
    }

    /*
     * ===========================================================
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */

    public VeluxGwState getState() {
        byte stateValue = responseData[0];
        byte subStateValue = responseData[1];
        VeluxGwState thisGwState = new VeluxGwState(stateValue, subStateValue);
        logger.trace("getState() returns {} ({}).", thisGwState, thisGwState.toDescription());
        return thisGwState;
    }

}
