/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

/**
 * Interface for MaxCul binding message processors
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface MaxCulBindingMessageProcessor {

	/**
	 * Process filtered CUL message in MAX! mode
	 * 
	 * @param data
	 *            Raw data of packet
	 * @param broadcast
	 *            True if a broadcast packet or is snooped and has a valid dest
	 *            addr, false if is addressed to us
	 */
	void maxCulMsgReceived(String data, boolean broadcast);
}
