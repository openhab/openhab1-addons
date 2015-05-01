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
 * Response to a connection or state change request     
 * 
 * @author Jeroen Idserda
 * @since 1.7
 */
public class Response {
	
	public static final String SUCCESS = "success";
	
	public static final String FAILURE = "failure";
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isSuccess() {
		return SUCCESS.equals(status);
	}

}
