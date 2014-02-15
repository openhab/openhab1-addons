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
import org.openhab.core.types.State;
import org.openhab.core.types.PrimitiveType;

public class StringType implements PrimitiveType, State, Command {

	public final static StringType EMPTY = new StringType("");
	
	private final String value;

	public StringType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
	
	public static StringType valueOf(String value) {
		return new StringType(value);
	}

	public String format(String pattern) {
		return String.format(pattern, value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(obj instanceof String) {
			return obj.equals(value);
		}
		if (getClass() != obj.getClass())
			return false;
		StringType other = (StringType) obj;
		if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	

}
