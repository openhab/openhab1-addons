/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal;

import org.openhab.binding.enigma2.internal.xml.XmlUtils;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representing an Enigma2 device
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 * 
 */
public class Enigma2Node {

	private static final Logger logger = LoggerFactory
			.getLogger(Enigma2Node.class);

	private static final String GET = "GET";

	/*
	 * Remote control codes from
	 * http://openremote.org/display/docs/OpenRemote+2.0
	 * +How+To+-+Enigma+2+Digitalbox
	 * #OpenRemote2.0HowTo-Enigma2Digitalbox-Nicetohave%3AD
	 */
	private static final String SUFFIX_REMOTE_CONTROL = "/web/remotecontrol?command=";
	private static final String RC_CHANNEL_UP = "402";
	private static final String RC_CHANNEL_DOWN = "403";
	private static final String RC_VOLUME_DOWN = "114";
	private static final String RC_VOLUME_UP = "115";
	private static final String RC_PLAY_PAUSE = "164";
	private static final String RC_MUTE_UNMUTE = "113";

	private static final String SUFFIX_VOLUME = "/web/vol";
	private static final String SUFFIX_VOLUME_SET = "?set=set";
	private static final String SUFFIX_CHANNEL = "/web/subservices";
	private static final String SUFFIX_POWERSTATE = "/web/powerstate";
	private static final String SUFFIX_DOWNMIX = "/web/downmix";

	private String hostName;
	private String userName;
	private String password;
	private int timeOut = 5000;

	/*
	 * Getter
	 */
	public String getHostName() {
		return hostName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	protected void setHostName(String hostName) {
		this.hostName = hostName;
	}

	protected void setUserName(String userName) {
		this.userName = userName;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	public boolean properlyConfigured() {
		return hostName != null && userName != null && password != null;
	}

	/**
	 * @return requests the current value the volume
	 */
	public String getVolume() {
		String content = HttpUtil.executeUrl(GET,
				createUserPasswordHostnamePrefix() + SUFFIX_VOLUME,
				this.timeOut);
		return XmlUtils.getContentOfElement(content, "e2current");
	}

	/**
	 * @return requests the current channel
	 */
	public String getChannel() {
		String content = HttpUtil.executeUrl(GET,
				createUserPasswordHostnamePrefix() + SUFFIX_CHANNEL,
				this.timeOut);
		return XmlUtils.getContentOfElement(content, "e2servicename");
	}

	/**
	 * Requests, whether the device is on or off
	 * 
	 * @return <code>true</code>, if the device is on, else <code>false</code>
	 */
	public String getOnOff() {
		String content = HttpUtil.executeUrl(GET,
				createUserPasswordHostnamePrefix() + SUFFIX_POWERSTATE,
				this.timeOut);
		content = XmlUtils.getContentOfElement(content, "e2instandby");
		return content.equals("true") ? OnOffType.OFF.name() : OnOffType.ON
				.name();
	}

	/**
	 * Requests, whether the device is muted or unmuted
	 * 
	 * @return <code>true</code>, if the device is muted, else
	 *         <code>false</code>
	 */
	public String getMuteUnmute() {
		String content = HttpUtil.executeUrl(GET,
				createUserPasswordHostnamePrefix() + SUFFIX_VOLUME,
				this.timeOut);
		content = XmlUtils.getContentOfElement(content, "e2ismuted");
		return content.toLowerCase().equals("True") ? OnOffType.ON.name()
				: OnOffType.OFF.name();
	}

	/**
	 * Requests, if downmix is active
	 * 
	 * @return <code>true</code>, if dowmix is active
	 *         <code>false</code>
	 */
	public String getDownmix() {
		String content = HttpUtil.executeUrl(GET,
				createUserPasswordHostnamePrefix() + SUFFIX_DOWNMIX,
				this.timeOut);
		content = XmlUtils.getContentOfElement(content, "e2state");
		return content.toLowerCase().equals("true") ? OnOffType.ON.name()
				: OnOffType.OFF.name();
	}

	/*
	 * Setter
	 */
	/**
	 * Sets the volume
	 */
	public void setVolume(Command command) {
		// up or down one step
		if (command instanceof IncreaseDecreaseType) {
			sendRcCommand(
					command,
					((IncreaseDecreaseType) command) == IncreaseDecreaseType.INCREASE ? RC_VOLUME_UP
							: RC_VOLUME_DOWN);
		} else if (command instanceof DecimalType) {
			// set absolute value
			int value = ((DecimalType) command).intValue();
			HttpUtil.executeUrl(GET, createUserPasswordHostnamePrefix()
					+ SUFFIX_VOLUME + SUFFIX_VOLUME_SET + value, this.timeOut);
		} else {
			logger.error("Unsupported command type");
		}
	}

	/**
	 * Sets the channel
	 */
	public void setChannel(Command command) {
		if (command instanceof StringType
				|| command instanceof IncreaseDecreaseType) {
			sendRcCommand(
					command,
					command.toString().equals(
							IncreaseDecreaseType.INCREASE.toString()) ? RC_CHANNEL_UP
							: RC_CHANNEL_DOWN);
		} else {
			logger.error("Unsupported command type: {}", command.getClass()
					.getName());
		}
	}

	/**
	 * Toggles play and pause
	 */
	public void sendPlayPause(Command command) {
		if (command instanceof OnOffType) {
			sendRcCommand(command, RC_PLAY_PAUSE);
		} else {
			logger.error("Unsupported command type: {}", command.getClass()
					.getName());
		}
	}

	/**
	 * Toggles mute and unmute
	 */
	public void sendMuteUnmute(Command command) {
		if (command instanceof OnOffType) {
			sendRcCommand(command, RC_MUTE_UNMUTE);
		} else {
			logger.error("Unsupported command type: {}", command.getClass()
					.getName());
		}
	}

	/**
	 * Toggles on and off
	 */
	public void sendOnOff(Command command, Enigma2PowerState powerState) {
		if (command instanceof OnOffType) {
			HttpUtil.executeUrl(GET, createUserPasswordHostnamePrefix()
					+ SUFFIX_POWERSTATE + "?newstate=" + powerState.getValue(),
					this.timeOut);
		} else {
			logger.error("Unsupported command type: {}", command.getClass()
					.getName());
		}
	}

	/*
	 * Setter
	 */
	/**
	 * Sets downmix
	 */
	public void setDownmix(Command command) {
		if (command instanceof OnOffType) {
			String enable = (OnOffType)command == OnOffType.ON ? "True" : "False";
			HttpUtil.executeUrl(GET, createUserPasswordHostnamePrefix()
					+ SUFFIX_DOWNMIX + "?enable=" + enable,
					this.timeOut);
		} else {
			logger.error("Unsupported command type: {}", command.getClass()
					.getName());
		}
	}

	/**
	 * Sends any custom rc command
	 */
	public void sendRcCommand(Command command, String commandValue) {
		if (commandValue == null) {
			logger.error("Error in item configuration. No remote control code provided (third part of item config)");
		}
		HttpUtil.executeUrl(GET, createUserPasswordHostnamePrefix()
				+ SUFFIX_REMOTE_CONTROL + commandValue, this.timeOut);
	}

	private String createUserPasswordHostnamePrefix() {
		return new StringBuffer("http://" + this.getUserName()).append(":")
				.append(this.getPassword()).append("@")
				.append(this.getHostName()).toString();
	}
}
