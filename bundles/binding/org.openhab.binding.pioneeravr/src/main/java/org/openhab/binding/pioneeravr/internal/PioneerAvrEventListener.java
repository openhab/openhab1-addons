/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal;

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines interface to receive status updates from pioneerav receiver.
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 * @since 1.4.0
 */
public interface PioneerAvrEventListener extends EventListener {

	/**
	 * Procedure for receive status update from Pioneer receiver.
	 * 
	 * @param data
	 *            Received data.
	 */
	void statusUpdateReceived(EventObject event, String ip, String data);

}
