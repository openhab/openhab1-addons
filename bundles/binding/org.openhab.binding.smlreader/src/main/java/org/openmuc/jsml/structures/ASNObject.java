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

public abstract class ASNObject {

	/**
	 * logger for debugging output
	 */

	/**
	 * true if the Object is Optional
	 */
	protected boolean isOptional = false;
	/**
	 * true if the Object is selected
	 */
	protected boolean isSelected = false;

	/**
	 * encodes this object and writes it to the stream os.
	 * 
	 * @param os
	 * @throws IOException
	 *             if something went wrong while writing to the stream
	 */
	public abstract void code(DataOutputStream os) throws IOException;

	/**
	 * decodes the data from the InputStream and writes it to an object
	 * 
	 * @param is
	 * @return true if successfully decoded
	 * @throws IOException
	 *             if something went wrong while reading from the stream
	 */
	public abstract boolean decode(DataInputStream is) throws IOException;

	/**
	 * prints the content or type of the object to stdout
	 */
	public abstract void print();

	/**
	 * marks this object as optional
	 */
	public void setOptional() {
		isOptional = true;
	}

	/**
	 * marks this object as selected
	 */
	public void setSelected() {
		isSelected = true;
	}

	/**
	 * returns if this object is selected
	 * 
	 * @return boolean
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * returns if this object is optional
	 * 
	 * @return boolean
	 */
	public boolean isOptional() {
		return isOptional;
	}
}
