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

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of scene configurations.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class BCgetScenes implements BridgeCommunicationProtocol<BCgetScenes.Response> {

    public static String url = "/api/v1/scenes";

    /**
     * Bridge Communication Structure containing the state of a product.
     * <P>
     * Therefore it includes the typeId and name identifying the product, as well as actuator and status.
     * <P>
     * Used within structure {@link BCscene} to describe the final states of the products belonging to this scene.
     */
    public static class BCproductState {
        /*
         * "typeId": 2,
         * "name": "Rolladen Schlafzimmer",
         * "actuator": 0,
         * "status": 0
         */
        private int typeId;
        private String name;
        private int actuator;
        private int status;

        public int getTypeId() {
            return this.typeId;
        }

        public String getName() {
            return this.name;
        }

        public int getActuator() {
            return this.actuator;
        }

        public int getStatus() {
            return this.status;
        }
    }

    /**
     * Bridge Communication Structure containing a scene especially with different states of products.
     * <P>
     * Therefore it includes the name and id identifying the scene, a flag about silence-mode, as well as the different
     * states.
     * <P>
     * These states are defined by an array of {@link BCproductState} as part of this structure.
     */
    public static class BCscene {
        /*
         * {
         * "name": "V_DG_Shutter_West_100",
         * "id": 0,
         * "silent": true,
         * "bCproductStates": [
         * {
         * "typeId": 2,
         * "name": "Rolladen Schlafzimmer",
         * "actuator": 0,
         * "status": 100
         * }
         * ]
         * },
         */
        private String name;
        private int id;
        private boolean silent;
        private BCproductState[] products;

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        public boolean getSilent() {
            return this.silent;
        }

        public BCproductState[] getProductStates() {
            return this.products;
        }
    }

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.VeluxBridge VeluxBridge} for
     * serializing:
     * <P>
     * Resulting JSON:
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
     * Bridge Communication Structure describing a response to be received from the Velux Bridge.
     */
    public static class Response {
        /*
         * {
         * "token": "kWwXRQ5mlwgYfvk23g2zXw==",
         * "result": true,
         * "deviceStatus": "IDLE",
         * "data": [
         * {
         * "name": "V_DG_Shutter_West_100",
         * "id": 0,
         * "silent": true,
         * "bCproductStates": [
         * {
         * "typeId": 2,
         * "name": "Rolladen Schlafzimmer",
         * "actuator": 0,
         * "status": 100
         * }
         * ]
         * },
         * "errors": []
         * }
         *
         */
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCscene[] data;
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

        public BCscene[] getScenes() {
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
        return "get Scenes";
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
 * end-of-bridge/comm/BCgetScenes.java
 */
