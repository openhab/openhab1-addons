/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.shared;

/**
 * Available TriggerTypes as used by the Rule Engine
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 * 
 */
public enum TriggerType {
	UPDATE, // fires whenever a status update is received for an item
	CHANGE, // same as UPDATE, but only fires if the current item state is
			// changed by the update
	COMMAND, // fires whenever a command is received for an item
	STARTUP, // fires when the rule engine bundle starts and once as soon as all
				// required items are available
	SHUTDOWN, // fires when the rule engine bundle is stopped
	TIMER; // fires at a given time

	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}
}
