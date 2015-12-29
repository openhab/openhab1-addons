/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Some utility methods to convert date between different realms and types.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
class PanStampConversions {

	/**
	 * Convert an endpoint to a string. Useful for debugging or logging.
	 * 
	 * @param ep
	 *            The endpoint to convert.
	 * @return The resulting string representation.
	 */
	static String toString(Endpoint<?> ep) {
		return String.format("%s/%s", toString(ep.getRegister()), ep.getName());
	}

	/**
	 * Convert a register to a string. Useful for debugging or logging.
	 * 
	 * @param reg
	 *            The register to convert.
	 * @return The resulting string representation.
	 */
	static String toString(Register reg) {
		return String.format("%s->%d", toString(reg.getDevice()), reg.getId());
	}

	/**
	 * Convert a panStamp to a string. Useful for debugging or logging.
	 * 
	 * @param ps
	 *            The panStamp to convert.
	 * @return The resulting string representation.
	 */
	static String toString(PanStamp ps) {
		return String.format("%d", ps.getAddress());
	}

	/**
	 * convert the value value for and endpoint to a state message
	 * 
	 * @param ep
	 *            The endpoint for which the value is converted
	 * @param val
	 *            The value being converted
	 * @return a new State object for the value
	 */
	static State toState(Endpoint<?> ep, Object val) throws ValueException {
		switch (ep.getType()) {
		case STRING:
			return new StringType(val.toString());
		case NUMBER:
			return new DecimalType(((Double) val));
		case INTEGER:
			return new DecimalType(((Long) val));
		case BINARY:
			return ((Boolean) val) ? OnOffType.ON : OnOffType.OFF;
		default:
			throw new ValueException(String.format("Unsupported panStamp endpoint type '%s' for endpoint %s. BUG!",
					ep.getType(), toString(ep)));
		}
	}

	/**
	 * Convert a command to a String value
	 * 
	 * @param cmd
	 *            The command to convert to a String
	 * @return The string
	 * @throws ValueException
	 *             If a conversion isn't possible
	 */
	static String toString(Command cmd) throws ValueException {
		if (cmd instanceof StringType) {
			return ((StringType) cmd).toString();
		}
		throw new ValueException(String.format("Unsopported command type '%s' for string endpoint", cmd.getClass()
				.getSimpleName()));
	}

	/**
	 * Convert a command to a boolean value
	 * 
	 * @param cmd
	 *            The command to convert to a boolean
	 * @return The boolean value
	 * @throws ValueException
	 *             If a conversion isn't possible
	 */
	static Boolean toBoolean(Command cmd) throws ValueException {
		if (cmd instanceof OnOffType) {
			return ((OnOffType) cmd) == OnOffType.ON ? true : false;
		}
		throw new ValueException(String.format("Unsopported command type '%s' for boolean endpoint", cmd.getClass()
				.getSimpleName()));
	}

	/**
	 * Convert a command to a long value
	 * 
	 * @param cmd
	 *            The command to convert to a integer
	 * @return The long value
	 * @throws ValueException
	 *             If a conversion isn't possible
	 */
	static Long toLong(Command cmd) throws ValueException {
		if (cmd instanceof DecimalType) {
			return ((DecimalType) cmd).longValue();
		}
		throw new ValueException(String.format("Unsopported command type '%s' for integer endpoint", cmd.getClass()
				.getSimpleName()));
	}

	/**
	 * Convert a command to a double value
	 * 
	 * @param cmd
	 *            The command to convert to a double
	 * @return The double value
	 * @throws ValueException
	 *             If a conversion isn't possible
	 */
	static Double toDouble(Command cmd) throws ValueException {
		if (cmd instanceof DecimalType) {
			return ((DecimalType) cmd).doubleValue();
		}
		throw new ValueException(String.format("Unsopported command type '%s' for number endpoint", cmd.getClass()
				.getSimpleName()));
	}

	/**
	 * return a non-empty string for the given object, or throw an exception
	 * 
	 * @param name
	 *            The value name to use in an error message
	 * @param val
	 *            The value to convert
	 * @return The new string
	 * @throws ValueException
	 *             If there is a problem converting the object to a string
	 */
	static String asString(String name, Object val) throws ValueException {
		if (val == null) {
			throw new ValueException(name + " is undefined");
		}
		String sVal = val.toString();
		sVal = sVal.trim();
		if (sVal.isEmpty()) {
			throw new ValueException(name + " is empty");
		}
		return sVal;
	}

	/**
	 * return a int for the given object, or throw an exception
	 * 
	 * @param name
	 *            The value name to use in an error message
	 * @param val
	 *            The value to convert
	 * @param min
	 *            The minimum allowed value for the int
	 * @param max
	 *            The maximum allowed value for the int
	 * @return The new int
	 * @throws ValueException
	 *             If there is a problem converting the object to a int, or if it is out of range.
	 */
	static int asInt(String name, Object val, int min, int max) throws ValueException {
		int iVal = asInt(name, val);
		if ((iVal < min) || (iVal > max)) {
			throw new ValueException("Invalid " + name + " (" + iVal + " is out of range)");
		}
		return iVal;
	}

	/**
	 * return a int for the given object, or throw an exception
	 * 
	 * @param name
	 *            The value name to use in an error message
	 * @param val
	 *            The value to convert
	 * @param allowed
	 *            The allowed values for the int
	 * @return The new int
	 * @throws ValueException
	 *             If there is a problem converting the object to a int, or if it is out of range
	 */
	static int asInt(String name, Object val, int[] allowed) throws ValueException {
		int iVal = asInt(name, val);
		for (int i = 0; i < allowed.length; ++i) {
			if (iVal == allowed[i]) {
				return iVal;
			}
		}
		throw new ValueException("Invalid " + name + " (" + iVal + " is not of allowed values)");
	}

	private static int asInt(String name, Object val) throws ValueException {
		String sVal = asString(name, val);
		try {
			int iVal;
			if (sVal.startsWith("0x")) {
				iVal = Integer.parseInt(sVal.replace("0x", ""), 16);
			} else {
				iVal = Integer.parseInt(sVal);
			}
			return iVal;
		} catch (NumberFormatException e) {
			throw new ValueException("Invalid " + name + " '" + sVal + "'", e);
		}

	}

}
