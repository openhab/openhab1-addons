/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecordDefinition {

	public static enum Type {
		Sensor, Status, Settings;
	}
	
	@XmlAttribute(required = true)
	private String name;
	
	@XmlAttribute(required = true)
	private int position;
	
	@XmlAttribute(required = true)
	private int length;
	
	@XmlAttribute(required = true)
	private double scale;
	
	@XmlAttribute(required = true)
	private Type dataType;

	public RecordDefinition() {
	}

	public RecordDefinition(String name, int position, int lenght, double scale, Type dataType) {
		this.name = name;
		this.position = position;
		this.length = lenght;
		this.scale = scale;
		this.dataType = dataType;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Type getDataType() {
		return dataType;
	}

	public void setDataType(Type dataType) {
		this.dataType = dataType;
	}	
}
