/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import org.slf4j.Logger;


/**
* The M message contains metadata about the MAX!Cube setup. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class M_Message extends Message {

	public M_Message(String raw) {
		super(raw);
	}
	
	@Override
	public void debug(Logger logger) {
		logger.debug("=== M_Message === ");
	}

	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
