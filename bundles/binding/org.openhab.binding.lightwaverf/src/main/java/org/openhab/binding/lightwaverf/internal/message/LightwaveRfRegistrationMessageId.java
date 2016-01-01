/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.message;

import java.util.Objects;

/**
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfRegistrationMessageId implements LightwaveRfMessageId {

	private final int messageId;

	public LightwaveRfRegistrationMessageId() {
		this.messageId = 100;
	}

	public String getMessageIdString() {
		return String.valueOf(messageId);
	}
	
	@Override
	public boolean equals(Object that) {
		if (that instanceof LightwaveRfRegistrationMessageId) {
			return Objects.equals(this.messageId,
					((LightwaveRfRegistrationMessageId) that).messageId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}

	@Override
	public String toString() {
		return "LightwaveRfRegistrationMessageId[" + messageId + "]";
	}
}
