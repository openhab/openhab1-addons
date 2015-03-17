/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_command.internal;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.caldav_command.CalDavBindingProvider;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.EventNotifier;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This is the default implementation of the caldav binding
 * 
 * 
 * BEGIN:ItemName:ON
 * BEGIN:ItemName:23
 * 
 * END:ItemName:OFF
 * END:ItemName:16
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public class CalDavBinding extends AbstractBinding<CalDavBindingProvider> implements ManagedService, EventNotifier {

	private static final String SCOPE_END = "END";
	private static final String SCOPE_BEGIN = "BEGIN";
	
	private static final String KEY_READ_CALENDARS = "readCalendars";

	private static final Logger logger = 
		LoggerFactory.getLogger(CalDavBinding.class);
	
	private ItemRegistry itemRegistry;
	
	private CalDavLoader calDavLoader;
	
	private List<String> readCalendars = new ArrayList<String>();
	
	// key=item config, value=list of item changes to listen to
	private ConcurrentHashMap<CalDavNextEventConfig, List<NextEventContainer>> itemNextEventMap = new ConcurrentHashMap<CalDavNextEventConfig, List<NextEventContainer>>();
	
	private List<String> disabledItems = new ArrayList<String>();
	
	public CalDavBinding() {
	}
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}
	
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}
	
	public void setCalDavLoader(CalDavLoader calDavLoader) {
		this.calDavLoader = calDavLoader;
		this.calDavLoader.addListener(this);
	}
	
	public void unsetCalDavLoader() {
		this.calDavLoader.removeListener(this);
		this.calDavLoader = null;
	}
	
	public void activate() {
		
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties == null) {
			logger.warn("no configuration found");
		} else {
			String read = (String) properties.get(KEY_READ_CALENDARS);
			if (read != null) {
				for (String value : read.split(",")) {
					this.readCalendars.add(value.trim());
				}
			}
		}
	}
	
	private String parseContent(String content, String scope) {
		StringBuilder out = new StringBuilder();
		
		String[] lines = content.split("\n");
		for (String line : lines) {
			line = line.trim();
			
			if (line.startsWith(scope)) {
				if (out.length() > 0) {
					out.append(",");
				}
				out.append(line.substring(scope.length() + 1));
			}
		}
		
		return out.toString();
	}
	
	private CalDavBindingProvider getCalDavBindingProvider() {
		for (CalDavBindingProvider provider : providers) {
			return provider;
		}
		return null;
	}
	
	private void handleForEventMap(CalDavEvent event, String scope, boolean add) {
		if (!readCalendars.contains(event.getCalendarId())) {
			logger.trace("event '{}' is not in used calendars", event.getShortName());
			return;
		}
		
		if (event.getContent() == null) {
			logger.warn("no content for event: {}", event.getShortName());
			return;
		}
		
		String content = parseContent(event.getContent(), scope);
		
		if (content.trim().isEmpty()) {
			logger.warn("no content for event: {}", event.getShortName());
			return;
		}
		
		String[] commands = content.split(",");
		for (String itemCommand : commands) {
			String[] commandSplit = itemCommand.split(":");
			String itemName = commandSplit[0];
			String commandString = commandSplit[1];
			
			Item item = null;
			try {
				item = itemRegistry.getItem(itemName);
			} catch (ItemNotFoundException e) {
				logger.error("item '" + itemName + "' could not be found");
				return;
			}
			if (item == null) {
				logger.error("cannot find item: {}", itemName);
				return;
			}
			
			CalDavBindingProvider provider = getCalDavBindingProvider();
			if (provider == null) {
				logger.error("cannot find any provider");
				return;
			}
			List<CalDavNextEventConfig> configList = provider.getConfigForListenerItem(itemName);
			for (CalDavNextEventConfig config : configList) {
				if (add) {
					this.addToEventMap(config, config.getItemNameToListenTo(), commandString, scope.equals(SCOPE_BEGIN) ? event.getStart() : event.getEnd(), config.getType(), event.getId(), scope);
				} else {
					this.removeFromEventMap(itemName, event.getId(), scope);		
				}
			}
		}
	}
	
	
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// get binding provider
		CalDavBindingProvider provider = getCalDavBindingProvider();
		if (provider == null) {
			logger.error("cannot find any provider");
			return;
		}
		
		CalDavNextEventConfig config = provider.getConfig(itemName);
		if (config == null) {
			logger.error("no config found for item {}", itemName);
			return;
		}
		
		if (config.getType() != CalDavType.DISABLE) {
			logger.error("can just use commands for type=DISABLE");
			return;
		}
		
		if (!(command instanceof OnOffType)) {
			logger.error("invalid command for DISABLE (just SwitchItems allowed)");
			return;
		}
		
		if (command == OnOffType.ON) {
			logger.info("execution for '{}' disabled", config.getItemNameToListenTo());
			this.disabledItems.add(config.getItemNameToListenTo());
		} else if (command == OnOffType.OFF) {
			logger.info("execution for '{}' enabled", config.getItemNameToListenTo());
			this.disabledItems.remove(config.getItemNameToListenTo());
		}
	}

	@Override
	public void eventRemoved(CalDavEvent event) {
		this.handleForEventMap(event, SCOPE_BEGIN, false);
		this.handleForEventMap(event, SCOPE_END, false);
	}

	@Override
	public void eventLoaded(CalDavEvent event) {
		this.handleForEventMap(event, SCOPE_BEGIN, true);
		this.handleForEventMap(event, SCOPE_END, true);
	}
	
	@Override
	public void eventChanged(CalDavEvent event) {
		this.handleForEventMap(event, SCOPE_BEGIN, false);
		this.handleForEventMap(event, SCOPE_END, false);
		
		this.handleForEventMap(event, SCOPE_BEGIN, true);
		this.handleForEventMap(event, SCOPE_END, true);
	}

	@Override
	public void eventBegins(CalDavEvent event) {
		this.doAction(event, SCOPE_BEGIN);
	}

	@Override
	public void eventEnds(CalDavEvent event) {
		this.doAction(event, SCOPE_END);
	}
	
	private void doAction(CalDavEvent event, String scope) {
		if (!readCalendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (event.getContent() == null) {
			logger.warn("no content for event: {}", event.getShortName());
			return;
		}
		
		String content = parseContent(event.getContent(), scope);
		
		if (content.trim().isEmpty()) {
			return;
		}
		String[] commands = content.split(",");
		for (String itemCommand : commands) {
			String[] commandSplit = itemCommand.split(":");
			String itemName = commandSplit[0];
			String commandString = commandSplit[1];
			
			if (this.disabledItems.contains(itemName)) {
				logger.info("execution for item '{}' disabled, do nothing");
			} else {
				logger.info("executing command '{}' for item '{}'", commandString, itemName);
				
				Item item = null;
				try {
					item = itemRegistry.getItem(itemName);
				} catch (ItemNotFoundException e) {
					logger.error("item '" + itemName + "' could not be found");
					return;
				}
				Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
				eventPublisher.postCommand(itemName, command);
			}
			
			this.removeFromEventMap(itemName, event.getId(), scope);
		}
	}
	
	private void removeFromEventMap(String itemName, String eventId, String scope) {
		for (Entry<CalDavNextEventConfig, List<NextEventContainer>> entry : this.itemNextEventMap.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				NextEventContainer container = entry.getValue().get(i);
				if (itemName.equals(entry.getKey().getItemNameToListenTo())
						&& container.getEventId().equals(eventId)
						&& container.getScope().equals(scope)) {
					

					logger.trace("remove: {}", entry.getValue().get(i).getEventId());
					entry.getValue().remove(i);
//					i--;
					
					for (int j = 0; j < entry.getValue().size(); j++) {
						logger.trace("{}: {}", j, entry.getValue().get(j).getEventId());
					}
					
					this.updateItemState(entry.getKey().getItemName());
				}
			}
		}
	}
	
	private void addToEventMap(CalDavNextEventConfig config, String itemName, String command, 
			Date changeDate, CalDavType type, String eventId, String scope) {
		if (!this.itemNextEventMap.containsKey(config)) {
			logger.trace("creating initial list for config: {}", config.getItemName());
			this.itemNextEventMap.put(config, new ArrayList<NextEventContainer>());
		}
		List<NextEventContainer> eventList = this.itemNextEventMap.get(config);
		
		NextEventContainer container = new NextEventContainer();
		container.setCommand(command);
		container.setChangeDate(changeDate);
		container.setType(type);
		container.setEventId(eventId);
		container.setScope(scope);
		eventList.add(container);
		
		this.updateItemState(config.getItemName());
	}
	
	private void updateItemState(String itemName) {
		logger.trace("update item state for item: {}", itemName);
		
		List<NextEventContainer> containerList = new ArrayList<NextEventContainer>();
		for (Entry<CalDavNextEventConfig, List<NextEventContainer>> entry : this.itemNextEventMap.entrySet()) {
			if (entry.getKey().getItemName().equals(itemName)) {
				// handle this
				containerList = entry.getValue();
			}
		}
		
		Collections.sort(containerList, new Comparator<NextEventContainer>() {
			@Override
			public int compare(NextEventContainer o1, NextEventContainer o2) {
				return o2.getChangeDate().compareTo(o1.getChangeDate());
			}
		});
		
		logger.trace("list all events for item: {}", itemName);
		int i = 0;
		for (NextEventContainer c : containerList) {
			logger.info("{}: {} -> {}", i++, c.getChangeDate(), c.getCommand());
		}
		
		if (containerList.size() == 0) {
			return;
		}
		
		NextEventContainer container = containerList.get(containerList.size() - 1);
		
		CalDavType type = container.getType();
		logger.trace("handling event of type: {}", type);
		if (type == CalDavType.VALUE) {
			Command c = null;
			try {
				c = TypeParser.parseCommand(itemRegistry.getItem(itemName).getAcceptedCommandTypes(), container.getCommand());
			} catch (ItemNotFoundException e) {
				logger.error("cannot find item: {}", itemName);
				return;
			}
			logger.debug("setting value for '{}' to: {}", itemName, c);
			eventPublisher.postCommand(itemName, c);
		} else if (type == CalDavType.DATE) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(container.getChangeDate());
			State c = new DateTimeType(cal);
			logger.debug("setting value for '{}' to: {}", itemName, c);
			eventPublisher.postUpdate(itemName, c);
		} else {
			logger.warn("unhandled type: " + type);
		}
	}
	
	class NextEventContainer {
		private String command;
		private Date changeDate;
		private CalDavType type;
		private String eventId;
		private String scope;
		
		public String getCommand() {
			return command;
		}
		
		public void setCommand(String command) {
			this.command = command;
		}


		public Date getChangeDate() {
			return changeDate;
		}

		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}
		
		public CalDavType getType() {
			return type;
		}

		public void setType(CalDavType type) {
			this.type = type;
		}
		
		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getScope() {
			return scope;
		}

		public void setScope(String scope) {
			this.scope = scope;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((changeDate == null) ? 0 : changeDate.hashCode());
			result = prime * result
					+ ((command == null) ? 0 : command.hashCode());
			result = prime * result
					+ ((eventId == null) ? 0 : eventId.hashCode());
			result = prime * result + ((scope == null) ? 0 : scope.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NextEventContainer other = (NextEventContainer) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (changeDate == null) {
				if (other.changeDate != null)
					return false;
			} else if (!changeDate.equals(other.changeDate))
				return false;
			if (command == null) {
				if (other.command != null)
					return false;
			} else if (!command.equals(other.command))
				return false;
			if (eventId == null) {
				if (other.eventId != null)
					return false;
			} else if (!eventId.equals(other.eventId))
				return false;
			if (scope == null) {
				if (other.scope != null)
					return false;
			} else if (!scope.equals(other.scope))
				return false;
			if (type != other.type)
				return false;
			return true;
		}

		private CalDavBinding getOuterType() {
			return CalDavBinding.this;
		}

		
	}
}
