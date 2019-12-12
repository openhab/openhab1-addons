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
package org.openhab.binding.velux.bridge.json;

import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;

/**
 * <B>Common JSON-based bridge communication message scheme supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * This bridge communication is an extension of the common
 * {@link org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol BridgeCommunicationProtocol}.
 * <P>
 * Message semantic will be defined by the implementation of the separate message classes,
 * which are defined within {@link org.openhab.binding.velux.bridge.json.JsonBridgeAPI JsonBridgeAPI}.
 * <P>
 * The implementations will define the information which to send query and receive answer
 * through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}.
 * <P>
 * (Methods in this interface for the appropriate interaction:
 * <UL>
 * <LI>{@link #getURL} to return the URL suffix for accessing the specific service access point.</LI>
 * <LI>{@link #getObjectOfRequest} to return the request object for further JSON serialization.</LI>
 * <LI>{@link #getClassOfResponse} to retrieve the class of the object of response message for further JSON
 * deserialization.</LI>
 * <LI>{@link #setResponse} for storing the response according to the desired class after JSON deserialization.</LI>
 * <LI>{@link #getDeviceStatus} to retrieve the current device status.</LI>
 * <LI>{@link #getErrors} to retrieve the current error status.</LI>
 * </UL>
 *
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public interface JsonBridgeCommunicationProtocol extends BridgeCommunicationProtocol {

    /**
     * Returning the URL suffix for accessing the specific service access point.
     *
     * @return <b>sapURL</b>
     *         as String which is to be combined with the bridge address.
     */
    public String getURL();

    /**
     * Returning the request object for further JSON serialization.
     *
     * @return <b>ObjectOfRequest</b>
     *         is an Object.
     */
    public Object getObjectOfRequest();

    /**
     * Returning the class of the object of response message for further JSON deserialization.
     *
     * @return <b>ClassOfResponseObject</b>
     *         is the appropriate class Object.
     */
    public Class<?> getClassOfResponse();

    /**
     * Storing the response according to the desired class after JSON deserialization.
     *
     * @param response is the appropriate object of previously given class Object.
     */
    public void setResponse(Object response);

    /**
     * Returning the communication status included within the response message.
     *
     * @return <b>deviceStatus</b> as String describing the current status of the bridge.
     */
    public String getDeviceStatus();

    /**
     * Returning the communication status included within the response message.
     *
     * @return <b>errors</b> as String[] describing the status of the operation according to the request in depth.
     */
    public String[] getErrors();

}
