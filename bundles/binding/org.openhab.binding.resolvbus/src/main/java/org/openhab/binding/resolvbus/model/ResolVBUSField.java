/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Michael Heckmann
 * @since 1.8.0
 */

public class ResolVBUSField {

	private String format;
	private BigInteger offset;

	private BigInteger bitSize;
	private BigDecimal factor;
	private String unit;
	private BigInteger bitPos;
	private String timeRef;
	private String mask;
	
	@XStreamImplicit
	private List<ResolVBUSValue> value;
	@XStreamImplicit
	private List<String> name;
	@XStreamImplicit
	private List<String> field;
	
	@XStreamAsAttribute
	private String lang;	

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public BigInteger getOffset() {
		return offset;
	}

	public void setOffset(BigInteger offset) {
		this.offset = offset;
	}

	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}



	public List<ResolVBUSValue> getValue() {
		return value;
	}

	public void setValue(List<ResolVBUSValue> value) {
		this.value = value;
	}

	public BigInteger getBitPos() {
		return bitPos;
	}

	public void setBitPos(BigInteger bitPos) {
		this.bitPos = bitPos;
	}

	public String getTimeRef() {
		return timeRef;
	}

	public void setTimeRef(String timeRef) {
		this.timeRef = timeRef;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public BigInteger getBitSize() {
		return bitSize;
	}

	public void setBitSize(BigInteger bitSize) {
		this.bitSize = bitSize;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public List<String> getField() {
		return field;
	}

	public void setField(List<String> field) {
		this.field = field;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
