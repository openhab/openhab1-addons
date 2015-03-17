package org.openhab.binding.resolvbus.model;

import java.util.ArrayList;
import java.util.List;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "destination", "source", "command", "field"})
@XmlRootElement(name = "packet")

public class ResolVBUSPacket {
	
	@XmlElement(required = true)
	protected String destination;
	@XmlElement(required = true)
	protected String source;
	@XmlElement(required = true)
	protected String command;
	@XmlElement(required = true)
	
	protected List<ResolVBUSField> field;
	
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public int getSourceInt() {
		return Integer.parseInt(source.substring(2), 16);
	}
	
	public int getCommandInt() {
		return Integer.parseInt(command.substring(2), 16);
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public List<ResolVBUSField> getField() {
		if (field == null) {
			field = new ArrayList<ResolVBUSField>();
		}
		return field;
	}
	public void setFields(List<ResolVBUSField> field) {
		this.field = field;
	}
	public ResolVBUSField getFieldWithName(String itemName) {
		
		for (ResolVBUSField field : getField()) {
			if (field.getName().equalsIgnoreCase(itemName))
				return field;
		}
		return null;
	}

}
