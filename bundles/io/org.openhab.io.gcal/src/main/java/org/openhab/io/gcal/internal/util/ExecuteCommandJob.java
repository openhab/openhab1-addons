/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.console.ConsoleInterpreter;
import org.openhab.io.gcal.internal.GCalActivator;
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

    private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandJob.class);

    public static final String JOB_DATA_CONTENT_KEY = "content";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String content = (String) context.getJobDetail().getJobDataMap().get(JOB_DATA_CONTENT_KEY);

        ItemRegistry registry = GCalActivator.itemRegistryTracker.getService();
        EventPublisher publisher = GCalActivator.eventPublisherTracker.getService();

        if (registry == null) {
            logger.warn("Sorry, no item registry service available!");
            return;
        }

        if (publisher == null) {
            logger.warn("Sorry, no event publisher service available!");
            return;
        }

        if (content.startsWith("[PresenceSimulation]")) {
            try {
                Item item = registry.getItem("PresenceSimulation");
                if (item.getState() != OnOffType.ON) {
                    logger.debug(
                            "Presence Simulation job detected, but PresenceSimulation is not in ON state. Job is not executed");
                    return;
                }
            } catch (ItemNotFoundException e) {
                logger.warn(
                        "Presence Simulation job detected, but PresenceSimulation item does not exists. Check configuration");
                return;
            }
        }

        if (StringUtils.isNotBlank(content)) {
            String[] commands = parseCommands(content);
            for (String command : commands) {
                String[] args = parseCommand(command);

                try {
                    if (args[0].equals("send")) {
                        if (args.length > 2) {
                            Item item = registry.getItem(args[1]);
                            Command cmd = TypeParser.parseCommand(item.getAcceptedCommandTypes(), args[2]);
                            if (cmd != null) {
                                publisher.sendCommand(item.getName(), cmd);
                                logger.debug("Command {} has been sent", Arrays.asList(args));
                            } else {
                                logger.warn("Command '{}' is not valid. Command not sent.", Arrays.asList(args));
                            }
                        }
                    } else if (args[0].equals("update")) {
                        if (args.length > 2) {
                            Item item = registry.getItem(args[1]);
                            State state = TypeParser.parseState(item.getAcceptedDataTypes(), args[2]);
                            publisher.postUpdate(item.getName(), state);
                            logger.debug("Published update {}", Arrays.asList(args));
                        } else {
                            logger.warn("Command '{}' is not valid. Update not sent.", Arrays.asList(args));
                        }
                    } else {
                        logger.warn("Command {} not supported", args[0]);
                    }
                } catch (ItemNotFoundException e) {
                    logger.warn("Executing command failed. Item {} not found", args[1]);
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

        if (content.startsWith("[PresenceSimulation]")) {
            // Presence Simulation event. Needs to be fired only if PresenceSimulation event is set to ON
            try {
                in.readLine();
            } catch (IOException e) {
                logger.error("reading event content throws exception", e);
            }
        }

        try {
            String command;
            while ((command = in.readLine()) != null) {
                if (StringUtils.isNotBlank(command)) {
                    parsedCommands.add(command.trim());
                }
            }
        } catch (IOException ioe) {
            logger.error("reading event content throws exception", ioe);
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
            }
        }

        return parsedCommands.toArray(new String[0]);
    }

    /**
     * Parses a <code>command</code>. Utilizes the {@link StreamTokenizer} which
     * takes care of quoted Strings as well.
     *
     * @param command the command to parse
     * @return the tokenized command which can be processed by the
     *         <code>ConsoleInterpreter</code>
     *
     * @see org.openhab.io.console.ConsoleInterpreter
     */
    protected String[] parseCommand(String command) {
        logger.trace("going to parse command '{}'", command);

        // if the command starts with '>' it contains a script which needs no
        // further handling here ...
        if (command.startsWith(">")) {
            return new String[] { ">", command.substring(1).trim() };
        }

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(command));
        // treat all characters as ordinary, including digits, so we never
        // have to deal with doubles
        tokenizer.resetSyntax();
        tokenizer.wordChars(0x23, 0xFF);
        tokenizer.whitespaceChars(0x00, 0x20);
        tokenizer.quoteChar('"');

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
                }
                tokens.add(token);
                logger.trace("read value {} from the given command", token);
            }
        } catch (IOException ioe) {
        }

        return tokens.toArray(new String[0]);
    }

}
