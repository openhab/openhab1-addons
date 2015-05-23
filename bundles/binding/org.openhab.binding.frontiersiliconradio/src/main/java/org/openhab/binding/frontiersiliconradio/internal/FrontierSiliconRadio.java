/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.frontiersiliconradio.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class representing a internet radio based on the frontier silicon chipset. Tested with "hama IR110" and Medion
 * MD87180" internet radios.
 * 
 * @author Rainer Ostendorf
 * @author paphko
 * @since 1.7.0
 */
public class FrontierSiliconRadio {

	/*-
	 * [paphko]: Some snippet I found in the internet:
	 * https://github.com/flammy/fsapi/blob/master/classes/fsapi.php
	 * 
	 * Maybe we can even add more info sources and actions here :-)
	 */
	private static final String REQUEST_SET_POWER = "SET/netRemote.sys.power";
	private static final String REQUEST_GET_POWER = "GET/netRemote.sys.power";
	private static final String REQUEST_GET_MODE = "GET/netRemote.sys.mode";
	private static final String REQUEST_SET_MODE = "SET/netRemote.sys.mode";
	private static final String REQUEST_SET_VOLUME = "SET/netRemote.sys.audio.volume";
	private static final String REQUEST_GET_VOLUME = "GET/netRemote.sys.audio.volume";
	private static final String REQUEST_SET_MUTE = "SET/netRemote.sys.audio.mute";
	private static final String REQUEST_GET_MUTE = "GET/netRemote.sys.audio.mute";
	private static final String REQUEST_SET_PRESET = "SET/netRemote.nav.state";
	private static final String REQUEST_SET_PRESET_ACTION = "SET/netRemote.nav.action.selectPreset";
	private static final String REQUEST_GET_PLAY_INFO_TEXT = "GET/netRemote.play.info.text";
	private static final String REQUEST_GET_PLAY_INFO_NAME = "GET/netRemote.play.info.name";

