/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.core.library.types;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.PrimitiveType;

public class StringType implements PrimitiveType, State, Command {

	private String value;

	public StringType() {
		this.value = "";
	};
	
	public StringType(String value) {
		// just to be sure that we stay with something human readable here;
		// if there's a usecase that requires more, the ComplexDataType might be a better option
		if(value.length() > 256) {
			throw new IllegalArgumentException("String must not exceed 256 characters!");
		}
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringType other = (StringType) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	

}
