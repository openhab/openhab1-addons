/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
				item.getState().toString(), Calendar.getInstance().getTime(), item.getName());
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
