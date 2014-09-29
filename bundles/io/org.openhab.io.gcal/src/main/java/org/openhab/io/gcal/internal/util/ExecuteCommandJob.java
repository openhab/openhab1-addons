/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gcal.internal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.console.Console;
import org.openhab.io.console.ConsoleInterpreter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of Quartz {@link Job}-Interface. It parses the Calendar-Event
 * content into single commands and let {@link ConsoleInterpreter} handle those
 * commands.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class ExecuteCommandJob implements Job {

	private static final Logger logger = 
		LoggerFactory.getLogger(ExecuteCommandJob.class);
		
	public static final String JOB_DATA_CONTENT_KEY = "content";
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String content = (String) 
			context.getJobDetail().getJobDataMap().get(JOB_DATA_CONTENT_KEY);
		
		if (StringUtils.isNotBlank(content)) {
			String[] commands = parseCommands(content);
			for (String command : commands) {
				String[] args = parseCommand(command);
				logger.debug("About to execute CommandJob with arguments {}", Arrays.asList(args));
				try {
					ConsoleInterpreter.handleRequest(args, new LogConsole());
				} catch (Exception e) {
					throw new JobExecutionException("Executing command '" + command + "' throws an Exception. Job will be refired immediately.", e, true);
				}
			}
		}
		
	}
	
	/**
	 * Reads the Calendar-Event content String line by line. It is assumed, that
	 * each line contains a single command. Blank lines are omitted.
	 * 
	 * @param content the Calendar-Event content
	 * @return an array of single commands which can be executed afterwards
	 */
	protected String[] parseCommands(String content) {
		Collection<String> parsedCommands = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new StringReader(content));
		
		try {
			String command;
			while ((command = in.readLine()) != null) {
				if (StringUtils.isNotBlank(command)) {
					parsedCommands.add(command.trim());
				}
			}
		} 
		catch (IOException ioe) {
			logger.error("reading event content throws exception", ioe);
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ioe) {}
		}
		
		return parsedCommands.toArray(new String[0]);
	}

	/**
	 * Parses a <code>command</code>. Utilizes the {@link StreamTokenizer} which
	 * takes care of quoted Strings as well.
	 * 
	 * @param command the command to parse 
	 * @return the tokenized command which can be processed by the 
	 * <code>ConsoleInterpreter</code>
	 * 
	 * @see org.openhab.io.console.ConsoleInterpreter
	 */
	protected String[] parseCommand(String command) {
		logger.trace("going to parse command '{}'", command);
		
		// if the command starts with '>' it contains a script which needs no
		// further handling here ...
		if (command.startsWith(">")) {
			return new String[] {">", command.substring(1).trim()};
		}
		
		StreamTokenizer tokenizer = 
			new StreamTokenizer(new StringReader(command));
		tokenizer.wordChars('_', '_');
		tokenizer.wordChars('-', '-');
		tokenizer.wordChars('.', '.');
		
		List<String> tokens = new ArrayList<String>();
		try {
			int tokenType = 0;
			while (tokenType != StreamTokenizer.TT_EOF && tokenType != StreamTokenizer.TT_EOL) {
				tokenType = tokenizer.nextToken();
				String token = ""; 
				switch (tokenType) {
					case StreamTokenizer.TT_WORD:
					case 34 /* quoted String */:
						token = tokenizer.sval;
						break;
					case StreamTokenizer.TT_NUMBER:
						token = String.valueOf(tokenizer.nval);
						break;
				}
				tokens.add(token);
				logger.trace("read value {} from the given command", token);
			}
		} catch (IOException ioe) {}

		return tokens.toArray(new String[0]);
	}
	
	/**
	 * Simple implementation of the {@link Console} interface. It's output is
	 * send to the logger (INFO-Level).
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 0.7.0
	 */
	private static class LogConsole implements Console {

		public void print(String s) {
			logger.info(s);
		}

		public void println(String s) {
			logger.info(s);
		}

		public void printUsage(String s) {
			logger.info(s);
		}
		
	}

}

