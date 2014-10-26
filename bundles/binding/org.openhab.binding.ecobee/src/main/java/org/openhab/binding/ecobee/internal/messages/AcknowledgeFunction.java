/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * The acknowledge function allows an alert to be acknowledged.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/Acknowledge.shtml">Acknowledge</a>
 * @author John Cocula
 * @author Ecobee
 */
public final class AcknowledgeFunction extends AbstractFunction {

	/**
	 * The type of acknowledgement. 
	 */
	public static enum AckType {
		ACCEPT("accept"),
		DECLINE("decline"),
		DEFER("defer"),
		UNACKNOWLEDGED("unacknowledged");
		
		private final String type;
		
		private AckType(String type) { this.type = type; }
		
		@Override
		@JsonValue
		public String toString() { return this.type; }
	}

	public AcknowledgeFunction( String thermostatIdentifier,
								String ackRef,
								AckType ackType,
								Boolean remindMeLater ) {
		super( "acknowledge" );
		
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
