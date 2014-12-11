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
 * The representation of GPIO pin, provides methods for pin manipulation
 * and query.
 * 
 * @author Dancho Penev
 * @since 1.5.0
 */
public interface GPIOPin {

	/** When is active the pin is set to high. */
	public static final int ACTIVELOW_DISABLED = 0;

	/** When is active the pin is set to low. */
	public static final int ACTIVELOW_ENABLED = 1;

	/** Input pin. */
	public static final int DIRECTION_IN = 0;

	/** Output pin. */
	public static final int DIRECTION_OUT = 1;

	/** Output pin, initial state is low. */
	public static final int DIRECTION_OUT_LOW = 2;

	/** Output pin, initial state is high. */
	public static final int DIRECTION_OUT_HIGH = 3;

	/** Interrupts are disabled. */
	public static final int EDGEDETECTION_NONE = 0;

	/** Interrupt is generated when the pin changes from low to high. */
	public static final int EDGEDETECTION_RISING = 1;

	/** Interrupt is generated when the pin changes from high to low. */
	public static final int EDGEDETECTION_FALLING = 2;

	/** Interrupt is generated when the pin state is changed. */
	public static final int EDGEDETECTION_BOTH = 3;

	/** The pin is low. */
	public static final int VALUE_LOW = 0;

	/** The pin is high. */
	public static final int VALUE_HIGH = 1;

	/**
	 * Query the pin number.
	 * 
	 * @return current pin number
	 * @throws IOException in case of inability to query the number
	 */
	public int getPinNumber() throws IOException;

	/**
	 * Query the debounce interval.
	 * 
	 * @return current debounce interval in milliseconds
	 * @throws IOException in case of inability to query the debounce interval
	 */
	public long getDebounceInterval() throws IOException;

	/**
	 * Sets debounce interval. During this interval any subsequent
	 * interrupt is skipped to prevent multiple events caused by signal
	 * bouncing. 
	 * 
	 * @param debounceInterval new debounce interval in milliseconds
	 * @throws IOException in case of inability to set the debounce interval
	 */
	public void setDebounceInterval(long debounceInterval) throws IOException;

	/**
	 * Query the activelow state.
	 * 
	 * @return current activelow state, either
	 * 		<code>ACTIVELOW_DISABLED</code> or
	 *  	<code>ACTIVELOW_ENABLED</code>
	 * @throws IOException in case of inability to query the state
	 */
	public int getActiveLow() throws IOException;

	/** Sets activelow state.
	 * 
	 * @param activeLow new activelow state, either
	 * 		<code>ACTIVELOW_DISABLED</code> or
	 * 		<code>ACTIVELOW_ENABLED</code>
	 * @throws IOException in case of inability to set the state
	 */
	public void setActiveLow(Integer activeLow) throws IOException;

	/**
	 * Query the pin direction.
	 * 
	 * @return current direction, either <code>DIRECTION_IN</code>,
	 * 		<code>DIRECTION_OUT</code>, <code>DIRECTION_OUT_LOW</code> or
	 * 		<code>DIRECTION_OUT_HIGH</code>
	 * @throws IOException in case of inability to query the direction
	 */
	public int getDirection() throws IOException;

	/**
	 * Sets pin direction.
	 * 
	 * @param direction new direction, either <code>DIRECTION_IN</code>,
	 * 		<code>DIRECTION_OUT</code>, <code>DIRECTION_OUT_LOW</code> or
	 * 		<code>DIRECTION_OUT_HIGH</code>
	 * @throws IOException in case of inability to set the direction
	 */
	public void setDirection(int direction) throws IOException;

	/**
	 * Query the edge detection state.
	 * 
	 * @return current edge detection state, either
	 * 		<code>EDGEDETECTION_NONE</code>,
	 * 		<code>EDGEDETECTION_RISING</code>,
	 * 		<code>EDGEDETECTION_FALLING</code> or
	 * 		<code>EDGEDETECTION_BOTH</code>
	 * @throws IOException in case of inability to query the
	 * 		edge detection state
	 */
	public int getEdgeDetection() throws IOException;

	/**
	 * Sets edge detection state.
	 * 
	 * @param edgeDetection new edge detection state, either
	 * 		<code>EDGEDETECTION_NONE</code>,
	 * 		<code>EDGEDETECTION_RISING</code>,
	 * 		<code>EDGEDETECTION_FALLING</code> or
	 * 		<code>EDGEDETECTION_BOTH</code>
	 * @throws IOException in case of inability to set the
	 * 		edge detection state
	 */
	public void setEdgeDetection(int edgeDetection) throws IOException;

	/**
	 * Query the pin state.
	 * 
	 * @return current state, either <code>VALUE_LOW</code> or
	 * 		<code>VALUE_HIGH</code>
	 * @throws IOException in case of inability to query the state
	 */
	public int getValue() throws IOException;

	/**
	 * Sets pin state.
	 * 
	 * @param value new state, either <code>VALUE_LOW</code> or
	 * 		<code>VALUE_HIGH</code>
	 * @throws IOException in case of inability to set the state
	 */
	public void setValue(Integer value) throws IOException;

	/**
	 * Registers event handler, multiple handlers are supported through
	 * multiple method invocations.
	 * 
	 * @param eventHandler handler to register
	 * @throws IOException in case of inability to add the event handler
	 */
	public void addEventHandler(GPIOPinEventHandler eventHandler) throws IOException;

	/**
	 * Removes already registered event handler.
	 * 
	 * @param eventHandler handler to unregister
	 * @throws IOException in case of inability to remove the event handler
	 */
	public void removeEventHandler(GPIOPinEventHandler eventHandler) throws IOException;
}
