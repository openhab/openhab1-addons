/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * (one of http or https),</li>
 * <li>{@link VeluxBridgeConfiguration#bridgeIPAddress bridgeIPAddress} bridge IP address,</li>
 * <li>{@link VeluxBridgeConfiguration#bridgeTCPPort bridgeTCPPort} bridge TCP port,</li>
 * <li>{@link VeluxBridgeConfiguration#bridgePassword bridgePassword} brigde password,</li>
 * <li>{@link VeluxBridgeConfiguration#timeoutMsecs timeoutMsecs} communication timeout in milliseconds,</li>
 * <li>{@link VeluxBridgeConfiguration#retries retries} number of retries (with exponential backoff algo),</li>
 * <li>{@link VeluxBridgeConfiguration#refresh refresh} refresh interval for retrieval of bridge information.</li>
 * </ul>
 * <p>
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeConfiguration {
    public static final String BRIDGE_IPADDRESS = "bridgeIPAddress";
    public static final String BRIDGE_TCPPORT = "bridgeTCPPort";
    public static final String BRIDGE_PASSWORD = "bridgePassword";
    public static final String BRIDGE_TIMEOUT_MSECS = "timeoutMsecs";
    public static final String BRIDGE_RETRIES = "retries";
    public static final String BRIDGE_REFRESH_SECS = "refreshSecs";

    /*
     * Default values - should not be modified
     */
    public String bridgeProtocol = "http";
    public String bridgeIPAddress = "192.168.1.1";
    public int bridgeTCPPort = 80 ;
    public String bridgePassword = "velux123";
    public int timeoutMsecs = 1000;
    public int retries = 10;
    public long refresh = 3600000L; // one hour
}
/*
 * end-of-internal/config/VeluxBridgeConfiguration.java
 */
