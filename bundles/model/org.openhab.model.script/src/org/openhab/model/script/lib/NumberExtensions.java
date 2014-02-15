/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
			return type==x; // both might be null, then we should return true
		}
	}

	public static boolean operator_notEquals(Type type, Number x) {
		if(type instanceof DecimalType) {
			return ((DecimalType)type).toBigDecimal().compareTo(new BigDecimal(x.toString()))!=0;
		} else {
			return type!=x; // both might be null, then we should return false, otherwise true
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
