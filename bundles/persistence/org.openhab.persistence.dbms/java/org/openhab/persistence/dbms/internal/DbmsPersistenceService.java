/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.persistence.dbms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.UnDefType;
import org.openhab.persistence.dbms.config.DbmsConfiguration;
import org.openhab.persistence.dbms.model.ItemVO;
import org.openhab.persistence.dbms.model.ItemsVO;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ########################### DBMS Persistence Service ##################################
 * # I N S T A L L   D B M S   P E R S I S T E N C E   S E R V I C E 
 * # To use this DBMS-service-bundle (org.openhab.persistence.dbms-X.X.X.jar),
 * # a appropriate JDBC database-driver is needed in OpenHab addons Folder.
 * # Prepared matching Bundles with JDBC database-driver embedded can be found in DBMS project. 
 * # Use: '/drivers/org.openhab.persistence.dbms.jdbc.[databaseName]-X.X.X.jar'.
 * # Copy both (DBMS-service-bundle and a JDBC database-driver) to your OpenHab '[OpneHab]/addons' Folder to make it work. 
 * #
 * # A special case is the driver for H2, it ONLY works by copying "h2-X.X.XXX.jar" to your '[OpneHab]/addons' folder directly.
 * # Alternatively copying the driver directly may work for some other JDBC database-driver Files too. 
 * #
 * # Tested databases/url-prefix: jdbc:derby, jdbc:h2, jdbc:hsqldb, jdbc:mariadb, jdbc:mysql, jdbc:postgresql, jdbc:sqlite
 * # If using a not known url like jdbc:XXX, configuration is falling back to the default mysql profile and SQL.
 * #
 * # derby, h2, hsqldb, sqlite can be embedded, 
 * # If no database is available, for Example the url 'jdbc:h2:./testH2' creates a new DB in OpenHab Folder. 
 * 
 * # D A T A B A S E  C O N F I G
 * # Some URL-Examples, 'service' identifies and activates internally the correct jdbc driver.
 * # required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
 * #dbms:url=jdbc:derby:./testDerby;create=true
 * #dbms:url=jdbc:h2:./testH2
 * #dbms:url=jdbc:hsqldb:./testHsqlDb
 * #dbms:url=jdbc:mariadb://192.168.0.1:3306/testMariadb
 * #dbms:url=jdbc:mysql://192.168.0.1:3306/testMysql
 * #dbms:url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
 * #dbms:url=jdbc:sqlite:./testSqlite.db
 * 
 * # required database user
 * #dbms:user=
 * dbms:user=test
 * 
 * # required database password
 * #dbms:password=
 * dbms:password=test
 * 
 * # E R R O R   H A N D L I N G
 * # optional when Service is deactivated (optional, default: 0 -> ignore) 
 * #dbms:errReconnectThreshold=
 * 
 * # I T E M   O P E R A T I O N S
 * # optional tweaking SQL datatypes
 * # see: https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html
 * # see: http://www.h2database.com/html/datatypes.html
 * # see: http://www.postgresql.org/docs/9.3/static/datatype.html
 * # defaults:
 * #dbms:sqltype.CALL           = VARCHAR(200)
 * #dbms:sqltype.COLOR          = VARCHAR(70)
 * #dbms:sqltype.CONTACT        = VARCHAR(6)
 * #dbms:sqltype.DATETIME       = DATETIME
 * #dbms:sqltype.DIMMER         = TINYINT
 * #dbms:sqltype.LOCATION       = VARCHAR(30)
 * #dbms:sqltype.NUMBER         = DOUBLE
 * #dbms:sqltype.ROLLERSHUTTER  = TINYINT
 * #dbms:sqltype.STRING         = VARCHAR(65500)
 * #dbms:sqltype.SWITCH         = VARCHAR(6)
 * 
 * # For OpenHab Itemtype "Number" default decimal digit count (optional, default: 3) 
 * #dbms:numberDecimalcount=
 * 
 * # T A B L E   O P E R A T I O N S
 * # Tablename Prefix generation, using Item real names or "item" (optional, default: false -> "item") 
 * #dbms:tableUseRealItemNames=true
 * 
 * # Tablename Suffix length (optional, default: 4 -> 0001-9999) 
 * #dbms:tableIdDigitCount=
 * 
 * # Rename existing Tables using tableUseRealItemNames and tableIdDigitCount (optional, default: false) 
 * # USE WITH CARE! Deactivate after Renaming is done!
 * #dbms:rebuildTableNames=true
 * 
 * # D A T A B A S E / M Y B A T I S   C O N N E C T I O N S
 * # Some embeded Databases can handle only one Connection (optional, default: 1) 
 * # dbms.maxActiveConnections = 1
 * # dbms.maxIdleConnections = 1
 * 
 * #######################################################################################
 * 
 * 
 * ADD A NEW DATABASE:
 * 1. Duplicate org.openhab.persistence.dbms.config.db.DbmsMysql.java with database name Dbms[database].java
 * 2. Minimum you have to configure: setDriverClassName("JDBC DRIVERNAME STRING")
 * 3. Setup OpenHab *.cfg File dbms:user=, dbms:password=, dbms:url=jdbc:[database]:
 * 4. Run and Test
 * 5. For adjustments, use default communication-code from DbmsBaseDB and copy parts to your new Dbms[database] class an customize it.
 * 6. Run and Test
 * 
 *
 * @author Helmut Lehmeyer
 * @since 1.8.0
 * 
 * 
 */
