/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.postgresql.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.PersistentStateRestorer;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/** Data types equivalent of:
 *
 * MySQL                   PostgreSQL          SQLite
 *
 * TINYINT                 SMALLINT            INTEGER
 * SMALLINT                SMALLINT
 * MEDIUMINT               INTEGER
 * BIGINT                  BIGINT
 * BIT                     BIT                 INTEGER
 * _______________________________________________________
 *
 * TINYINT UNSIGNED        SMALLINT            INTEGER
 * SMALLINT UNSIGNED       INTEGER
 * MEDIUMINT UNSIGNED      INTEGER
 * INT UNSIGNED            BIGINT
 * BIGINT UNSIGNED         NUMERIC(20)
 * _______________________________________________________
 *
 * DOUBLE                  DOUBLE PRECISION    REAL
 * FLOAT                   REAL                REAL
 * DECIMAL                 DECIMAL             REAL
 * NUMERIC                 NUMERIC             REAL
 * _______________________________________________________
 *
 * BOOLEAN                 BOOLEAN             INTEGER
 * _______________________________________________________
 *
 * DATE                    DATE                TEXT
 * TIME                    TIME
 * DATETIME                TIMESTAMP
 * _______________________________________________________
 *
 * TIMESTAMP DEFAULT       TIMESTAMP DEFAULT   TEXT
 * NOW()                   NOW()
 * _______________________________________________________
 *
 * LONGTEXT                TEXT                TEXT
 * MEDIUMTEXT              TEXT                TEXT
 * BLOB                    BYTEA               BLOB
 * VARCHAR                 VARCHAR             TEXT
 * CHAR                    CHAR                TEXT
 * _______________________________________________________
 *
 * columnname INT          columnname SERIAL   INTEGER PRIMARY
 * AUTO_INCREMENT                              KEY AUTOINCREMENT
 *
 *
 *
 *
 * This is the implementation of the PostreSQL {@link PersistenceService}.
 *
 * Data is persisted with the following conversions -:
 *
 * Item-Type    	 Data-Type     PostreSQL-Type
 * =========    	 =========     ==========
 * ColorItem         HSBType       CHAR(25)
 * ContactItem       OnOffType     VARCHAR(6)
 * DateTimeItem      DateTimeType  TIMESTAMP
 * DimmerItem        PercentType   SMALLINT
 * NumberItem        DecimalType   DOUBLE PRECISION
 * RollershutterItem PercentType   SMALLINT
 * StringItem        StringType    VARCHAR(65500)
 * SwitchItem        OnOffType     CHAR(3)
 *
 * In the store method, type conversion is performed where the default type for
 * an item is not as above For example, DimmerType can return OnOffType, so to
 * keep the best resolution, we store as a number in SQL and convert to
 * DecimalType before persisting to PostreSQL.
 *
 *
 * PostreSQL:
 * @author Helmut Lehmeyer
 * @since 1.7.0
 * 
 * Mysql:
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson
 * @since 1.1.0
 * 
 */
public class PostgresqlPersistenceService implements QueryablePersistenceService {

	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");
	
	//private static final Pattern ITEM_NAME_PATTERN = Pattern.compile("[^a-zA-Z]");
	private static final String ITEM_NAME_PATTERN = "[^a-zA-Z_0-9\\-]";

	private static final Logger logger = LoggerFactory.getLogger(PostgresqlPersistenceService.class);

	private String driverClass = "org.postgresql.Driver";
	private String url;
	private String user;
	private String password;
	private int    numberDecimalcount = 3;
	private boolean tableUseRealItemNames = false;
	private String tableNamePrefix = "item";
	private int tableIdDigitCount = 4;
	private boolean rebuildTableNames = false;
	

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;
	@SuppressWarnings("unused")
	private PersistentStateRestorer persistentStateRestorer;

	// Error counter - used to reconnect to database on error
	private int errCnt;
	private int errReconnectThreshold = 0;
	
	private Connection connection = null;

	private Map<String, String> sqlTables = new HashMap<String, String>();
	private Map<String, String> sqlTypes = new HashMap<String, String>();

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
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
		logger.debug(getName()+":  persistence service activated");
		this.bundleContext = bundleContext;
		updateConfig(configuration);

