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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Class to handle numeric values
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class DataTypeNumber implements ComfoAirDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(int[] data, ComfoAirCommandType commandType) {

		int[] get_reply_data_pos = commandType.getGetReplyDataPos();

		int value = 0;
		int base = 0;

		for (int i = get_reply_data_pos.length - 1; i >= 0; i--) {
			value += data[get_reply_data_pos[i]] << base;
			base += 8;
		}

		int[] possibleValues = commandType.getPossibleValues();
		if (possibleValues != null) {

			// fix for unexpected value for "level" value. got a 0x33. valid was
			// the 0x03. 0x30 was to much.
			// send DATA: 07 f0 00 cd 00 7a 07 0f
			// receive CMD: ce DATA: 0f 20 32 00 0f 21 33 2d 33 03 01 5a 5b 00
			for (int possibleValue : possibleValues) {
				if ((value & possibleValue) == possibleValue) {
					return new DecimalType(value);
				}
			}

			return null;
		}

		return new DecimalType(value);
	}

	/**
	 * {@inheritDoc}
	 */
	public int[] convertFromState(State value, ComfoAirCommandType commandType) {

		int[] template = commandType.getChangeDataTemplate();
		int[] possibleValues = commandType.getPossibleValues();

		int intValue = ((DecimalType) value).intValue();

		for (int i = 0; i < possibleValues.length; i++) {
			if (possibleValues[i] == intValue) {
				template[commandType.getChangeDataPos()] = intValue;
				break;
			}
		}

		return template;
	}
	
}
