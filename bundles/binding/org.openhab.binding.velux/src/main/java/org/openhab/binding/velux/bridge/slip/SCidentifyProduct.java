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

import java.util.Random;

import org.openhab.binding.velux.bridge.common.IdentifyProduct;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Identify Product</B>
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
 * <LI>{@link #setProductId(int)} to define the intended product.</LI>
 * </UL>
 *
 * @see IdentifyProduct
 * @see SlipBridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SCidentifyProduct extends IdentifyProduct implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SCidentifyProduct.class);

    private static final String DESCRIPTION = "Identify Product";
    private static final Command COMMAND = Command.GW_WINK_SEND_REQ;

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private int reqSessionID = 0;
    private int reqCommandOriginator = 8; // SAAC
    private int reqPriorityLevel = 5; // Comfort Level 2
    private int reqWinkState = 1; // Enable wink
    private int reqWinkTime = 2; // Winktime = 2 seconds
    private int reqIndexArrayCount = 1; // Number of actuators to be winking
    private int reqIndexValue0 = 0; // Value for the ONE actuator

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
     * Constructor Method
     */

    /**
     * Constructor.
     * <P>
     * Initializes the session id {@link #reqSessionID} with a random start value.
     */
    public SCidentifyProduct() {
        logger.debug("SCidentifyProduct(Constructor) called.");
        Random rand = new Random();
        reqSessionID = rand.nextInt(0x0fff);
        logger.debug("SCidentifyProduct(): starting sessions with the random number {}.", reqSessionID);
    }

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
        logger.debug("getRequestCommand() returns {} ({}).", COMMAND.name(), COMMAND.getCommand());
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        Packet request = new Packet(new byte[27]);
        reqSessionID = (reqSessionID + 1) & 0xffff;
        request.setTwoByteValue(0, reqSessionID);
        request.setOneByteValue(2, reqCommandOriginator);
        request.setOneByteValue(3, reqPriorityLevel);
        request.setOneByteValue(4, reqWinkState);
        request.setOneByteValue(5, reqWinkTime);
        request.setOneByteValue(6, reqIndexArrayCount);
        request.setTwoByteValue(7, reqIndexValue0);
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
            case GW_WINK_SEND_CFM:
                finished = true;
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 3)) {
                    break;
                }
                int cfmSessionID = responseData.getTwoByteValue(0);
                int cfmStatus = responseData.getOneByteValue(2);
                switch (cfmStatus) {
                    case 0:
                        logger.trace("setResponse(): returned status: Error - Wink is rejected.");
                        break;
                    case 1:
                        if (!KLF200Response.check4matchingSessionID(logger, cfmSessionID, reqSessionID)) {
                            break;
                        }
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
     * Passes the intended Actuator Identifier towards this class for building the request.
     *
     * @param actuatorId as type int describing the scene to be processed.
     * @return <b>this</b> of type {@link SCidentifyProduct} as class itself.
     */
    @Override
    public SCidentifyProduct setProductId(int actuatorId) {
        logger.trace("setProductId({}) called.", actuatorId);
        this.reqIndexValue0 = actuatorId;
        return this;
    }

}
