/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

import org.openhab.binding.velux.bridge.VeluxBridge;

/**
 * <B>Specific bridge communication message supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * <I>Message semantic:</I> setting of scene silent mode, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class BCsetSilentMode implements BridgeCommunicationProtocol<BCsetSilentMode.Response> {

    public static String url = "/api/v1/scenes";
    private static int productId;
    private static boolean silentMode;

    /*
     * Constructors with parameters passing
     */

    public BCsetSilentMode(int id, boolean silent) {
        BCsetSilentMode.productId = id;
        BCsetSilentMode.silentMode = silent;
    }

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
     * Bridge I/O Request message used by {@link VeluxBridge} for serializing:
     * <P>
     * <B>Resulting JSON (sample):</B>
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
            this.params = new ParamsRunScene(BCsetSilentMode.productId, BCsetSilentMode.silentMode);
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access methods
     * <P>
     * <B>Expected JSON (sample):</B>
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
     * ===========================================================
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return "modify silent mode";
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public boolean isCommunicationSuccessful(Response response) {
        return response.getResult();
    };

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
 * end-of-bridge/comm/BCsetSilentMode.java
 */
