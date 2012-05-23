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
package org.openhab.model.script.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The static methods of this class are made available as functions in the scripts.
 * This allows a script to log to the SLF4J-Log.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class LogExtension {
	
	private final static String LOGGER_NAME_PREFIX = "org.openhab.model.script.";
	
	/**
	 * Creates the Log-Entry <code>format</code> with level <code>DEBUG</code>
	 * and logs under the loggers name <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName the name of the Logger which is prefixed with 
	 * <code>org.openhab.model.script.</code> 
	 * @param format the Log-Statement which can contain placeholders '<code>{}</code>'
	 * @param args the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logDebug(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).debug(format, args);
	}

	/**
	 * Creates the Log-Entry <code>format</code> with level <code>INFO</code>
	 * and logs under the loggers name <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName the name of the Logger which is prefixed with 
	 * <code>org.openhab.model.script.</code> 
	 * @param format the Log-Statement which can contain placeholders '<code>{}</code>'
	 * @param args the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logInfo(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).info(format, args);
	}
	
	/**
	 * Creates the Log-Entry <code>format</code> with level <code>WARN</code>
	 * and logs under the loggers name <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName the name of the Logger which is prefixed with 
	 * <code>org.openhab.model.script.</code> 
	 * @param format the Log-Statement which can contain placeholders '<code>{}</code>'
	 * @param args the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logWarn(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).warn(format, args);
	}
	
	/**
	 * Creates the Log-Entry <code>format</code> with level <code>ERROR</code>
	 * and logs under the loggers name <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName the name of the Logger which is prefixed with 
	 * <code>org.openhab.model.script.</code> 
	 * @param format the Log-Statement which can contain placeholders '<code>{}</code>'
	 * @param args the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logError(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).error(format, args);
	}
	
}
