/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.util.EventObject;

import org.openhab.binding.ihc.ws.datatypes.WSControllerState;

/**
 * IHC controller status update event.
 * 
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class IhcStatusUpdateEvent extends EventObject {

	private static final long serialVersionUID = -2636867578360939315L;

	public IhcStatusUpdateEvent(Object source) {
		super(source);
	}

	/**
	 * Invoked when status updates received from IHC controller.
	 * 
	 * @param data
	 *            Data from receiver.

	 */
	public void StatusUpdateEventReceived(WSControllerState state) {
	}

}
