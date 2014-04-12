/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import org.openhab.core.types.State;
/**
 * Interface for classes to which state updates can be pushed
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public interface StatePublisher {
	/**
	 * Publishes the openhab state changes to the openhab bus
	 * @param state the state to publish
	 */
	public void publish(State state);
	/**
	 * Interface for listening to openhab state changes
	 */
	public interface StateListener {
		/**
		 * Called whenever the state changes
		 * @param state new state
		 */
		public void stateChanged(State state);
	}
}
