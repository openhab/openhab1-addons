/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.hue.internal.hardware;

import org.openhab.binding.hue.internal.data.HueSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Represents a physical Hue bridge and grants access to data the bridge
 * contains.
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 * 
 */
public class HueBridge {

	static final Logger logger = LoggerFactory.getLogger(HueBridge.class);

	private final String ip;
	private final String secret;

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
	}

	/**
	 * Checks if the secret is already registered in the Hue bridge. If not it
	 * pings the bridge for an initial connect. This pinging will take place for
	 * 100 seconds. In this time the connect button on the Hue bridge has to be
	 * pressed to enable the pairing.
	 * 
	 */
	public void pairBridgeIfNecessary() {

		String output = getSettingsJson();

		if (output.contains("error")) {
			logger.info("Hue bridge not paired.");
			Thread pairingThread = new Thread(new BridgePairingProcessor());
			pairingThread.start();
		}
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

		while (countdownInSeconds > 0) {
			logger.info("Please press the connect button on the Hue bridge. Waiting for pairing for "
					+ countdownInSeconds + " seconds...");
			Client client = Client.create();
			WebResource webResource = client.resource("http://" + ip + "/api");

			String input = "{\"username\":\"" + getSecret()
					+ "\",\"devicetype\":\"openHAB_binding\"}";

			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				logger.error("Failed to connect to Hue bridge with IP '" + ip
						+ "': HTTP error code: " + response.getStatus());
				return;
			}

			String output = response.getEntity(String.class);

			if (output.contains("success")) {
				logger.info("Hue bridge successfully paired!");
				return;
			}

			try {
				Thread.sleep(1000);
				countdownInSeconds--;
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * Determines the settings of the Hue bridge as a Json raw data String.
	 * 
	 * @return The settings of the bridge if they could be determined. Null
	 *         otherwise.
	 */
	private String getSettingsJson() {
		Client client = Client.create();
		WebResource webResource = client.resource(getUrl());

		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);

		String settingsString = response.getEntity(String.class);

		if (response.getStatus() != 200) {
			logger.warn("Failed to connect to Hue bridge: HTTP error code: "
					+ response.getStatus());
			return null;
		}
		return settingsString;
	}

	/**
	 * @return The unique identifier, that is used as a kind of password. This
	 *         password is registered at the Hue bridge while connecting to it
	 *         the first time. To do this initial connect the connect button the
	 *         the Hue bridge as to be pressed.
	 */
	public String getSecret() {
		return secret;
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
