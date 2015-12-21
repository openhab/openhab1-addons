/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities.commands;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Response to an {@link AppCommandRequest}, wraps a list of {@link CommandRx} 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="rx")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppCommandResponse {
	
	@XmlElement(name="cmd")
	private List<CommandRx> commands = new ArrayList<CommandRx>();
	
	public AppCommandResponse() {
	}

	public List<CommandRx> getCommands() {
		return commands;
	}

	public void setCommands(List<CommandRx> commands) {
		this.commands = commands;
	}
}
