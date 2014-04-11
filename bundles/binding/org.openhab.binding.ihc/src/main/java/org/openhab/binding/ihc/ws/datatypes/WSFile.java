/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws.datatypes;

import org.openhab.binding.ihc.ws.IhcExecption;

/**
 * <p>
 * Java class for WSFile complex type.
 * 
 */

public class WSFile extends WSBaseDataType {
	private byte[] data;
	private String filename;

	public WSFile() {
	}

	public WSFile(byte[] data, String filename) {
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
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename value for this WSFile.
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public void encodeData(String data) throws IhcExecption {
		filename = parseValue(data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegment4/ns1:filename");

		this.data = parseValue(data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getIHCProjectSegment4/ns1:data").getBytes();
	}

}
