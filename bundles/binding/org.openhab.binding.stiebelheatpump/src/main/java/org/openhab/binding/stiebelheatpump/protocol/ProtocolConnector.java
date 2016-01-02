/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.openhab.binding.stiebelheatpump.protocol;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;

public interface ProtocolConnector {

	public abstract void connect(String s, int i)
			throws StiebelHeatPumpException;

	public abstract void disconnect();

	public abstract byte get() throws StiebelHeatPumpException;

	public abstract short getShort() throws StiebelHeatPumpException;

	public abstract void get(byte abyte0[]) throws StiebelHeatPumpException;

	public abstract void mark();

	public abstract void reset();

	public abstract void write(byte abyte0[]) throws StiebelHeatPumpException;

	public abstract void write(byte byte0) throws StiebelHeatPumpException;
}
