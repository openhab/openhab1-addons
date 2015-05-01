/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.communication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of {@link Video}.
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "Video")
@XmlAccessorType(XmlAccessType.FIELD)
public class Player {
	
	@XmlAttribute
	private String machineIdentifier;
	
	@XmlAttribute
	private String state;

	public String getMachineIdentifier() {
		return machineIdentifier;
	}

	public void setMachineIdentifier(String machineIdentifier) {
		this.machineIdentifier = machineIdentifier;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
