/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
