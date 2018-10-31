/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.json;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.comm.Logout;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Communication to deauthenticate itself, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class JClogout extends Logout implements JsonBridgeCommunicationProtocol {

    private static String url = "/api/v1/auth";
    private static String description = "deauthenticate / logout";

    private Request request;
    private Response response;

    /*
     * Message Objects
     */

    static class ParamsLogin {
        @SuppressWarnings("unused")
        private String password;

        private ParamsLogin(String password) {
            this.password = password;
        }
    }

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for serializing:
     * <P>
     * Resulting JSON:
     * <pre>
     * {"action":"logout","params":{}}
     * </pre>
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;
        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "logout";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge I/O Response message used by {@link VeluxBridge} for deserializing with including component access methods
     * <P>
     * Expected JSON (sample):
     * <pre>
     * '{"token": "PHPnfLda71xfGlxoYEOTGQ==", "result": true, "deviceStatus": "IDLE", "data": {}, "errors": [] }'
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

     
}
/**
 * end-of-bridge/comm/BClogout.java
 */
