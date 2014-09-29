/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.console.internal.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.core.scriptengine.ScriptEngine;
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

	protected static ScriptEngine scriptEngine;

	protected void setScriptEngine(ScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
	}

	protected void unsetScriptEngine(ScriptEngine scriptEngine) {
		this.scriptEngine = null;
	}

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

		List<String> argsList = new ArrayList<String>();
		argsList.add(arg);
		Console console = new OSGiConsole(interpreter);
		
		while(true) {
			String narg = interpreter.nextArgument();
			if(!StringUtils.isEmpty(narg)) {
				argsList.add(narg);
			} else {
				break;
			}
		}
		
		String[] args = argsList.toArray(new String[argsList.size()]);
		ConsoleInterpreter.handleRequest(args, console);
		
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
		buffer.append("openhab " + ConsoleInterpreter.getItemsUsage() + "\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getSayUsage() + "\n\t");
		buffer.append("openhab " + ConsoleInterpreter.getScriptUsage() + "\n");
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
