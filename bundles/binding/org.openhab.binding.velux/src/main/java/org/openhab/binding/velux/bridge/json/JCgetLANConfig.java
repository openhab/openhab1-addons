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

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.common.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.common.GetLANConfig;
import org.openhab.binding.velux.things.VeluxGwLAN;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of LAN configuration.
 * <P>
 *
 * It defines information how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class JCgetLANConfig extends GetLANConfig
        implements BridgeCommunicationProtocol, JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/lan";
    private static final String DESCRIPTION = "get LAN configuration";

    private Request request = new Request();
    private Response response;

    /*
     * Message Objects
     */

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for serializing.
     * <P>
     * Resulting JSON:
     *
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
     * Bridge Communication Structure containing the network parameters.
     * <P>
     * Used within structure {@link JCgetLANConfig} to describe the network connectivity of the Bridge.
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
     * Bridge I/O Response message used by {@link VeluxBridge} for unmarshalling with including component access methods
     * <P>
     * Expected JSON (sample):
     *
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
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return DESCRIPTION;
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public Object getObjectOfRequest() {
        return request;
    }

    @Override
    public Class<Response> getClassOfResponse() {
        return Response.class;
    }

    @Override
    public void setResponse(Object response) {
        this.response = (Response) response;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return response.getResult();
    }

    @Override
    public String getDeviceStatus() {
        return response.getDeviceStatus();
    }

    @Override
    public String[] getErrors() {
        return response.getErrors();
    }

    /**
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */
    @Override
    public VeluxGwLAN getLANConfig() {
        VeluxGwLAN gwLAN = new VeluxGwLAN(response.data.ipAddress, response.data.subnetMask,
                response.data.defaultGateway, response.data.dhcp);
        return gwLAN;
    }

}