		//for (Entry<String, String> entry : sqlTypes.entrySet()){
		//    logger.debug(getName()+": TEST: {} = {}", entry.getKey(), entry.getValue());
		//}
		logger.debug(getName()+": numberDecimalcount = {}", numberDecimalcount);
    }    

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		logger.debug(getName()+":  persistence bundle stopping. Disconnecting from database. reason={}",reason);
		this.bundleContext = null;
		disconnectFromDatabase();
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug(getName()+":  setItemRegistry");
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		logger.debug(getName()+":  unsetItemRegistry");
		this.itemRegistry = null;
	}
	
	public void setPersistentStateRestorer(PersistentStateRestorer persistentStateRestorer) {
		logger.debug(getName()+":  setPersistentStateRestorer");
		this.persistentStateRestorer = persistentStateRestorer;
	}
	
	public void unsetPersistentStateRestorer(PersistentStateRestorer persistentStateRestorer) {
		logger.debug(getName()+":  unsetPersistentStateRestorer");
		this.persistentStateRestorer = null;
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public String getName() {
		return "postgresql";
	}

	private void initSqlTypes() {
		// Initialise the type array
		sqlTypes.put("CALLITEM", 			"VARCHAR(200)");
		sqlTypes.put("COLORITEM", 			"VARCHAR(70)");
		sqlTypes.put("CONTACTITEM", 		"VARCHAR(6)");
		sqlTypes.put("DATETIMEITEM", 		"TIMESTAMP");		
		sqlTypes.put("DIMMERITEM", 			"SMALLINT");
		//sqlTypes.put("GROUPITEM", 		"VARCHAR(200)");
		sqlTypes.put("LOCATIONITEM", 		"VARCHAR(30)");
		sqlTypes.put("NUMBERITEM", 			"DOUBLE PRECISION");
		sqlTypes.put("ROLLERSHUTTERITEM", 	"SMALLINT");		
		sqlTypes.put("STRINGITEM", 			"VARCHAR(65500)");	
		sqlTypes.put("SWITCHITEM", 			"VARCHAR(3)");
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	private String getItemType(Item i) {
		Item item = i;		
		String def = "STRINGITEM";
		if(i instanceof GroupItem){
			logger.debug(getName()+": Item is GroupItem, try to find ItemType by BaseItem");
			item = ((GroupItem) i).getBaseItem();
			if(item == null){//if GroupItem:<ItemType> is not defined in *.items using StringType
				logger.debug(getName()+": BaseItem GroupItem:<ItemType> is not defined in *.items searching for first Member and try to use as ItemType");
				item = ((GroupItem) i).getMembers().get(0);
				if(item == null){
					logger.debug(getName()+": ItemType NOT found! Use STRINGITEM now.");
					return def;
				}
				logger.debug(getName()+": ItemType found by first Member!");
			}
		}
		String itemType = item.getClass().toString().toUpperCase();
		//Pointsyntax??
		itemType = itemType.substring(itemType.lastIndexOf('.') + 1);
		if(sqlTypes.get(itemType) == null)
			return def;

		//return sqlTypes.get(itemType);
		return itemType;
	}
	
	private String getTable(Item item) {
		PreparedStatement statement = null;
		String sqlCmd = null;
		int rowId = 0;

		String itemName = item.getName();

		String tableName = sqlTables.get(itemName);

		// Table already exists - return the name
		if (tableName != null)
			return tableName;

		logger.debug(getName()+": no Table found for itemName={} get:{}", itemName, sqlTables.get(itemName));

		// Create a new entry in the items table. This is the translation of
		// item name to table
		try {
			sqlCmd = "INSERT INTO Items (ItemName) VALUES (?)";

			statement = connection.prepareStatement(sqlCmd, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, itemName);
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet != null && resultSet.next()) {
				rowId = resultSet.getInt(1);
			}

			if (rowId == 0) {
				throw new SQLException(getName()+": Creating table for item '{}' failed.", itemName);
			}

			// Create the table name with real Item Names
			//String pre = itemName.replaceAll(ITEM_NAME_PATTERN, ""); 
			//tableName = pre + rowId;
			// Create the table name
			//tableName = getTableNamePrefix(itemName) + formatRight(rowId, tableIdDigitCount);
			tableName = getTableName(rowId, itemName);
			logger.debug(getName()+": new item {} is item {}", rowId, itemName);
		} catch (SQLException e) {
			logger.error(getName()+": Could not create table for item '{}': {}", itemName, e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException logOrIgnore) {
				}
			}
		}

		// An error occurred adding the item name into the index list!
		if (tableName == null) {
			logger.error(getName()+": tableName was null");
			return null;
		}
		
		String postgresqlType = sqlTypes.get(getItemType(item));

		// We have a rowId, create the table for the data
		sqlCmd = "CREATE TABLE " + tableName + " (time TIMESTAMP NOT NULL, value " + postgresqlType + ", PRIMARY KEY(time));";
		logger.debug(getName()+": " + sqlCmd);

		try {
			statement = connection.prepareStatement(sqlCmd);
			statement.executeUpdate();

			logger.debug(getName()+": Table created for item '{}' with datatype {} in SQL database.", itemName, postgresqlType);
			sqlTables.put(itemName, tableName);
		} catch (Exception e) {
			logger.error(getName()+": Could not create table for item '{}' with statement '{}': {}",itemName, sqlCmd, e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception hidden) {
				}
			}
		}

		// Check if the new entry is in the table list
		// If it's not in the list, then there was an error and we need to do some tidying up
		// The item needs to be removed from the index table to avoid duplicates
		if(sqlTables.get(itemName) == null) {
			logger.error(getName()+": Item '{}' was not added to the table - removing index", itemName);
			sqlCmd = "DELETE FROM Items WHERE ItemName=?";
			logger.debug(getName()+": " + sqlCmd);

			try {
				statement = connection.prepareStatement(sqlCmd);
				statement.setString(1, itemName);
				statement.executeUpdate();	
			} catch (Exception e) {
				logger.error(getName()+": Could not remove index for item '{}' with statement '{}': {}", itemName, sqlCmd, e.getMessage());
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (Exception hidden) {
					}
				}
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
		logger.debug(getName()+":  store");
		// Don't log undefined/uninitialised data
		if(item.getState() instanceof UnDefType)
			return;

		// If we've not initialised the bundle, then return
		if (initialized == false)
			return;

		// Connect to Postgresql server if we're not already connected
		if (!isConnected())
			connectToDatabase();

		// If we still didn't manage to connect, then return!
		if (!isConnected()) {
			logger.warn(getName()+":  No connection to database. Can not persist item '{}'! Will retry connecting to database when error count:{} equals errReconnectThreshold:{}",
					item,errCnt,errReconnectThreshold);
			return;
		}

		// Get the table name for this item
		String tableName = getTable(item);
		String postgresqlType = getItemType(item);
		if (tableName == null) {
			logger.error(getName()+": Unable to store item '{}'.", item.getName());
			return;
		}

		String sqlCmd = null;
		PreparedStatement statement = null;
		try {

			sqlCmd = "INSERT INTO " + tableName + " (TIME, VALUE) VALUES( NOW(), ?);";
			statement = connection.prepareStatement(sqlCmd);
			
			// Do some type conversion to ensure we know the data type.
			// This is necessary for items that have multiple types and may return their
			// state in a format that's not preferred or compatible with the PostgreSql type.
			// eg. DimmerItem can return OnOffType (ON, OFF), or PercentType (0-100).
			// We need to make sure we cover the best type for serialisation.
			/*
			!!ATTENTION!!
			
			1.
			DimmerItem.getStateAs(PercentType.class).toString() always returns 0
			RollershutterItem.getStateAs(PercentType.class).toString() works as expected
			
			2.
			(item instanceof ColorItem) == (item instanceof DimmerItem) = true
			Therefore for instance tests ColorItem always has to be tested before DimmerItem
			
			!!ATTENTION!!
			*/
			if ("COLORITEM".equals(postgresqlType)) {
				statement.setString(1, item.getStateAs(HSBType.class).toString());
			} else if ("NUMBERITEM".equals(postgresqlType) || "ROLLERSHUTTERITEM".equals(postgresqlType) || "DIMMERITEM".equals(postgresqlType)){
				statement.setObject(1, item.getState(), java.sql.Types.NUMERIC);
			} else if (item instanceof DateTimeItem) {
				Calendar d = ((DateTimeType) item.getState()).getCalendar();
				//http://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
				statement.setDate(1, new java.sql.Date(d.getTime().getTime()));
			} else {
				// All other items should return the best format by default
				statement.setString(1, item.getState().toString());	
			}
			
			statement.executeUpdate();
			
			logger.debug(getName()+": Stored item '{}' as '{}'[{}] in SQL database at {}.", item.getName(),
					item.getState().toString(), (new java.util.Date()).toString());
			logger.debug(getName()+": {}", sqlCmd);

			// Success
			errCnt = 0;
		} catch (Exception e) {
			errCnt++;

			logger.error(getName()+": Could not store item '{}' value '{}' in database with statement '{}': {}", item.getName(), item.getState().toString(),
					sqlCmd, e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception hidden) {
				}
			}
		}
	}

	/**
	 * Checks if we have a database connection
	 *
	 * @return true if connection has been established, false otherwise
	 */
	private boolean isConnected() {
		//Check if connection is valid
		try {
			if (connection!= null && !connection.isValid(5000)) {
				errCnt++;
				logger.error(getName()+": Connection is not valid!");
			}
		} catch (SQLException e) {
			errCnt++;
			logger.error(getName()+": Error while checking connection: {}", e);
		}
		
		// Error check. If we have 'errReconnectThreshold' errors in a row, then
		// reconnect to the database
		if (errReconnectThreshold != 0 && errCnt > errReconnectThreshold) {
			logger.error(getName()+": Error count exceeded {}. Disconnecting database.", errReconnectThreshold);
			disconnectFromDatabase();
		}
		return connection != null;
	}
	

	/**
	 * Connects to the database
	 */
	private void connectToDatabase() {
		try {
			logger.debug(getName()+": errCnt={}", errCnt);
			// Reset the error counter
			errCnt = 0;

			logger.debug(getName()+": Attempting to connect to database {}", url);
			Class.forName(driverClass).newInstance();
			connection = DriverManager.getConnection(url, user, password);
			logger.debug(getName()+": Connected to database!");
			
			PreparedStatement statement = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS items ("
							+ "itemid   SERIAL NOT NULL,"
							+ "itemname VARCHAR(200) NOT NULL,"
							+ "CONSTRAINT items_pkey PRIMARY KEY (itemid)"
					+ ");");
			statement.executeUpdate();
			statement.close();
			
			if(rebuildTableNames){
				formatTableNames();
			}else{
				// Retrieve the table array
				statement = connection.prepareStatement("SELECT itemid, itemname FROM items");
				// Turn use of the cursor on.
				statement.setFetchSize(50);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					logger.debug(getName()+": found Table item{} for itemname {}", rs.getInt(1), rs.getString(2));
					sqlTables.put(rs.getString(2), getTableName(rs.getInt(1), rs.getString(2)));
				}
				rs.close();
				statement.close();
			}
		} catch (Exception e) {
			logger.error(getName()+": Failed connecting to the SQL db using: driverClass={}, url={}, user={}, password={} e={}", driverClass, url, user, password, e);
		}
	}
	
	private void formatTableNames() {

		boolean tmpinit = initialized;
		if(tmpinit)initialized = false;
		
		logger.warn(getName()+": START RENAMING TABLES!");
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("SELECT itemid, itemname FROM items");
			// Turn use of the cursor on.
			statement.setFetchSize(50);
			ResultSet rs = statement.executeQuery();
			HashMap<Integer, String> tables = new HashMap<Integer, String>();
			while (rs.next()) {
				sqlTables.put(rs.getString(2), getTableName(rs.getInt(1), rs.getString(2)));
				tables.put(rs.getInt(1), getTableName(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			statement.close();
			
			statement = connection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema NOT IN ('information_schema', 'pg_catalog') AND table_schema='public' AND NOT table_name='items';");
			statement.setFetchSize(50);
			ResultSet rsNames = statement.executeQuery();

			String oldName = "";
			String newName = "";
			String sqlCmd = "";
			while (rsNames.next()) {

				int id = -1; 
				oldName = rsNames.getString(1); 
				
				if(oldName.startsWith(tableNamePrefix) && !oldName.contains("_")){
					id = Integer.parseInt(oldName.substring("items".length()-1));
					logger.warn(getName()+": found Table Name= {} id={}", rsNames.getString(1), (id));
				}else if(oldName.contains("_")){
					id = Integer.parseInt(oldName.substring(oldName.lastIndexOf("_")+1));
					logger.warn(getName()+": found Table Name= {} id={}", rsNames.getString(1), (id));
				}
				logger.warn(getName()+": found Table Name= {}", rsNames.getString(1));

				newName = tables.get(id);
				
				if(tables.get(id) != null){
					if(!oldName.equalsIgnoreCase(newName)){
						sqlCmd += "ALTER TABLE "+ oldName + " RENAME TO "+ newName + ";";
						logger.warn(getName()+": Table '{}' will be renamed to '{}'", oldName, newName);
					}else logger.warn(getName()+": Table oldName='{}' newName='{}' nothing to rename", oldName, newName);
				}else{
					logger.error(getName()+": Table '{}' could NOT be renamed to '{}'", oldName, newName);
					break;
				}
			}
			
			if(sqlCmd.length() > 1){
				statement = connection.prepareStatement(sqlCmd);
				statement.executeUpdate();
			}else logger.warn(getName()+": Nothing to rename sqlCmd = {}. PLEASE set rebuildTableNames config back to false", sqlCmd);
			
			rsNames.close();
			statement.close();
		} catch (SQLException e) {
			logger.error(getName()+": Failed formatTableNames: {}. PLEASE set rebuildTableNames config back to false", e.getMessage());
		}
		initialized = tmpinit;
	}

	private String getTableName(int rowId, String itemName ){
		return getTableNamePrefix(itemName) + formatRight(rowId, tableIdDigitCount);
	}

	/**
	 * Disconnects from the database
	 */
	private void disconnectFromDatabase() {
		if (isConnected()) {
			try {
				connection.close();
				logger.debug(getName()+": Disconnected from database " + url);
			} catch (Exception e) {
				logger.error(getName()+": Failed disconnecting from the SQL database", e);
			}
			connection = null;
		}
	}

	/**
	 * Formats the given <code>alias</code> by utilizing {@link Formatter}.
	 *
	 * @param alias
	 *            the alias String which contains format strings
	 * @param values
	 *            the values which will be replaced in the alias String
	 *
	 * @return the formatted value. All format strings are replaced by
	 *         appropriate values
	 * @see java.util.Formatter for detailed information on format Strings.
	 */
	protected String formatAlias(String alias, Object... values) {
		return String.format(alias, values);
	}
	
	/**
	 * Queries the {@link PersistenceService} for data with a given filter criteria
	 * 
	 * @param filter the filter to apply to the query
	 * @return a time series of items
	 */
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		logger.debug(getName()+":  query");
		String queryString = "";
		if (!initialized) {
			logger.debug(getName()+": Query aborted on item {} - db not initialised!", filter.getItemName());
			return Collections.emptyList();
		}

		if (!isConnected())
			connectToDatabase();

		if (!isConnected()) {
			logger.debug("Query aborted on item {} - db not connected!", filter.getItemName());
			return Collections.emptyList();
		}

		SimpleDateFormat postgresqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Get the item name from the filter
		// Also get the Item object so we can determine the type
		Item item = null;
		String itemName = filter.getItemName();
		logger.debug(getName()+": query: item is {}", itemName);
		try {
			if (itemRegistry != null) {
				item = itemRegistry.getItem(itemName);
			}
		} catch (ItemNotFoundException e1) {
			logger.error(getName()+": Unable to get item type for {}", itemName);

			// Set type to null - data will be returned as StringType
			item = null;
		}
        
		if(item instanceof GroupItem){
			// For Group Items is BaseItem needed to get correct Type of Value.
			item = GroupItem.class.cast(item).getBaseItem();
		}

		String table = sqlTables.get(itemName);
		if (table == null) {

			logger.warn(getName()+": Unable to find table for query, no Data in Database, don't know Item: '{}'", itemName );
			//if enabled, table will be created immediately
			if(item != null){
				logger.warn(getName()+": Try to generate the table for item: '{}'", itemName );
				table = getTable(item);
			}else{
				logger.warn(getName()+": No way to generate the table for item: '{}'", itemName );
				return Collections.emptyList();
			}
		}

		String filterString = "";		
		
		if (filter.getBeginDate() != null) {
			if (filterString.isEmpty())
				filterString += " WHERE";
			else
				filterString += " AND";
			
			filterString += " TIME>'" + postgresqlDateFormat.format(filter.getBeginDate()) + "'";
		}
		if (filter.getEndDate() != null) {
			if (filterString.isEmpty())
				filterString += " WHERE";
			else
				filterString += " AND";
			
			filterString += " TIME<'" + postgresqlDateFormat.format(filter.getEndDate().getTime()) + "'";
		}

		if (filter.getOrdering() == Ordering.ASCENDING) {
			filterString += " ORDER BY time ASC";
		} else {
			filterString += " ORDER BY time DESC";
		}

		if (filter.getPageSize() != 0x7fffffff)
			//see: http://www.jooq.org/doc/3.5/manual/sql-building/sql-statements/select-statement/limit-clause/
			/*OLD Bug
			filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + " OFFSET " + filter.getPageSize();*/
			filterString += " OFFSET " + filter.getPageNumber() * filter.getPageSize() + " LIMIT " + filter.getPageSize();
		try {
			long timerStart = System.currentTimeMillis();

			queryString = (item instanceof NumberItem) ? "SELECT time, ROUND(CAST (value AS numeric)," + numberDecimalcount + ") FROM " + table : "SELECT time, value FROM " + table;
			if (!filterString.isEmpty())
				queryString += filterString;
			
			// Retrieve the table array
			PreparedStatement statement = connection.prepareStatement(queryString);
			
			// Turn use of the cursor on.
			statement.setFetchSize(50);

			//ONLY FOR DEBUG
			//SELECT time, ROUND(value,3) AS value FROM item32 ORDER BY time DESC OFFSET 0 LIMIT 1
			//logger.debug(getName()+": query: queryString is {}", queryString);
			
			ResultSet rs = statement.executeQuery();

			long count = 0;
			List<HistoricItem> items = new ArrayList<HistoricItem>();
			State state = null;
			String type = "none";		
			
			while (rs.next()) {
				count++;
				
				logger.debug(getName()+":");
				logger.debug(getName()+": itemName= "+itemName);

				if (item instanceof NumberItem){
					//Test like in mysql
					logger.debug(getName()+": found NumberItem val1='{}'",rs.getString(2));
					//state = new DecimalType(rs.getDouble(2));
					//state = new DecimalType(String.valueOf(rs.getObject(2)));
					state = new DecimalType(rs.getString(2));
				}else if (item instanceof ColorItem){
					logger.debug(getName()+": found ColorItem");
					state = new HSBType(rs.getString(2));
				}else if (item instanceof DimmerItem){
					logger.debug(getName()+": found DimmerItem");
					state = new PercentType(rs.getInt(2));
				}else if (item instanceof SwitchItem){
					logger.debug(getName()+": found SwitchItem val='{}'",rs.getString(2));
					//This works only with varchar(3-n)!
					//state = OnOffType.valueOf(rs.getString(2));
					//This works with char(3-n) too, used for backward compatibility
					state = rs.getString(2) == "OFF" ? OnOffType.OFF : OnOffType.ON;
				}else if (item instanceof ContactItem){
					logger.debug(getName()+": found ContactItem val='{}'",rs.getString(2));
					//OLD state = rs.getString(2) == "CLOSED" ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
					state = OpenClosedType.valueOf(rs.getString(2));
				}else if (item instanceof RollershutterItem){
					logger.debug(getName()+": found RollershutterItem");
					state = new PercentType(rs.getInt(2));
				}else if (item instanceof DateTimeItem) {
					logger.debug(getName()+": found DateTimeItem String='{}'",rs.getString(2));
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(rs.getTimestamp(2).getTime());
					state = new DateTimeType(calendar);
				}else if (item instanceof StringItem) {
					logger.debug(getName()+": found StringItem");
					state = new StringType(rs.getString(2));
				} else {//Call, Location, String
					logger.debug(getName()+": no instanceof Item found, seems to be Call or Location, use StringType");
					state = new StringType(rs.getString(2));
				}
				
				PostgresqlItem postgresqlItem = new PostgresqlItem(itemName, state, rs.getTimestamp(1));
				logger.debug(getName()+": postgresqlItem= "+type+" "+ postgresqlItem.toString());

				items.add(postgresqlItem);
			}

			rs.close();
			statement.close();

			long timerStop = System.currentTimeMillis();
			logger.debug(getName()+": query returned {} rows in {}ms", count, timerStop - timerStart);

			if(items.size()==0) logger.warn(getName()+": No data found for item {}!", itemName);
			// Success
			errCnt = 0;

			return items;
		} catch (SQLException e) {
			errCnt++;
			logger.error(getName()+": Error running querying : " + e.getMessage());
			logger.error("-> fatal queryString : " + queryString);
		}
		return null;
	}
	
	/**
	 * @{inheritDoc
	 */
	//@Override
	//public void updated(Dictionary<String, ?> config) throws ConfigurationException {
	public void updateConfig(Map<Object, Object> configuration){
		logger.debug(getName()+":  updateConfig called. configuration.size = "+configuration.size());
		
		initSqlTypes();

		@SuppressWarnings("unchecked")
		Enumeration<String> keys = new IteratorEnumeration(configuration.keySet().iterator());

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

			if (!matcher.matches()) {
				continue;
			}

			matcher.reset();
			matcher.find();

			if (!matcher.group(1).equals("sqltype"))
				continue;

			String itemType = matcher.group(2).toUpperCase() + "ITEM";
			String value = (String) configuration.get(key);

			sqlTypes.put(itemType, value);
		}

		url = (String) configuration.get("url");
		logger.debug(getName()+":  url = "+url);
		
		if (StringUtils.isBlank(url)) {
			logger.error(getName()+": updateConfig:url The SQL database URL is missing - please configure the sql:url parameter in openhab.cfg");
		}

		user = (String) configuration.get("user");
		if (StringUtils.isBlank(user)) {
			logger.error(getName()+": updateConfig:sql:user The SQL user is missing - please configure the sql:user parameter in openhab.cfg");
		}

		password = (String) configuration.get("password");
		if (StringUtils.isBlank(password)) {
			logger.error(getName()+": updateConfig:password The SQL password is missing. Attempting to connect without password. To specify a password configure the sql:password parameter in openhab.cfg.");
		}

		String et = (String) configuration.get("reconnectCnt");
		if (StringUtils.isNotBlank(et)) {
			errReconnectThreshold = Integer.parseInt(et);
		}

		String dd = (String) configuration.get("numberDecimalcount");
		if (StringUtils.isNotBlank(dd)) {
			numberDecimalcount = Integer.parseInt(dd);
		}

		String rn = (String) configuration.get("tableUseRealItemNames");
		if (StringUtils.isNotBlank(rn)) {
			tableUseRealItemNames = "true".equals(rn) ? Boolean.parseBoolean(rn) : false;
		}

		String td = (String) configuration.get("tableIdDigitCount");
		if (StringUtils.isNotBlank(td)) {
			tableIdDigitCount = Integer.parseInt(td);
		}

		String rt = (String) configuration.get("rebuildTableNames");
		if (StringUtils.isNotBlank(rt)) {
			rebuildTableNames = "true".equals(rt) ? Boolean.parseBoolean(rt) : false;
		}
		
		
		
		// reconnect to the database in case the configuration has changed.
		disconnectFromDatabase();
		connectToDatabase();

		// connection has been established ... initialization completed!
		initialized = true;
		
		logger.debug(getName()+":updateConfig configuration complete. service={}",getName());
	}

	private String getTableNamePrefix(String itemName) {
		String name = tableNamePrefix;
		if(tableUseRealItemNames){
			// Create the table name with real Item Names
			name = (itemName.replaceAll(ITEM_NAME_PATTERN, "")+"_").toLowerCase(); 
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
