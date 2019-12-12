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
import org.openhab.binding.velux.bridge.common.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Communication to authenticate itself, resulting in a return of current bridge state.
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
public class JClogin extends Login implements JsonBridgeCommunicationProtocol {
    private final Logger logger = LoggerFactory.getLogger(JClogin.class);

    private static final String URL = "/api/v1/auth";
    private static final String DESCRIPTION = "authenticate / login";

    private static Request request = new Request();
    private static Response response = new Response();

    /*
     * Message Objects
     */

    static class ParamsLogin {
        @SuppressWarnings("unused")
        private String password;
    }

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for serializing.
     * <P>
     * Resulting JSON:
     *
     * <pre>
     * {"action":"login","params":{"password":"PASSWORD"}}
     * </pre>
     */
    private static class Request {

        @SuppressWarnings("unused")
        private final String action = "login";
        @SuppressWarnings("unused")
        private ParamsLogin params;

        public Request() {
            this.params = new ParamsLogin();
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access methods
     * <P>
     * <B>Expected JSON (sample):</B>
     *
     * <pre>
     * '{"token": "PHPnfLda71xfGlxoYEOTGQ==", "result": true, "deviceStatus": "IDLE", "data": {}, "errors": [] }'
     * </pre>
     */
    private static class Response {
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
    }

    /*
     * Constructor Method
     */

    public JClogin() {
        logger.trace("JClogin(constructor) called.");
    }

    @Override
    public void finalize() {
        logger.trace("finalize({}) called.", this);
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
    public void setPassword(String thisPassword) {
        logger.trace("setPassword({}) called.", thisPassword);
        request.params.password = thisPassword;
    }

    @Override
    public String getAuthToken() {
        return response.getToken();
    }

}
