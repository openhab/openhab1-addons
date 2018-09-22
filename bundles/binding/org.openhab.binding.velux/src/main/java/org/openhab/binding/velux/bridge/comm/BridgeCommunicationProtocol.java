/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

/**
 * <B>Common bridge communication message scheme supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * Message semantic will be defined by the implementation of the separate message classes.
 * <P>
 *
 * The implementations will define the informations how to send query and receive answer
 * through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}.
 *
 * <UL>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCcheckLostNodes BCcheckLostNodes}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCdetectProducts BCdetectProducts}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetDeviceStatus BCgetDeviceStatus}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetFirmware BCgetFirmware}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetLANConfig BCgetLANConfig}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetProducts BCgetProducts}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetScenes BCgetScenes}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCgetWLANConfig BCgetWLANConfig}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCidentifyProduct BCidentifyProduct}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BClogin BClogin}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BClogout BClogout}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCrunScene BCrunScene}</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BCsetSilentMode BCsetSilentMode}</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public interface BridgeCommunicationProtocol<T> {

    /**
     * Returning a description of this communication pair.
     *
     * @return <b>name</b>
     *         as String describing the communication pair for human beings.
     */
    public String name();

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
    public Class<T> getClassOfResponse();

    /**
     * Returning the communication status included within the response message.
     *
     * @param response From the bridge returned message to be examined.
     * @return <b>boolean</b> whether the operation according to the request was successful.
     */
    public boolean isCommunicationSuccessful(T response);

    /**
     * Returning the communication status included within the response message.
     *
     * @param response From the bridge returned message to be examined.
     * @return <b>authentication token</b> as String which can be used for next operations.
     */
    public String getAuthToken(T response);

    /**
     * Returning the communication status included within the response message.
     *
     * @param response From the bridge returned message to be examined.
     * @return <b>deviceStatus</b> as String describing the current status of the bridge.
     */
    public String getDeviceStatus(T response);

    /**
     * Returning the communication status included within the response message.
     *
     * @param response From the bridge returned message to be examined.
     * @return <b>errors</b> as String[] describing the status of the operation according to the request in depth.
     */
    public String[] getErrors(T response);

}

/**
 * end-of-bridge/comm/BridgeCommunicationProtocol.java
 */
