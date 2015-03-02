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
import java.io.DataOutputStream;
import java.io.IOException;

public class Integer8 extends ASNObject {

	protected byte val;

	public Integer8(byte i) {
		val = i;
		isSelected = true;
	}

	public Integer8() {
	}

	public byte getVal() {
		return val;
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		os.writeByte(0x52);
		os.writeByte(val);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		int typeLengthField = is.readByte();
		if (isOptional && (typeLengthField == 0x01)) {
			isSelected = false;
			return true;
		}
		if (typeLengthField != 0x52) {
			return false;
		}
		val = is.readByte();
		isSelected = true;
		return true;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("Integer8: " + val);
		}
	}
}
