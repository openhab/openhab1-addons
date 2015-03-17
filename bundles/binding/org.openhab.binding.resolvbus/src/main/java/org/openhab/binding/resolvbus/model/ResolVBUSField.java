package org.openhab.binding.resolvbus.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "factor", "unit", "offset", "bitSize", "format", "commonUsage" })
@XmlRootElement(name = "field")

public class ResolVBUSField {

	protected String format;
	
	@XmlElement(required = true)
	protected BigInteger offset;
	
	@XmlElement(required = true)
	protected String name;
	
	@XmlElement(required = true)
	protected BigInteger bitSize;
	
	@XmlElement(required = true)
	protected BigDecimal factor;
	
	@XmlElement(required = true)
	protected String unit;
	
	@XmlAttribute
	protected String commonUsage;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getBitSize() {
		return bitSize;
	}

	public void setBitSize(BigInteger bitSize) {
		this.bitSize = bitSize;
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

	public String getCommonUsage() {
		return commonUsage;
	}

	public void setCommonUsage(String commonUsage) {
		this.commonUsage = commonUsage;
	}
	
}
