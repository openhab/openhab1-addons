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

import org.openhab.binding.velux.bridge.comm.CheckLostNodes;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: query for lost nodes, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class JCcheckLostNodes extends CheckLostNodes implements JsonBridgeCommunicationProtocol {

    public static String url = "/api/v1/device";
	private static String description = "check lost nodes";

	private Request request;
	private Response response;

    public JCcheckLostNodes() {
    }

    /*
     * Message Objects
     */

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for serializing:
     * 
     * Resulting JSON:
     * <pre>
     * {"action":"checkLostNodes","params":{}}
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
            this.action = "checkLostNodes";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge I/O Response message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for deserializing with including component access methods
     * 
     * Expected JSON (sample):
     * <pre>
     * {
     *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
     *  "result":true,
     *  "deviceStatus":"IDLE",
     *  "data":[],
     *  "errors":[]
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        private String[] data;
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

        public String[] getData() {
            return data;
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

}
/**
 * end-of-bridge/comm/BCcheckLostNodes.java
 */
