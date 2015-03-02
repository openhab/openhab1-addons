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

public class EndOfSmlMessage extends ASNObject {

	@Override
	public void code(DataOutputStream os) throws IOException {
		os.writeByte(0);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		if (is.readByte() == 0x00) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void print() {
		System.out.println("EndOfSMLMessage");
	}

}
