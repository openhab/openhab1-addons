/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.extensions;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for using persistence services
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @author Chris Jackson
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
		if(isDefaultServiceAvailable()) {
			persist(item, defaultService);
		}
	} 

	/**
	 * Retrieves the state of a given <code>item</code> to a certain point in time through the default persistence service. 
	 * 
	 * @param item the item to retrieve the state for
	 * @param the point in time for which the state should be retrieved 
	 * @return the item state at the given point in time
	 */
	static public HistoricItem historicState(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return historicState(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Retrieves the state of a given <code>item</code> to a certain point in time through  a {@link PersistenceService} identified
	 * by the <code>serviceName</code>. 
	 * 
	 * @param item the item to retrieve the state for
	 * @param the point in time for which the state should be retrieved 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the item state at the given point in time
	 */
	static public HistoricItem historicState(Item item, AbstractInstant timestamp, String serviceName) {
		PersistenceService service = services.get(serviceName);
		if (service instanceof QueryablePersistenceService) {
			QueryablePersistenceService qService = (QueryablePersistenceService) service;
			FilterCriteria filter = new FilterCriteria();
			filter.setEndDate(timestamp.toDate());
			filter.setItemName(item.getName());
			filter.setPageSize(1);
			filter.setOrdering(Ordering.DESCENDING);
			Iterable<HistoricItem> result = qService.query(filter);
			if(result.iterator().hasNext()) {
				return result.iterator().next();
			} else {
				return null;
			}
		} else {
			logger.warn("There is no queryable persistence service registered with the name '{}'", serviceName);
			return null;
		}
	} 

	/**
	 * Checks if the state of a given <code>item</code> has changed since a certain point in time. 
	 * The default persistence service is used. 
	 * 
	 * @param item the item to check for state changes
	 * @param the point in time to start the check 
	 * @return true, if item state had changed
	 */
	static public Boolean changedSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return changedSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Checks if the state of a given <code>item</code> has changed since a certain point in time. 
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used. 
	 * 
	 * @param item the item to check for state changes
	 * @param the point in time to start the check 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return true, if item state had changed
	 */
	static public Boolean changedSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		HistoricItem itemThen = historicState(item, timestamp);
		if(itemThen == null) {
			// Can't get the state at the start time
			// If we've got results more recent that this, it must have changed
			return(it.hasNext());
		}

		State state = itemThen.getState();
		while(it.hasNext()) {
			HistoricItem hItem = it.next();
			if(state!=null && !hItem.getState().equals(state)) {
				return true;
			}
			state = hItem.getState();
		}
		return false;
	} 

	/**
	 * Checks if the state of a given <code>item</code> has been updated since a certain point in time. 
	 * The default persistence service is used. 
	 * 
	 * @param item the item to check for state updates
	 * @param the point in time to start the check 
	 * @return true, if item state was updated
	 */
	static public Boolean updatedSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return updatedSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Checks if the state of a given <code>item</code> has changed since a certain point in time. 
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used. 
	 * 
	 * @param item the item to check for state changes
	 * @param the point in time to start the check 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return true, if item state was updated
	 */
	static public Boolean updatedSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		if(result.iterator().hasNext()) {
			return true;
		} else {
			return false;
		}
	} 

	/**
	 * Gets the historic item with the maximum value of the state of a given <code>item</code> since
	 * a certain point in time. 
	 * The default persistence service is used. 
	 * 
	 * @param item the item to get the maximum state value for
	 * @param the point in time to start the check 
	 * @return a historic item with the maximum state value since the given point in time
	 */
	static public HistoricItem maximumSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return maximumSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Gets the historic item with the maximum value of the state of a given <code>item</code> since
	 * a certain point in time. 
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used. 
	 * 
	 * @param item the item to get the maximum state value for
	 * @param the point in time to start the check 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return a historic item with the maximum state value since the given point in time
	 */
	static public HistoricItem maximumSince(final Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		HistoricItem maximumHistoricItem = null;
		DecimalType maximum = (DecimalType) item.getStateAs(DecimalType.class);
		while(it.hasNext()) {
			HistoricItem historicItem = it.next();
			State state = historicItem.getState();
			if (state instanceof DecimalType) {
				DecimalType value = (DecimalType) state;
				if(maximum==null || value.compareTo(maximum)>0) {
					maximum = value;
					maximumHistoricItem = historicItem;
				}
			}
		}
		if(maximumHistoricItem==null && maximum!=null) {
			// the maximum state is the current one, so construct a historic item on the fly
			final DecimalType state = maximum;
			return new HistoricItem() {
				
				public Date getTimestamp() {
					return Calendar.getInstance().getTime();
				}
				
				public State getState() {
					return state;
				}
				
				public String getName() {
					return item.getName();
				}
			};
		} else {
			return maximumHistoricItem;
		}
	} 

	/**
	 * Gets the historic item with the minimum value of the state of a given <code>item</code> since
	 * a certain point in time. 
	 * The default persistence service is used. 
	 * 
	 * @param item the item to get the minimum state value for
	 * @param the point in time to start the check 
	 * @return the historic item with the minimum state value since the given point in time
	 */
	static public HistoricItem minimumSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return minimumSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Gets the historic item with the minimum value of the state of a given <code>item</code> since
	 * a certain point in time. 
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used. 
	 * 
	 * @param item the item to get the minimum state value for
	 * @param the point in time to start the check 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the historic item with the minimum state value since the given point in time
	 */
	static public HistoricItem minimumSince(final Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		HistoricItem minimumHistoricItem = null;
		DecimalType minimum = (DecimalType) item.getStateAs(DecimalType.class);
		while(it.hasNext()) {
			HistoricItem historicItem = it.next();
			State state = historicItem.getState();
			if (state instanceof DecimalType) {
				DecimalType value = (DecimalType) state;
				if(minimum==null || value.compareTo(minimum)<0) {
					minimum = value;
					minimumHistoricItem = historicItem;
				}
			}
		}
		if(minimumHistoricItem==null && minimum!=null) {
			// the minimal state is the current one, so construct a historic item on the fly
			final DecimalType state = minimum;
			return new HistoricItem() {
				
				public Date getTimestamp() {
					return Calendar.getInstance().getTime();
				}
				
				public State getState() {
					return state;
				}
				
				public String getName() {
					return item.getName();
				}
			};
		} else {
			return minimumHistoricItem;
		}
	} 
	
	/**
	 * Gets the average value of the state of a given <code>item</code> since a certain point in time. 
	 * The default persistence service is used. 
	 * 
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check 
	 * @return the average state value since the given point in time
	 */
	static public DecimalType averageSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return averageSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}

	/**
	 * Gets the average value of the state of a given <code>item</code> since a certain point in time. 
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used. 
	 * 
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check 
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the average state value since the given point in time
	 */
	static public DecimalType averageSince(Item item, AbstractInstant timestamp, String serviceName) {
		Iterable<HistoricItem> result = getAllStatesSince(item, timestamp, serviceName);
		Iterator<HistoricItem> it = result.iterator();
		
		DecimalType value = (DecimalType) item.getStateAs(DecimalType.class);
		if (value == null) {
			value = DecimalType.ZERO;
		}
		
		double average = value.doubleValue();
		int quantity = 1;
		while(it.hasNext()) {
			State state = it.next().getState();
			if (state instanceof DecimalType) {
				value = (DecimalType) state;
				average += value.doubleValue();
				quantity++;
			}
		}
		average /= quantity;
		
		return new DecimalType(average);
	}
	
	/**
	 * Query for the last update timestamp of a given <code>item</code>.
	 * The default persistence service is used.
	 *
	 * @param item the item to check for state updates
	 * @return point in time of the last update or null if none available
	 */
	static public Date lastUpdate(Item item) {
		if(isDefaultServiceAvailable()) {
			return lastUpdate(item, defaultService);
		} else {
			return null;
		}
	}
	
	/**
	 * Query for the last update timestamp of a given <code>item</code>.
	 *
	 * @param item the item to check for state updates
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return point in time of the last update or null if none available
	 */
	static public Date lastUpdate(Item item, String serviceName) {
		PersistenceService service = services.get(serviceName);
		if (service instanceof QueryablePersistenceService) {
			QueryablePersistenceService qService = (QueryablePersistenceService) service;
			FilterCriteria filter = new FilterCriteria();
			filter.setItemName(item.getName());
			filter.setOrdering(Ordering.DESCENDING);
			filter.setPageSize(1);
			Iterable<HistoricItem> result = qService.query(filter);
			if (result.iterator().hasNext()) {
				return result.iterator().next().getTimestamp();
			} else {
				return null;
			}
		} else {
			logger.warn("There is no queryable persistence service registered with the name '{}'", serviceName);
			return null;
		}
	}
	
	/**
	 * Gets the difference value of the state of a given <code>item</code> since a certain point in time.
	 * The default persistence service is used.
	 *
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check
	 * @return the difference between now and then, null if not calculable
	 */
	static public DecimalType deltaSince(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return deltaSince(item, timestamp, defaultService);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the difference value of the state of a given <code>item</code> since a certain point in time.
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used.
	 *
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the difference between now and then, null if not calculable
	 */
	static public DecimalType deltaSince(Item item, AbstractInstant timestamp, String serviceName) {
		HistoricItem itemThen = historicState(item, timestamp);
		DecimalType valueThen = (DecimalType) itemThen.getState();
		DecimalType valueNow = (DecimalType) item.getStateAs(DecimalType.class);
		DecimalType result = null;
		if (( valueThen != null) && ( valueNow != null)) {
			result = new DecimalType(valueNow.doubleValue() - valueThen.doubleValue());
		};
		return result;
 	}
	
	/**
	 * Gets the evolution rate of the state of a given <code>item</code> since a certain point in time.
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used.
	 *
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the evolution rate in percent (positive and negative) between now and then, 
	 * 			null if not calculable
	 */
	static public DecimalType evolutionRate(Item item, AbstractInstant timestamp) {
		if(isDefaultServiceAvailable()) {
			return evolutionRate(item, timestamp, defaultService);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the evolution rate of the state of a given <code>item</code> since a certain point in time.
	 * The {@link PersistenceService} identified by the <code>serviceName</code> is used.
	 *
	 * @param item the item to get the average state value for
	 * @param the point in time to start the check
	 * @param serviceName the name of the {@link PersistenceService} to use
	 * @return the evolution rate in percent (positive and negative) between now and then, 
	 * 			null if not calculable
	 */
	static public DecimalType evolutionRate(Item item, AbstractInstant timestamp, String serviceName) {
		HistoricItem itemThen = historicState(item, timestamp);
		DecimalType valueThen = (DecimalType) itemThen.getState();
		DecimalType valueNow = (DecimalType) item.getStateAs(DecimalType.class);
		DecimalType result = null;
		if (( valueThen != null) && ( valueNow != null)) {
			result = new DecimalType(100 * (valueNow.doubleValue() - valueThen.doubleValue()) / valueThen.doubleValue());
		};
		return result;
 	}
	
	static private Iterable<HistoricItem> getAllStatesSince(Item item, AbstractInstant timestamp, String serviceName) {
		PersistenceService service = services.get(serviceName);
		if (service instanceof QueryablePersistenceService) {
			QueryablePersistenceService qService = (QueryablePersistenceService) service;
			FilterCriteria filter = new FilterCriteria();
			filter.setBeginDate(timestamp.toDate());
			filter.setItemName(item.getName());
			filter.setOrdering(Ordering.ASCENDING);
			return qService.query(filter);
		} else {
			logger.warn("There is no queryable persistence service registered with the name '{}'", serviceName);
			return Collections.emptySet();
		}
	}
	
	/**
	 * Returns <code>true</code>, if a default service is configured and returns <code>false</code> and logs a warning otherwise.
	 * @return true, if a default service is available
	 */
	static private boolean isDefaultServiceAvailable() {
		if(defaultService!=null) {
			return true;
		} else {
			logger.warn("No default persistence service is configured in openhab.cfg!");
			return false;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {
			PersistenceExtensions.defaultService = (String) config.get("default");			
		}
	}

}
