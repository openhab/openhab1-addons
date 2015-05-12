/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Individual commands that can be sent to a Denon receiver to request specific information. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="cmd")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandTx {
	
	private static String DEFAULT_ID = "1"; 
	
	public static CommandTx CMD_ALL_POWER = of("GetAllZonePowerStatus");
	
	public static CommandTx CMD_VOLUME_LEVEL = of("GetVolumeLevel");
	
	public static CommandTx CMD_MUTE_STATUS = of("GetMuteStatus");
	
	public static CommandTx CMD_SOURCE_STATUS = of("GetSourceStatus");
	
	public static CommandTx CMD_SURROUND_STATUS = of("GetSurroundModeStatus");
	
	public static CommandTx CMD_ZONE_NAME = of("GetZoneName");
	
	public static CommandTx CMD_NET_STATUS = of("GetNetAudioStatus");
	
	@XmlAttribute(name="id")
	private String id;
	
	@XmlValue
	private String value;
	
	public CommandTx() {
	}
	
	public CommandTx(String value) {
		this.value=value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static CommandTx of(String command) {
		CommandTx cmdTx = new CommandTx(command);
		cmdTx.setId(DEFAULT_ID);
		return cmdTx;
	}
}
