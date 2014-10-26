/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base class for all messages.
 * 
 * @author John Cocula
 * @author Andreas Brenk
 */
public class AbstractMessage {

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();

		return builder.toString();
	}

	protected final ToStringBuilder createToStringBuilder() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE);
	}

}
