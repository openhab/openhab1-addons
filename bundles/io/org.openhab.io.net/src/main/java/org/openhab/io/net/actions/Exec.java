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
package org.openhab.io.net.actions;

import java.io.IOException;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.net.exec.ExecUtil;

/**
 * This class provides static methods that can be used in automation rules for
 * executing commands on command line.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 * 
 */
public class Exec {

	/**
	 * <p>
	 * Executes <code>commandLine</code>. Sometimes (especially observed on
	 * MacOS) the commandLine isn't executed properly. In that cases another
	 * exec-method is to be used. To accomplish this please use the special
	 * delimiter '<code>@@</code>'. If <code>commandLine</code> contains this
	 * delimiter it is split into a String[] array and the special exec-method
	 * is used.
	 * </p>
	 * <p>
	 * A possible {@link IOException} gets logged but no further processing is
	 * done.
	 * </p>
	 * 
	 * @param commandLine
	 *            the command line to execute
	 * @see http://www.peterfriese.de/running-applescript-from-java/
	 */
	@ActionDoc(text="Executes <code>commandLine</code>.")
	static public void executeCommandLine(@ParamDoc(name="commandLine") String commandLine) {
		ExecUtil.executeCommandLine(commandLine);
	}

	/**
	 * <p>
	 * Executes <code>commandLine</code>. Sometimes (especially observed on
	 * MacOS) the commandLine isn't executed properly. In that cases another
	 * exec-method is to be used. To accomplish this please use the special
	 * delimiter '<code>@@</code>'. If <code>commandLine</code> contains this
	 * delimiter it is split into a String[] array and the special exec-method
	 * is used.
	 * </p>
	 * <p>
	 * A possible {@link IOException} gets logged but no further processing is
	 * done.
	 * </p>
	 * 
	 * @param commandLine
	 *            the command line to execute
	 * @param timeout
	 *            timeout for execution in milliseconds
	 * @return response data from executed command line
	 */
	@ActionDoc(text="Executes <code>commandLine</code>.")
	static public String executeCommandLine(
			@ParamDoc(name="commandLine")String commandLine, 
			@ParamDoc(name="timeout", text="timeout for execution in milliseconds") int timeout) {
		return ExecUtil.executeCommandLineAndWaitResponse(commandLine, timeout);
	}

}
