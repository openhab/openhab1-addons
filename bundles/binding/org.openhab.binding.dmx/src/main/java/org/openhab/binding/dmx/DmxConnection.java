/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.dmx;

/**
 * DmxConnection. Used for sending DMX values to an (virtual) device. Implement
 * this interface and provide it as an OSGI service if you want to use your own
 * HW interface.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxConnection {

	/**
	 * Send the given buffer to the DMX device.
	 * 
	 * @param buffer
	 *            buffer containing max 512 DMX values
	 * @throws Exception
	 */
	public void sendDmx(byte[] buffer) throws Exception;

	/**
	 * @return true if the connection is closed.
	 */
	public boolean isClosed();

	/**
	 * Open the connection. The connection string is taken from the
	 * dmx:connection property in the openhab configuration file.
	 * 
	 * @param connectionString
	 * @throws Exception
	 */
	public void open(String connectionString) throws Exception;

	/**
	 * Close the connection.
	 */
	public void close();

}
