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
package org.openhab.core.persistence.actions;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joda.time.base.AbstractInstant;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending persistence requests
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class PersistenceExtensions implements ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(PersistenceExtensions.class);
	
	private static Map<String, PersistenceService> services = new HashMap<String, PersistenceService>();
	private static String defaultService = null; 
	
	public PersistenceExtensions() {
		// default constructor, necessary for osgi-ds
	}
	
	public void addPersistenceService(PersistenceService service) {
		services.put(service.getName(), service);
	}
	
	public void removePersistenceService(PersistenceService service) {
		services.remove(service.getName());
	}
	
	
	/**
	 * Persists the state of a given <code>item</code> through a {@link PersistenceService} identified
	 * by the <code>serviceName</code>. 
	 * 
	 * @param item the item to store
	 * @param serviceName the name of the {@link PersistenceService} to use
	 */
	static public void persist(Item item, String serviceName) {
		PersistenceService service = services.get(serviceName);
		if (service != null) {
			service.store(item);
		} else {
			logger.warn("There is no persistence service registered with the name '{}'", serviceName);
		}
	} 

	/**
	 * Persists the state of a given <code>item</code> through the default persistence service. 
	 * 
	 * @param item the item to store
	 */
	static public void persist(Item item) {
		if(defaultService!=null) {
			persist(item, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
		}
	} 

	static public State historicState(Item item, AbstractInstant timestamp) {
		if(defaultService!=null) {
			return historicState(item, timestamp, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return UnDefType.NULL;
		}
	}

	static public State historicState(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		if(result.iterator().hasNext()) {
			return result.iterator().next().getState();
		} else {
			return UnDefType.NULL;
		}
	} 

	static public Boolean changedSince(Item item, AbstractInstant timestamp) {
		if(defaultService!=null) {
			return changedSince(item, timestamp, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return null;
		}
	}

	static public Boolean changedSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		while(it.hasNext()) {
			HistoricItem hItem = it.next();
			if(!hItem.getState().equals(item.getState())) {
				return true;
			}
		}
		return false;
	} 

	static public Boolean updatedSince(Item item, AbstractInstant timestamp) {
		if(defaultService!=null) {
			return updatedSince(item, timestamp, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return null;
		}
	}

	static public Boolean updatedSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		if(result.iterator().hasNext()) {
			return true;
		} else {
			return false;
		}
	} 

	static public DecimalType maximumSince(Item item, AbstractInstant timestamp) {
		if(defaultService!=null) {
			return maximumSince(item, timestamp, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return null;
		}
	}

	static public DecimalType maximumSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		DecimalType maximum = null;
		while(it.hasNext()) {
			State state = it.next().getState();
			if (state instanceof DecimalType) {
				DecimalType value = (DecimalType) state;
				if(maximum==null || value.compareTo(maximum)>0) {
					maximum = value;
				}
			}
		}
		return maximum;
	} 

	static public DecimalType minimumSince(Item item, AbstractInstant timestamp) {
		if(defaultService!=null) {
			return minimumSince(item, timestamp, defaultService);
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return null;
		}
	}

	static public DecimalType minimumSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		DecimalType minimum = null;
		while(it.hasNext()) {
			State state = it.next().getState();
			if (state instanceof DecimalType) {
				DecimalType value = (DecimalType) state;
				if(minimum==null || value.compareTo(minimum)<0) {
					minimum = value;
				}
			}
		}
		return minimum;
	} 

	static private Iterable<HistoricItem> getAllStatesSince(Item item, AbstractInstant timestamp, String serviceName) {
		PersistenceService service = services.get(serviceName);
		if (service instanceof QueryablePersistenceService) {
			QueryablePersistenceService qService = (QueryablePersistenceService) service;
			FilterCriteria filter = new FilterCriteria();
			filter.setBeginDate(timestamp.toDate());
			filter.setItemName(item.getName());
			return qService.query(filter);
		} else {
			logger.warn("There is no queryable persistence service registered with the name '{}'", serviceName);
			return Collections.emptySet();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {
			PersistenceExtensions.defaultService = (String) config.get("default");			
		}
	}

}
