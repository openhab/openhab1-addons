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
 * This message is sent when we want to change the state of a device or request the 
 * current configuration in pilight.  
 * 
 * @author Jeroen Idserda
 * @since 1.7
 */
public class Action {
	
	public static String ACTION_SEND = "send";
	
	public static String ACTION_CONTROL = "control";
	
	public static String ACTION_REQUEST_CONFIG = "request config";

	private String action;
	
	private Code code;
	
	private Options options;
	
	public Action(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}
	
}
