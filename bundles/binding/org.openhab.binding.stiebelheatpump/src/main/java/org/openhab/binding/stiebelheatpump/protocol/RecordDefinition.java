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
	
	@XmlAttribute(required = false)
	private int min;

	@XmlAttribute(required = false)
	private int max;

	@XmlAttribute(required = false)
	private int step;
	
	@XmlAttribute(required = false)
	private String unit;
	
	@XmlAttribute(required = false)
	private int bitPosition;
	
	public RecordDefinition() {
	}

	public RecordDefinition(String name, int position, int lenght, double scale, Type dataType) {
		this.name = name;
		this.position = position;
		this.length = lenght;
		this.scale = scale;
		this.dataType = dataType;
	}
	
	public RecordDefinition(String name, int position, int lenght, double scale, Type dataType, int bitPosition) {
		this.name = name;
		this.position = position;
		this.length = lenght;
		this.scale = scale;
		this.dataType = dataType;
		this.bitPosition = bitPosition;
		}	
	
	public RecordDefinition(String name, int position, int lenght, double scale, Type dataType, int min, int max, int step, String unit) {
		this.name = name;
		this.position = position;
		this.length = lenght;
		this.scale = scale;
		this.dataType = dataType;
		this.min = min;
		this.max = max;
		this.step = step;
		this.unit = unit;
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
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}
}
