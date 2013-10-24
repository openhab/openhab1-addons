/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
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


package org.openhab.io.gpio;

import java.io.IOException;

/**
 * Base interface for interacting with GPIO subsystem. Implementation
 * class should be dynamically registered as OSGi service in bundle
 * activator code if underlying platform is one of the supported.  
 * 
 * @author Dancho Penev
 * @since 1.3.1
 */
public interface GPIO {

	/**
	 * Creates and initializes backend object representing GPIO pin.
	 * Further pin manipulations are made using methods exposed by
	 * <code>GPIOPin</code> interface.
	 * 
	 * @param pinNumber platform specific pin number
	 * @return object representing the GPIO pin 
	 * @throws IOException in case of inability to initialize the pin
	 */
	public GPIOPin reservePin(Integer pinNumber) throws IOException;

	/**
	 * Uninitializes backend object and free used resources. Further
	 * using of this pin object is invalid.
	 * 
	 * @param pin object representing already initialized GPIO pin
	 * @throws IOException in case of inability to uninitialize the pin
	 */
	public void releasePin(GPIOPin pin) throws IOException;

	/**
	 * Query default debounce interval.
	 * 
	 * @return current default debounce interval
	 */
	public long getDefaultDebounceInterval();
}
