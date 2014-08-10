/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal;

import java.util.EventListener;
import java.util.EventObject;


/**
 * DSC Alarm Event Listener interface. Handles incoming DSC Alarm events.
 * @author Russell Stephens
 * @since 1.6.0
 */
public interface DSCAlarmEventListener extends EventListener {

	/**
	 * Event handler method for incoming DSC Alarm events.
	 * @param event.
	 */
	void dscAlarmEventRecieved(EventObject event);
}
