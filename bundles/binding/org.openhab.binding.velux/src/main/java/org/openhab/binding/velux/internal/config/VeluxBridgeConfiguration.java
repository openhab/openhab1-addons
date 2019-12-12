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
package org.openhab.binding.velux.internal.config;

/**
 * The {@link VeluxBridgeConfiguration} is a wrapper for
 * configuration settings needed to access the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider}
 * device.
 * <p>
 * It contains the factory default values as well.
 * <ul>
 * <li>{@link VeluxBridgeConfiguration#bridgeProtocol bridgeProtocol} protocol type
 * (one of http or https or slip),</li>
 * <li>{@link VeluxBridgeConfiguration#bridgeIPAddress bridgeIPAddress} bridge IP address,</li>
 * <li>{@link VeluxBridgeConfiguration#bridgeTCPPort bridgeTCPPort} bridge TCP port,</li>
 * <li>{@link VeluxBridgeConfiguration#bridgePassword bridgePassword} bridge password,</li>
 * <li>{@link VeluxBridgeConfiguration#timeoutMsecs timeoutMsecs} communication timeout in milliseconds,</li>
 * <li>{@link VeluxBridgeConfiguration#retries retries} number of retries (with exponential backoff algorithm),</li>
 * <li>{@link VeluxBridgeConfiguration#refreshMSecs refreshMSecs} refreshMSecs interval for retrieval of bridge
 * information.</li>
 * <li>{@link VeluxBridgeConfiguration#isBulkRetrievalEnabled isBulkRetrievalEnabled} flag to use bulk product
 * retrieval.</li>
 * </ul>
 * <p>
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeConfiguration {
    public static final String BRIDGE_PROTOCOL = "bridgeProtocol";
    public static final String BRIDGE_IPADDRESS = "bridgeIPAddress";
    public static final String BRIDGE_TCPPORT = "bridgeTCPPort";
    public static final String BRIDGE_PASSWORD = "bridgePassword";
    public static final String BRIDGE_TIMEOUT_MSECS = "timeoutMsecs";
    public static final String BRIDGE_RETRIES = "retries";
    public static final String BRIDGE_REFRESH_MSECS = "refreshMsecs";
    public static final String BRIDGE_IS_BULK_RETRIEVAL_ENABLED = "isBulkRetrievalEnabled";

    /*
     * Value to flag any changes towards the getter.
     */
    public boolean hasChanged = true;

    /*
     * Default values - should not be modified
     */
    public String bridgeProtocol = "slip";
    public String bridgeIPAddress = "192.168.1.1";
    public int bridgeTCPPort = 51200;
    public String bridgePassword = "velux123";
    public int timeoutMsecs = 1000; // one second
    public int retries = 5;
    public long refreshMSecs = 15000L; // 15 seconds
    public boolean isBulkRetrievalEnabled = true;
}
