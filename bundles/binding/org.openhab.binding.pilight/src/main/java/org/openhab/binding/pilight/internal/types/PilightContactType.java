/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.types;

import org.openhab.core.library.types.OpenClosedType;

/**
 * Enum to represent the state of a contact sensor in pilight 
 * 
 * @author Jeroen Idserda
 * @since 1.7
 */
public enum PilightContactType {
	OPENED, CLOSED;
	
	public OpenClosedType toOpenClosedType() {
		return this.equals(PilightContactType.OPENED) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
	}
}
