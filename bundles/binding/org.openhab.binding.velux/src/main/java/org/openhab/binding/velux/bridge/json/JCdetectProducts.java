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
import org.openhab.binding.velux.bridge.common.DetectProducts;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic:Action to start discovery of products, i.e. Velux devices.
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
public class JCdetectProducts extends DetectProducts implements JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/products";
    private static final String DESCRIPTION = "discover products";

    private Request request = new Request();
    private Response response;

    /*
     * Message Objects
     */

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for serializing.
     *
     * Resulting JSON:
     *
     * <pre>
     * {"action":"discover","params":{}}
     * </pre>
     *
     * NOTE: the gateway software is extremely sensitive to this exact JSON structure.
     * Any modifications (like omitting empty params) will lead to an gateway error.
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;

        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "discover";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access
     * methods.
     *
     * Expected JSON (sample):
     *
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"discovering",
     *  "data":{},
     *  "errors":[]
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        @SuppressWarnings("unused")
        private Object data;
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

}
