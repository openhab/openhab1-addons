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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.binding.caldav_command.CalDavBindingProvider;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.CalDavQuery;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.caldav.EventUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implementation of the caldav command binding.
 * Every event which is loaded from the server can be triggered with 4 notifications.
 * <br/>
 * All events which are loaded must fulfill a name syntax for the description.
 * All other fields of a event are free to choose.
 * 
 * <pre>
 * Sample configuration for event description:
 * BEGIN:Livingroom_Heater:23
 * END:Livingroom_Heater:16
 * => Meaning: when the event starts the heater turns on to 23 and when the event ends
 * it turns back to 16
 * 
 * <br/>
 * Sample configuration for event description:
 * BEGIN:Kitchen_Music:ON
 * END:Kitchen_Music:OFF
 * => Meaning: when the event starts the music turns on and when the event ends
 * it turns off
 * </pre>
 *
 * @see org.openhab.io.caldav.EventNotifier 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavBinding extends AbstractBinding<CalDavBindingProvider> implements ManagedService, EventNotifier {
	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	private static final String KEY_READ_CALENDARS = "readCalendars";

	private static final Logger logger = 
		LoggerFactory.getLogger(CalDavBinding.class);
	
	private ItemRegistry itemRegistry;
	
	private CalDavLoader calDavLoader;
	
	private List<String> readCalendars = new ArrayList<String>();
	
	private List<String> disabledItems = new ArrayList<String>();

	private boolean calendarReloaded;
	
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
	
	public void deactivate() { }

	
	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties != null) {
			logger.debug("reading configuration data...");
			String read = (String) properties.get(KEY_READ_CALENDARS);
			if (read != null) {
				for (String value : read.split(",")) {
					this.readCalendars.add(value.trim());
				}
			}
			this.reloadCurrentLoadedEvents();
		}
	}
	
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		for (String itemName : provider.getItemNames()) {
			this.bindingChanged(provider, itemName);
		}
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider.getItemNames().contains(itemName)) {
			final CalDavNextEventConfig config = ((CalDavBindingProvider) provider).getConfig(itemName);
			List<CalDavEvent> events = calDavLoader.getEvents(new CalDavQuery(this.readCalendars, DateTime.now()));
			this.updateItemState(config, events);
		}
	}

	private void reloadCurrentLoadedEvents() {
		if (this.calDavLoader == null) {
			return;
		}
		for (String calendarKey : this.readCalendars) {
			calendarReloaded(calendarKey);
		}
	}
	
	private CalDavBindingProvider getCalDavBindingProvider() {
		for (CalDavBindingProvider provider : providers) {
			return provider;
		}
		return null;
	}
	
	private synchronized void handleForEventPreview() {
		CalDavBindingProvider provider = getCalDavBindingProvider();
		if (provider == null) {
			logger.error("cannot find any provider");
			return;
		}
		
		List<CalDavEvent> events = calDavLoader.getEvents(new CalDavQuery(this.readCalendars, DateTime.now()));
		
		for (String configItemName : provider.getItemNames()) {
			final CalDavNextEventConfig config = provider.getConfig(configItemName);
			this.updateItemState(config, events);
		}
	}
	
	
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (!(command instanceof OnOffType)) {
			logger.trace("invalid command for DISABLE (just SwitchItems allowed)");
			return;
		}
		
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
			logger.trace("can just use commands for type=DISABLE");
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
	}

	@Override
	public void eventLoaded(CalDavEvent event) {
	}
	
	@Override
	public void eventBegins(CalDavEvent event) {
		if (!readCalendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (this.itemRegistry == null) {
			logger.error("item registry is not set");
			return;
		}
		
		this.doAction(event, EventUtils.SCOPE_BEGIN);
		this.handleForEventPreview();
	}

	@Override
	public void eventEnds(CalDavEvent event) {
		if (!readCalendars.contains(event.getCalendarId())) {
			return;
		}
		
		if (this.itemRegistry == null) {
			logger.error("item registry is not set");
			return;
		}
		
		this.doAction(event, EventUtils.SCOPE_END);
		this.handleForEventPreview();
	}
	
	@Override
	public void calendarReloaded(String calendarId) {
		if (!readCalendars.contains(calendarId)) {
			return;
		}
		
		if (this.itemRegistry == null) {
			logger.error("item registry is not set");
			return;
		}
		
		if (!this.calendarReloaded) {
			this.doActionInitial();
		}
		this.handleForEventPreview();
		
		this.calendarReloaded = true;
	}
	
	private void doActionInitial() {
		List<CalDavEvent> events = calDavLoader.getEvents(new CalDavQuery(this.readCalendars, DateTime.now(), DateTime.now()));
		Map<String, EventUtils.EventContent> map = new HashMap<String, EventUtils.EventContent>();
		for (CalDavEvent calDavEvent : events) {
			final List<EventUtils.EventContent> parseContent = EventUtils.parseContent(calDavEvent, this.itemRegistry, null);
			for (EventUtils.EventContent eventContent : parseContent) {
				if (disabledItems.contains(eventContent.getItem().getName())) {
					// changing this is item is disabled, do not change it
					continue;
				}
				
				EventUtils.EventContent currentEventContent = map.get(eventContent.getItem().getName());
				if (eventContent.getTime().isBefore(DateTime.now())
						&& (currentEventContent == null || eventContent.getTime().isAfter(currentEventContent.getTime()))) {
					map.put(eventContent.getItem().getName(), eventContent);
				}
			}
		}
		
		for (EventUtils.EventContent currentEventContent : map.values()) {
			eventPublisher.sendCommand(currentEventContent.getItem().getName(), currentEventContent.getCommand());
			logger.debug("setting initial value for {} to {}", currentEventContent.getItem().getName(), currentEventContent.getCommand());
		}
	}
	
	private void doAction(CalDavEvent event, String scope) {
		final List<EventUtils.EventContent> parseContent = EventUtils.parseContent(event, this.itemRegistry, scope);
		for (EventUtils.EventContent eventContent : parseContent) {
			if (disabledItems.contains(eventContent.getItem().getName())) {
				continue;
			}
			
			eventPublisher.sendCommand(eventContent.getItem().getName(), eventContent.getCommand());
		}
	}
	
	private void updateItemState(CalDavNextEventConfig config, List<CalDavEvent> events) {
		String itemName = config.getItemNameToListenTo();
		String itemNamePreview = config.getItemName();
		logger.trace("update item state for item: {}", itemName);
		
		Command state = null;
		DateTime time = null;
		
		if (calDavLoader == null) {
			logger.warn("caldav loader is not set");
			return;
		}
		
		for (CalDavEvent calDavEvent : events) {
			try {
				final Item item = this.itemRegistry.getItem(itemName);
				
				final List<EventUtils.EventContent> parseContent = EventUtils.parseContent(calDavEvent, item);
				for (EventUtils.EventContent eventContent : parseContent) {
					if (!eventContent.getTime().isBefore(DateTime.now()) 
							&& (time == null || time.isAfter(eventContent.getTime()))) {
						time = eventContent.getTime();
						state = eventContent.getCommand();
					}
				}
			} catch (ItemNotFoundException e) {
				logger.error("item {} could not be found", itemName);
			}
		}
		
		if (time == null) {
			// no item found
			eventPublisher.postUpdate(itemNamePreview, org.openhab.core.types.UnDefType.UNDEF);
			return;
		}
		
		CalDavType type = config.getType();
		logger.trace("handling event of type: {}", type);
		if (type == CalDavType.VALUE) {
			logger.debug("setting value for '{}' to: {}", itemNamePreview, state);
			eventPublisher.sendCommand(itemNamePreview, state);
		} else if (type == CalDavType.DATE) {
			Command c = new DateTimeType(FORMATTER.print(time));
			logger.debug("setting value for '{}' to: {}", itemNamePreview, c);
			eventPublisher.sendCommand(itemNamePreview, c);
		} else if (type == CalDavType.DISABLE) {
			// nothing to do
			return;
		} else {
			logger.warn("unhandled type: {}", type);
		}
	}
}
