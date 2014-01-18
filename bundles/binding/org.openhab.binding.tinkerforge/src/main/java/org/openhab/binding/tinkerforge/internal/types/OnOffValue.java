/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.types;

/**
 * A enumeration with values ON, OFF, UNDEF, commonly used to describe the state
 * of switching devices. UNDEF should be used for the unknown state.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 */
public enum OnOffValue implements TinkerforgeValue {
	ON, OFF, UNDEF;
	
	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}

}
