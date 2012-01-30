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
package org.openhab.model.script.lib;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Type;

/**
 * This class contains all kinds of extensions to be used by scripts and not provided by
 * Xbase. These include things like number handling and comparisons.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class NumberExtensions {
	
	// Calculation operators for numbers
	
	public static BigDecimal operator_plus(Number x, Number y) {
		return new BigDecimal(x.toString()).add(new BigDecimal(y.toString()));
	}

	public static BigDecimal operator_minus(Number x) {
		return new BigDecimal(x.toString()).negate();
	}

	public static BigDecimal operator_minus(Number x, Number y) {
		return new BigDecimal(x.toString()).subtract(new BigDecimal(y.toString()));
	}

	public static BigDecimal operator_multiply(Number x, Number y) {
		return new BigDecimal(x.toString()).multiply(new BigDecimal(y.toString()));
	}

	public static BigDecimal operator_divide(Number x, Number y) {
		return new BigDecimal(x.toString()).divide(new BigDecimal(y.toString()), 8, RoundingMode.HALF_UP);
	}


	// Comparison operations between numbers
	
	public static boolean operator_equals(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) == 0;
	}

	public static boolean operator_notEquals(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) != 0;
	}

	public static boolean operator_lessThan(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) < 0;
	}

	public static boolean operator_greaterThan(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) > 0;
	}

	public static boolean operator_lessEqualsThan(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) <= 0;
	}

	public static boolean operator_greaterEqualsThan(Number left, Number right) {
		return new BigDecimal(left.toString()).compareTo(new BigDecimal(right.toString())) >= 0;
	}
	

	// Comparison operators between openHAB types and numbers
	
	public static boolean operator_equals(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))==0;
		} else {
			return false;
		}
	}

	public static boolean operator_notEquals(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))!=0;
		} else {
			return false;
		}
	}

	public static boolean operator_greaterThan(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))>0;
		} else {
			return false;
		}
	}

	public static boolean operator_greaterEqualsThan(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))>=0;
		} else {
			return false;
		}
	}

	public static boolean operator_lessThan(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))<0;
		} else {
			return false;
		}
	}

	public static boolean operator_lessEqualsThan(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))<=0;
		} else {
			return false;
		}
	}
}
