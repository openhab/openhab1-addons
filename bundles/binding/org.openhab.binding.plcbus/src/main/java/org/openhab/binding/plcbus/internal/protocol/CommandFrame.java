/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.*;

/**
 * CommandFrame in PLCBus Protocols
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class CommandFrame {
	
	private boolean extendAddress;
	private boolean threePhase;
	private boolean demandAck;
	private Command command;

	
	public CommandFrame() {
	}
	
	public CommandFrame(Command command) {
		this.command = command;
	}
	

	/**
	 * Flag if the address should be extended
	 * 
	 * @return True if the Address should be extended
	 */
	private boolean shouldExtendAddress() {
		return extendAddress;
	}

	/**
	 * Sets the flag if the address should be extended
	 * 
	 * @param value
	 *            Value for the address
	 */
	public void setExtendAddressTo(boolean value) {
		this.extendAddress = value;
	}

	/**
	 * Flag for three phase mode
	 * 
	 * @return true if PLCBus is in three phase mode
	 */
	public boolean isThreePhase() {
		return threePhase;
	}

	/**
	 * Sets the flag for three phase mode
	 * 
	 * @param value
	 *            for three phase mode
	 */
	public void setThreePhaseTo(boolean value) {
		threePhase = value;
	}

	/**
	 * Flag for demand with Acknowlagement
	 * 
	 * @return true if PLCBus should demand
	 */
	private boolean shouldDemandAck() {
		return demandAck;
	}

	/**
	 * Sets demand mode
	 * 
	 * @param value
	 *            for demand mode
	 */
	public void setDemandAckTo(boolean value) {
		demandAck = value;
	}

	/**
	 * Returns bytes of command frame
	 * 
	 * @return bytes of command frame
	 */
	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();

		result.add(getCommandByte());
		result.addAll(getDataBytes());

		return result;
	}

	/**
	 * Return the first parameter
	 * 
	 * @return first parameter
	 */
	public int getFirstParameter() {
		return command.getData1();
	}

	/**
	 * Returns the second parameter
	 * 
	 * @return second parameter
	 */
	public int getSecondParameter() {
		return command.getData2();
	}

	/**
	 * Initializes command frame from byte array
	 * 
	 * @param data
	 *            bytes of command frame
	 */
	public void parse(byte[] data) {
		extendAddress = ((data[0] & 0x80) == 0x80);
		threePhase = ((data[0] & 0x40) == 0x40);
		demandAck = ((data[0] & 0x20) == 0x20);

		command = CommandFactory.createBy((byte) (data[0] & 0x1F));

		if (command != null) {
			command.parse(new byte[] { data[1], data[2] });
		}
	}

	private byte getCommandByte() {
		byte result = 0x00;

		if (shouldExtendAddress()) {
			result |= 0x80;
		}

		if (isThreePhase()) {
			result |= 0x40;
		}

		if (shouldDemandAck()) {
			result |= 0x20;
		}

		if (command != null) {
			result |= command.getId();
		}

		return result;
	}

	private List<Byte> getDataBytes() {
		if (command == null) {
			return null;
		}

		return command.getDataBytes();
	}

	/**
	 * Returns the command of frame
	 * 
	 * @return command of frame
	 */
	public Command getCommand() {
		return command;
	}

}
