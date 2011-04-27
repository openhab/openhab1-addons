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

package org.openhab.io.console.internal.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.io.console.Console;
import org.openhab.io.console.ConsoleInterpreter;

/**
 * This class provides access to openHAB functionality through the OSGi console
 * of Equinox. Unfortunately, there these command providers are not standardized
 * for OSGi, so we need different implementations for different OSGi containers.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class ConsoleCommandProvider implements CommandProvider {

	/**
	 * Methods staring with "_" will be used as commands. We only define one command "openhab" to make
	 * sure we do not get into conflict with other existing commands. The different functionalities
	 * can then be used by the first argument.
	 * 
	 * @param interpreter the equinox command interpreter
	 * @return null, return parameter is not used
	 */
	public Object _openhab(CommandInterpreter interpreter) {
		String arg = interpreter.nextArgument();
		
		if(arg==null) {
			interpreter.println(getHelp());
			return null;
		}
		
		Console console = new OSGiConsole(interpreter);
		List<String> argsList = new ArrayList<String>();
		while(true) {
			String narg = interpreter.nextArgument();
			if(!StringUtils.isEmpty(narg)) {
				argsList.add(narg);
			} else {
				break;
			}
		}
		String[] args = argsList.toArray(new String[argsList.size()]);
		
		if(arg.equals("items")) {
			ConsoleInterpreter.handleItems(args, console);
		} else if(arg.equals("send")) {
			ConsoleInterpreter.handleSend(args, console);
		} else if(arg.equals("update")) {
			ConsoleInterpreter.handleUpdate(args, console);
		} else if(arg.equals("status")) {
			ConsoleInterpreter.handleStatus(args, console);
		} else {
			interpreter.println(getHelp());
		}
		
		return null;
	}


	/**
	 * Contributes the usage of our command to the console help output.
	 */
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---openHAB commands---\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getCommandUsage() + "\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getUpdateUsage() + "\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getStatusUsage() + "\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getItemsUsage() + "\n");
		return buffer.toString();
	}
	
	private static class OSGiConsole implements Console {
		
		private CommandInterpreter interpreter;

		public OSGiConsole(CommandInterpreter interpreter) {
			this.interpreter = interpreter;
		}
		
		public void print(String s) {
			interpreter.print(s);
		}

		public void println(String s) {
			interpreter.println(s);
		}

		public void printUsage(String s) {
			interpreter.println("Usage: openhab " + s);
		}
		
	}

}
