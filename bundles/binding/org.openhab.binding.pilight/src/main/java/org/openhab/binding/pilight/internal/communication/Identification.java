/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
	
	public static String CLIENT_GUI = "client gui";
	public static String REQUEST_CONFIG = "request config";
	public static String ACCEPTED = "accept client";
	public static String REJECTED = "reject client";

	private String message;
	
	public Identification() {
		
	}
	
	public Identification (String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
