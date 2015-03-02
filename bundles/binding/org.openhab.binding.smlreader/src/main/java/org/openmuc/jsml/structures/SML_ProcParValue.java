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

import java.io.DataInputStream;
import java.io.IOException;

public class SML_ProcParValue extends Choice {

	public static final int SMLVALUE = 1, SMLPERIODENTRY = 2, SMLTUPELENTRY = 3, SMLTIME = 4;

	public SML_ProcParValue(int tag, ASNObject choice) {
		if (!(tag == SMLVALUE || tag == SMLPERIODENTRY || tag == SMLTUPELENTRY || SMLTIME == tag)) {
			throw new IllegalArgumentException("SML_ProcParValue: Wrong value for tag! " + tag + " is not allowed.");
		}

		this.tag = new Unsigned8(tag);
		this.choice = choice;
		isSelected = true;
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		byte tlField = is.readByte();
		if (isOptional && (tlField == 0x01)) {
			isSelected = false;
			return true;
		}
		if ((tlField & 0xff) != 0x72 || !tag.decode(is)) {
			return false;
		}

		switch (tag.val) {
		case SMLVALUE:
			choice = new SML_Value();
			break;
		case SMLPERIODENTRY:
			choice = new SML_PeriodEntry();
			break;
		case SMLTUPELENTRY:
			choice = new SML_TupelEntry();
			break;
		case SMLTIME:
			choice = new SML_Time();
			break;
		}

		if (!choice.decode(is)) {
			return false;
		}

		isSelected = true;

		return true;
	}

	public SML_ProcParValue() {
	}

}
