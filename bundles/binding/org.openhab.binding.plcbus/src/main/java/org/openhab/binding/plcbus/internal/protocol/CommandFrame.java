/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
