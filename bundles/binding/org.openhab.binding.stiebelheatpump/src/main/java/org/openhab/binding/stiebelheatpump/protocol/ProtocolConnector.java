/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;

public interface ProtocolConnector {

	void connect();

	void disconnect();

	byte get() throws StiebelHeatPumpException;

	short getShort() throws StiebelHeatPumpException;

	void get(byte abyte0[]) throws StiebelHeatPumpException;

	void mark();

	void reset();

	void write(byte abyte0[]) throws StiebelHeatPumpException;
	
	void write(byte byte0) throws StiebelHeatPumpException;

}
