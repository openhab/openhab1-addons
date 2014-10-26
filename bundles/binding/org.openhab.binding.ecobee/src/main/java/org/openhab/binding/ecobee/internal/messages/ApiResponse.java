/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class for all Ecobee API responses.
 * 
 * @author John Cocula
 */
public class ApiResponse extends AbstractMessage implements Response {

	private Status status;

	@JsonProperty("status")
	public Status getStatus() {
		return this.status;
	}

	@Override
	public String getResponseMessage() {
		return status.toString();
	}

	@Override
	public boolean isError() {
		return status.getCode() != 0;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		if (this.status != null) {
			builder.append("status", this.status);
		}

		return builder.toString();
	}
}
