package org.openhab.io.caldav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
	
	public static final String SCOPE_BEGIN = "BEGIN";
	public static final String SCOPE_END = "END";
	public static final String SCOPE_BETWEEN = "BETWEEN";
	public static final String DATE_FORMAT = "dd.MM.yyyy'T'HH:mm:ss";
	public static final String SEPERATOR = ":";
	
	public static List<EventContent> parseContent(CalDavEvent event, ItemRegistry itemRegistry, String scope) {
		return parseContent(event, itemRegistry, null, scope);
	}
	
	public static List<EventContent> parseContent(CalDavEvent event, Item item) {
		return parseContent(event, null, item, null);
	}
	
	private static List<EventContent> parseContent(CalDavEvent event, ItemRegistry itemRegistry, 
			Item itemIn, String expectedScope) {
		final List<EventContent> outMap = new ArrayList<EventUtils.EventContent>();
		
		try {
			BufferedReader reader = new BufferedReader(new StringReader(event.getContent()));
		
			String line = null;
			while ((line = reader.readLine()) != null) {
				Item item = itemIn;
				line = line.trim();
				
				String scope = null;
				DateTime time = null;
				String itemName = null;
				String stateString = null;
				int indexItemName = -1;
				
				if (line.startsWith(SCOPE_BEGIN)) {
					scope = SCOPE_BEGIN;
					if (line.length() < scope.length() + 4) {
						log.error("invalid format for line: {}", line);
						continue;
					}
					indexItemName = scope.length();
					time = event.getStart();
				} else if (line.startsWith(SCOPE_END)) {
					scope = SCOPE_END;
					if (line.length() < scope.length() + 4) {
						log.error("invalid format for line: {}", line);
						continue;
					}
					indexItemName = scope.length();
					time = event.getEnd();
				} else if (line.startsWith(SCOPE_BETWEEN)) {
					scope = SCOPE_BETWEEN;
					if (line.length() < scope.length() + 4 + 1 + 19) {
						log.error("invalid format for line: {}", line);
						continue;
					}
					String timeString = line.substring(SCOPE_BETWEEN.length() + 1, SCOPE_BETWEEN.length() + 1 + 19);
					time = DateTimeFormat.forPattern(EventUtils.DATE_FORMAT).parseDateTime(timeString);
					indexItemName = scope.length() + 19 + 1;
				} else {
					log.trace("line skipped: unknown content: " + line);
					continue;
				}
				
				if (line.substring(indexItemName + 1, indexItemName + 2).equals(":")) {
					log.error("invalid format for line: {}", line);
				}
				
				String itemAndCommand = line.substring(indexItemName + 1);
				final String[] split = itemAndCommand.split(SEPERATOR);
				if (split.length != 2) {
					log.error("invalid format for line: {}", line);
					continue;
				}
				
				itemName = split[0];
				stateString = split[1];
				
				if (expectedScope != null && !expectedScope.equals(scope)) {
					continue;
				}
				
				if (item == null) {
					if (itemRegistry == null) {
						log.error("item is null, but itemRegistry as well");
						continue;
					}
					
					try {
						item = itemRegistry.getItem(itemName);
					} catch (ItemNotFoundException e) {
						log.error("cannot find item: {}", itemName);
						continue;
					}
				}
				
				if (!item.getName().equals(itemName)) {
					log.debug("name of item {} does not match itemName {}", item.getName(), itemName);
					continue;
				}
				
				State state = TypeParser.parseState(item.getAcceptedDataTypes(), stateString);
				Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), stateString);
				log.debug("add item {} to action list (scope={}, state={}, time={})", item, scope, state, time);
				outMap.add(new EventContent(scope, item, state, command, time));
			}
		} catch (IOException e) {
			log.error("cannot parse event content", e);
		}
		
		return outMap;
	}
	
	
	
	private EventUtils() {}
	
	public final static class EventContent {
		private Item item;
		private State state;
		private Command command;
		private DateTime time;
		private String scope;
		
		public EventContent() {
			super();
		}

		public EventContent(String scope, Item item, State state, Command command, DateTime time) {
			super();
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

		
	}

	public static String createBetween(String itemName, State state) {
		return new StringBuilder()
		.append(SCOPE_BETWEEN)
		.append(SEPERATOR)
		.append(DateTimeFormat.forPattern(EventUtils.DATE_FORMAT).print(DateTime.now()))
		.append(SEPERATOR)
		.append(itemName)
		.append(SEPERATOR)
		.append(state)
		.toString();
	}

	public static String createEnd(String alias, State state) {
		return new StringBuilder()
		.append(SCOPE_END)
		.append(SEPERATOR)
		.append(alias)
		.append(SEPERATOR)
		.append(state)
		.toString();
	}
	
	public static String createBegin(String alias, State state) {
		return new StringBuilder()
		.append(SCOPE_BEGIN)
		.append(SEPERATOR)
		.append(alias)
		.append(SEPERATOR)
		.append(state)
		.toString();
	}
}
