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

import org.openhab.binding.velux.bridge.common.GetFirmware;
import org.openhab.binding.velux.things.VeluxGwFirmware;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of Bridge configuration.
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
public class JCgetFirmware extends GetFirmware implements JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/settings";
    private static final String DESCRIPTION = "get firmware version";

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
     * {"action":"getFirmware","params":{}}
     * </pre>
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;

        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "getFirmware";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge Communication Structure containing the version of the firmware.
     * <P>
     * Used within structure {@link JCgetFirmware} to describe the software of the Bridge.
     */
    public static class BCfirmwareVersion {
        /*
         * "version": "0.1.1.0.41.0"
         */
        private String version;

        public String getVersion() {
            return this.version;
        }
    }

    /**
     * Bridge I/O Response message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for
     * deserializing with including component access methods
     * <P>
     * Expected JSON (sample):
     *
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"IDLE",
     *  "data":{"version":"0.1.1.0.41.0"},
     *  "errors":[]
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCfirmwareVersion data;
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

        public String getFirmwareVersion() {
            return data.getVersion();
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
    public void setResponse(Object thisResponse) {
        response = (Response) thisResponse;
    }

    @Override
    public boolean isCommunicationSuccessful() {
        return response.result;
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
    public VeluxGwFirmware getFirmware() {
        VeluxGwFirmware gwFirmware = new VeluxGwFirmware(response.data.version);
        return gwFirmware;
    }

}
