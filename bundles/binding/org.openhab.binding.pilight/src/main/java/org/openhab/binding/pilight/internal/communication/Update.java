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
 * This message is sent when we want to change the state of a device in pilight. 
 * 
 * {@link http://www.pilight.org/development/api/#controller}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Update {
	
	public static String SEND = "send";
	
	private String message;
	
	private Code code;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Code getCode() {
		return code;
	}
	
	public void setCode(Code code) {
		this.code = code;
	}
}
