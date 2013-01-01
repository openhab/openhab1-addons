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
 * DataFrame of PLCBus Protocol
 * 
 * @author Robin Lenz
 * @since 1.0.0
 */
public class DataFrame {

	private int userCode;
	private int home;
	private int unit;
	private CommandFrame commandFrame;

	/**
	 * Create a new DataFrame
	 */
	public DataFrame() {

	}

	/**
	 * Creates a new DataFrame with CommandFrame
	 * 
	 * @param commandFrame
	 *            for DataFrame
	 */
	public DataFrame(CommandFrame commandFrame) {
		this.commandFrame = commandFrame;
	}

	/**
	 * Sets the UserCode to DataFrame
	 * 
	 * @param userCode
	 *            for DataFrame
	 */
	public void setUserCode(String userCode) {
		this.userCode = Convert.toByte(userCode, 16);
	}

	/**
	 * Sets address to DataFrame
	 * 
	 * @param address
	 *            for DataFrame
	 */
	public void SetAddress(String address) {

		this.home = getHomeFrom(address.charAt(0));
		this.unit = getUnitFrom(address.charAt(1));
	}

	private int getHomeFrom(char home) {
		switch (home) {
		case 'A':
			return 0;
		case 'B':
			return 1;
		case 'C':
			return 2;
		case 'D':
			return 3;
		case 'E':
			return 4;
		case 'F':
			return 5;
		case 'G':
			return 6;
		case 'H':
			return 7;
		case 'I':
			return 8;
		case 'J':
			return 9;
		case 'K':
			return 10;
		}

		return 0;
	}

	private int getUnitFrom(char unit) {
		switch (unit) {
		case '1':
			return 0;
		case '2':
			return 1;
		case '3':
			return 2;
		case '4':
			return 3;
		case '5':
			return 4;
		case '6':
			return 5;
		case '7':
			return 6;
		case '8':
			return 7;
		case '9':
			return 8;
		}

		return 0;
	}

	/**
	 * Returns the Bytes of DataFrame
	 * 
	 * @return bytes of DataFrame
	 */
	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();

		result.add(Convert.toByte(userCode));
		result.add(Convert.toByte(home | unit));

		if (commandFrame != null) {
			result.addAll(commandFrame.getBytes());
		}

		return result;
	}

	/**
	 * Initializes DataFrame from byte array
	 * 
	 * @param data
	 *            byte of DataFrame
	 */
	public void parse(byte[] data) {
		userCode = data[0];
		home = data[1] & 0xF0;
		unit = data[1] & 0x0F;

		commandFrame = new CommandFrame();
		commandFrame.parse(new byte[] { data[2], data[3], data[4] });
	}

	/**
	 * Gets the first parameter of CommandFrame
	 * 
	 * @return first parameter
	 */
	public int getFirstParameter() {
		return commandFrame.getFirstParameter();
	}

	/**
	 * Gets the second parameter of CommandFrame
	 * 
	 * @return second parameter
	 */
	public int getSecondParameter() {
		return commandFrame.getSecondParameter();
	}

	/**
	 * Gets the Command of CommandFrame
	 * 
	 * @return Command of CommandFrame
	 */
	public Command getCommand() {
		return commandFrame.getCommand();
	}
	
}
