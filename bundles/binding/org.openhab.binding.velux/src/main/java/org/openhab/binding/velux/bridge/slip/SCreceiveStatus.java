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

import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Retrieve House Status</B>
 * <P>
 * Common Message semantic: Communication from the bridge and storing returned information within the class itself.
 * <P>
 * As 3rd level class it defines informations how to receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the interface {@link org.openhab.binding.velux.bridge.json.JsonBridgeCommunicationProtocol
 * SlipBridgeCommunicationProtocol}.
 * <P>
 * Methods in addition to the mentioned interface:
 * <UL>
 * <LI>{@link #getNtfNodeID} to retrieve the node identifier which has been changed.</LI>
 * <LI>{@link #getNtfState} to retrieve the state of the node which has been changed.</LI>
 * <LI>{@link #getNtfCurrentPosition} to retrieve the actual position of this node.</LI>
 * <LI>{@link #getNtfTarget} to retrieve the target position of this node.</LI>
 * </UL>
 * <P>
 * NOTE: the class does NOT define a request as it only works as receiver.
 *
 * @see BridgeCommunicationProtocol
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCreceiveStatus implements BridgeCommunicationProtocol, SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCreceiveStatus.class);

    private static final String DESCRIPTION = "Retrieve House Status";
    private static final Command COMMAND = Command.UNDEFTYPE;

    /*
     * ===========================================================
     * Message Objects
     */

    @SuppressWarnings("unused")
    private byte[] requestData;

    /*
     * ===========================================================
     * Result Objects
     */

    private boolean success = false;
    private boolean finished = false;

    private int ntfNodeID;
    private int ntfState;
    private int ntfCurrentPosition;
    private int ntfTarget;

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
        logger.debug("getRequestCommand() returns {} ({}).", COMMAND.name(), COMMAND.getCommand());
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        return null;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = true;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_NODE_STATE_POSITION_CHANGED_NTF:
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 20)) {
                    break;
                }
                ntfNodeID = responseData.getOneByteValue(0);
                ntfState = responseData.getOneByteValue(1);
                ntfCurrentPosition = responseData.getTwoByteValue(2);
                ntfTarget = responseData.getTwoByteValue(4);
                @SuppressWarnings("unused")
                int ntfFP1CurrentPosition = responseData.getTwoByteValue(6);
                @SuppressWarnings("unused")
                int ntfFP2CurrentPosition = responseData.getTwoByteValue(8);
                @SuppressWarnings("unused")
                int ntfFP3CurrentPosition = responseData.getTwoByteValue(10);
                @SuppressWarnings("unused")
                int ntfFP4CurrentPosition = responseData.getTwoByteValue(12);
                int ntfRemainingTime = responseData.getTwoByteValue(14);
                int ntfTimeStamp = responseData.getFourByteValue(16);
                // Extracting information items
                logger.trace("setResponse(): ntfNodeID={}.", ntfNodeID);
                logger.trace("setResponse(): ntfState={}.", ntfState);
                logger.trace("setResponse(): ntfCurrentPosition={}.", ntfCurrentPosition);
                logger.trace("setResponse(): ntfTarget={}.", ntfTarget);
                logger.trace("setResponse(): ntfRemainingTime={}.", ntfRemainingTime);
                logger.trace("setResponse(): ntfTimeStamp={}.", ntfTimeStamp);
                success = true;
                break;

            default:
                KLF200Response.errorLogging(logger, responseCommand);
        }
        KLF200Response.outroLogging(logger, success, finished);
    }

    @Override
    public boolean isCommunicationFinished() {
        return true;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return true;
    }

    /*
     * ===========================================================
     * Methods in addition to the interface {@link BridgeCommunicationProtocol}
     */

    /**
     * @return <b>ntfNodeID</b> returns the Actuator Id as int.
     */
    public int getNtfNodeID() {
        return ntfNodeID;
    }

    /**
     * @return <b>ntfState</b> returns the state of the Actuator as int.
     */
    public int getNtfState() {
        return ntfState;
    }

    /**
     * @return <b>ntfCurrentPosition</b> returns the current position of the Actuator as int.
     */
    public int getNtfCurrentPosition() {
        return ntfCurrentPosition;
    }

    /**
     * @return <b>ntfTarget</b> returns the target position of the Actuator as int.
     */
    public int getNtfTarget() {
        return ntfTarget;
    }

}
