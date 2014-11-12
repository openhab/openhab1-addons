/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;


/**
* The S message contains information about Command execution results (guessed) 
* 
* @author Andreas Heil (info@aheil.de)
* @author Bernd Michael Helm (bernd.helm at helmundwalter.de)
* @since 1.6.0
*/
public final class S_Message extends Message {
	public S_Message(String raw) {
		super(raw);

		String[] tokens = this.getPayload().split(Message.DELIMETER);

		/**
		 * TODO: Implement S Message
		 */
	}

	@Override
	public void debug(Logger logger) {

	}

	@Override
	public MessageType getType() {
		return MessageType.S;
	}
}
