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