	private static final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioConnection.class);

	/** The http connection/session used for controlling the radio. */
	private FrontierSiliconRadioConnection conn;

	/** the volume of the radio. we cache it for fast increase/decrease. */
	private int currentVolume = 0;

	/**
	 * Constructor for the Radio class
	 * 
	 * @param hostname
	 *            Host name of the Radio addressed, e.g. "192.168.0.100"
	 * @param port
	 *            Port number, default: 80 (http)
	 * @param pin
	 *            Access PIN number of the radio. Must be 4 digits, e.g. "1234"
	 * 
	 * @author Rainer Ostendorf
	 * @since 1.7.0
	 */
	public FrontierSiliconRadio(String hostname, int port, String pin) {
		this.conn = new FrontierSiliconRadioConnection(hostname, port, pin);
	}

	/**
	 * Perform login to the radio and establish new session
	 * 
	 * @author Rainer Ostendorf
	 * @since 1.7.0
	 */
	public void login() {
		conn.doLogin();
	}

	/**
	 * get the radios power state
	 * 
	 * @return true when radio is on, false when radio is off
	 */
	public boolean getPower() {
		try {
			final FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_POWER);
			return result.getValueU8AsBoolean();
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_POWER);
		}
		return false;
	}

	/**
	 * Turn radio on/off
	 * 
	 * @param powerOn
	 *            true turns on the radio, false turns it off
	 */
	public void setPower(boolean powerOn) {
		final String params = "value=" + (powerOn ? "1" : "0");
		try {
			conn.doRequest(REQUEST_SET_POWER, params);
			return;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_SET_POWER + " = " + params);
		}
	}

	/**
	 * read the volume (as absolute value, 0-32)
	 * 
	 * @return volume: 0=muted, 32=max. volume
	 */
	public int getVolume() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_VOLUME);
			currentVolume = result.getValueU8AsInt();
			return currentVolume;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_VOLUME);
		}
		return 0;
	}

	/**
	 * Set the radios volume
	 * 
	 * @param volume
	 *            Radio volume: 0=mute, 32=max. volume
	 */
	public void setVolume(int volume) {
		final int newVolume = volume < 0 ? 0 : volume > 32 ? 32 : volume;
		final String params = "value=" + newVolume;
		try {
			conn.doRequest(REQUEST_SET_VOLUME, params);
			currentVolume = volume;
			return;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_SET_VOLUME + " = " + params);
		}
	}

	/**
	 * increase radio volume by 1 step
	 */
	public void increaseVolume() {
		if (currentVolume < 32)
			setVolume(currentVolume + 1);
	}

	/**
	 * decrease radio volume by 1 step
	 */
	public void decreaseVolume() {
		if (currentVolume > 0)
			setVolume(currentVolume - 1);
	}

	/**
	 * Read the radios operating mode
	 * 
	 * @return operating mode. On hama radio: 0="Internet Radio", 1=Spotify, 2=Player, 3="AUX IN"
	 */
	public int getMode() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_MODE);
			return result.getValueU32AsInt();
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_MODE);
		}
		return 0;
	}

	/**
	 * Set the radio operating mode
	 * 
	 * @param mode
	 *            On hama radio: 0="Internet Radio", 1=Spotify, 2=Player, 3="AUX IN"
	 */
	public void setMode(int mode) {
		final String params = "value=" + mode;
		try {
			conn.doRequest(REQUEST_SET_MODE, params);
			return;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_SET_MODE + " = " + params);
		}
	}

	/**
	 * Read the Station info name, e.g. "WDR2"
	 * 
	 * @return the station name, e.g. "WDR2"
	 */
	public String getPlayInfoName() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_PLAY_INFO_NAME);
			return result.getValueC8ArrayAsString();
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_PLAY_INFO_NAME);
			return "";
		}
	}

	/**
	 * read the stations radio text like the song name currently playing
	 * 
	 * @return the radio info text, e.g. music title
	 */
	public String getPlayInfoText() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_PLAY_INFO_TEXT);
			return result.getValueC8ArrayAsString();
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_PLAY_INFO_TEXT);
			return "";
		}
	}

	/**
	 * set a station preset. Tunes the radio to a preselected station.
	 * 
	 * @param presetId
	 */
	public void setPreset(Integer presetId) {
		try {
			conn.doRequest(REQUEST_SET_PRESET, "value=1");
			conn.doRequest(REQUEST_SET_PRESET_ACTION, "value=" + presetId.toString());
			conn.doRequest(REQUEST_SET_PRESET, "value=0");
			return;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_SET_PRESET);
			return;
		}
	}

	/**
	 * read the muted state
	 * 
	 * @return true: radio is muted, false: radio is not muted
	 */
	public boolean getMuted() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest(REQUEST_GET_MUTE);
			return result.getValueU8AsBoolean();
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_GET_MUTE);
		}

		return false;
	}

	/**
	 * mute the radio volume
	 * 
	 * @param muted
	 *            true: mute the radio, false: unmute the radio
	 */
	public void setMuted(boolean muted) {
		final String params = "value=" + (muted ? "1" : "0");
		try {
			conn.doRequest(REQUEST_SET_MUTE, params);
			return;
		} catch (Exception e) {
			logger.error("request failed: " + REQUEST_SET_MUTE + " = " + params);
		}
	}

	/**
	 * map the radio volume values to percent values
	 * 
	 * receiver volume 0 is 0% receiver volume 32 is 100%
	 * 
	 * @param volume
	 *            the receiver volume value
	 * 
	 */
	public int convertVolumeToPercent(int volume) {
		final int percent = Math.round((volume * 100) / 32);
		logger.debug("converted volume '" + volume + "' to '" + percent + "%'");
		return percent;
	}

	/**
	 * map percent values to radio volumes
	 * 
	 * receiver volume 0 is 0% receiver volume 32 is 100%
	 * 
	 * @param volume
	 *            the receiver volume value
	 * 
	 */
	public int convertPercentToVolume(int percent) {
		int volume = Math.round((percent * 32) / 100);
		logger.debug("converted " + percent + "% to volume " + volume);
		return volume;
	}

}
