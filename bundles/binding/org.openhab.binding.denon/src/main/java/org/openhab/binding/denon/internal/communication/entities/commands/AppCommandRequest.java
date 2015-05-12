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
 * Wrapper for a list of {@link CommandTx}
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="tx")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppCommandRequest {
	
	@XmlElement(name="cmd")
	private List<CommandTx> commands = new ArrayList<CommandTx>();
	
	public AppCommandRequest() {
	}

	public List<CommandTx> getCommands() {
		return commands;
	}

	public void setCommands(List<CommandTx> commands) {
		this.commands = commands;
	}
	
	public AppCommandRequest add(CommandTx command) {
		commands.add(command);
		return this;
	}
	
	public static AppCommandRequest of(CommandTx command) {
		AppCommandRequest tx = new AppCommandRequest();
		return tx.add(command);
	}
}
