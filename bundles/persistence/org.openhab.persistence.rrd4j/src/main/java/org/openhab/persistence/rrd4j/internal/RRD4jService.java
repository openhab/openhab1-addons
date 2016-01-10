/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.lang.StringUtils;
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
import org.osgi.framework.BundleContext;
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
 * more about RRD4j please visit their <a href="https://github.com/rrd4j/rrd4j">website</a>.
 * 
 * @author Kai Kreuzer
 * @author Jan N. Klug
 * @since 1.0.0
 */
public class RRD4jService implements QueryablePersistenceService {

	private ConcurrentHashMap<String, RrdDefConfig> rrdDefs = new ConcurrentHashMap<String, RrdDefConfig>();

	private static final String DATASOURCE_STATE = "state";

	public final static String DB_FOLDER = getUserPersistenceDataFolder() + File.separator + "rrd4j";

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
	public synchronized void store(final Item item, final String alias) {
		final String name = alias==null ? item.getName() : alias;
		RrdDb db = getDB(name);
		if(db!=null) {
			ConsolFun function = getConsolidationFunction(db);
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
							logger.debug("Stored '{}' with state '{}' in rrd4j database (again)", name, mapToState(lastValue, item.getName()));
						}
					}
				} catch (IOException e) {
					logger.debug("Error storing last value (again): {}", e.getMessage());
				}
			}
			try {
				Sample sample = db.createSample();
				sample.setTime(now);

				DecimalType state = (DecimalType) item.getStateAs(DecimalType.class);
				if (state!=null) {
					double value = state.toBigDecimal().doubleValue();
					if (db.getDatasource(DATASOURCE_STATE).getType()==DsType.COUNTER) { // counter values must be adjusted by stepsize
						value = value * db.getRrdDef().getStep();
					}
					sample.setValue(DATASOURCE_STATE, value);
					sample.update();
					logger.debug("Stored '{}' with state '{}' in rrd4j database", name, state);
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
		RrdDb db = getDB(itemName);
		if(db!=null) {
			ConsolFun consolidationFunction = getConsolidationFunction(db);
			long start = 0L;
			long end = filter.getEndDate()==null ? System.currentTimeMillis()/1000 : filter.getEndDate().getTime()/1000;

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
								"unless order is descending and a single value is requested");
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

	protected synchronized RrdDb getDB(String alias) {
		RrdDb db = null;
		File file = new File(DB_FOLDER + File.separator + alias + ".rrd");
		try {
			if (file.exists()) {
				// recreate the RrdDb instance from the file
				db = new RrdDb(file.getAbsolutePath());
			} else {
				File folder = new File(DB_FOLDER);
				if(!folder.exists()) {
					folder.mkdirs();
				}
				// create a new database file
				//db = new RrdDb(getRrdDef(function, file));
				db = new RrdDb(getRrdDef(alias, file));
			}
		} catch (IOException e) {
			logger.error("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		} catch(RejectedExecutionException e) {
			// this happens if the system is shut down
			logger.debug("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		}
		return db;
	}


	private RrdDefConfig getRrdDefConfig(String itemName) {
		RrdDefConfig useRdc = null;
		for (Map.Entry<String, RrdDefConfig> e: rrdDefs.entrySet()) { // try to find special config
			RrdDefConfig rdc = e.getValue();
			if (rdc.appliesTo(itemName)) {
				useRdc = rdc;
				break;
			}
		}
		if (useRdc == null) { // not defined, use defaults
			if (itemRegistry!=null) {
				try {
					Item item = itemRegistry.getItem(itemName);
					if (item instanceof NumberItem) {
						useRdc = rrdDefs.get("default_numeric");
					} else {
						useRdc = rrdDefs.get("default_other");
					}
				} catch (ItemNotFoundException e) {
					logger.debug("Could not find item '{}' in registry", itemName);
				}
			} else {
				useRdc = rrdDefs.get("default_other");
			}
		}
		return useRdc;
	}

	private RrdDef getRrdDef(String itemName, File file) {
		RrdDef rrdDef = new RrdDef(file.getAbsolutePath());
		RrdDefConfig useRdc = getRrdDefConfig(itemName);

		rrdDef.setStep(useRdc.step);
		rrdDef.setStartTime(System.currentTimeMillis()/1000-1);
		rrdDef.addDatasource(DATASOURCE_STATE, useRdc.dsType, useRdc.heartbeat, useRdc.min, useRdc.max);
		for (RrdArchiveDef rad : useRdc.archives) {
			rrdDef.addArchive(rad.fcn, rad.xff, rad.steps, rad.rows);
		}
		return rrdDef;
	}

	public ConsolFun getConsolidationFunction(RrdDb db) {
		try {
			return db.getRrdDef().getArcDefs()[0].getConsolFun();
		} catch (IOException e) {
			return ConsolFun.MAX;
		}
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

	static private String getUserPersistenceDataFolder() {
		String progArg = System.getProperty("smarthome.userdata");
		if (progArg != null) {
			return progArg + File.separator + "persistence";
		} else {
			return "etc";
		}
	}

	/**
	 * @{inheritDoc
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> config) {

		// add default configurations
		RrdDefConfig defaultNumeric = new RrdDefConfig("default_numeric");
		defaultNumeric.setDef("GAUGE,60,U,U,60");
		defaultNumeric.addArchives("AVERAGE,0.5,1,480:AVERAGE,0.5,4,360:AVERAGE,0.5,14,644:AVERAGE,0.5,60,720:AVERAGE,0.5,720,730:AVERAGE,0.5,10080,520");
		rrdDefs.put("default_numeric", defaultNumeric);

		RrdDefConfig defaultOther = new RrdDefConfig("default_other");
		defaultOther.setDef("GAUGE,3600,U,U,1");
		defaultOther.addArchives("MAX,.999,1,3600:MAX,.999,10,1440:MAX,.999,60,1440:MAX,.999,900,2880:MAX,.999,21600,1460:MAX,.999,86400,3650");
		rrdDefs.put("default_other", defaultOther);

		if ((config == null) || config.isEmpty()) {
			logger.debug("using default configuration only");
			return;
		}

		Iterator<String> keys = config.keySet().iterator();
		while (keys.hasNext()) {

			String key = keys.next();

			if (key.equals("service.pid")) {	// ignore servioce.pid
				continue;
			}

			String[] subkeys = key.split("\\.");
			if (subkeys.length != 2) {
				logger.debug("config '{}' should have the format 'name.configkey'", key);
				continue;
			}

            Object v = config.get(key);
            if (v instanceof String) {
                String value = (String) v;
                String name = subkeys[0].toLowerCase();
                String property = subkeys[1].toLowerCase();

                if (StringUtils.isBlank(value)) {
                    logger.trace("Config is empty: {}", property);
                    continue;
                } else {
                    logger.trace("Processing config: {} = {}", property, value);
                }

                RrdDefConfig rrdDef = rrdDefs.get(name);
                if (rrdDef == null) {
                    rrdDef = new RrdDefConfig(name);
                    rrdDefs.put(name, rrdDef);
                }

                try {
                    if (property.equals("def")) {
                        rrdDef.setDef(value);
                    } else if (property.equals("archives")) {
                        rrdDef.addArchives(value);
                    } else if (property.equals("items")) {
                        rrdDef.addItems(value);
                    } else {
                        logger.debug("Unknown property {} : {}", property, value);
                    }
                } catch (IllegalArgumentException e) {
                    logger.warn("Ignoring illegal configuration: {}", e.getMessage());
                }
            }
        }

        for (RrdDefConfig rrdDef : rrdDefs.values()) {
            if (rrdDef.isValid()) {
                logger.debug("Created {}", rrdDef.toString());
            } else {
                logger.info("Removing invalid defintion {}", rrdDef.toString());
                rrdDefs.remove(rrdDef.name);
            }
        }
	}

	private class RrdArchiveDef {
		public ConsolFun fcn;
		public double xff;
		public int steps, rows;

		public String toString() {
			StringBuilder sb = new StringBuilder(" " + fcn);
			sb.append(" xff = ").append(xff);
			sb.append(" steps = ").append(steps);
			sb.append(" rows = ").append(rows);
			return sb.toString();
		}

	}

	private class RrdDefConfig {
		public String name;
		public DsType dsType;
		public int heartbeat, step;
		public double min, max;
		public List<RrdArchiveDef> archives;
		public List<String> itemNames;

		private boolean isInitialized;

		public RrdDefConfig(String name) {
			this.name = name;
			archives = new ArrayList<RrdArchiveDef>();
			itemNames = new ArrayList<String>();
			isInitialized = false;
		}

		public void setDef(String defString) {
			String[] opts = defString.split(",");
			if (opts.length != 5) { // check if correct number of parameters
				logger.warn("invalid number of parameters {}: {}", name, defString);
				return;
			}

			if (opts[0].equals("ABSOLUTE")) { // dsType
				dsType = DsType.ABSOLUTE;
			} else if (opts[0].equals("COUNTER")) {
				dsType = DsType.COUNTER;
			} else if (opts[0].equals("DERIVE")) {
				dsType = DsType.DERIVE;
			} else if (opts[0].equals("GAUGE")) {
				dsType = DsType.GAUGE;
			} else {
				logger.warn("{}: dsType {} not supported", name, opts[0]);
			}

			heartbeat = Integer.parseInt(opts[1]);

			if (opts[2].equals("U")) {
				min = Double.NaN;
			} else {
				min = Double.parseDouble(opts[2]);
			}

			if (opts[3].equals("U")) {
				max = Double.NaN;
			} else {
				max = Double.parseDouble(opts[3]);
			}

			step = Integer.parseInt(opts[4]);

			isInitialized = true; // successfully initialized

			return;
		}

		public void addArchives(String archivesString) {
			String splitArchives[] = archivesString.split(":");
			for (String archiveString: splitArchives) {
				String[] opts = archiveString.split(",");
				if (opts.length != 4) { // check if correct number of parameters
					logger.warn("invalid number of parameters {}: {}", name, archiveString);
					return;
				}
				RrdArchiveDef arc = new RrdArchiveDef();

				if (opts[0].equals("AVERAGE")) {
					arc.fcn = ConsolFun.AVERAGE;
				} else if (opts[0].equals("MIN")) {
					arc.fcn = ConsolFun.MIN;
				} else if (opts[0].equals("MAX")) {
					arc.fcn = ConsolFun.MAX;
				} else if (opts[0].equals("LAST")) {
					arc.fcn = ConsolFun.LAST;
				} else if (opts[0].equals("FIRST")) {
					arc.fcn = ConsolFun.FIRST;
				} else if (opts[0].equals("TOTAL")) {
					arc.fcn = ConsolFun.TOTAL;
				} else {
					logger.warn("{}: consolidation function  {} not supported", name, opts[0]);
				}
				arc.xff = Double.parseDouble(opts[1]);
				arc.steps = Integer.parseInt(opts[2]);
				arc.rows = Integer.parseInt(opts[3]);
				archives.add(arc);
			}
		}

		public void addItems(String itemsString) {
			String splitItems[] = itemsString.split(",");
			for (String item: splitItems) {
				itemNames.add(item);
			}
		}

		public boolean appliesTo(String item) {
			return itemNames.contains(item);
		}

		public boolean isValid() { // a valid configuration must be initialized and contain at least one function
			return (isInitialized && (archives.size()>0));
		}

		public ConsolFun getDefaultConsolFun() {
			return archives.iterator().next().fcn;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(name);
			sb.append(" = ").append(dsType);
			sb.append(" heartbeat = ").append(heartbeat);
			sb.append(" min/max = ").append(min).append("/").append(max);
			sb.append(" step = ").append(step);
			sb.append(" ").append(archives.size()).append(" archives(s) = [");
			for (RrdArchiveDef arc : archives) {
				sb.append(arc.toString());
			}
			sb.append("] ");
			sb.append(itemNames.size()).append(" items(s) = [");
			for (String item : itemNames) {
				sb.append(item).append(" ");
			}
			sb.append("]");
			return sb.toString();
		}
	}
}
