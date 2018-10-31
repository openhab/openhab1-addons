/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.json;

import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.comm.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Communication to authenticate itself, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public  class JClogin extends Login implements JsonBridgeCommunicationProtocol {
	private final Logger logger = LoggerFactory.getLogger(JClogin.class);

    private final static String url = "/api/v1/auth";
    private final static String description = "authenticate / login";

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
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for serializing:
     * <P>
     * Resulting JSON:
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
     * ===========================================================
     * Constructor Method
     */

    public JClogin() {
		logger.trace("JClogin(constructor) called.");
		logger.trace("JClogin(this={}) called.",this);
    }
	@Override
	public void finalize() {
		logger.trace("finalize({}) called.", this);
	}

    /*
     * ===========================================================
     * Methods required for interface {@link BridgeCommunicationProtocol}.
     */

    @Override
    public String name() {
        return description;
    }

    @Override
    public String getURL() {
        return url;
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
     * ===========================================================
     * Methods in addition to interface {@link BridgeCommunicationProtocol}.
     */

    public void setPassword(String thisPassword) {
		logger.trace("setPassword({}) called.",thisPassword);
    	request.params.password = thisPassword;
    	return;
    }
    
    public String getAuthToken() {
        return response.getToken();
    }


}
/**
 * end-of-bridge/comm/BClogin.java
 */
