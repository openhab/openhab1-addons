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
package org.openhab.binding.exec.internal;

import java.io.IOException;
import java.util.Arrays;

import org.openhab.binding.exec.ExecBindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * The swiss army knife binding which executes given commands on the commandline.
 * It could act as the opposite of WoL and sends the shutdown command to servers.
 * Or switches of WLAN connectivity if a scene "sleeping" is activated.
 * <p>
 * <i>Note</i>: when using 'ssh' you should use private key authorization since
 * the password cannot be read from commandline. The given user should have the
 * necessary permissions.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class ExecBinding extends AbstractEventSubscriberBinding<ExecBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(ExecBinding.class);
	
	private static final String CMD_LINE_DELIMITER = "@@";
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		
		ExecBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command.toString());
		
		if (provider == null) {
			logger.warn("doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String commandLine = provider.getCommandLine(itemName, command.toString());
		
        // fallback 
		if (commandLine == null) {
			commandLine = provider.getCommandLine(itemName, "*"); 
		}
		if (commandLine != null && !commandLine.isEmpty()) {
			executeCommand(commandLine);
		}
	}
	
	/**
	 * Find the first matching {@link ExecBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. If no direct match is
	 * found, a second match is issued with wilcard-command '*'. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 * provider could be found
	 */
	private ExecBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		
		ExecBindingProvider firstMatchingProvider = null;
		
		for (ExecBindingProvider provider : this.providers) {
			
			String commandLine = provider.getCommandLine(itemName, command);
			
			if (commandLine != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		// we didn't find an exact match. probably one configured a fallback
		// command?
		if (firstMatchingProvider == null) {
			for (ExecBindingProvider provider : this.providers) {
				
				String commandLine = provider.getCommandLine(itemName, "*");
				if (commandLine != null) {
					firstMatchingProvider = provider;
					break;
				}
			}
		}
		
		return firstMatchingProvider;
	}

	/**
	 * <p>Executes <code>commandLine</code>. Sometimes (especially observed on 
	 * MacOS) the commandLine isn't executed properly. In that cases another 
	 * exec-method is to be used. To accomplish this please use the special 
	 * delimiter '<code>@@</code>'. If <code>commandLine</code> contains this 
	 * delimiter it is split into a String[] array and the special exec-method
	 * is used.</p>
	 * <p>A possible {@link IOException} gets logged but no further processing is
	 * done.</p> 
	 * 
	 * @param commandLine the command line to execute
	 * @see http://www.peterfriese.de/running-applescript-from-java/
	 */
	private void executeCommand(String commandLine) {
		try {
			if (commandLine.contains(CMD_LINE_DELIMITER)) {
				String[] cmdArray = commandLine.split(CMD_LINE_DELIMITER);
				Runtime.getRuntime().exec(cmdArray);
				logger.info("executed commandLine '{}'", Arrays.asList(cmdArray));
			} else {
				Runtime.getRuntime().exec(commandLine);
				logger.info("executed commandLine '{}'", commandLine);
			}
		}
		catch (IOException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'", e);
		} 
	}
	
	
}
