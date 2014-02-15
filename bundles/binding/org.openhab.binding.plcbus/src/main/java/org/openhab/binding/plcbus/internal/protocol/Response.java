/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Representation of Response from PLCBus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class Response {
	
	private boolean acknowlagement;
	private int firstParameter;
	private int secondParameter;
	protected Command command;

	public Response(boolean acknowlagement, Command command, int firstParameter, int secondParameter) {
		this.acknowlagement = acknowlagement;
		this.command = command;
		this.firstParameter = firstParameter;
		this.secondParameter = secondParameter;
	}

	public boolean isAcknowlagement() {
		return acknowlagement;
	}

	public int getFirstParameter() {
		return firstParameter;
	}

	public int getSecondParameter() {
		return secondParameter;
	}
	
}
