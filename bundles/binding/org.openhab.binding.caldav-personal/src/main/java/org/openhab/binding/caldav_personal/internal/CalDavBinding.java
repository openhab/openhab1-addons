/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_personal.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.binding.caldav_personal.CalDavBindingProvider;
import org.openhab.binding.caldav_personal.internal.CalDavConfig.Type;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.CalDavQuery;
import org.openhab.io.caldav.CalDavQuery.Sort;
import org.openhab.io.caldav.EventNotifier;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps events from calDAV to items. Can be used to show personal events in sitemaps.
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavBinding extends AbstractBinding<CalDavBindingProvider> implements ManagedService, EventNotifier {

    private static final String PARAM_HOME_IDENTIFIERS = "homeIdentifiers";
    private static final String PARAM_USED_CALENDARS = "usedCalendars";
    private static final DateTimeFormatter DF = DateTimeFormat.shortDateTime();
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(CalDavBinding.class);

    private CalDavLoader calDavLoader;

    private List<String> calendars = new ArrayList<String>();
    private List<String> homeIdentifier = new ArrayList<String>();

    public CalDavBinding() {
    }

    public void setCalDavLoader(CalDavLoader calDavLoader) {
        logger.debug("setting CalDavLoader: {}", calDavLoader != null);
        this.calDavLoader = calDavLoader;
        this.calDavLoader.addListener(this);
    }

    public void unsetCalDavLoader(CalDavLoader calDavLoader) {
        this.calDavLoader.removeListener(this);
        this.calDavLoader = null;
    }

    @Override
    public void activate() {
        logger.debug("CalDavBinding (personal) activated");
    }

    @Override
    public void deactivate() {
        if (this.calDavLoader != null) {
            this.calDavLoader.removeListener(this);
        }
    }

    protected void addBindingProvider(CalDavBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(CalDavBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties != null) {
            logger.debug("loading configuration...");
            String usedCalendars = (String) properties.get(PARAM_USED_CALENDARS);
            if (usedCalendars != null) {
                for (String cal : usedCalendars.split(",")) {
                    this.calendars.add(cal.trim());
                }
            }

            String homeIdentifiers = (String) properties.get(PARAM_HOME_IDENTIFIERS);
            if (homeIdentifiers != null) {
                for (String homeIdent : homeIdentifiers.split(",")) {
                    this.homeIdentifier.add(homeIdent.trim().toLowerCase());
                }
            }
            logger.debug("loading configuration done");
            this.reloadCurrentLoadedEvents();
        }

    }

    private void reloadCurrentLoadedEvents() {
        try {
            if (this.calDavLoader == null) {
                return;
            }
            logger.trace("reloading events");
            for (String calendarKey : this.calendars) {
                calendarReloaded(calendarKey);
            }
        } catch (Exception e) {
            logger.error("cannot load events", e);
        }
    }

    @Override
    public void allBindingsChanged(BindingProvider provider) {
        this.updateItemsForEvent();
        super.allBindingsChanged(provider);
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        if (!(provider instanceof CalDavBindingProvider)) {
            return;
        }
        CalDavConfig config = ((CalDavBindingProvider) provider).getConfig(itemName);
        if (config == null) {
            return;
        }
        final List<CalDavEvent> events = this.calDavLoader.getEvents(getQueryForConfig(config));
        this.updateItem(itemName, config, events);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventRemoved(CalDavEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventLoaded(CalDavEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventBegins(CalDavEvent event) {
        logger.debug("eventBegins() called for event '{}'", event.getShortName());

        if (!calendars.contains(event.getCalendarId())) {
            logger.trace("event calendarId does not exist in calendars");
            return;
        }

        logger.debug("check start of event '{}'", event.getShortName());
        if (event.getStart().isBeforeNow()) {
            logger.debug("event is before now; ignoring and updating anyway.");
            logger.trace("event start time: {} ", event.getStart());
            logger.trace("now:              {} ", DateTime.now());
        }

        logger.debug("adding event to map: {}", event.getShortName());

        this.updateItemsForEvent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventEnds(CalDavEvent event) {
        if (!calendars.contains(event.getCalendarId())) {
            return;
        }

        logger.debug("remove event from map: {}", event.getShortName());

        this.updateItemsForEvent();
    }

    @Override
    public void calendarReloaded(String calendarId) {
        if (!calendars.contains(calendarId)) {
            return;
        }

        logger.debug("calendar reloaded: {}", calendarId);

        this.updateItemsForEvent();
    }

    private void updateItemsForEvent() {
        if (calDavLoader == null) {
            logger.debug("calDavLoader not available. Unable to update items for event.");
            return;
        }

        CalDavBindingProvider bindingProvider = null;

        for (CalDavBindingProvider bindingProvider_ : this.providers) {
            bindingProvider = bindingProvider_;
        }

        if (bindingProvider == null) {
            logger.warn("No binding provider found");
            return;
        }

        Map<Integer, List<CalDavEvent>> eventCache = new HashMap<Integer, List<CalDavEvent>>();

        for (String item : bindingProvider.getItemNames()) {
            CalDavConfig config = bindingProvider.getConfig(item);
            List<CalDavEvent> events = eventCache.get(config.getUniqueEventListKey());
            if (events == null) {
                logger.debug("No events found in event cache for item '{}'. Attempting to load from calendar.", item);
                CalDavQuery query = getQueryForConfig(config);
                events = calDavLoader.getEvents(query);
                eventCache.put(config.getUniqueEventListKey(), events);
            }

            if (events == null) {
                logger.debug("No events found for item '{}'. Nothing to update.", item);
                continue;
            }
            this.updateItem(item, config, events);
        }
    }

    private CalDavQuery getQueryForConfig(CalDavConfig config) {
        CalDavQuery query = new CalDavQuery(config.getCalendar(), DateTime.now(), Sort.ASCENDING);
        query.setFilterName(config.getFilterName());
        query.setFilterCategory(config.getFilterCategory());
        query.setFilterCategoryMatchesAny(config.getCategoriesFiltersAny());
        return query;
    }

    private synchronized void updateItem(String itemName, CalDavConfig config, List<CalDavEvent> events) {
        if (config.getType() == Type.PRESENCE) {
            List<CalDavEvent> subList = getActiveEvents(events);
            subList = this.removeWithMatchingPlace(subList);
            if (subList.size() == 0) {
                eventPublisher.sendCommand(itemName, OnOffType.OFF);
            } else {
                eventPublisher.sendCommand(itemName, OnOffType.ON);
            }
        } else {
            List<CalDavEvent> subList = new ArrayList<CalDavEvent>();
            if (config.getType() == Type.EVENT) {
                subList = getAllEvents(events);
            } else if (config.getType() == Type.ACTIVE) {
                subList = getActiveEvents(events);
            } else if (config.getType() == Type.UPCOMING) {
                subList = getUpcomingEvents(events);
            }

            if (config.getEventNr() > subList.size()) {
                logger.debug("no event found for {}, setting to UNDEF", itemName);
                eventPublisher.postUpdate(itemName, org.openhab.core.types.UnDefType.UNDEF);
                return;
            }

            logger.debug("found {} events for config: {}", subList.size(), config);

            CalDavEvent event = subList.get(config.getEventNr() - 1);
            logger.trace("found event {} for config {}", event.getShortName(), config);
            State command = null;

            switch (config.getValue()) {
                case NAME:
                    command = new StringType(event.getName());
                    break;
                case DESCRIPTION:
                    command = new StringType(event.getContent());
                    break;
                case PLACE:
                    command = new StringType(event.getLocation());
                    break;
                case START:
                    command = new DateTimeType(FORMATTER.print(event.getStart()));
                    break;
                case END:
                    command = new DateTimeType(FORMATTER.print(event.getEnd()));
                    break;
                case TIME:
                    String startEnd = DF.print(event.getStart()) + " - " + DF.print(event.getEnd());
                    command = new StringType(startEnd);
                    break;
                case NAMEANDTIME:
                    String startEnd2 = DF.print(event.getStart()) + " - " + DF.print(event.getEnd());
                    String name = event.getName();
                    command = new StringType(name + " @ " + startEnd2);
            }

            logger.debug("sending command {} for item {}", command, itemName);
            eventPublisher.postUpdate(itemName, command);
            logger.trace("command {} successfully sent", command);
        }
    }

    private List<CalDavEvent> getActiveEvents(List<CalDavEvent> events) {
        List<CalDavEvent> subList = new ArrayList<CalDavEvent>();
        for (CalDavEvent event : events) {
            if (!(event.getStart().isBeforeNow() && event.getEnd().isAfterNow())) {
                continue;
            }

            subList.add(event);
        }
        return subList;
    }

    private List<CalDavEvent> getUpcomingEvents(List<CalDavEvent> events) {
        List<CalDavEvent> subList = new ArrayList<CalDavEvent>();
        for (CalDavEvent event : events) {
            if (event.getStart().isBeforeNow()) {
                continue;
            }

            subList.add(event);
        }
        return subList;
    }

    private List<CalDavEvent> getAllEvents(List<CalDavEvent> events) {
        List<CalDavEvent> subList = new ArrayList<CalDavEvent>();
        for (CalDavEvent event : events) {
            subList.add(event);
        }
        return subList;
    }

    private List<CalDavEvent> removeWithMatchingPlace(List<CalDavEvent> list) {
        List<CalDavEvent> out = new ArrayList<CalDavEvent>();
        for (CalDavEvent event : list) {
            if (this.homeIdentifierMatch(event.getLocation())) {
                continue;
            }
            out.add(event);
        }
        return out;
    }

    private boolean homeIdentifierMatch(String location) {
        if (location == null) {
            return false;
        }

        boolean match = this.homeIdentifier.contains(location.toLowerCase());
        if (match) {
            return true;
        }

        for (String homeIdentifier : this.homeIdentifier) {
            if (location.contains(homeIdentifier)) {
                return true;
            }
        }

        return false;
    }
}
