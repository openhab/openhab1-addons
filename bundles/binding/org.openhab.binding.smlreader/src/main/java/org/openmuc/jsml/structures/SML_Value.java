/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

public class SML_Value extends ImplicitChoice {

	public static final int BOOLEAN = 1, OCTETSTRING = 2, INTEGER8 = 3, INTEGER16 = 4, INTEGER32 = 5;
	public static final int INTEGER64 = 6, UNSIGNED8 = 7, UNSIGNED16 = 8, UNSIGNED32 = 9, UNSIGNED64 = 10;

	/**
	 * 
	 * @param choice
	 *            possible values: Boolean, OctetString, Integer{8,16,32,64}, Unsigned{8-64}
	 */
	public SML_Value(ASNObject choice) {
		setValue(choice);
	}

	public void setValue(ASNObject choice) {
		if (!(choice instanceof SML_Boolean || choice instanceof OctetString || choice instanceof Integer8
				|| choice instanceof Integer16 || choice instanceof Integer32 || choice instanceof Integer64
				|| choice instanceof Unsigned8 || choice instanceof Unsigned16 || choice instanceof Unsigned32 || choice instanceof Unsigned64)) {
			throw new IllegalArgumentException("SML_Value: Wrong ASNObject! " + choice.getClass().getName()
					+ " is not allowed.");
		}

		this.choice = choice;
		isSelected = true;
	}

	public SML_Value() {
	}
}
