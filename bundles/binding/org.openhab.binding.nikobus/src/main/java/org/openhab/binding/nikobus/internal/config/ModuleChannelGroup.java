/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.config;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.binding.nikobus.internal.core.NikobusModule;
import org.openhab.binding.nikobus.internal.util.CRCUtil;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Group of 6 channels in a Nikobus switch or dimmer module. This can be used to
 * represent either channels 0-4 for the compact switch module (05-002-02), or
 * channels 1-6 or 7-12 for the large switch module (05-000-02).
 * 
 * Example commands used by Nikobus for module with address 6C94. <br/>
 * <br/>
 * Request Status First Channel Group: <br/>
 * CMD : $10126C946CE5A0. <br/>
 * ACK : $0512 <br/>
 * REPLY: $1C6C9400000000FF0000557CF8.<br/>
 * 
 * <br/>
 * Request Status Second Channel Group: <br/>
 * CMD : $10176C948715BB. <br/>
 * ACK : $0517 <br/>
 * REPLY: $1C6C94000000FF0000FFCF4CC3. <br/>
 * <br/>
 * <br/>
 * Update Status First Channel Group: <br/>
 * CMD : $1E156C94000000FF0000FF60E149. <br/>
 * ACK : $0515 <br/>
 * REPLY: $0EFF6C94009A. <br/>
 * <br/>
 * Update Status Second Channel Group: <br/>
 * CMD : $1E166C940000000000FFFF997295. <br/>
 * ACK : $0516 <br/>
 * REPLY: $0EFF6C94009A.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class ModuleChannelGroup implements NikobusModule {

	public static final String STATUS_REQUEST_CMD = "$10";
	public static final String STATUS_REQUEST_ACK = "$05";
	public static final String STATUS_REQUEST_GROUP_1 = "12";
	public static final String STATUS_REQUEST_GROUP_2 = "17";

	public static final String STATUS_RESPONSE = "$1C";

	public static final String STATUS_CHANGE_CMD = "$1E";
	public static final String STATUS_CHANGE_ACK = "$05";
	public static final String STATUS_CHANGE_GROUP_1 = "15";
	public static final String STATUS_CHANGE_GROUP_2 = "16";

	public static final String HIGH_BYTE = "FF";
	public static final String LOW_BYTE = "00";
	public static final String UP_BYTE = "01";
	public static final String DOWN_BYTE = "02";

	private Boolean nextStatusResponseIsForThisGroup;

	private String statusRequestGroup;
	private String statusUpdateGroup;

	private ModuleChannel[] channels = new ModuleChannel[6];

	private long lastUpdatedTime;
	private String address;
	private int group = 1;

	private static Logger log = LoggerFactory
			.getLogger(ModuleChannelGroup.class);

	/**
	 * Default constructor.
	 * 
	 * @param address
	 *            Nikobus module address.
	 * @param group
	 *            1 or 2 indicating channels 1-6 or 7-12 respectively
	 * 
	 * @param bindingProvider
	 */
	public ModuleChannelGroup(String address, int group) {
		this.address = address;
		this.group = group;

		if (group == 1) {
			statusRequestGroup = STATUS_REQUEST_GROUP_1;
			statusUpdateGroup = STATUS_CHANGE_GROUP_1;
		} else {
			statusRequestGroup = STATUS_REQUEST_GROUP_2;
			statusUpdateGroup = STATUS_CHANGE_GROUP_2;
		}
	}

	/**
	 * Add a new item channel to the switch module.
	 * 
	 * @param name
	 *            channel name
	 * @param channelNum
	 *            number 1-12
	 * @return SwitchModuleChannel bound to the given number.
	 */
	public ModuleChannel addChannel(String name, int channelNum, List<Class<? extends Command>> supportedCommands) {

		log.trace("Adding channel {}", name);

		if (channelNum > 6) {
			channelNum -= 6;
		}
		if (channelNum < 1 || channelNum > 6) {
			return null;
		}

		channels[channelNum - 1] = new ModuleChannel(name, address, this, supportedCommands);
		return channels[channelNum - 1];
	}


	/**
	 * Push the state of all channels to the Nikobus.
	 * 
	 * @param moduleChannel
	 */
	public void publishStateToNikobus(ModuleChannel moduleChannel,
			NikobusBinding binding) {

		log.trace("Publishing group {}-{} status to eventbus and nikobus",
				address, group);

		// update the channel on the event bus..
		binding.postUpdate(moduleChannel.getName(),
				moduleChannel.getState());

		StringBuilder command = new StringBuilder();
		command.append(statusUpdateGroup);
		command.append(address);

		for (int i = 0; i < 6; i++) {

			if (channels[i] == null) {
				// no channel defined
				command.append(LOW_BYTE);
				continue;
			}

			State channelState = channels[i].getState();
			if (channelState == null || channelState.equals(OnOffType.OFF)
					|| channelState.equals(PercentType.ZERO)) {
				command.append(LOW_BYTE);
			} else if (channelState.equals(UpDownType.UP)) {
				command.append(UP_BYTE);
			} else if (channelState.equals(UpDownType.DOWN)) {
				command.append(DOWN_BYTE);
			} else if (channelState instanceof PercentType){
				// calculate dimmer value...
				PercentType currentState = (PercentType) channelState;
				int value = BigDecimal.valueOf(255).multiply(currentState.toBigDecimal()).divide(BigDecimal.valueOf(100), 0,
						BigDecimal.ROUND_UP).intValue();
				command.append(StringUtils.leftPad(Integer.toHexString(value), 2, "0").toUpperCase());
			} else {
				command.append(HIGH_BYTE);
			}

		}

		command.append(HIGH_BYTE);

		NikobusCommand cmd = new NikobusCommand(CRCUtil.appendCRC2(
				STATUS_CHANGE_CMD + CRCUtil.appendCRC(command.toString())));

		try {
			binding.sendCommand(cmd);
		} catch (Exception e) {
			log.error("Error sending command.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The channel group can only process status update commands. These commands
	 * are sent by the module in response to a status request and contain
	 * the ON/OFF/Dimming status of the different channels.
	 */
	@Override
	public void processNikobusCommand(NikobusCommand cmd, NikobusBinding binding) {

		String command = cmd.getCommand();

		// check if it was an ACK for a status request
		if (command.startsWith(STATUS_REQUEST_ACK)) {
			if (command.startsWith(STATUS_REQUEST_ACK + statusRequestGroup)) {
				nextStatusResponseIsForThisGroup = Boolean.TRUE;
			} else {
				nextStatusResponseIsForThisGroup = Boolean.FALSE;
			}
			return;
		}

		if (!command.startsWith(STATUS_RESPONSE)) {
			return;
		}

		if (!command.startsWith(STATUS_RESPONSE + address)
				|| nextStatusResponseIsForThisGroup == null
				|| nextStatusResponseIsForThisGroup.equals(Boolean.FALSE)) {
			nextStatusResponseIsForThisGroup = null;
			return;
		}

		log.debug(
				"Processing nikobus command {} for module ({}-{})",
				new Object[] { cmd.getCommand(), address,
						Integer.toString(group) });
		lastUpdatedTime = System.currentTimeMillis();

		// for every channel, update the status if it was changed
		for (int i = 0; i < 6; i++) {
			if (channels[i] == null) {
				continue;
			}
			State currentState = channels[i].getState();
			String newValue = command.substring(9 + (i * 2), 11 + (i * 2));
			
			if (channels[i].supportsPercentType()) {					
				PercentType value = getPercentTypeFromByteString(newValue);					
				if (!currentState.equals(value)) {
					binding.postUpdate(channels[i].getName(), value);
					channels[i].setState(value);
				}	
			} else {
				if (newValue.equals(LOW_BYTE)) {
					if (channels[i].getState().equals(OnOffType.ON)) {
						binding.postUpdate(channels[i].getName(), OnOffType.OFF);
						channels[i].setState(OnOffType.OFF);
					}
				} else {
					if (channels[i].getState().equals(OnOffType.OFF)) {
						binding.postUpdate(channels[i].getName(), OnOffType.ON);
						channels[i].setState(OnOffType.ON);
					}
				}
			}
		}
	}

	/**
	 * @return time when last status feedback from the switch module was
	 *         received
	 */
	public long getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public NikobusCommand getStatusRequestCommand() {

		return new NikobusCommand(CRCUtil.appendCRC2(STATUS_REQUEST_CMD
				+ CRCUtil.appendCRC(statusRequestGroup + address)),
				STATUS_RESPONSE + address, 2000);
	}

	@Override
	public String getName() {
		return address + "-" + group;
	}

	/**
	 * Convert a 0-FF scale value to a percent type.
	 */
	private PercentType getPercentTypeFromByteString(String byteValue) {
		
		long value = Long.parseLong(byteValue, 16);
		return new PercentType(BigDecimal
				.valueOf(value)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(255), 0,
						BigDecimal.ROUND_UP).intValue());
	}
	
}
