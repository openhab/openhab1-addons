/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

/**
 * Part of the {@link Action} message that is sent to pilight.  
 * This contains the desired state for a single device. 
 * 
 * {@link http://www.pilight.org/development/api/#sender}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Code {
	
	public static String STATE_ON = "on";
	
	public static String STATE_OFF = "off";
	
	private String device;
	
	private String state;
	
	private Values values;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Values getValues() {
		return values;
	}

	public void setValues(Values values) {
		this.values = values;
	}
}
