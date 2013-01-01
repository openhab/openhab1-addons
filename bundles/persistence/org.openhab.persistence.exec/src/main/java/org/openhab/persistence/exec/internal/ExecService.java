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
package org.openhab.persistence.exec.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Formatter;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the implementation of the Exec {@link PersistenceService}.
 * 
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.1.0
 */
public class ExecService implements PersistenceService {
		
	private static final Logger logger = 
		LoggerFactory.getLogger(ExecService.class);
	
	/**
	 * @{inheritDoc
	 */
	public String getName() {
		return "exec";
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item, String alias) {
		String execCmd = null;
		BufferedReader reader = null;
		
		try {
			execCmd = formatAlias(alias, 
				item.getState().toString(), Calendar.getInstance().getTime());
			logger.debug("Executing command [" + execCmd + "]");

			Process process = Runtime.getRuntime().exec(execCmd);
			String line = null;
			String output = "";
			logger.debug("Stored item '{}' as '{}' using Exec at {}.",
					new String[] { item.getName(), item.getState().toString(),
							(new java.util.Date()).toString() });

			// Collect the output stream (if any)
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {
				output = output + line;
			}
			reader.close();
			if (output.length() > 0) {
				logger.debug("Output from exec command is: " + output);
			}

			// Collect the error stream (if any)
			output = "";
			reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = reader.readLine()) != null) {
				output = output + line;
			}
			reader.close();
			if (output.length() > 0) {
				logger.debug("Error from exec command is: " + output);
			}

			process.waitFor();

		} catch (Exception e) {
			logger.error("Could not execute command [" + execCmd + "]", e);
		} finally {
			try {
				reader.close();
				reader = null;
			} catch (Exception hidden) {
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item) {
		throw new UnsupportedOperationException(
				"The Exec service requires aliases for persistence configurations that should match the Exec statement. Please configure exec.persist properly.");
	}
	
	/**
	 * Formats the given <code>alias</code> by utilizing {@link Formatter}.
	 * 
	 * @param alias the alias String which contains format strings
	 * @param values the values which will be replaced in the alias String
	 * 
	 * @return the formatted value. All format strings are replaced by 
	 * appropriate values
	 * @see java.util.Formatter for detailed information on format Strings.
	 */
	protected String formatAlias(String alias, Object... values) {
		return String.format(alias, values);
	}
	
}
