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
import org.openhab.binding.velux.bridge.comm.SetSilentMode;



/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: setting of scene silent mode, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */

	public class JCsetSilentMode extends SetSilentMode implements JsonBridgeCommunicationProtocol  {

    public static String url = "/api/v1/scenes";
    private static String description = "modify silent mode";
    
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
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for serializing:
     * <P>
     * Resulting JSON (sample):
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
     * Constructor addon
     */
	public JCsetSilentMode setMode(int id, boolean silent) {
		JCsetSilentMode.productId = id;
		JCsetSilentMode.silentMode = silent;
        return this;
	}

}
/**
 * end-of-bridge/comm/BCsetSilentMode.java
 */
