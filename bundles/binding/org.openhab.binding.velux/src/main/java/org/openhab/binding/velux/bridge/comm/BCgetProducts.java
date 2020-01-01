/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge.comm;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.VeluxBridge;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of products.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
@Deprecated
public class BCgetProducts implements BridgeCommunicationProtocol<BCgetProducts.Response> {

    public static String url = "/api/v1/products";

    /**
     * Bridge Communication class describing a product
     */
    public class BCproduct {
        /*
         * "name": "Rolladen Bad",
         * "category": "Roller shutter",
         * "id": 2,
         * "typeId": 2,
         * "subtype": 0,
         * "scenes": [
         * "V_DG_Shutter_Mitte_000",
         * "V_DG_Shutter_Mitte_085",
         * "V_DG_Shutter_Mitte_100"
         * ]
         */
        private String name;
        private String category;
        private int id;
        private int typeId;
        private int subtype;
        private String[] scenes;

        public String getName() {
            return this.name;
        }

        public String getCategory() {
            return this.category;
        }

        public int getId() {
            return this.id;
        }

        public int getTypeId() {
            return this.typeId;
        }

        public int getSubtype() {
            return this.subtype;
        }

        public String[] getScenes() {
            return this.scenes;
        }
    }

    /**
     * Bridge I/O Request message used by {@link VeluxBridge} for serializing:
     * <P>
     * Resulting JSON:
     * 
     * <pre>
     * {"action":"get","params":{}}
     * </pre>
     */
    public static class Request {

        @SuppressWarnings("unused")
        private String action;

        @SuppressWarnings("unused")
        private Map<String, String> params;

        public Request() {
            this.action = "get";
            this.params = new HashMap<String, String>();
        }
    }

    /**
     * Bridge I/O Response message used by {@link org.openhab.binding.velux.bridge.VeluxBridge VeluxBridge} for
     * deserialization with including component access methods
     * <P>
     * Expected JSON (sample):
     * 
     * <pre>
     * {
     * "token": "pESIc/9zDWa1CJR6hCDzLw==",
     * "result": true,
     * "deviceStatus": "IDLE",
     * "data": [
     *  { "name": "Bad",
     *    "category": "Window opener",
     *    "id": 0,
     *    "typeId": 4,
     *    "subtype": 1,
     *    "scenes": [
     *       "V_DG_Window_Mitte_000",
     *       "V_DG_Window_Mitte_100"
     *    ]
     *  },
     * ],
     * "errors": []
     * }
     * </pre>
     */
    public static class Response {
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCgetProducts.BCproduct[] data;
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

        public BCgetProducts.BCproduct[] getDevices() {
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
        return "get Products";
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
