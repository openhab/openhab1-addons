/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class for all Netatmo API responses.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
public abstract class AbstractResponse extends AbstractMessage implements
		Response {

	private NetatmoError error;

	@Override
	@JsonProperty("error")
	public NetatmoError getError() {
		return this.error;
	}

	@Override
	public boolean isError() {
		return this.error != null;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		if (this.error != null) {
			builder.append("error", this.error);
		}

		return builder.toString();
	}
}
