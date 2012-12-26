/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import java.math.BigDecimal;

import org.openhab.core.types.Command;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * The decimal type uses a BigDecimal internally and thus can be used for
 * integers, longs and floating point numbers alike.
 * 
 * @author Kai Kreuzer
 * 
 */
public class DecimalType extends Number implements PrimitiveType, State, Command, Comparable<DecimalType> {

	private static final long serialVersionUID = 4226845847123464690L;

	final static public DecimalType ZERO = new DecimalType(0);

	protected BigDecimal value;

	public DecimalType() {
		this.value = BigDecimal.ZERO;
	}

	public DecimalType(BigDecimal value) {
		this.value = value;
	}

	public DecimalType(long value) {
		this.value = new BigDecimal(value);
	}

	public DecimalType(double value) {
		this.value = new BigDecimal(value);
	}

	public DecimalType(String value) {
		this.value = new BigDecimal(value);
	}

	public String toString() {
		return value.toPlainString();
	}

	public static DecimalType valueOf(String value) {
		return new DecimalType(value);
	}

	public String format(String pattern) {
		if (pattern.contains("%d")) {
			return String.format(pattern, value.toBigInteger());
		} else {
			return String.format(pattern, value);
		}
	}

	public BigDecimal toBigDecimal() {
		return value;
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
		if (!(obj instanceof DecimalType))
			return false;
		DecimalType other = (DecimalType) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (value.compareTo(other.value) != 0)
			return false;
		return true;
	}

	public int compareTo(DecimalType o) {
		return value.compareTo(o.toBigDecimal());
	}

	@Override
	public double doubleValue() {
		return value.doubleValue();
	}

	@Override
	public float floatValue() {
		return value.floatValue();
	}

	@Override
	public int intValue() {
		return value.intValue();
	}

	@Override
	public long longValue() {
		return value.longValue();
	}
}
