/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.model;

/**
 * @author Michael Heckmann
 * @since 1.8.0
 */

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;


public class ResolVBUSPacket {
	
	private String destination;
	private String source;
	private String command;
	private String destinationMask;
	private String sourceMask;
	
	@XStreamImplicit
	private List<ResolVBUSField> field;
	
	
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
	public String getDestinationMask() {
		return destinationMask;
	}
	public void setDestinationMask(String destinationMask) {
		this.destinationMask = destinationMask;
	}
	public void setField(List<ResolVBUSField> field) {
		this.field = field;
	}
	public String getSourceMask() {
		return sourceMask;
	}
	public void setSourceMask(String sourceMask) {
		this.sourceMask = sourceMask;
	}

}
