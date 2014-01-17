/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal;

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines an interface to receive data from the Heatmiser thermostats.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
public interface HeatmiserEventListener extends EventListener {

	/**
	 * Receive data from the Heatmiser interface.
	 * 
	 * @param data
	 */
	void packetReceived(EventObject event, byte[] data);

}

