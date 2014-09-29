/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio;

import java.io.IOException;

/**
 * Base interface for interacting with GPIO subsystem. Implementation
 * class should be dynamically registered as OSGi service in bundle
 * activator code if underlying platform is one of the supported.  
 * 
 * @author Dancho Penev
 * @since 1.5.0
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
