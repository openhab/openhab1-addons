/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio;

/**
 * Provides an asynchronous mechanism for event notification. The
 * component which implements it need to be registered as pin event
 * handler using <code>addEventHandler</code> method of
 * <code>GPIOPin</code>.
 *
 * @author Dancho Penev
 * @since 1.5.0
 */
public interface GPIOPinEventHandler {

	/**
	 * Callback function executed on pin interrupt. When will the interrupt
	 * be generated depends on pin's "edge detection" setting. Not all
	 * boards/pins supports interrupts, refer to board's system
	 * reference manual.
	 * 
	 * @param pin pin which generated the interrupt
	 * @param value the value of pin after the interrupt was generated, either
	 * <code>GPIO.VALUE_LOW</code> or <code>GPIO.VALUE_HIGH</code>
	 */
	public void onEvent(GPIOPin pin, int value);

	/**
	 * Callback function executed when an error is detected.
	 * 
	 * @param pin the pin which generated the error
	 * @param exception the exception which was thrown
	 */
	public void onError(GPIOPin pin, Exception exception);
}
