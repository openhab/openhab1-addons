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
package org.openhab.binding.hue.internal.hardware;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.hue.internal.data.HueSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Represents a physical Hue bridge and grants access to data the bridge
 * contains.
 *
 * @author Roman Hartmann
 * @author Kai Kreuzer
 * @since 1.2.0
 *
 */
public class HueBridge {

    private static final Logger logger = LoggerFactory.getLogger(HueBridge.class);

    private final String ip;
    private final String secret;

    private Client client;

    private static final int RETRY_INTERVAL_IN_SEC = 5;

    /**
     * Constructor for the HueBridge.
     *
     * @param ip
     *            The IP of the Hue bridge.
     * @param secret
     *            A unique identifier, that is used as a kind of password. This
     *            password is registered at the Hue bridge while connecting to
     *            it the first time. To do this initial connect the connect
     *            button the the Hue bridge as to be pressed.
     */
    public HueBridge(String ip, String secret) {
        this.ip = ip;
        this.secret = secret;
        client = Client.create();
        client.setConnectTimeout(5000);
    }

    /**
     * Pings the bridge for an initial connect. This pinging will take place for 100 seconds.
     * In this time the connect button on the Hue bridge has to be pressed to enable the pairing.
     */
    public void pairBridge() {
        Thread pairingThread = new Thread(new BridgePairingProcessor());
        pairingThread.start();
    }

    /**
     * Checks if the configured secret/user is authorized on the hue bridge.
     */
    public boolean isAuthorized() {
        boolean isAuthorized = false;
        if (!StringUtils.isBlank(secret)) {
            HueSettings settings = getSettings();
            isAuthorized = (settings != null && settings.isAuthorized());
        }
        return isAuthorized;
    }

    /**
     * Requests the settings of the Hue bridge that also contains the settings
     * of all connected Hue devices.
     *
     * @return The settings determined from the bridge. Null if they could not
     *         be requested.
     */
    public HueSettings getSettings() {
        String json = getSettingsJson();
        return json != null ? new HueSettings(json) : null;
    }

    /**
     * Pings the Hue bridge for 100 seconds to establish a first pairing
     * connection to the bridge. This requires the button on the Hue bridge to
     * be pressed.
     *
     */
    private void pingForPairing() {
        int countdownInSeconds = 100;
        boolean isPaired = false;

        while (countdownInSeconds > 0 && !isPaired) {
            logger.info("Please press the connect button on the Hue bridge. Waiting for pairing for "
                    + countdownInSeconds + " seconds...");
            WebResource webResource = client.resource("http://" + ip + "/api");

            String input = "{\"devicetype\":\"openHAB_binding\"}";

            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

            if (response.getStatus() != 200) {
                logger.error("Failed to connect to Hue bridge with IP '" + ip + "': HTTP error code: "
                        + response.getStatus());
                return;
            }

            String output = response.getEntity(String.class);
            logger.debug("Received pairing response: {}", output);

            isPaired = processPairingResponse(output);

            if (!isPaired) {
                try {
                    Thread.sleep(RETRY_INTERVAL_IN_SEC * 1000);
                    countdownInSeconds -= RETRY_INTERVAL_IN_SEC;
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private boolean processPairingResponse(String response) {
        try {
            JsonNode node = convertToJsonNode(response);
            if (node.has("success")) {
                String userName = node.path("success").path("username").getTextValue();
                logger.info("########################################");
                logger.info("# Hue bridge successfully paired!");
                logger.info("# Please set the following secret in your openhab.cfg (and restart openHAB):");
                logger.info("# " + userName);
                logger.info("########################################");
                return true;
            }
        } catch (IOException e) {
            logger.error("Could not read Settings-Json from Hue Bridge.", e);
        }
        return false;
    }

    private JsonNode convertToJsonNode(String response) throws IOException {
        JsonNode rootNode;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayWrappedNode = mapper.readTree(response);
        // Hue bridge returns the complete JSON response wrapped in an array, therefore the first
        // element of the array has to be extracted
        if (arrayWrappedNode.has(0)) {
            rootNode = arrayWrappedNode.get(0);
        } else {
            throw new IOException("Unexpected response from hue bridge (not an array)");
        }
        return rootNode;
    }

    /**
     * Determines the settings of the Hue bridge as a Json raw data String.
     *
     * @return The settings of the bridge if they could be determined. Null
     *         otherwise.
     */
    private String getSettingsJson() {
        WebResource webResource = client.resource(getUrl());

        try {
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            String settingsString = response.getEntity(String.class);

            if (response.getStatus() != 200) {
                logger.warn("Failed to connect to Hue bridge: HTTP error code: " + response.getStatus());
                return null;
            }
            logger.trace("Received Hue Bridge Settings: {}", settingsString);
            return settingsString;
        } catch (ClientHandlerException e) {
            logger.warn("Failed to connect to Hue bridge: HTTP request timed out.");
            return null;
        }
    }

    /**
     * @return The IP of the Hue bridge.
     */
    public String getUrl() {
        return "http://" + ip + "/api/" + secret + "/";
    }

    /**
     * Thread to ping the Hue bridge for pairing.
     *
     */
    class BridgePairingProcessor implements Runnable {

        @Override
        public void run() {
            pingForPairing();
        }
    }
}