public class DbmsPersistenceService extends DbmsMyBatisMapper implements QueryablePersistenceService {

	private static final String ITEM_NAME_PATTERN = "[^a-zA-Z_0-9\\-]";

	static final Logger logger = LoggerFactory.getLogger(DbmsPersistenceService.class);

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;

	// Error counter - used to reconnect to database on error
	private int errCnt;

	private Map<String, String> sqlTables = new HashMap<String, String>();

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(BundleContext bundleContext, Map<Object, Object> configuration) {
		logger.debug("DBMS::activate  persistence service activated");
		this.bundleContext = bundleContext;

		updateConfig(configuration);

		logger.debug("DBMS: numberDecimalcount = {}", conf.getNumberDecimalcount());
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		logger.debug("DBMS:  persistence bundle stopping. Disconnecting from database. reason={}", reason);
		this.bundleContext = null;
		initialized = false;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("DBMS:  setItemRegistry");
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("DBMS:  unsetItemRegistry");
		this.itemRegistry = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public String getName() {
		logger.debug("DBMS:  getName: returning name for register queryable persistence service with the name 'dbms'");
		// return conf != null ? conf.getName() : "dbms";
		return "dbms";// "postgresql";
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private String getItemType(Item i) {
		Item item = i;
		String def = "STRINGITEM";
		if (i instanceof GroupItem) {
			item = ((GroupItem) i).getBaseItem();
			if (item == null) {
				// if GroupItem:<ItemType> is not defined in
				// *.items using StringType
				// logger.debug("DBMS: BaseItem GroupItem:<ItemType> is not
				// defined in *.items searching for first Member and try to use
				// as ItemType");
				logger.debug(
						"DBMS: Cannot detect ItemType for {} because the GroupItems' base type isn't set in *.items File.",
						i.getName());
				item = ((GroupItem) i).getMembers().get(0);
				if (item == null) {
					logger.debug(
							"DBMS: No ItemType found for first Child-Member of GroupItem {}, use ItemType for STRINGITEM as Fallback",
							i.getName());
					return def;
				}
			}
		}
		String itemType = item.getClass().getSimpleName().toUpperCase();
		logger.debug("DBMS: Try to use ItemType {} for Item {}", itemType, i.getName());
		if (conf.getSqlType(itemType) == null) {
			logger.warn("DBMS: No sqlType found for ItemType {}, use ItemType for STRINGITEM as Fallback for {}",
					itemType, i.getName());
			return def;
		}
		return itemType;
	}

	private String getTable(Item item) {
		int rowId = 0;
		ItemsVO isvo;
		ItemVO ivo;

		String itemName = item.getName();
		String tableName = sqlTables.get(itemName);

		// Table already exists - return the name
		if (tableName != null) {
			return tableName;
		}

		logger.debug("DBMS: no Table found for itemName={} in sqlTables", itemName);

		// Create a new entry in items table
		isvo = new ItemsVO();
		isvo.setItemname(itemName);
		try {
			isvo = createNewEntryInItemsTable(isvo);
			rowId = isvo.getItemid();
			if (rowId == 0) {
				throw new PersistenceException("DBMS: Creating table for item '" + itemName + "' failed.");
			}
			// Create the table name
			tableName = getTableName(rowId, itemName);
		} catch (Exception e) {
			errCnt++;
			logger.error(
					"DBMS::createNewEntryInItemsTable Could not create entry for '{}' in table 'items' with statement: {}",
					itemName, e.getMessage());
		}

		// An error occurred adding the item name into the index list!
		if (tableName == null) {
			logger.error("DBMS: tableName was null, could not create a table for item '{}'", itemName);
			return null;
		}

		String dataType = conf.getSqlType(getItemType(item));
		ivo = new ItemVO(tableName, itemName);
		ivo.setDbType(dataType);
		try {
			ivo = createItemTable(ivo);
			logger.debug("DBMS: Table created for item '{}' with dataType {} in SQL database.", itemName, dataType);
			sqlTables.put(itemName, tableName);
		} catch (Exception e1) {
			errCnt++;
			logger.error("DBMS: Could not create table for item '{}' Exception: {}, Cause: {}", itemName,
					e1.getMessage(), e1.getCause());
		}

		// Check if the new entry is in the table list
		// If it's not in the list, then there was an error and we need to do
		// some tidying up
		// The item needs to be removed from the index table to avoid duplicates
		if (sqlTables.get(itemName) == null) {
			logger.error("DBMS: Item '{}' was not added to the table - removing index", itemName);
			isvo = new ItemsVO();
			isvo.setItemname(itemName);
			try {
				deleteItemsEntry(isvo);
			} catch (Exception e) {
				errCnt++;
				logger.error(
						"DBMS::createNewEntryInItemsTable Could not create entry for '{}' in table 'items' with statement: {}",
						itemName, e.getMessage());
			}
		}

		return tableName;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void store(Item item) {
		store(item, null);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void store(Item item, String alias) {
		// Don't log undefined/uninitialised data
		if (item.getState() instanceof UnDefType) {
			logger.warn("DBMS: store Item is UnDefType");
			return;
		}

		// If we've not initialised the bundle, then return
		if (initialized == false) {
			logger.warn("DBMS: store bundle is not initialized");
			return;
		}

		if (!checkDBAcessability()) {
			logger.warn(
					"DBMS:  No connection to database. Can not persist item '{}'! Will retry connecting to database when error count:{} equals errReconnectThreshold:{}",
					item, errCnt, conf.getErrReconnectThreshold());
			return;
		}

		// Get the table name for this item
		String tableName = getTable(item);
		String itemType = getItemType(item);
		if (tableName == null) {
			logger.error("DBMS: Unable to store item '{}'.", item.getName());
			return;
		}

		// insertItemValue
		long timerStart = System.currentTimeMillis();
		String itemName = item.getName();
		ItemVO ivo = new ItemVO(tableName, itemName);
		logger.debug("DBMS:store try to store now '{}' as Type '{}' in '{}' with state '{}'", itemName, itemType,
				tableName, item.getState().toString());
		try {

			logger.debug("DBMS:store getState: '{}'", item.getState().toString());
			if ("COLORITEM".equals(itemType)) {
				ivo.setValueTypes(conf.getSqlType(itemType), java.lang.String.class,
						org.apache.ibatis.type.JdbcType.VARCHAR);
				ivo.setValue(item.getState().toString());
			} else if ("NUMBERITEM".equals(itemType)) {
				ivo.setValueTypes(conf.getSqlType(itemType), java.lang.Double.class,
						org.apache.ibatis.type.JdbcType.DOUBLE);
				Number newVal = ((DecimalType) item.getState());
				logger.debug("DBMS:store newVal.doubleValue: '{}'", newVal.doubleValue());
				ivo.setValue(newVal.doubleValue());
			} else if ("ROLLERSHUTTERITEM".equals(itemType) || "DIMMERITEM".equals(itemType)) {
				ivo.setValueTypes(conf.getSqlType(itemType), java.lang.Integer.class,
						org.apache.ibatis.type.JdbcType.SMALLINT);
				Number newVal = ((DecimalType) item.getState());
				logger.debug("DBMS:store newVal.intValue: '{}'", newVal.intValue());
				ivo.setValue(newVal.intValue());
			} else if (item instanceof DateTimeItem) {
				ivo.setValueTypes(conf.getSqlType(itemType), java.util.Date.class,
						org.apache.ibatis.type.JdbcType.DATE);
				Date d = ((DateTimeType) item.getState()).getCalendar().getTime();
				logger.debug("DBMS:store DateTimeItem: '{}'", d);
				ivo.setValue(d);
			} else {
				/*
				 * !!ATTENTION!!
				 * 
				 * 1. DimmerItem.getStateAs(PercentType.class).toString() always
				 * returns 0
				 * RollershutterItem.getStateAs(PercentType.class).toString()
				 * works as expected
				 * 
				 * 2. (item instanceof ColorItem) == (item instanceof
				 * DimmerItem) = true Therefore for instance tests ColorItem
				 * always has to be tested before DimmerItem
				 * 
				 * !!ATTENTION!!
				 */
				// All other items should return the best format by default
				ivo.setValueTypes(conf.getSqlType(itemType), java.lang.String.class,
						org.apache.ibatis.type.JdbcType.VARCHAR);
				logger.debug("DBMS:store other: item.getState().toString(): '{}'", item.getState().toString());
				ivo.setValue(item.getState().toString());
			}
			ivo = insertItemValue(ivo);

			logger.info("DBMS: Stored item '{}' as '{}' in SQL database at {} in {}ms.", item.getName(),
					item.getState().toString(), (new java.util.Date()).toString(),
					System.currentTimeMillis() - timerStart);
			logger.debug("DBMS: Saved itemType='{}' as '{}'", itemType, conf.getSqlType(itemType));

			// Success
			errCnt = 0;
		} catch (Exception e1) {
			errCnt++;
			logger.error("DBMS: Could not store item '{}' value '{}' in database with statement Exception: {}",
					item.getName(), item.getState().toString(), e1.getMessage());
		}

	}

	/**
	 * Checks if we have a database connection
	 *
	 * @return true if connection has been established, false otherwise
	 */
	private boolean checkDBAcessability() {
		// Check if connection is valid
		try {
			return (pingDB() == 1
					&& !(conf.getErrReconnectThreshold() > 0 && errCnt <= conf.getErrReconnectThreshold()));
		} catch (Exception e) {
			errCnt++;
			logger.error("DBMS: Error while checking connection: {}", e);
		}

		return false;
	}

	/**
	 * Connects to the database
	 */
	private void checkDBSchema() {
		// Create Items Table if does not exist
		try {
			createItemsTableIfNot(new ItemsVO());
		} catch (Exception e) {
			logger.error("DBMS: Failed createItemsTableIfNot Exception={}", e);
		}

		// Create Item Tables if does not exist
		if (conf.getRebuildTableNames()) {
			formatTableNames();
		} else {
			List<ItemsVO> al;
			try {
				// Reset the error counter
				errCnt = 0;
				al = getItemIDTableNames();
				for (int i = 0; i < al.size(); i++) {
					String t = getTableName(al.get(i).getItemid(), al.get(i).getItemname());
					sqlTables.put(al.get(i).getItemname(), t);
				}
			} catch (Exception e) {
				logger.error(
						"DBMS: Failed connecting to the SQL db using: serviceName={}, "
								+ "url={}, user={}, password={} e={}",
						conf.getServiceName(), conf.getUrl(), conf.getUser(), conf.getPassword(), e);
			}
		}
	}

	private void formatTableNames() {

		boolean tmpinit = initialized;
		if (tmpinit)
			initialized = false;

		List<ItemsVO> al;
		HashMap<Integer, String> tableIds = new HashMap<Integer, String>();
		try {
			al = getItemIDTableNames();
			for (int i = 0; i < al.size(); i++) {
				String t = getTableName(al.get(i).getItemid(), al.get(i).getItemname());
				sqlTables.put(al.get(i).getItemname(), t);
				tableIds.put(al.get(i).getItemid(), t);
			}

			al = getItemTables();

			String oldName = "";
			String newName = "";
			List<ItemVO> oldNewTablenames = new ArrayList<ItemVO>();
			for (int i = 0; i < al.size(); i++) {

				int id = -1;
				oldName = al.get(i).getTable_name();
				logger.warn("DBMS: found Table Name= {}", oldName);

				if (oldName.startsWith(conf.getTableNamePrefix()) && !oldName.contains("_")) {
					id = Integer.parseInt(oldName.substring(conf.getTableNamePrefix().length()));
					logger.warn("DBMS: found Table with Prefix '{}' Name= {} id={}", conf.getTableNamePrefix(), oldName,
							(id));
				} else if (oldName.contains("_")) {
					id = Integer.parseInt(oldName.substring(oldName.lastIndexOf("_") + 1));
					logger.warn("DBMS: found Table Name= {} id={}", oldName, (id));
				}
				logger.warn("DBMS: found Table id= {}", id);

				newName = tableIds.get(id);
				logger.warn("DBMS: found Table newName= {}", newName);

				if (newName != null) {
					if (!oldName.equalsIgnoreCase(newName)) {
						oldNewTablenames.add(new ItemVO(oldName, newName));
						logger.warn("DBMS: Table '{}' will be renamed to '{}'", oldName, newName);
					} else
						logger.warn("DBMS: Table oldName='{}' newName='{}' nothing to rename", oldName, newName);
				} else {
					logger.error("DBMS: Table '{}' could NOT be renamed to '{}'", oldName, newName);
					break;
				}
			}

			updateItemTableNames(oldNewTablenames);

		} catch (Exception e) {
			logger.error(
					"DBMS: Failed connecting to the SQL db using: serviceName={}, url={}, user={}, password={} e={}",
					conf.getServiceName(), conf.getUrl(), conf.getUser(), conf.getPassword(), e);
		}
		initialized = tmpinit;
	}

	private String getTableName(int rowId, String itemName) {
		return getTableNamePrefix(itemName) + formatRight(rowId, conf.getTableIdDigitCount());
	}

	/**
	 * Queries the {@link PersistenceService} for data with a given filter
	 * criteria
	 * 
	 * @param filter
	 *            the filter to apply to the query
	 * @return a time series of items
	 */
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {

		if (!initialized) {
			logger.debug("DBMS: Query aborted on item {} - db not initialised!", filter.getItemName());
			return Collections.emptyList();
		}

		if (!checkDBAcessability()) {
			logger.debug("Query aborted on item {} - db not connected, give up!", filter.getItemName());
			return Collections.emptyList();
		}

		// Get the item name from the filter
		// Also get the Item object so we can determine the type
		Item item = null;
		String itemName = filter.getItemName();
		logger.debug("DBMS: query: item is {}", itemName);
		try {
			if (itemRegistry != null) {
				item = itemRegistry.getItem(itemName);
			}
		} catch (ItemNotFoundException e1) {
			logger.error("DBMS: Unable to get item type for {}", itemName);

			// Set type to null - data will be returned as StringType
			item = null;
		}

		if (item instanceof GroupItem) {
			// For Group Item is BaseItem needed to get correct Type of Value.
			item = GroupItem.class.cast(item).getBaseItem();
		}

		String table = sqlTables.get(itemName);
		if (table == null) {
			logger.warn("DBMS: Unable to find table for query, no Data in Database, don't know Item: '{}'", itemName);
			// if enabled, table will be created immediately
			if (item != null) {
				logger.warn("DBMS: Try to generate the table for item: '{}'", itemName);
				table = getTable(item);
			} else {
				logger.warn("DBMS: No way to generate the table for item: '{}'", itemName);
				return Collections.emptyList();
			}
		}

		try {
			long timerStart = System.currentTimeMillis();
			List<HistoricItem> items = new ArrayList<HistoricItem>();
			items = getHistItemFilterQuery(filter, conf.getNumberDecimalcount(), table, item);

			logger.info("DBMS: query for {} returned {} rows in {}ms", item.getName(), items.size(),
					System.currentTimeMillis() - timerStart);
			// Success
			errCnt = 0;
			return items;
		} catch (Exception e) {
			errCnt++;
			logger.error("DBMS::query getHistItemFilterQuery failed for {}, Exception = {}", item.getName(),
					e.getMessage());
		}

		return Collections.emptyList();
	}

	/**
	 * @{inheritDoc
	 */
	public void updateConfig(Map<Object, Object> configuration) {
		logger.debug("DBMS:  updateConfig called. configuration.size = " + configuration.size());

		conf = new DbmsConfiguration(configuration);
		checkDBAcessability();
		checkDBSchema();

		// connection has been established ... initialization completed!
		initialized = true;

		logger.debug("DBMS:updateConfig configuration complete for service={}.", getName());
	}

	private String getTableNamePrefix(String itemName) {
		String name = conf.getTableNamePrefix();
		if (conf.getTableUseRealItemNames()) {
			// Create the table name with real Item Names
			name = (itemName.replaceAll(ITEM_NAME_PATTERN, "") + "_").toLowerCase();
		}
		return name;
	}

	private static String formatRight(final Object value, final int len) {
		final String valueAsString = String.valueOf(value);
		if (valueAsString.length() < len) {
			final StringBuffer result = new StringBuffer(len);
			for (int i = len - valueAsString.length(); i > 0; i--) {
				result.append('0');
			}
			result.append(valueAsString);
			return result.toString();
		} else {
			return valueAsString;
		}
	}
}
