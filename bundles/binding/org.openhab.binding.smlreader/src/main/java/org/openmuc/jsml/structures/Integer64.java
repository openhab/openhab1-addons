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

public class Integer64 extends ASNObject {

	protected long val;

	public Integer64(long i) {
		val = i;
		isSelected = true;
	}

	public Integer64() {
	}

	public long getVal() {
		return val;
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		os.writeByte(0x59);
		os.writeLong(val);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		int typeLengthField = is.readByte();
		if (isOptional && (typeLengthField == 0x01)) {
			isSelected = false;
			return true;
		}
		if ((typeLengthField & 0x50) != 0x50) {
			return false;
		}
		// get length of unsigned with tl-field and subtract the tl-field
		int length = (typeLengthField & 0x0F) - 1;
		val = 0;

		for (int j = length - 1; j >= 0; j--) {
			val |= ((long) (is.readByte() & 0xff)) << (8 * j);
		}

		if (((val >> (8 * (length - 1))) & 0x80) == 0x80) {
			for (int i = 2; i > length; i--) {
				val |= (0xffl) << (8 * (i - 1));
			}
		}
		isSelected = true;
		return true;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("Integer64: " + val);
		}
	}
}
