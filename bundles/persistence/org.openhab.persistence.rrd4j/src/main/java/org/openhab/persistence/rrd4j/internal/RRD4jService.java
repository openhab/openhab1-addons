/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.rrd4j.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RejectedExecutionException;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the implementation of the RRD4j {@link PersistenceService}. To learn
 * more about RRD4j please visit their <a href="http://code.google.com/p/rrd4j/">website</a>.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class RRD4jService implements QueryablePersistenceService {

	private static final String DATASOURCE_STATE = "state";

	protected final static String DB_FOLDER = "etc/rrd4j";
	
	private static final Logger logger = LoggerFactory.getLogger(RRD4jService.class);

	private Map<String,Timer> timers = new HashMap<String,Timer>();
	
	protected ItemRegistry itemRegistry;
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "rrd4j";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(final Item item, final String alias) {
		final String name = alias==null ? item.getName() : alias;
		ConsolFun function = getConsolidationFunction(item);
		RrdDb db = getDB(name, function);
		if(db!=null) {
			long now = System.currentTimeMillis()/1000;
			if(function!=ConsolFun.AVERAGE) {
				try {
					// we store the last value again, so that the value change in the database is not interpolated, but
					// happens right at this spot
					if(now - 1 > db.getLastUpdateTime()) {
						// only do it if there is not already a value
						double lastValue = db.getLastDatasourceValue(DATASOURCE_STATE);
						if(!Double.isNaN(lastValue)) {
							Sample sample = db.createSample();
				            sample.setTime(now - 1);
				            sample.setValue(DATASOURCE_STATE, lastValue);
				            sample.update();
		                    logger.debug("Stored '{}' with state '{}' in rrd4j database", name, mapToState(lastValue, item.getName()));
						}
					}
				} catch (IOException e) {
					logger.debug("Error re-storing last value: {}", e.getMessage());
				}
			}
			try {
				Sample sample = db.createSample();
	            sample.setTime(now);
	            
	            DecimalType state = (DecimalType) item.getStateAs(DecimalType.class);
	            if (state!=null) {
                    double value = state.toBigDecimal().doubleValue();
                    sample.setValue(DATASOURCE_STATE, value);
                    sample.update();
                    logger.debug("Stored '{}' with state '{}' in rrd4j database", name, item.getState());
	            }
			} catch (IllegalArgumentException e) {
				if(e.getMessage().contains("at least one second step is required")) {

					// we try to store the value one second later
					TimerTask task = new TimerTask() {
						public void run() {
							store(item, name);
						}
					};
					Timer timer = timers.get(name);
					if(timer!=null) {
						timer.cancel();
						timers.remove(name);
					}
					timer = new Timer();
					timers.put(name, timer);
					timer.schedule(task, 1000);
				} else {
					logger.warn("Could not persist '{}' to rrd4j database: {}", new String[] { name, e.getMessage() });
				}
			} catch (Exception e) {
				logger.warn("Could not persist '{}' to rrd4j database: {}", new String[] { name, e.getMessage() });
			}
            try {
				db.close();
			} catch (IOException e) {
				logger.debug("Error closing rrd4j database: {}", e.getMessage());
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		store(item, null);
	}
	
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		String itemName = filter.getItemName();
		ConsolFun consolidationFunction = getConsolidationFunction(itemName);
		RrdDb db = getDB(itemName, consolidationFunction);
		if(db!=null) {
			long start = 0L;
			long end = filter.getEndDate()==null ? System.currentTimeMillis()/1000 - 1 : filter.getEndDate().getTime()/1000;

			try {
				if(filter.getBeginDate()==null) {
					// as rrd goes back for years and gets more and more inaccurate, we only support descending order and a single return value
					// if there is no begin date is given - this case is required specifically for the historicState() query, which we
					// want to support
					if(filter.getOrdering()==Ordering.DESCENDING && filter.getPageSize()==1 && filter.getPageNumber()==0) {
						if(filter.getEndDate()==null) {
							// we are asked only for the most recent value!
							double lastValue = db.getLastDatasourceValue(DATASOURCE_STATE);
							if(!Double.isNaN(lastValue)) {
								HistoricItem rrd4jItem = new RRD4jItem(itemName, mapToState(lastValue, itemName), new Date(db.getLastArchiveUpdateTime() * 1000));
								return Collections.singletonList(rrd4jItem);
							} else {
								return Collections.emptyList();
							}
						} else {
							start = end;
						}
					} else {
						throw new UnsupportedOperationException("rrd4j does not allow querys without a begin date, " + 
								"unless order is decending and a single value is requested");
					}
				} else {
					start = filter.getBeginDate().getTime()/1000;
				}
				FetchRequest request = db.createFetchRequest(consolidationFunction, start, end, 1);

				List<HistoricItem> items = new ArrayList<HistoricItem>();
				FetchData result = request.fetchData();
				long ts = result.getFirstTimestamp();
				long step = result.getRowCount() > 1 ? result.getStep() : 0;
				for(double value : result.getValues(DATASOURCE_STATE)) {
					if(!Double.isNaN(value)) {
						RRD4jItem rrd4jItem = new RRD4jItem(itemName, mapToState(value, itemName), new Date(ts * 1000));
						items.add(rrd4jItem);
					}
					ts += step;
				}
				return items;
			} catch (IOException e) {
				logger.warn("Could not query rrd4j database for item '{}': {}", new String[] { itemName, e.getMessage() });
			}	
		}
		return Collections.emptyList();
	}

	protected synchronized RrdDb getDB(String alias, ConsolFun function) {
		RrdDb db = null;
        File file = new File(DB_FOLDER + File.separator + alias + ".rrd");
    	try {
            if (file.exists()) {
            	// recreate the RrdDb instance from the file
            	db = new RrdDb(file.getAbsolutePath());
            } else {
            	File folder = new File(DB_FOLDER);
            	if(!folder.exists()) {
            		folder.mkdir();
            	}
            	// create a new database file
                db = new RrdDb(getRrdDef(function, file));
            }
		} catch (IOException e) {
			logger.error("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		} catch(RejectedExecutionException e) {
			// this happens if the system is shut down
			logger.debug("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		}
		return db;
	}

	private RrdDef getRrdDef(ConsolFun function, File file) {
    	RrdDef rrdDef = new RrdDef(file.getAbsolutePath());
    	if(function==ConsolFun.AVERAGE) {
    		// for measurement values, we define archives that are suitable for charts
    		rrdDef.setStep(60);
	        rrdDef.setStartTime(System.currentTimeMillis()/1000-1);
	        rrdDef.addDatasource(DATASOURCE_STATE, DsType.GAUGE, 60, Double.NaN, Double.NaN);
	        rrdDef.addArchive(function, 0.5, 1, 480); // 8 hours (granularity 1 min)
	        rrdDef.addArchive(function, 0.5, 4, 360); // one day (granularity 4 min)
	        rrdDef.addArchive(function, 0.5, 15, 644); // one week (granularity 15 min)
	        rrdDef.addArchive(function, 0.5, 60, 720); // one month (granularity 1 hour)
	        rrdDef.addArchive(function, 0.5, 720, 730); // one year (granularity 12 hours)
	        rrdDef.addArchive(function, 0.5, 10080, 520); // ten years (granularity 7 days)
    	} else {
    		// for other things, we mainly provide a high level of detail for the last hour
    		rrdDef.setStep(1);
	        rrdDef.setStartTime(System.currentTimeMillis()/1000-1);
	        rrdDef.addDatasource(DATASOURCE_STATE, DsType.GAUGE, 3600, Double.NaN, Double.NaN);
	        rrdDef.addArchive(function, .999, 1, 3600); // 1 hour (granularity 1 sec)
	        rrdDef.addArchive(function, .999, 10, 1440); // 4 hours (granularity 10 sec)
	        rrdDef.addArchive(function, .999, 60, 1440); // one day (granularity 1 min)
	        rrdDef.addArchive(function, .999, 900, 2880); // one month (granularity 15 min)
	        rrdDef.addArchive(function, .999, 21600, 1460); // one year (granularity 6 hours)
	        rrdDef.addArchive(function, .999, 86400, 3650); // ten years (granularity 1 day)
    	}
		return rrdDef;
	}

	static public ConsolFun getConsolidationFunction(Item item) {
		if(item instanceof NumberItem) {
			return ConsolFun.AVERAGE;
		} else {
			// for all other values (like ON/OFF etc.) use the maximum value for consolidation
			return ConsolFun.MAX;
		}
	}

	private ConsolFun getConsolidationFunction(String itemName) {
		if(itemRegistry!=null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				return getConsolidationFunction(item);
			} catch (ItemNotFoundException e) {
				logger.debug("Could not find item '{}' in registry", itemName);
			}
		}
		// use MAX as the default
		return ConsolFun.MAX;
	}

	private State mapToState(double value, String itemName) {
		if(itemRegistry!=null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				if(item instanceof SwitchItem && !(item instanceof DimmerItem)) {
					return value==0.0d ? OnOffType.OFF : OnOffType.ON;
				} else if(item instanceof ContactItem) {
					return value==0.0d ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
				}
			} catch (ItemNotFoundException e) {
				logger.debug("Could not find item '{}' in registry", itemName);
			}
		}
		// just return a DecimalType as a fallback
		return new DecimalType(value);
	}
	
}
