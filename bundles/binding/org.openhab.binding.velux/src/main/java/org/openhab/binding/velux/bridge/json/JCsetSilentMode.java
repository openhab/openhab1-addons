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

import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.common.SetSilentMode;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: setting of scene silent mode, resulting in a return of current bridge state.
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

public class JCsetSilentMode extends SetSilentMode implements JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/scenes";
    private static final String DESCRIPTION = "modify silent mode";

    private Request request = new Request();
    private Response response;

    private static int productId;
    private static boolean silentMode;

    /*
     * Message Objects
     */

    static class ParamsRunScene {
        @SuppressWarnings("unused")
        private int id;
        @SuppressWarnings("unused")
        private boolean silent;

        private ParamsRunScene(int id, boolean silent) {
            this.id = id;
            this.silent = silent;
        }
    }

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for serializing.
     * <P>
     * Resulting JSON (sample):
     *
     * <pre>
     * {"action":"setSilentMode","params":{"id":9,"silent":false}}}
     * </pre>
     */
    public static class Request {
        @SuppressWarnings("unused")
        private String action;
        @SuppressWarnings("unused")
        private ParamsRunScene params;

        public Request() {
            this.action = "setSilentMode";
            this.params = new ParamsRunScene(JCsetSilentMode.productId, JCsetSilentMode.silentMode);
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access methods
     * <P>
     * Expected JSON (sample):
     *
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"IDLE",
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

    /*
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */
    @Override
    public JCsetSilentMode setMode(int id, boolean silent) {
        JCsetSilentMode.productId = id;
        JCsetSilentMode.silentMode = silent;
        return this;
    }

}
