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
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;

/**
 * Protocol specific bridge communication supported by the Velux bridge:
 * <B>Authentication</B>
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class
 * itself.
 * <P>
 * As 2nd level interface it defines the methods to help in sending a query and
 * processing the received answer.
 * <P>
 * (Additional) Methods in this interface for the appropriate interaction:
 * <UL>
 * <LI>{@link getRequestCommand} to return the intended command to be sent.</LI>
 * <LI>{@link getRequestDataAsArrayOfBytes} to return the intended data part to be sent.</LI>
 * <LI>{@link setResponse} to store the response already separated into response command and data part.</LI>
 * <LI>{@link isCommunicationFinished} to signal the completeness of the interaction (only available
 * after storing the response).</LI>
 * </UL>
 * <P>
 * Other mandatory methods are inherited from {@link BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public interface SlipBridgeCommunicationProtocol extends BridgeCommunicationProtocol {

    /**
     * Provides an empty array of bytes.
     *
     */
    public static final byte[] EMPTYDATA = new byte[0];

    /**
     * Returning the command part of the request object for further SLIP serialization.
     *
     * @return <b>commandNumber</b>
     *         is of type {@link CommandNumber}.
     */
    public CommandNumber getRequestCommand();

    /**
     * Returning the data part of the request object for further SLIP serialization.
     *
     * @return <b>dataAsArrayOfBytes</b>
     *         is an Array of byte.
     */
    public byte[] getRequestDataAsArrayOfBytes();

    /**
     * Storing the command and the data part of the response object for further checks.
     * 
     * @param thisResponseCommand of type short: command part of the response packet.
     * @param thisResponseData of type Array of bytes: data part of response packet to be processed.
     */
    public void setResponse(short thisResponseCommand, byte[] thisResponseData);

    /**
     * Returning the communication status included within the response message..
     *
     * @return <b>isFinished</b>
     *         is a boolean signaling the end of this transaction.
     */
    public boolean isCommunicationFinished();

}
