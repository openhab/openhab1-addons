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

import org.openhab.binding.velux.bridge.common.SetSilentMode;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProductVelocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Modify Velocity of an Actuator</B>
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
 * <LI>{@link #setMode} to define the new silence mode of the intended actuator.</LI>
 * </UL>
 *
 * @see SetSilentMode
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
// ToDo: Check the updated Velux doc against this implementation.
public class SCsetSilentMode extends SetSilentMode implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCsetSilentMode.class);

    private static final String DESCRIPTION = "Modify Velocity of an Actuator";
    private static final Command COMMAND = Command.GW_SET_NODE_VELOCITY_REQ;

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private int reqNodeID = -1;
    private int reqNodeVelocity = -1;

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
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return DESCRIPTION;
    }

    @Override
    public CommandNumber getRequestCommand() {
        success = false;
        finished = false;
        logger.debug("getRequestCommand() returns {}.", COMMAND.getCommand());
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        Packet request = new Packet(new byte[2]);
        request.setOneByteValue(0, reqNodeID);
        request.setOneByteValue(1, reqNodeVelocity);
        requestData = request.toByteArray();
        logger.trace("getRequestDataAsArrayOfBytes() data is {}.", new Packet(requestData).toString());
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = false;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_SET_NODE_VELOCITY_CFM:
                finished = true;
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 3)) {
                    break;
                }
                int cfmStatus = responseData.getOneByteValue(0);
                switch (cfmStatus) {
                    case 0:
                        logger.trace("setResponse(): returned status: Error - Wink is rejected.");
                        break;
                    case 1:
                        logger.trace("setResponse(): returned status: OK – Wink is accepted.");
                        success = true;
                        break;
                    default:
                        logger.warn("setResponse({}): returned status={} (Reserved/unknown).",
                                Command.get(responseCommand).toString(), cfmStatus);
                        break;
                }
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
     * Methods in addition to the interface {@link BridgeCommunicationProtocol}
     * and the abstract class {@link IdentifyProduct}
     */

    /**
     * Constructor Addon Method.
     * <P>
     * Passes the intended Actuator Identifier towards this class for building the request lateron.
     *
     * @param actuatorId as type int describing the scene to be processed.
     * @param silent as type boolean describing the silence mode of this node.
     * @return <b>this</b> of type {@link SCsetSilentMode} as class itself.
     */
    @Override
    public SCsetSilentMode setMode(int actuatorId, boolean silent) {
        logger.trace("setProductId({},{}) called.", actuatorId, silent);
        this.reqNodeID = actuatorId;
        this.reqNodeVelocity = silent ? VeluxProductVelocity.SILENT.getVelocity()
                : VeluxProductVelocity.FAST.getVelocity();
        return this;
    }

}
