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
				logger.debug("about to execute CommandJob with arguments {}", Arrays.asList(args));
				ConsoleInterpreter.handleRequest(args, new LogConsole());
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

