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
package org.openhab.binding.rfxcom.internal.connector;

import java.io.IOException;

/**
 * This interface defines interface to communicate RFXCOM controller.
 * 
 * @author Pauli Anttila, Evert van Es
 * @since 1.2.0
 */
public interface RFXComConnectorInterface {

	/**
	 * Procedure for connecting to RFXCOM controller.
	 * 
	 * @param device
	 *            Controller connection parameters (e.g. serial port name or IP
	 *            address).
	 */
	public void connect(String device) throws Exception;


	/**
	 * Procedure for disconnecting to RFXCOM controller.
	 * 
	 */
	public void disconnect();
	
	
	/**
	 * Procedure for send raw data to RFXCOM controller.
	 * 
	 * @param data
	 *            raw bytes.
	 */
	public void sendMessage(byte[] data) throws IOException;

	/**
	 * Procedure for register event listener.
	 * 
	 * @param listener
	 *            Event listener instance to handle events.
	 */
	public void addEventListener(RFXComEventListener listener);

	/**
	 * Procedure for remove event listener.
	 * 
	 * @param listener
	 *            Event listener instance to remove.
	 */
	public void removeEventListener(RFXComEventListener listener);

}
