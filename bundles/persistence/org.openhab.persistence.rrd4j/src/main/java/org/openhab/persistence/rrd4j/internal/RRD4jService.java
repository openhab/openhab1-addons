/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.persistence.rrd4j.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.types.State;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.Archive;
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
	public void store(Item item, String alias) {
		RrdDb db = getDB(item.getName(), getConsolidationFunction(item));
		if(db!=null) {
			try {
				Sample sample = db.createSample();
	            sample.setTime(System.currentTimeMillis()/1000);
	            
	            DecimalType state = (DecimalType) item.getStateAs(DecimalType.class);
	            if (state!=null) {
                    double value = state.toBigDecimal().doubleValue();
                    sample.setValue(DATASOURCE_STATE, value);
                    sample.update();
                    logger.debug("Stored item '{}' with state '{}' in rrd4j database", item.getName(), item.getState());
	            }
	            db.close();
			} catch (IllegalArgumentException e) {
				if(e.getMessage().contains("at least one second step is required")) {
					try {
						Thread.sleep(1000);
						store(item, alias);
					} catch (InterruptedException e1) {}
				} else {
					logger.warn("Could not persist item '{}' to rrd4j database: {}", new String[] { item.getName(), e.getMessage() });
				}
			} catch (Exception e) {
				logger.warn("Could not persist item '{}' to rrd4j database: {}", new String[] { item.getName(), e.getMessage() });
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
			long end = filter.getEndDate()==null ? System.currentTimeMillis()/1000 : filter.getEndDate().getTime()/1000;

			if(filter.getBeginDate()==null) {
				if(filter.getOrdering()==Ordering.DESCENDING) {
					try {
						Archive archive = db.findStartMatchArchive(consolidationFunction.toString(), filter.getEndDate().getTime()/1000L, 1L);
						long stepInSecs = archive.getArcStep();
						start = end - stepInSecs;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				start = filter.getBeginDate().getTime()/1000;
			}
			FetchRequest request = db.createFetchRequest(consolidationFunction, start, end);
			try {
				HashSet<HistoricItem> items = new HashSet<HistoricItem>();
				FetchData result = request.fetchData();
				long ts = result.getFirstTimestamp();
				for(double value : result.getValues(DATASOURCE_STATE)) {
					if(!Double.isNaN(value)) {
						items.add(new RRD4jItem(itemName, mapToState(value, itemName), new Date(ts * 1000)));
					}
					ts += result.getStep();
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
			logger.error("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
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
	        rrdDef.addArchive(function, 0.5, 1, 480); // 8 hours
	        rrdDef.addArchive(function, 0.5, 4, 360); // one day
	        rrdDef.addArchive(function, 0.5, 15, 644); // one week
	        rrdDef.addArchive(function, 0.5, 60, 720); // one month
	        rrdDef.addArchive(function, 0.5, 720, 730); // one year
	        rrdDef.addArchive(function, 0.5, 10080, 520); // ten years
    	} else {
    		// for other things, we mainly provide a high level of detail for the last hour
    		rrdDef.setStep(1);
	        rrdDef.setStartTime(System.currentTimeMillis()/1000-1);
	        rrdDef.addDatasource(DATASOURCE_STATE, DsType.GAUGE, 86400, Double.NaN, Double.NaN);
	        rrdDef.addArchive(function, 0.5, 1, 3600); // 1 hour (granularity 1 sec)
	        rrdDef.addArchive(function, 0.5, 10, 1440); // 4 hours (granularity 10 sec)
	        rrdDef.addArchive(function, 0.5, 60, 1440); // one day (granularity 1 min)
	        rrdDef.addArchive(function, 0.5, 900, 2880); // one month (granularity 15 min)
	        rrdDef.addArchive(function, 0.5, 21600, 1460); // one year (granularity 6 hours)
	        rrdDef.addArchive(function, 0.5, 86400, 3650); // ten years (granularity 1 day)
    	}
		return rrdDef;
	}

	private ConsolFun getConsolidationFunction(Item item) {
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
