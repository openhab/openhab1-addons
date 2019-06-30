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

import org.openhab.binding.velux.bridge.common.GetWLANConfig;
import org.openhab.binding.velux.things.VeluxGwWLAN;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of WLAN configuration.
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
public class JCgetWLANConfig extends GetWLANConfig implements JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/settings";
    private static final String DESCRIPTION = "get WLAN configuration";

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
     * {"action":"wifi","params":{}}
     * </pre>
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;

        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "wifi";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge Communication Structure containing the version of the firmware.
     * <P>
     * Used within structure {@link JCgetWLANConfig} to describe the network connectivity of the Bridge.
     */
    public static class BCWLANConfig {
        /*
         * {"password":"Esf56mxqFY","name":"VELUX_KLF_847C"}
         */
        private String password;
        private String name;

        public String getPassword() {
            return this.password;
        }

        public String getSSID() {
            return this.name;
        }

        @Override
        public String toString() {
            return String.format("SSID=%s,password=********", this.name);
        }
    }

    /**
     * Bridge I/O Response message used by {@link JsonBridgeCommunicationProtocol} for deserialization with including
     * component access
     * methods
     * <P>
     * Expected JSON (sample):
     *
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"IDLE",
     *  "data":{"password":"Esf56mxqFY","name":"VELUX_KLF_847C"},
     *  "errors":[]
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCWLANConfig data;
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

        public BCWLANConfig getWLANConfig() {
            return data;
        }

        public String[] getErrors() {
            return errors;
        }

        /*
         * Methods required for interface {@link org.openhab.binding.velux.bridge.common.BCgetWLANConfig}.
         */
        public String getWLANSSID() {
            return data.getSSID();
        }

        public String getWLANPassword() {
            return data.getPassword();
        }

        @Override
        public String toString() {
            return data.toString();
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
        return response.deviceStatus;
    }

    @Override
    public String[] getErrors() {
        return response.errors;
    }

    /**
     * Methods in addition to interface {@link JsonBridgeCommunicationProtocol}.
     */
    @Override
    public VeluxGwWLAN getWLANConfig() {
        VeluxGwWLAN gwWLAN = new VeluxGwWLAN(response.data.name, response.data.password);
        return gwWLAN;
    }

}
