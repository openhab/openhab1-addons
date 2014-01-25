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
 * 
 * @author Theo Weiss
 * @since 1.4.0
 */
public enum UnDefValue implements TinkerforgeValue {
	UNDEF, NULL;
	
	public String toString() {
		switch(this) {
			case UNDEF: return "Undefined";
			case NULL:  return "Uninitialized";
		}
		return "";
	}
	
	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}

}
