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

public abstract class Sequence extends ListOf {

	protected abstract void createElements();

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		byte typeLength = is.readByte();
		if (isOptional && (typeLength == 0x01)) {
			isSelected = false;
			return true;
		}
		createElements();
		isSelected = true;
		if (!(((typeLength & 0x70) >> 4) == 7)) {
			return false;
		}
		int length = (typeLength & 0x0f);

		while ((typeLength & 0x80) == 0x80) {
			typeLength = is.readByte();
			if (!(((typeLength & 0x70) >> 4) == 0)) {
				return false;
			}
			length = ((length & 0xffffffff) << 4) | (typeLength & 0x0f);
		}

		if (length != seqArray.length) {
			return false;
		}

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
			String classname = this.getClass().getName();
			classname = classname.substring(classname.lastIndexOf('.') + 1);
			System.out.println("Sequence: " + classname);
			for (int i = 0; i < seqArray.length; i++) {
				if (seqArray[i].isSelected) {
					System.out.println("Element " + i + ": ");
					seqArray[i].print();
				}
				else if (seqArray[i].isOptional) {
					System.out.println("Element " + i + " is optional and not set");
				}
			}
		}
	}
}
