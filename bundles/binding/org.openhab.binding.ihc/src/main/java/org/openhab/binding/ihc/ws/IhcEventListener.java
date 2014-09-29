/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.util.EventListener;
import java.util.EventObject;

import org.openhab.binding.ihc.ws.datatypes.WSControllerState;
import org.openhab.binding.ihc.ws.datatypes.WSResourceValue;

/**
 * This interface defines interface to receive updates from IHC controller.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface IhcEventListener extends EventListener {

	/**
	 * Event for receive status update from IHC controller.
	 * 
	 * @param data
	 *            Received data.
	 */
	void statusUpdateReceived(EventObject event, WSControllerState status);

	/**
	 * Event for receive resource value updates from IHC controller.
	 * 
	 * @param data
	 *            Received data.
	 */
	void resourceValueUpdateReceived(EventObject event, WSResourceValue value);
	
}
