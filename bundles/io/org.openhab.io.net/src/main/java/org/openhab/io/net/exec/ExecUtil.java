/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some common methods to execute commands on command line.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class ExecUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(ExecUtil.class);

	private static final String CMD_LINE_DELIMITER = "@@";

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
	public static void executeCommandLine(String commandLine) {
		try {
			if (commandLine.contains(CMD_LINE_DELIMITER)) {
				String[] cmdArray = commandLine.split(CMD_LINE_DELIMITER);
				Runtime.getRuntime().exec(cmdArray);
				logger.info("executed commandLine '{}'",
						Arrays.asList(cmdArray));
			} else {
				Runtime.getRuntime().exec(commandLine);
				logger.info("executed commandLine '{}'", commandLine);
			}
		} catch (IOException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'",
					e);
		}
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
	public static String executeCommandLineAndWaitResponse(String commandLine,
			int timeout) {
		String retval = null;

		CommandLine cmdLine = null;

		if (commandLine.contains(CMD_LINE_DELIMITER)) {
			String[] cmdArray = commandLine.split(CMD_LINE_DELIMITER);
			cmdLine = new CommandLine(cmdArray[0]);

			for (int i = 1; i < cmdArray.length; i++) {
				cmdLine.addArgument(cmdArray[i], false);
			}
		} else {
			cmdLine = CommandLine.parse(commandLine);
		}

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		Executor executor = new DefaultExecutor();

		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(stdout);

		executor.setExitValue(1);
		executor.setStreamHandler(streamHandler);
		executor.setWatchdog(watchdog);

		try {
			executor.execute(cmdLine, resultHandler);
			logger.debug("executed commandLine '{}'", commandLine);
		} catch (ExecuteException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'",
					e);
		} catch (IOException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'",
					e);
		}

		// some time later the result handler callback was invoked so we
		// can safely request the exit code
		try {
			resultHandler.waitFor();
			int exitCode = resultHandler.getExitValue();
			retval = StringUtils.chomp(stdout.toString());
			logger.debug("exit code '{}', result '{}'", exitCode, retval);

		} catch (InterruptedException e) {
			logger.error("Timeout occured when executing commandLine '"
					+ commandLine + "'", e);
		}

		return retval;
	}

}
