/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhab.binding.denon.internal.communication.entities.types.OnOffType;

/**
 * Holds information about the Main zone of the receiver
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Main {
	
	private OnOffType power; 

	public OnOffType getPower() {
		return power;
	}

	public void setPower(OnOffType power) {
		this.power = power;
	}

}
