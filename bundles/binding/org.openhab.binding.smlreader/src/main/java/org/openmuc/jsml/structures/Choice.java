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

public abstract class Choice extends ASNObject {

	/**
	 * tag defines the type of the choice object
	 */
	protected Unsigned8 tag;
	/**
	 * data of the choice-object
	 */
	protected ASNObject choice;

	public Choice() {
		tag = new Unsigned8();
	}

	@Override
	public void code(DataOutputStream os) throws IOException {
		if (isOptional && !isSelected) {
			os.writeByte(0x01);
			return;
		}
		os.writeByte(0x72);
		tag.code(os);
		choice.code(os);
	}

	/**
	 * General print method, which prints the classname of derived classes
	 */
	@Override
	public void print() {
		if (!isOptional || isSelected) {
			String classname = this.getClass().getName();
			classname = classname.substring(classname.lastIndexOf('.') + 1);
			System.out.println("Choice: " + classname + ": ");
			choice.print();
		}
	}

	public ASNObject getChoice() {
		return choice;
	}

	public Unsigned8 getTag() {
		return tag;
	}
}
