/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.caldav.internal;

import static org.openhab.persistence.caldav.internal.CaldavConfiguration.calendarId;
import static org.openhab.persistence.caldav.internal.CaldavConfiguration.duration;
import static org.openhab.persistence.caldav.internal.CaldavConfiguration.singleEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.CalDavQuery;
import org.openhab.io.caldav.EventUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a {@link PersistenceService} implementation using calDAV.
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CaldavPersistenceService implements QueryablePersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(CaldavPersistenceService.class);
	
	private static final String SERVICE_NAME = "caldav";
	
	private CalDavLoader calDavLoader;
	private ItemRegistry itemRegistry;
	
	public String getName() {
		return SERVICE_NAME;
	}
	
	public void setCalDavLoader(CalDavLoader calDavLoader) {
		this.calDavLoader = calDavLoader;
	}
	
	public void unsetCalDavLoader() {
		this.calDavLoader = null;
	}
	
	public void activate() {
	}

	public void deactivate() {
	}
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	public void store(Item item) {
		store(item, null);
	}
	
	private CaldavItem findLastOn(String alias, State state) {
		final FilterCriteria filter = new FilterCriteria();
		filter.setEndDate(new Date());
		filter.setItemName(alias);
		filter.setOrdering(FilterCriteria.Ordering.DESCENDING);
		filter.setPageSize(1);
		final Iterable<HistoricItem> query = this.query(filter);
		final Iterator<HistoricItem> iterator = query.iterator();
		
		if (iterator.hasNext()) {
			CaldavItem caldavItem = (CaldavItem) iterator.next();
			if (!isOff(caldavItem.getState())) {
				return caldavItem;
			}
		}
		return null;
	}

	public void store(Item item, String alias) {
		if(item.getState() instanceof UnDefType) {
			return;
		}
		
		if(alias==null) alias = item.getName();
		State state = item.getState();
		logger.trace("persisting item: {}", item);
		
		if (!singleEvents) {
			// try to create events with correct ON OFF duration
			final CaldavItem lastOn = this.findLastOn(alias, state);
			if (lastOn != null) {
				if (isOff(item.getState())) {
					CalDavEvent event = lastOn.getEvent();
					event.setLastChanged(DateTime.now());
					String offContent = EventUtils.createEnd(alias, state);
					event.setContent(event.getContent() + "\n" + offContent);
					event.setEnd(DateTime.now());
					logger.debug("existing event found, updated for persistence: {}", event);
					this.calDavLoader.addEvent(event);
				} else {
					CalDavEvent event = lastOn.getEvent();
					event.setLastChanged(DateTime.now());
					
					String offContent = EventUtils.createBetween(alias, state);
					event.setContent(event.getContent() + "\n" + offContent);
					logger.debug("existing event found, updated for persistence: {}", event);
					this.calDavLoader.addEvent(event);
				}
				return;
			}
		}
		
		logger.debug("creating new event");
		CalDavEvent event = new CalDavEvent();
		final String id = UUID.randomUUID().toString();
		event.setId(id);
		event.setName(alias);
		event.setLastChanged(DateTime.now());
		event.setContent(EventUtils.createBegin(alias, state));
		DateTime now = DateTime.now();
		event.setStart(now);
		event.setEnd(now.plusMinutes(duration));
		event.setCalendarId(calendarId);
		event.setFilename("openHAB-" + id);
		logger.debug("new event for persistence created: {}", event);
		this.calDavLoader.addEvent(event);
	}

	private boolean isOff(State state) {
		if (state instanceof PercentType 
				&& state.equals(new PercentType(0))) {
			return true;
		} else if (state instanceof OnOffType
				&& state.equals(OnOffType.OFF)) {
			return true;
		} else if (state instanceof OpenClosedType
				&& state.equals(OpenClosedType.CLOSED)) {
			return true;
		}
		return false;
	}

	public Iterable<HistoricItem> query(final FilterCriteria filter) {
		List<CalDavEvent> events = calDavLoader.getEvents(new CalDavQuery(calendarId));
		List<HistoricItem> outList = new ArrayList<HistoricItem>();
		
		for (CalDavEvent calDavEvent : events) {
			if (filter.getBeginDate() != null
					&& calDavEvent.getEnd().toDate().before(filter.getBeginDate())) {
				continue;
			}
			if (filter.getEndDate() != null
					&& calDavEvent.getStart().toDate().after(filter.getEndDate())) {
				continue;
			}
			
			Item item = null;
			try {
				item = this.itemRegistry.getItem(filter.getItemName());
			} catch (ItemNotFoundException e) {
				logger.error("item {} could not be found", filter.getItemName());
				continue;
			}
			
			final List<EventUtils.EventContent> parseContent = EventUtils.parseContent(calDavEvent, item);
			for (EventUtils.EventContent eventContent : parseContent) {
				if (filter.getBeginDate() != null
						&& eventContent.getTime().toDate().before(filter.getBeginDate())) {
					continue;
				}
				if (filter.getEndDate() != null
						&& eventContent.getTime().toDate().after(filter.getEndDate())) {
					continue;
				}
				
				final State eventState = eventContent.getState();
				
				if (filter.getState() != null && filter.getOperator() != null) {
					switch (filter.getOperator()) {
						case EQ: {
							if (!filter.getState().equals(eventState)) continue;
							break;
						}
						case NEQ: {
							if (filter.getState().equals(eventState)) continue;
							break;
						}
						case LTE: {
							if (eventState instanceof DecimalType
									&& filter.getState() instanceof DecimalType) {
								if (((DecimalType) eventState).longValue() > ((DecimalType) filter.getState()).longValue()) {
									continue;
								}
							} else {
								continue;
							}
							break;
						}
						case GTE: {
							if (eventState instanceof DecimalType
									&& filter.getState() instanceof DecimalType) {
								if (((DecimalType) eventState).longValue() < ((DecimalType) filter.getState()).longValue()) {
									continue;
								}
							} else {
								continue;
							}
							break;
						}
						case LT: {
							if (eventState instanceof DecimalType
									&& filter.getState() instanceof DecimalType) {
								if (((DecimalType) eventState).longValue() >= ((DecimalType) filter.getState()).longValue()) {
									continue;
								}
							} else {
								continue;
							}
							break;
						}
						case GT: {
							if (eventState instanceof DecimalType
									&& filter.getState() instanceof DecimalType) {
								if (((DecimalType) eventState).longValue() <= ((DecimalType) filter.getState()).longValue()) {
									continue;
								}
							} else {
								continue;
							}
							break;
						}
					}
				}
				
				// just filtered events are here...
				final CaldavItem caldavItem = new CaldavItem(filter.getItemName(), eventState, eventContent.getTime().toDate());
				caldavItem.setEvent(calDavEvent);
				outList.add(caldavItem);
			}
		}
		
		Collections.sort(outList, new Comparator<HistoricItem>() {
			@Override
			public int compare(HistoricItem arg0, HistoricItem arg1) {
				if (filter.getOrdering().equals(FilterCriteria.Ordering.ASCENDING)) {
					return (int) (arg0.getTimestamp().getTime() - arg1.getTimestamp().getTime());	
				} else {
					return (int) (arg1.getTimestamp().getTime() - arg0.getTimestamp().getTime());
				}
				
			}
		});
		
		if (outList.size() < filter.getPageNumber() * filter.getPageSize()) {
			return Collections.emptyList();
		}

		outList = outList.subList(filter.getPageNumber() * filter.getPageSize(), Math.min((filter.getPageNumber() * filter.getPageSize()) + filter.getPageSize(), outList.size()));
		logger.trace("result size for query: {}", outList.size());
		
		return outList;
	}
}
