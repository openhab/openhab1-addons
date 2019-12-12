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
package org.openhab.binding.velux.bridge.slip.util;

import org.openhab.binding.velux.things.VeluxKLFAPI.Command;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.slf4j.Logger;

/**
 * Utility class for handling of KLF200 response packets.
 *
 * <P>
 * Methods available:
 * <P>
 * Static methods are:
 * <UL>
 * <LI>{@link #introLogging} for logging used at the beginning of the response handling.</LI>
 * <LI>{@link #errorLogging} for error logging during processing.</LI>
 * <LI>{@link #outroLogging} logging used at the end of the response handling.</LI>
 * <LI>{@link #isLengthValid} for validation of length of the data part of the message.</LI>
 * <LI>{@link #check4matchingNodeID} for validation of node identifier.</LI>
 * <LI>{@link #check4matchingSessionID} for validation of session identifier.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class KLF200Response {

    /**
     * Provides user-oriented logging in two log levels for monitoring KLF behavior.
     * <P>
     * Introduction logging used at the beginning of the response handling.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param responseCommand The command byte of the response packet.
     * @param thisResponseData The array of bytes which are passed back within the response package.
     */
    public static void introLogging(Logger logger, short responseCommand, byte[] thisResponseData) {
        logger.debug("setResponse({} with {} bytes of data) called.", Command.get(responseCommand).toString(),
                thisResponseData.length);
        logger.trace("setResponse(): handling response {} ({}).", Command.get(responseCommand).toString(),
                new CommandNumber(responseCommand).toString());
    }

    /**
     * Provides user-oriented logging in two log levels for monitoring KLF behavior.
     * <P>
     * Error logging used at the point where an unexpected or unrecognized command has been received.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param responseCommand The command byte of the response packet.
     */
    public static void errorLogging(Logger logger, short responseCommand) {
        logger.trace("setResponse(): cannot handle response {} ({}).", Command.get(responseCommand).toString(),
                responseCommand);
        logger.warn("Gateway response {} ({}) cannot be handled at this point of interaction.",
                Command.get(responseCommand).toString(), responseCommand);
    }

    /**
     * Provides user-oriented logging in two log levels for monitoring KLF behavior.
     * <P>
     * Conclusion logging used at the end of the response handling.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param success Describes the success of the response processing.
     * @param finished Describes whether the response processing has come to an end.
     */
    public static void outroLogging(Logger logger, boolean success, boolean finished) {
        logger.trace("setResponse(): finished={},success={}.", finished, success);
    }

    /**
     * Provides user-oriented logging in two log levels for monitoring KLF behavior.
     * <P>
     * Check the intended length of the response packet.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param responseCommand The command byte of the response packet.
     * @param thisResponseData The array of bytes which are passed back within the response package.
     * @param requiredResponseDataLength The expected size of the array of bytes which are passed back within the
     *            response package.
     * @return <b>isLengthValid</b> of type boolean which signals the validity of the packet length.
     */
    public static boolean isLengthValid(Logger logger, short responseCommand, byte[] thisResponseData,
            int requiredResponseDataLength) {
        logger.trace("isLengthValid() called for {} ({}) with {} bytes of data.",
                Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString(),
                thisResponseData.length);
        if (thisResponseData.length != requiredResponseDataLength) {
            logger.warn(
                    "Gateway response {} ({}) consists of a malformed packet (effective length is {}, required length is {}).",
                    Command.get(responseCommand).toString(), new CommandNumber(responseCommand).toString(),
                    thisResponseData.length, requiredResponseDataLength);
            return false;
        }
        logger.trace("isLengthValid() returns {}.", true);
        return true;
    }

    /**
     * Provides user-oriented logging in two log levels for monitoring KLF behavior.
     * <P>
     * Internal support method to match two values for equality.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param idName String describing the type of values being compared.
     * @param requestID Value of type int have been replaced within the request.
     * @param responseID Value of type int being received within the response.
     * @return <b>check4matchingAnyID</b> of type boolean which signals the equality.
     */
    private static boolean check4matchingAnyID(Logger logger, String idName, int requestID, int responseID) {
        logger.trace("check4matchingAnyID() called for request{} {} and response{} {}.", idName, requestID, idName,
                responseID);
        if (requestID != responseID) {
            logger.warn("Gateway response with {} {} unexpected as query asked for {} {}.", idName, requestID, idName,
                    responseID);
            return false;
        }
        logger.trace("check4matchingAnyID() returns {}.", true);
        return true;
    }

    /**
     * Compare the node identifier of the request with the node identifier within the response
     * with user-oriented logging in two log levels for monitoring KLF behavior.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param reqNodeID Node identifier of the request.
     * @param cfmNodeID Node identifier of the response.
     * @return <b>success</b> of type boolean which signals the success of the communication.
     */
    public static boolean check4matchingNodeID(Logger logger, int reqNodeID, int cfmNodeID) {
        logger.trace("check4matchingNodeID() called for requestNodeID {} and responseNodeID {}.", reqNodeID, cfmNodeID);
        return check4matchingAnyID(logger, "NodeID", reqNodeID, cfmNodeID);
    }

    /**
     * Compare the session identifier of the request with the session identifier within the response
     * with user-oriented logging in two log levels for monitoring KLF behavior.
     *
     * @param logger Instantiated logging class to be used for output.
     * @param reqSessionID Session identifier of the request.
     * @param cfmSessionID Session identifier of the response.
     * @return <b>success</b> of type boolean which signals the success of the communication.
     */
    public static boolean check4matchingSessionID(Logger logger, int reqSessionID, int cfmSessionID) {
        logger.trace("check4matchingSessionID() called for requestNodeID {} and responseNodeID {}.", reqSessionID,
                cfmSessionID);
        return check4matchingAnyID(logger, "SessionID", reqSessionID, cfmSessionID);
    }
}
