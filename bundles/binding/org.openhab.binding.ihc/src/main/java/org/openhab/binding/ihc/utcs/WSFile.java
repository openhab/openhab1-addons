/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.ihc.utcs;

/**
 * <p>
 * Java class for WSFile complex type.
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */

public class WSFile {
	private byte[] data;

	private java.lang.String filename;

	public WSFile() {
	}

	public WSFile(byte[] data, java.lang.String filename) {
		this.data = data;
		this.filename = filename;
	}

	/**
	 * Gets the data value for this WSFile.
	 * 
	 * @return data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the data value for this WSFile.
	 * 
	 * @param data
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Gets the filename value for this WSFile.
	 * 
	 * @return filename
	 */
	public java.lang.String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename value for this WSFile.
	 * 
	 * @param filename
	 */
	public void setFilename(java.lang.String filename) {
		this.filename = filename;
	}

}
