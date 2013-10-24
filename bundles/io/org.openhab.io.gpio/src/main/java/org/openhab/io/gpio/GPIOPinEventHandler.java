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

/**
 * Provides an asynchronous mechanism for event notification. The
 * component which implements it need to be registered as pin event
 * handler using <code>addEventHandler</code> method of
 * <code>GPIOPin</code>.
 *
 * @author Dancho Penev
 * @since 1.3.1
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
