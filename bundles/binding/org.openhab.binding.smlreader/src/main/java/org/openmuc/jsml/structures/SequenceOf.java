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

public abstract class SequenceOf extends ListOf {

	protected abstract void createElements(int length);

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		byte typeLength = is.readByte();
		if (isOptional && (typeLength == 0x01)) {
			isSelected = false;
			return true;
		}

		if ((typeLength & 0x70) != 0x70) {
			return false;
		}
		int length = (typeLength & 0x0f);

		while ((typeLength & 0x80) == 0x80) {
			typeLength = is.readByte();
			length = (length << 4) | (typeLength & 0x0f);
		}

		createElements(length);
		isSelected = true;

		for (int i = 0; i < length; i++) {
			if (!seqArray[i].decode(is)) {
				return false;
			}
		}
		isSelected = true;
		return true;
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SequenceOf: ");
			for (ASNObject element : seqArray) {
				element.print();
			}
		}
	}

}
