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

public class SML_Time extends Choice {

	public static final int SECINDEX = 1, TIMESTAMP = 2, TIMESTAMP_LOCAL = 3;

	public SML_Time(int tag, ASNObject choice) {
		setTime(tag, choice);
	}

	public void setTime(int tag, ASNObject choice) {
		if (!(tag == SECINDEX || tag == TIMESTAMP)) {
			throw new IllegalArgumentException("SML_Time: Wrong value for tag! " + tag + " is not allowed.");
		}

		this.choice = choice;
		this.tag = new Unsigned8(tag);
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
		case SECINDEX:
			choice = new Unsigned32();
			break;
		case TIMESTAMP:
			choice = new SML_Timestamp();
			break;
		case TIMESTAMP_LOCAL:
			choice = new SML_TimestampLocal();
			break;
		}

		if (!choice.decode(is)) {
			return false;
		}

		isSelected = true;

		return true;
	}

	public SML_Time() {
	}
}
