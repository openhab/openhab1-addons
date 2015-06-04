/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.shared;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Base EventTrigger
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public interface EventTrigger {
	public String getItem();

	public boolean evaluate(Item item, State oldState, State newState, Command command, TriggerType type);
}
