/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

/**
 * The send message function allows an alert message to be sent to the thermostat. 
 * TODO The message properties are same as those of the {@link Thermostat.Alert} object.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SendMessage.shtml">SendMessage</a>
 * @author John Cocula
 */
public final class SendMessageFunction extends AbstractFunction {

	/**
	 * @param text the message text to send. 
	 * Text will be truncated to 500 characters if longer.
	 * @throws IllegalArgumentException is text is <code>null</code>.
	 */
	public SendMessageFunction(String text) {
		super("sendMessage");
		if (text == null) {
			throw new IllegalArgumentException("text is required.");
		}

		makeParams().put("text", text);
	}
}
