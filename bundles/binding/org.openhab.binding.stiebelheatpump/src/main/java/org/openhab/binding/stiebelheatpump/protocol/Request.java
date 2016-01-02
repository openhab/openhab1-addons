/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * request class for Stiebel heat pump.
 * 
 * @author Peter Kreutzer
 */
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

	@XmlAttribute(required = true)
	private String name;

	@XmlAttribute(required = true)
	private String description;

	@XmlJavaTypeAdapter(ByteAdapter.class)
	@XmlAttribute(required = true)
	private Byte requestByte;

	@XmlElement(name = "recordDefinition")
	private List<RecordDefinition> recordDefinitions = new ArrayList<RecordDefinition>();

	public Request() {
	}

	public Request(String name, String description, byte requestByte) {
		this.name = name;
		this.description = description;
		this.requestByte = requestByte;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getRequestByte() {
		return requestByte;
	}

	public void setRequestByte(byte requestByte) {
		this.requestByte = requestByte;
	}

	public List<RecordDefinition> getRecordDefinitions() {
		return recordDefinitions;
	}

	public void setRecordDefinitions(List<RecordDefinition> recordDefinitions) {
		this.recordDefinitions = recordDefinitions;
	}
}
