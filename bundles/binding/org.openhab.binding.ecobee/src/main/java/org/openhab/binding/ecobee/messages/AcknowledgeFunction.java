/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * The acknowledge function allows an alert to be acknowledged.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/Acknowledge.shtml">Acknowledge</a>
 * @author John Cocula
 * @since 1.7.0
 */
public final class AcknowledgeFunction extends AbstractFunction {

	/**
	 * The type of acknowledgment.
	 */
	public static enum AckType {
		ACCEPT("accept"), DECLINE("decline"), DEFER("defer"), UNACKNOWLEDGED("unacknowledged");

		private final String type;

		private AckType(String type) {
			this.type = type;
		}

		@JsonCreator
		public static AckType forValue(String v) {
			for (AckType at : AckType.values()) {
				if (at.type.equals(v)) {
					return at;
				}
			}
			throw new IllegalArgumentException("Invalid ack type: " + v);
		}

		@Override
		@JsonValue
		public String toString() {
			return this.type;
		}
	}

	public AcknowledgeFunction(String thermostatIdentifier, String ackRef, AckType ackType, Boolean remindMeLater) {
		super("acknowledge");

		if (thermostatIdentifier == null || ackRef == null || ackType == null) {
			throw new IllegalArgumentException("thermostatIdentifier, ackRef and ackType are required.");
		}

		makeParams().put("thermostatIdentifier", thermostatIdentifier);
		makeParams().put("ackRef", ackRef);
		makeParams().put("ackType", ackType);
		if (remindMeLater != null) {
			makeParams().put("remindMeLater", remindMeLater);
		}
	}
}
