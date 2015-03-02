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
import java.io.PushbackInputStream;

public abstract class ImplicitChoice extends ASNObject {

	public static final int OctetString = 0, SML_Boolean = 4, Integer = 5, Unsigned = 6, TL_Field = 8;

	protected ASNObject choice;

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		// os.writeByte(0x71);
		choice.code(os);
	}

	@Override
	public boolean decode(DataInputStream is) throws IOException {
		PushbackInputStream ispushback = new PushbackInputStream(is);
		DataInputStream is2 = new DataInputStream(ispushback);
		// byte tlField = is2.readByte();
		byte tlFieldChoice = (byte) ispushback.read();
		if (isOptional && (tlFieldChoice == 0x01)) {
			isSelected = false;
			return true;
		}
		// if ((tlField & 0xff) != 0x71 ) {
		// return false;
		// }

		// read type-part of tlFieldChoice
		switch ((tlFieldChoice & 0xF0) >> 4) {
		case OctetString:
			choice = new OctetString();
			break;

		case SML_Boolean:
			choice = new SML_Boolean();
			break;

		case Integer:
			// read length-part of tlFieldChoice
			switch (tlFieldChoice & 0x0F) {
			case 2:
				choice = new Integer8();
				break;
			case 3:
				choice = new Integer16();
				break;
			case 4:
			case 5:
				choice = new Integer32();
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				choice = new Integer64();
				break;
			default:
				return false;
			}

			break;

		case Unsigned:
			// read length-part of tlFieldChoice
			switch (tlFieldChoice & 0x0F) {
			case 2:
				choice = new Unsigned8();
				break;
			case 3:
				choice = new Unsigned16();
				break;
			case 4:
			case 5:
				choice = new Unsigned32();
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				choice = new Unsigned64();
				break;
			default:
				return false;
			}

			break;
		// another TL-Field follows
		// this is only possible if an octetstring was sent
		case TL_Field:
			choice = new OctetString();
			break;
		default:
			return false;
		}

		// push back the TL-field of the choice-object
		ispushback.unread(tlFieldChoice);
		if (!choice.decode(is2)) {
			return false;
		}

		isSelected = true;
		return true;
	}

	public ASNObject getChoice() {
		return choice;
	}

	/**
	 * General print method, which prints the classname of derived classes and the print method of choice
	 * 
	 */
	@Override
	public void print() {
		if (!isOptional || isSelected) {
			String classname = this.getClass().getName();
			classname = classname.substring(classname.lastIndexOf('.') + 1);
			System.out.println("Implicit Choice: " + classname + ": ");
			choice.print();
		}
	}
}
