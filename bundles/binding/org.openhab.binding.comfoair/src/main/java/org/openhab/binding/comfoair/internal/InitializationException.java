/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.comfoair.internal;

/**
 * Default serial port initialization exception. Is used to catch and handle
 * different serial ios exceptions
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class InitializationException extends Exception {

	private static final long serialVersionUID = -5106059856757667266L;

	/**
	 * Initialize the exceptions with an error message
	 * @param msg
	 */
	public InitializationException(String msg) {
		super(msg);
	}

	/**
	 * Initialize the exceptions with a encapsulated exceptions
	 * @param msg
	 */
	public InitializationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Initialize the exceptions with a error message and a encapsulated exceptions
	 * @param msg
	 */
	public InitializationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
