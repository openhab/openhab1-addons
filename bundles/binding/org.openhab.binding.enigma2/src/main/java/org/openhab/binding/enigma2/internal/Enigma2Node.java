package org.openhab.binding.enigma2.internal;

import java.io.IOException;

import org.openhab.binding.enigma2.internal.http.HttpUtils;
import org.openhab.binding.enigma2.internal.xml.XmlUtils;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
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

	private static final String SUFFIX_REMOTE_CONTROL = "/web/remotecontrol?command=";

	/*
	 * Remote control codes from
	 * http://openremote.org/display/docs/OpenRemote+2.0
	 * +How+To+-+Enigma+2+Digitalbox
	 * #OpenRemote2.0HowTo-Enigma2Digitalbox-Nicetohave%3AD
	 */
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

	private String hostName;
	private String userName;
	private String password;

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
		try {
			String content = HttpUtils.getGetResponse(this.getHostName(),
					SUFFIX_VOLUME, this.getUserName(), this.getPassword());
			return XmlUtils.getContentOfElement(content, "e2current");
		} catch (IOException e) {
			logger.error("getVolume failed with IOException", e);
			return null;
		}
	}

	/**
	 * @return requests the current channel
	 */
	public String getChannel() {
		try {
			String content = HttpUtils.getGetResponse(this.getHostName(),
					SUFFIX_CHANNEL, this.getUserName(), this.getPassword());
			return XmlUtils.getContentOfElement(content, "e2servicename");
		} catch (IOException e) {
			logger.error("getChannel failed with IOException", e);
			return null;
		}
	}

	/**
	 * Requests, whether the device is on or off
	 * 
	 * @return <code>true</code>, if the device is on, else <code>false</code>
	 */
	public String getOnOff() {
		try {
			String content = HttpUtils.getGetResponse(this.getHostName(),
					SUFFIX_POWERSTATE, this.getUserName(), this.getPassword());
			content = XmlUtils.getContentOfElement(content, "e2instandby");
			return content.equals("true") ? OnOffType.OFF.name() : OnOffType.ON
					.name();
		} catch (IOException e) {
			logger.error("getOnOff failed with IOException", e);
			return null;
		}
	}

	/**
	 * Requests, whether the device is muted or unmuted
	 * 
	 * @return <code>true</code>, if the device is muted, else
	 *         <code>false</code>
	 */
	public String getMuteUnmute() {
		try {
			String content = HttpUtils.getGetResponse(this.getHostName(),
					SUFFIX_VOLUME, this.getUserName(), this.getPassword());
			content = XmlUtils.getContentOfElement(content, "e2ismuted");
			return content.toLowerCase().equals("True") ? OnOffType.ON.name()
					: OnOffType.OFF.name();
		} catch (IOException e) {
			logger.error("getMuteUnmute failed with IOException", e);
			return null;
		}
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
			try {
				int value = ((DecimalType) command).intValue();
				HttpUtils.getGetResponse(this.getHostName(), SUFFIX_VOLUME
						+ SUFFIX_VOLUME_SET + value, this.getUserName(),
						this.getPassword());
			} catch (IOException e) {
				logger.error("setVolume failed with IOException", e);
			}
		} else {
			logger.error("Unsupported command type");
		}
	}

	/**
	 * Sets the channel
	 */
	public void setChannel(Command command) {
		if (command instanceof IncreaseDecreaseType) {
			sendRcCommand(
					command,
					((IncreaseDecreaseType) command) == IncreaseDecreaseType.INCREASE ? RC_CHANNEL_UP
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
			try {
				HttpUtils.getGetResponse(hostName, SUFFIX_POWERSTATE
						+ "?newstate=" + powerState.getValue(), userName,
						password);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		try {
			HttpUtils.getGetResponse(hostName, SUFFIX_REMOTE_CONTROL
					+ commandValue, userName, password);
		} catch (IOException e) {
			logger.error("sendRcCommand failed with IOException", e);
		}
	}
}