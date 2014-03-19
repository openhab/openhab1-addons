/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal.hardware;

import org.openhab.binding.hue.internal.data.HueSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * The representation of a physical Hue bulb, providing control of the bulbs
 * features.
 * 
 * @author Roman Hartmann
 * @author Kai Kreuzer
 * @since 1.2.0
 * 
 */
public class HueBulb {

	static final Logger logger = LoggerFactory.getLogger(HueBulb.class);

	/**
	 * Enumeration of RGB color components that may be customized for the bulb.
	 */
	public enum ColorComponent {
		red, green, blue
	}

	private HueBridge bridge = null;
	private int deviceNumber = 1;
	private boolean isOn = false;
	private int brightness = 0; // possible values are 0 - 255
	private int colorTemperature = 154; // possible values are 154 - 500
	private int hue = 0; // 0 - 65535
	private int saturation = 0; // 0 - 255

	private Client client;

	/**
	 * Constructor for the HueBulb.
	 * 
	 * @param connectedBridge
	 *            The bridge the bulb is connected to.
	 * @param deviceNumber
	 *            The number under which the bulb is filed in the bridge.
	 */
	public HueBulb(HueBridge connectedBridge, int deviceNumber) {
		HueSettings settings = connectedBridge.getSettings();
		this.bridge = connectedBridge;
		this.deviceNumber = deviceNumber;
		this.isOn = settings.isBulbOn(deviceNumber);
		this.colorTemperature = settings.getColorTemperature(deviceNumber);
		this.brightness = settings.getBrightness(deviceNumber);
		this.hue = settings.getHue(deviceNumber);
		this.saturation = settings.getSaturation(deviceNumber);
		this.client = Client.create();
		this.client.setReadTimeout(1000);
		this.client.setConnectTimeout(2000);
	}

	/**
	 * Changes the color of the bulb to the color defined in the HSB format.
	 * 
	 * @param newHue
	 *            The hue of the color. (0..1)
	 * @param newSaturation
	 *            The saturation of the color. (0..1)
	 * @param newBrightness
	 *            The brightness of the color. (0..1)
	 */
	public void colorizeByHSB(double newHue, double newSaturation,
			double newBrightness) {

		int newHueCalculated = new Long(Math.round(newHue * 360.0 * 182.0))
				.intValue();
		int newSaturationCalculated = new Long(
				Math.round(newSaturation * 255.0)).intValue();
		int newBrightnessCalculated = new Long(
				Math.round(newBrightness * 255.0)).intValue();

		colorizeByHSBInternally(newHueCalculated, newSaturationCalculated,
				newBrightnessCalculated);
	}

	/**
	 * Increases the brightness of the bulb by the given amount to a maximum of
	 * 255. If the bulb is not switched on this will be done implicitly.
	 * 
	 * @param amount
	 *            The amount by which the brightness shall be increased.
	 * @return The resulting brightness in percent 0-100
	 */
	public int increaseBrightness(int amount) {
		return setBrightness(this.brightness + amount);
	}

	/**
	 * Decreases the brightness of the bulb by the given amount to a minimum of
	 * 0. If the bulb reaches a brightness of 0 the bulb will be switched off.
	 * 
	 * @param amount
	 *            The amount by which the brightness shall be decreased.
	 * @return The resulting brightness in percent 0-100
	 */
	public int decreaseBrightness(int amount) {
		return setBrightness(this.brightness - amount);
	}

	/**
	 * Sets the brightness of the bulb to the given amount (0 - 255). If the
	 * bulb has a brightness of 0 it will be switched off.
	 * 
	 * @param brightness
	 *            The new brightness.
	 * @return The resulting brightness in percent 0-100
	 */
	public int setBrightness(int brightness) {

		this.brightness = brightness;
		this.brightness = this.brightness < 0 ? 0 : this.brightness;
		this.brightness = this.brightness > 255 ? 255 : this.brightness;

		if (this.brightness > 0) {
			this.isOn = true;
			executeMessage("{\"bri\":" + this.brightness + ",\"on\":true}");
		} else {
			this.isOn = false;
			executeMessage("{\"on\":false}");
		}

		return (int) Math.round((100.0 / 255.0) * this.brightness);

	}

	/**
	 * Increases the color temperature of the bulb by the given amount to a
	 * maximum of 500.
	 * 
	 * @param amount
	 *            The amount by which the color brightness shall be increased.
	 */
	public void increaseColorTemperature(int amount) {
		setColorTemperature(this.colorTemperature + amount);
	}

	/**
	 * Decreases the color temperature of the bulb by the given amount to a
	 * minimum of 154.
	 * 
	 * @param amount
	 *            The amount by which the color brightness shall be decreased.
	 */
	public void decreaseColorTemperature(int amount) {
		setColorTemperature(this.colorTemperature - amount);
	}

	/**
	 * Sets the color temperature of the bulb to the given amount (154 - 500).
	 * 
	 * @param temperature
	 *            The amount by which the color brightness shall be decreased.
	 */
	public void setColorTemperature(int temperature) {

		this.colorTemperature = temperature;
		this.colorTemperature = this.colorTemperature < 154 ? 154
				: this.colorTemperature;
		this.colorTemperature = this.colorTemperature > 500 ? 500
				: this.colorTemperature;

		executeMessage("{\"ct\":" + this.colorTemperature + "}");

//		return (int) Math.round((100.0 / (500.0 - 154.0)) * (this.colorTemperature - 154.0));

	}

	/**
	 * Switches the bulb on or off at full brightness.
	 * 
	 * @param newState
	 *            True if the bulb should be turned on at full brightness, false
	 *            to turn it off.
	 */
	public void setOnAtFullBrightness(boolean newState) {
		if (newState) {
			increaseBrightness(255);
		} else {
			decreaseBrightness(255);
		}
	}

	/**
	 * Sends the values to the bulb to change its color accordingly.
	 * 
	 * @param hue
	 *            The hue of the color. (0..65535)
	 * @param saturation
	 *            The saturation of the color. (0..255)
	 * @param brightness
	 *            The brightness of the color. (0..255)
	 */
	private void colorizeByHSBInternally(int hue, int saturation, int brightness) {

		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
		this.isOn = true;

		String input = "{\"hue\":" + this.hue + ",\"sat\":" + this.saturation
				+ ",\"bri\":" + this.brightness + ",\"on\":" + this.isOn + "}";
		executeMessage(input);
	}

	/**
	 * Sends the message to the bulb for execution.
	 * 
	 * @param message
	 *            A Json message with the information that should be send to the
	 *            bulb.
	 */
	private void executeMessage(String message) {
		String targetURL = bridge.getUrl() + "lights/" + deviceNumber + "/state";
		WebResource webResource = client.resource(targetURL);
		ClientResponse response = webResource.type("application/json").put(
				ClientResponse.class, message);

		logger.debug("Sent message: '" + message + "' to " + targetURL);

		if (response.getStatus() != 200) {
			logger.error("Failed to connect to Hue bridge: HTTP error code: "
					+ response.getStatus());
		}
	}

}
