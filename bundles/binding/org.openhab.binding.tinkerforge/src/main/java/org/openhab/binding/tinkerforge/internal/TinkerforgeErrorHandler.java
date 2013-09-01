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
package org.openhab.binding.tinkerforge.internal;

import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.slf4j.Logger;

public class TinkerforgeErrorHandler {
	/**
	 * @author Theo Weiss
	 * @since 1.3.0
	 */

	public static String TF_TIMEOUT_EXCEPTION = "Tinkerforge timeout occurred";
	public static String TF_NOT_CONNECTION_EXCEPTION = "Tinkerforge \"not connected\" occurred";

	/**
	 * Logs errors from devices using the device specific logger.
	 * 
	 * @param mBaseDevice
	 *            The device to log errors for as {@code MBaseDevice}.
	 * @param message
	 *            The log message as {@code String}.
	 * @param e
	 *            The throwable to log as {@code Throwable}
	 */
	public static void handleError(MBaseDevice mBaseDevice, String message,
			Throwable e) {
		mBaseDevice.getLogger().error("Tinkerforge Error: {} : {}", message,
				e.getMessage());
	}

	/**
	 * Logs errors using the given logger.
	 * 
	 * @param logger
	 *            The logger to use as {@code Logger}.
	 * @param message
	 *            The message to log as {@code String}.
	 * @param e
	 *            The throwable to log as {@code Throwable}
	 */
	public static void handleError(Logger logger, String message, Throwable e) {
		logger.error("Tinkerforge Error: {} : {}", message, e.getMessage());
	}
}
