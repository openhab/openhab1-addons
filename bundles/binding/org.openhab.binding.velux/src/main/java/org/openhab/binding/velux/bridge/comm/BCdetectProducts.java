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
 * Message semantic:Action to start discovery of products, i.e. Velux devices.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class BCdetectProducts implements BridgeCommunicationProtocol<BCdetectProducts.Response> {

    public static String url = "/api/v1/products";

    public BCdetectProducts() {
    }

    /*
     * Message Objects
     */

    /**
     * Bridge I/O Request message used by {@link VeluxBridge} for serializing:
     * Resulting JSON:
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
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access methods
     * Expected JSON (sample):
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
     * ===========================================================
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return "discover products";
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
 * end-of-bridge/comm/BCdetectProducts.java
 */
