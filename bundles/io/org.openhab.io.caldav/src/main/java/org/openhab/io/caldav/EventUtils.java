/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventUtils {
    private static final Logger log = LoggerFactory.getLogger(EventUtils.class);

    // patterns used to parse event entries
    private static final Pattern COMMAND_PATTERN = Pattern.compile("(?:BEGIN|END|BETWEEN):");
    private static final Pattern LINE_PATTERN = Pattern.compile("(BEGIN|END|BETWEEN)(:[^:\\s]{19})?:([^:\\s]+):(.+)");

    public static final String SCOPE_BEGIN = "BEGIN";
    public static final String SCOPE_END = "END";
    public static final String SCOPE_BETWEEN = "BETWEEN";
    public static final String DATE_FORMAT = "dd.MM.yyyy'T'HH:mm:ss";
    public static final String SEPERATOR = ":";

    public static List<EventContent> parseContent(CalDavEvent event, ItemRegistry itemRegistry, String scope,
            String defaultItemOnBegin) {
        return parseContent(event, itemRegistry, null, scope, defaultItemOnBegin);
    }

    public static List<EventContent> parseContent(CalDavEvent event, ItemRegistry itemRegistry, String scope) {
        return parseContent(event, itemRegistry, null, scope, null);
    }

    public static List<EventContent> parseContent(CalDavEvent event, Item item) {
        return parseContent(event, null, item, null, null);
    }

    private static List<EventContent> parseContent(CalDavEvent event, ItemRegistry itemRegistry, Item itemIn,
            String expectedScope, String defaultItemOnBegin) {
        final List<EventContent> outMap = new ArrayList<EventUtils.EventContent>();

        // no content, nothing to parse
        if (StringUtils.isEmpty(event.getContent())) {
            log.trace("Event content was empty! Returning empty event list.");
            return outMap;
        }

        // pre-process the list of strings; add each to the list
        List<String> linesToProcess = new Vector<String>();

        try {
            final BufferedReader reader = new BufferedReader(new StringReader(event.getContent()));

            String line = null;
            while ((line = reader.readLine()) != null) {
                log.trace("Raw event from readLine: {}", line);
                //if the line contains the Unicode line separator (U+2028), break it up
                if( line.indexOf("\u2028") > -1 ) {
                    log.trace("Found Unicode line separator. Breaking line into multiple lines.");
                    String[] splitLines = line.split("\u2028");
                    for(String s : splitLines) {
                        linesToProcess.add(s);
                    }
                }
                else {
                    linesToProcess.add(line);
                }
            }
        } catch (IOException e) {
            log.warn("Cannot parse event content", e);
        }

        for(String line : linesToProcess) {
            log.trace("Processing line: {}", line);
            Item item = itemIn;

            final EventLine eventLine = parseEventLine(line.trim(), event, defaultItemOnBegin);
            if (eventLine == null) {
                continue;
            }
            if (expectedScope != null && !expectedScope.equals(eventLine.scope)) {
                continue;
            }

            if (item == null) {
                if (itemRegistry == null) {
                    log.warn("Item is null and itemRegistry is also null.  Unable to get item.");
                    continue;
                }

                try {
                    item = itemRegistry.getItem(eventLine.itemName);
                } catch (ItemNotFoundException e) {
                    log.warn("Cannot find item in registry: {}", eventLine.itemName);
                    continue;
                }
            }

            if (!item.getName().equals(eventLine.itemName)) {
                log.trace("name of item in registry {} does not match itemName in event {}", item.getName(), eventLine.itemName);
                continue;
            }

            final State state = TypeParser.parseState(item.getAcceptedDataTypes(), eventLine.stateString);
            final Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), eventLine.stateString);
            log.trace("add item {} to action list (scope={}, state={}, time={})", item, eventLine.scope, state,
                    eventLine.time);
            outMap.add(new EventContent(eventLine.scope, item, state, command, eventLine.time));
        }

        return outMap;
    }

    private static EventLine parseEventLine(String line, CalDavEvent event, String defaultItemOnBegin) {
        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (matcher.matches()) {
            final String scope = matcher.group(1);
            final String itemName = matcher.group(3);
            final String stateString = matcher.group(4);
            if (itemName.trim().length() > 0 && stateString.trim().length() > 0) {
                if (SCOPE_BEGIN.equals(scope)) {
                    return new EventLine(itemName, stateString, scope, event.getStart());
                }
                if (SCOPE_END.equals(scope)) {
                    return new EventLine(itemName, stateString, scope, event.getEnd());
                }
                if (SCOPE_BETWEEN.equals(scope)) {
                    final String timeString = matcher.group(2);
                    final DateTime time = DateTimeFormat.forPattern(EventUtils.DATE_FORMAT).parseDateTime(timeString);
                    return new EventLine(itemName, stateString, scope, time);
                }
            }
        } else if (defaultItemOnBegin != null && !COMMAND_PATTERN.matcher(line).matches()) {
            // if defaultItemOnBegin is set, use entire line as command value
            return new EventLine(defaultItemOnBegin, line, SCOPE_BEGIN, event.getStart());
        }
        log.error("invalid format for line: {}", line);
        return null; // nothing meaningful found in line
    }

    private EventUtils() {
    }

    private final static class EventLine {
        final String itemName;
        final String stateString;
        final String scope;
        final DateTime time;

        EventLine(String itemName, String stateString, String scope, DateTime time) {
            this.itemName = itemName;
            this.stateString = stateString;
            this.scope = scope;
            this.time = time;
        }
    }

    public final static class EventContent {
        private final Item item;
        private final State state;
        private final Command command;
        private final DateTime time;
        private final String scope;

        public EventContent(String scope, Item item, State state, Command command, DateTime time) {
            this.scope = scope;
            this.item = item;
            this.state = state;
            this.command = command;
            this.time = time;
        }

        public Item getItem() {
            return item;
        }

        public State getState() {
            return state;
        }

        public Command getCommand() {
            return command;
        }

        public DateTime getTime() {
            return time;
        }

        public String getScope() {
            return scope;
        }

        @Override
        public String toString() {
            return "EventContent [item=" + item + ", state=" + state + ", command=" + command + ", time=" + time
                    + ", scope=" + scope + "]";
        }
    }

    public static String createBetween(String itemName, State state) {
        return new StringBuilder().append(SCOPE_BETWEEN).append(SEPERATOR)
                .append(DateTimeFormat.forPattern(EventUtils.DATE_FORMAT).print(DateTime.now())).append(SEPERATOR)
                .append(itemName).append(SEPERATOR).append(state).toString();
    }

    public static String createEnd(String alias, State state) {
        return new StringBuilder().append(SCOPE_END).append(SEPERATOR).append(alias).append(SEPERATOR).append(state)
                .toString();
    }

    public static String createBegin(String alias, State state) {
        return new StringBuilder().append(SCOPE_BEGIN).append(SEPERATOR).append(alias).append(SEPERATOR).append(state)
                .toString();
    }
}
