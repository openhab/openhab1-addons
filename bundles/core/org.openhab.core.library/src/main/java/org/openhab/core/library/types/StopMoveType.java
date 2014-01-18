/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import org.openhab.core.types.Command;
import org.openhab.core.types.PrimitiveType;

public enum StopMoveType implements PrimitiveType, Command {
	STOP, MOVE;
	
	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}

}
