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
	
}
