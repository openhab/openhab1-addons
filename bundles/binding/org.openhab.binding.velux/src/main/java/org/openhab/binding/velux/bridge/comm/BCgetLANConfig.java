/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.VeluxBridge;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of LAN configuration.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class BCgetLANConfig implements BridgeCommunicationProtocol<BCgetLANConfig.Response> {

    public static String url = "/api/v1/lan";

    public BCgetLANConfig() {
    }

    /*
     * Message Objects
     */

    /**
     * Bridge I/O Request message used by {@link VeluxBridge} for serializing:
     * <P>
     * Resulting JSON:
     * <pre>
     * {"action":"get","params":{}}
     * </pre>
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;

        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "get";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge Communication Structure containing the version of the firmware.
     * <P>
     * Used within structure {@link BCgetLANConfig} to describe the network connectivity of the Bridge.
     */
    public static class BCLANConfig {
        /*
         * {"ipAddress":"192.168.45.9","subnetMask":"255.255.255.0","defaultGateway":"192.168.45.129","dhcp":false}
         */
        private String ipAddress;
        private String subnetMask;
        private String defaultGateway;
        private boolean dhcp;

        public String getIPAddress() {
            return this.ipAddress;
        }

        public String getSubnetMask() {
            return this.subnetMask;
        }

        public String getDefaultGateway() {
            return this.defaultGateway;
        }

        public boolean getDHCP() {
            return this.dhcp;
        }

        @Override
        public String toString() {
            return String.format("ipAddress=%s,subnetMask=%s,defaultGateway=%s,dhcp=%s", this.ipAddress,
                    this.subnetMask, this.defaultGateway, this.dhcp ? "on" : "off");
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for unmarshelling with including component access methods
     * <P>
     * Expected JSON (sample):
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"IDLE",
     *  "data":"ipAddress":"192.168.45.9","subnetMask":"255.255.255.0","defaultGateway":"192.168.45.129","dhcp":false},
     *  "errors":[]
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCLANConfig data;
        private String[] errors;

        public String getToken() {
            return token;
        }

        public boolean getResult() {
            return result;
        }

        public String getDeviceStatus() {
            return deviceStatus;
        }

        public BCLANConfig getLANConfig() {
            return data;
        }

        public String[] getErrors() {
            return errors;
        }
    }

    /*
     * ===========================================================
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return "get LAN configuration";
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public boolean isCommunicationSuccessful(Response response) {
        return response.getResult();
    }

    @Override
    public Class<Response> getClassOfResponse() {
        return Response.class;
    }

    @Override
    public Object getObjectOfRequest() {
        return new Request();
    }

    @Override
    public String getAuthToken(Response response) {
        return response.getToken();
    }

    @Override
    public String getDeviceStatus(Response response) {
        return response.getDeviceStatus();
    }

    @Override
    public String[] getErrors(Response response) {
        return response.getErrors();
    }

}
/**
 * end-of-bridge/comm/BCgetLANConfig.java
 */
