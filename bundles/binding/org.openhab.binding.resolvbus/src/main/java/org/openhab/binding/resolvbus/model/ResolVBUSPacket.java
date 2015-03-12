package org.openhab.binding.resolvbus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

public class ResolVBUSPacket {
	
	@XmlElement(required = true)
	protected String destination;
	@XmlElement(required = true)
	protected String source;
	@XmlElement(required = true)
	protected String command;
	@XmlElement(required = true)
	protected List<ResolVBUSField> field;

}
