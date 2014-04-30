/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class DecimalType extends Number implements PrimitiveType, State,
		Command, Comparable<DecimalType> {

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
		if (value.scale() == 0) {
			// The value is an integer value. Convert to BigInteger in order to
			// have access to more conversion formats.
			try {
				return String.format(pattern, value.toBigIntegerExact());
			} catch (ArithmeticException ae) {
				// Could not convert to integer value without loss of
				// information. Fall through to default behavior.
			}
		}

		return String.format(pattern, value);
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
