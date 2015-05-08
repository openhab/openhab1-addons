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

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ListOf extends ASNObject {

	protected ASNObject[] seqArray;

	@Override
	public void code(DataOutputStream os) throws IOException {

		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}

		if (seqArray == null || seqArray.length < 1) {
			throw new IOException("ListOf contains no elements");
		}

		int numTlField = 1;

		while ((Math.pow(2, 4 * numTlField) - 1) < seqArray.length) {
			numTlField++;
		}

		for (int i = numTlField; i > 0; i--) {
			int firstFourBits;
			if (i < numTlField) {
				if (i > 1) {
					firstFourBits = 0x80;
				}
				else {
					firstFourBits = 0x00;
				}
			}
			else {
				if (numTlField > 1) {
					firstFourBits = 0xf0;
				}
				else {
					firstFourBits = 0x70;
				}
			}
			os.writeByte((firstFourBits & 0xff) | (((seqArray.length) >> ((i - 1) * 4)) & 0xf));
		}

		for (ASNObject element : seqArray) {
			element.code(os);
		}
	}

	public ASNObject[] seqArray() {
		return seqArray;
	}

}
