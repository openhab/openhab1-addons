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

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.common.GetScenes;
import org.openhab.binding.velux.things.VeluxProductName;
import org.openhab.binding.velux.things.VeluxProductReference;
import org.openhab.binding.velux.things.VeluxProductState;
import org.openhab.binding.velux.things.VeluxScene;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of scene configurations.
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
public class JCgetScenes extends GetScenes implements JsonBridgeCommunicationProtocol {

    private static final String URL = "/api/v1/scenes";
    private static final String DESCRIPTION = "get Scenes";

    private Request request = new Request();
    private Response response;

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
    }

    /**
     * Bridge Communication Structure containing a scene with different states of products.
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
    }

    /**
     * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.json.JsonVeluxBridge} for serializing.
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
        @SuppressWarnings("unused")
        private String token;
        private boolean result;
        private String deviceStatus;
        private BCscene[] data;
        private String[] errors;
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

    /**
     * Methods in addition to interface {@link JsonBridgeCommunicationProtocol}.
     */
    @Override
    public VeluxScene[] getScenes() {
        VeluxScene[] scenes = new VeluxScene[response.data.length];
        for (int sceneIdx = 0; sceneIdx < response.data.length; sceneIdx++) {

            VeluxProductState[] productStates = new VeluxProductState[response.data[sceneIdx].products.length];
            for (int productIdx = 0; productIdx < response.data[sceneIdx].products.length; productIdx++) {
                productStates[productIdx] = new VeluxProductState(
                        new VeluxProductReference(
                                new VeluxProductName(response.data[sceneIdx].products[productIdx].name),
                                response.data[sceneIdx].products[productIdx].typeId),
                        response.data[sceneIdx].products[productIdx].actuator,
                        response.data[sceneIdx].products[productIdx].status);
            }
            scenes[sceneIdx] = new VeluxScene(response.data[sceneIdx].name, response.data[sceneIdx].id,
                    response.data[sceneIdx].silent, productStates);
        }
        return scenes;
    }

}
