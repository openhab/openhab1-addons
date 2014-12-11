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

/**
 * IHC controller error event.
 * 
 * @author Pauli Anttila
 * @since 1.6.0
 */
public class IhcErrorEvent extends EventObject {

	private static final long serialVersionUID = 3224923200664904390L;

	public IhcErrorEvent(Object source) {
		super(source);
	}

	/**
	 * Invoked when fatal error occurred during communication to IHC controller.
	 * 
	 * @param e
	 *            Reason for error.

	 */
	public void StatusUpdateEventReceived(IhcExecption e) {
	}

}
