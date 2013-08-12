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
package org.openhab.binding.comfoair.datatypes;

import org.openhab.binding.comfoair.handling.ComfoAirCommandType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * Class to handle error messages
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class DataTypeMessage implements ComfoAirDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(int[] data, ComfoAirCommandType commandType) {

		int[] get_reply_data_pos = commandType.getGetReplyDataPos();

		int errorAlo = data[get_reply_data_pos[0]];
		int errorE = data[get_reply_data_pos[1]];
		int errorEA = data[get_reply_data_pos[2]];
		int errorAhi = data[get_reply_data_pos[3]];

		String errorCode = "";

		if (errorAlo > 0) {
			errorCode = "A:" + convertToCode(errorAlo);
		}

		else if (errorAhi > 0) {
			if (errorAhi == 0x80) {
				errorCode = "A0";
			}
			errorCode = "A:" + (convertToCode(errorAhi) + 8);
		}

		if (errorE > 0) {
			if (errorCode.length() > 0) {
				errorCode += " ";
			}
			errorCode += "E:" + convertToCode(errorE);
		} else if (errorEA > 0) {
			if (errorCode.length() > 0) {
				errorCode += " ";
			}
			errorCode += "EA:" + convertToCode(errorEA);
		}

		return new StringType(errorCode.length() > 0 ? errorCode : "Ok");
	}

	/**
	 * {@inheritDoc}
	 */
	public int[] convertFromState(State value, ComfoAirCommandType commandType) {
		return null;
	}

	private int convertToCode(int code) {
		if (code == 0x1)
			return 1;
		if (code == 0x2)
			return 2;
		if (code == 0x4)
			return 3;
		if (code == 0x8)
			return 4;
		if (code == 0xF)
			return 5;
		if (code == 0x20)
			return 6;
		if (code == 0x40)
			return 7;
		if (code == 0x80)
			return 8;
		return -1;
	}
}
