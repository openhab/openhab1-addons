/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
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
