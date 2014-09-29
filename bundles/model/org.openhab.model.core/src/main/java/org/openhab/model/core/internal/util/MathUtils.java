/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.core.internal.util;

/**
 * This class provides a few mathematical helper functions that are required by
 * code of this bundle.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 * 
 */
public class MathUtils {

	/**
	 * calculates the greatest common divisor of two numbers
	 * 
	 * @param m
	 *            first number
	 * @param n
	 *            second number
	 * @return the gcd of m and n
	 */
	static public int gcd(int m, int n) {
		if (m % n == 0)
			return n;
		return gcd(n, m % n);
	}

	/**
	 * calculates the least common multiple of two numbers
	 * 
	 * @param m
	 *            first number
	 * @param n
	 *            second number
	 * @return the lcm of m and n
	 */
	static public int lcm(int m, int n) {
		return m * n / gcd(n, m);
	}

	/**
	 * calculates the greatest common divisor of n numbers
	 * 
	 * @param numbers
	 *            an array of n numbers
	 * @return the gcd of the n numbers
	 */
	static public int gcd(Integer[] numbers) {
		int n = numbers[0];
		for (int m : numbers) {
			n = gcd(n, m);
		}
		return n;
	}

	/**
	 * determines the least common multiple of n numbers
	 * @param numbers
	 *            an array of n numbers
	 * @return the least common multiple of all numbers of the array
	 */
	static public int lcm(Integer[] numbers) {
		int n = numbers[0];
		for (int m : numbers) {
			n = lcm(n, m);
		}
		return n;
	}

}
