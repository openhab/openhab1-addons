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

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.bridge.common.Login;
import org.openhab.binding.velux.bridge.slip.util.KLF200Response;
import org.openhab.binding.velux.bridge.slip.util.Packet;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Authenticate / login</B>
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
 * <LI>{@link #setPassword(String)} to define the authentication reqPassword to be used.</LI>
 * <LI>{@link #getAuthToken()} to retrieve the authentication reqPassword.</LI>
 * </UL>
 *
 * @see Login
 * @see SlipBridgeCommunicationProtocol
 *
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SClogin extends Login implements SlipBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(SClogin.class);

    private static final String DESCRIPTION = "Authenticate / login";
    private static final Command COMMAND = Command.GW_PASSWORD_ENTER_REQ;

    /*
     * ===========================================================
     * Message Content Parameters
     */

    private String reqPassword = "";

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
        return COMMAND.getCommand();
    }

    @Override
    public byte[] getRequestDataAsArrayOfBytes() {
        requestData = new byte[32];
        byte[] password = reqPassword.getBytes();
        System.arraycopy(password, 0, requestData, 0, password.length);
        return requestData;
    }

    @Override
    public void setResponse(short responseCommand, byte[] thisResponseData) {
        KLF200Response.introLogging(logger, responseCommand, thisResponseData);
        success = false;
        finished = true;
        Packet responseData = new Packet(thisResponseData);
        switch (Command.get(responseCommand)) {
            case GW_PASSWORD_ENTER_CFM:
                if (!KLF200Response.isLengthValid(logger, responseCommand, thisResponseData, 1)) {
                    break;
                }
                int cfmStatus = responseData.getOneByteValue(0);
                switch (cfmStatus) {
                    case 0:
                        logger.info("{} bridge connection successfully established (login succeeded).",
                                VeluxBindingConstants.BINDING_ID);
                        logger.debug("setResponse(): returned status: The request was successful.");
                        success = true;
                        break;
                    case 1:
                        logger.debug("setResponse(): returned status: The request failed.");
                        break;
                    default:
                        logger.warn("setResponse(): returned status={} (not defined).", cfmStatus);
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
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public void setPassword(String thisPassword) {
        logger.trace("setPassword({}) called.", thisPassword);
        reqPassword = thisPassword;
        return;
    }

    @Override
    public String getAuthToken() {
        logger.trace("getAuthToken() called, returning {}.", reqPassword);
        return reqPassword;
    }

}
