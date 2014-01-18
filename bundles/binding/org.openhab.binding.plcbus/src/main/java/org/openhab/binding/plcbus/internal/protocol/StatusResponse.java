/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import org.openhab.binding.plcbus.internal.protocol.commands.StatusOn;

/**
 * Response from PLCBusStatus request
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class StatusResponse extends Response {

	public StatusResponse(boolean acknowlagement, Command command, int firstParameter, int secondParameter) {
		super(acknowlagement, command, firstParameter, secondParameter);
	}

	public boolean isUnitOn() {
		return command instanceof StatusOn;
	}
	
}
