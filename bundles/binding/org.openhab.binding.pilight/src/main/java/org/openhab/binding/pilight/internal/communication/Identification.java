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
 * This object is sent to pilight right after the initial connection. It describes what kind of client we want to be.      
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Identification {
	
	public static String ACTION_IDENTIFY = "identify";

	private String action;
	
	private Options options;
	
	public Identification() {
		this.action = ACTION_IDENTIFY;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}
	
}
