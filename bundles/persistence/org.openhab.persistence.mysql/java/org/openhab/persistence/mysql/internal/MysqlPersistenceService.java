/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.mysql.internal;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the implementation of the SQL {@link PersistenceService}.
 * 
 * Data is persisted with the following conversions -:
 * 
 * Item-Type    Data-Type          MySQL-Type 
 * =========    =========          ========== 
 * ColorItem         HSBType       CHAR(25)
 * ContactItem       OnOffType     CHAR(6)
 * DateTimeItem      DateTimeType  DATETIME
 * DimmerItem        PercentType   TINYINT
 * NumberItem        DecimalType   DOUBLE
 * RollershutterItem PercentType   TINYINT
 * StringItem        StringType    VARCHAR(20000)
 * SwitchItem        OnOffType     CHAR(3)
 * 
 * In the store method, type conversion is performed where the default type for
 * an item is not as above For example, DimmerType can return OnOffType, so to
 * keep the best resolution, we store as a number in SQL and convert to
 * DecimalType before persisting to MySQL.
 * 
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson
 * @since 1.1.0
 */
public class MysqlPersistenceService implements QueryablePersistenceService, ManagedService {

	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");

	private static final Logger logger = LoggerFactory.getLogger(MysqlPersistenceService.class);

	private String driverClass = "com.mysql.jdbc.Driver";
	private String url;
	private String user;
	private String password;

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;

	// Error counter - used to reconnect to database on error
	private int errCnt;
	private int errReconnectThreshold = 0;
	
	private int waitTimeout = -1;

	private Connection connection = null;

	private Map<String, String> sqlTables = new HashMap<String, String>();
	private Map<String, String> sqlTypes = new HashMap<String, String>();

	public void activate() {
		// Initialise the type array
		sqlTypes.put("COLORITEM", "CHAR(25)");
		sqlTypes.put("CONTACTITEM", "VARCHAR(6)");
		sqlTypes.put("DATETIMEITEM", "DATETIME(3)");
		sqlTypes.put("DIMMERITEM", "TINYINT");
		sqlTypes.put("GROUPITEM", "DOUBLE");
		sqlTypes.put("NUMBERITEM", "DOUBLE");
		sqlTypes.put("ROLERSHUTTERITEM", "TINYINT");
		sqlTypes.put("STRINGITEM", "VARCHAR(20000)");
		sqlTypes.put("SWITCHITEM", "CHAR(3)");
	}

	public void deactivate() {
		logger.debug("mySQL persistence bundle stopping. Disconnecting from database.");
		disconnectFromDatabase();
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * @{inheritDoc
	 */
	public String getName() {
		return "mysql";
	}

	private String getTable(Item item) {
		Statement statement = null;
		String sqlCmd = null;
		int rowId = 0;

		String itemName = item.getName();

		String tableName = sqlTables.get(itemName);

		// Table already exists - return the name
		if (tableName != null)
			return tableName;

		// Create a new entry in the Items table. This is the translation of
		// item name to table
		try {
			sqlCmd = new String("INSERT INTO Items (ItemName) VALUES ('" + itemName + "')");

			statement = connection.createStatement();
			statement.executeUpdate(sqlCmd, Statement.RETURN_GENERATED_KEYS);

			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet != null && resultSet.next()) {
				rowId = resultSet.getInt(1);
			}

			if (rowId == 0) {
				throw new SQLException("mySQL: Creating table for item '" + itemName + "' failed.");
			}

			// Create the table name
			tableName = new String("Item" + rowId);
			logger.debug("mySQL: new item {} is Item{}", itemName, rowId);
		} catch (SQLException e) {
			logger.error("mySQL: Could not create table for item '{}': ", itemName, e.getMessage());
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
			logger.error("mySQL: tableName was null");
			return null;
		}

		// Default the type to double
		String mysqlType = new String("DOUBLE");
		String itemType = item.getClass().toString().toUpperCase();
		itemType = itemType.substring(itemType.lastIndexOf('.') + 1);
		if (sqlTypes.get(itemType) != null) {
			mysqlType = sqlTypes.get(itemType);
		}

		// We have a rowId, create the table for the data
		sqlCmd = new String("CREATE TABLE " + tableName + " (Time DATETIME, Value " + mysqlType
				+ ", PRIMARY KEY(Time));");
		logger.debug("SQL: " + sqlCmd);

		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlCmd);

			logger.debug("mySQL: Table created for item '" + itemName + "' with datatype " + mysqlType
					+ " in SQL database.");
			sqlTables.put(itemName, tableName);
		} catch (Exception e) {
			logger.error("mySQL: Could not create table for item '" + itemName + "' with statement '" + sqlCmd + "': "
					+ e.getMessage());			
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
			logger.error("mySQL: Item '{}' was not added to the table - removing index", itemName);
			sqlCmd = new String("DELETE FROM Items WHERE ItemName='" + itemName+"'");
			logger.debug("SQL: {}", sqlCmd);
	
			try {
				statement = connection.createStatement();
				statement.executeUpdate(sqlCmd);	
			} catch (Exception e) {
				logger.error("mySQL: Could not remove index for item '" + itemName + "' with statement '" + sqlCmd + "': "
						+ e.getMessage());			
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
	public void store(Item item, String alias) {
		// Don't log undefined/uninitialised data
		if(item.getState() instanceof UnDefType)
			return;

		// If we've not initialised the bundle, then return
		if (initialized == false)
			return;

		// Connect to mySQL server if we're not already connected 
		if (!isConnected())
			connectToDatabase();

		// If we still didn't manage to connect, then return!
		if (!isConnected()) {
			logger.warn(
					"mySQL: No connection to database. Can not persist item '{}'! Will retry connecting to database next time.",
					item);
			return;
		}

		// Get the table name for this item
		String tableName = getTable(item);
		if (tableName == null) {
			logger.error("Unable to store item '{}'.", item.getName());
			return;
		}

		// Do some type conversion to ensure we know the data type.
		// This is necessary for items that have multiple types and may return their
		// state in a format that's not preferred or compatible with the MySQL type.
		// eg. DimmerItem can return OnOffType (ON, OFF), or PercentType (0-100).
		// We need to make sure we cover the best type for serialisation.
		String value;
		if (item instanceof DimmerItem || item instanceof RollershutterItem) {
			value = item.getStateAs(PercentType.class).toString();
		} else if (item instanceof ColorItem) {
			value = item.getStateAs(HSBType.class).toString();
		} else {
			// All other items should return the best format by default
			value = item.getState().toString();
		}

		String sqlCmd = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			sqlCmd = new String("INSERT INTO " + tableName + " (TIME, VALUE) VALUES(NOW(),'"
					+ item.getState().toString() + "') ON DUPLICATE KEY UPDATE VALUE='"
					+ item.getState().toString() + "';");
			statement.executeUpdate(sqlCmd);

			logger.debug("mySQL: Stored item '{}' as '{}'[{}] in SQL database at {}.", item.getName(), item.getState()
					.toString(), value, (new java.util.Date()).toString());
			logger.debug("mySQL: {}", sqlCmd);

			// Success
			errCnt = 0;
		} catch (Exception e) {
			errCnt++;

			logger.error("mySQL: Could not store item '{}' in database with statement '{}': {}", item.getName(),
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
	 * @{inheritDoc
	 */
	public void store(Item item) {
		store(item, null);
	}

	/**
	 * Checks if we have a database connection
	 * 
	 * @return true if connection has been established, false otherwise
	 */
	private boolean isConnected() {
		// Error check. If we have 'errReconnectThreshold' errors in a row, then
		// reconnect to the database
		if (errReconnectThreshold != 0 && errCnt >= errReconnectThreshold) {
			logger.error("mySQL: Error count exceeded {}. Disconnecting database.", errReconnectThreshold);
			disconnectFromDatabase();
		}
		return connection != null;
	}

	/**
	 * Connects to the database
	 */
	private void connectToDatabase() {
		try {
			// Reset the error counter
			errCnt = 0;

			logger.debug("mySQL: Attempting to connect to database {}", url);
			Class.forName(driverClass).newInstance();
			connection = DriverManager.getConnection(url, user, password);
			logger.debug("mySQL: Connected to database {}", url);

			Statement st = connection.createStatement();
			int result = st.executeUpdate("SHOW TABLES LIKE 'Items'");
			st.close();
			
			if(waitTimeout != -1) {
				logger.debug("mySQL: Setting wait_timeout to {} seconds.", waitTimeout);
				st = connection.createStatement();
				st.executeUpdate("SET SESSION wait_timeout="+waitTimeout);
				st.close();
			}
			if (result == 0) {
				st = connection.createStatement();
				st.executeUpdate(
						"CREATE TABLE Items (ItemId INT NOT NULL AUTO_INCREMENT,ItemName VARCHAR(200) NOT NULL,PRIMARY KEY (ItemId));",
						Statement.RETURN_GENERATED_KEYS);
				st.close();
			}

			// Retrieve the table array
			st = connection.createStatement();

			// Turn use of the cursor on.
			st.setFetchSize(50);
			ResultSet rs = st.executeQuery("SELECT ItemId, ItemName FROM Items");
			while (rs.next()) {
				sqlTables.put(rs.getString(2), "Item" + rs.getInt(1));
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			logger.error("mySQL: Failed connecting to the SQL database using: driverClass=" + driverClass + ", url="
					+ url + ", user=" + user + ", password=" + password, e);
		}
	}

	/**
	 * Disconnects from the database
	 */
	private void disconnectFromDatabase() {
		if (connection != null) {
			try {
				connection.close();
				logger.debug("mySQL: Disconnected from database {}", url);
			} catch (Exception e) {
				logger.error("mySQL: Failed disconnecting from the SQL database {}", e);
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
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("mySQL configuration starting");
		if (config != null) {
			Enumeration<String> keys = config.keys();

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					continue;
				}

				matcher.reset();
				matcher.find();

				if (!matcher.group(1).equals("sqltype"))
					continue;

				String itemType = matcher.group(2).toUpperCase() + "ITEM";
				String value = (String) config.get(key);

				sqlTypes.put(itemType, value);
			}

			url = (String) config.get("url");
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException("mysql:url",
						"The SQL database URL is missing - please configure the sql:url parameter in openhab.cfg");
			}

			user = (String) config.get("user");
			if (StringUtils.isBlank(user)) {
				throw new ConfigurationException("sql:user",
						"The SQL user is missing - please configure the sql:user parameter in openhab.cfg");
			}

			password = (String) config.get("password");
			if (StringUtils.isBlank(password)) {
				throw new ConfigurationException(
						"mysql:password",
						"The SQL password is missing. Attempting to connect without password. To specify a password configure the sql:password parameter in openhab.cfg.");
			}

			String tmpString = (String) config.get("reconnectCnt");
			if (StringUtils.isNotBlank(tmpString)) {
				errReconnectThreshold = Integer.parseInt(tmpString);
			}

			tmpString = (String) config.get("waitTimeout");
			if (StringUtils.isNotBlank(tmpString)) {
				waitTimeout = Integer.parseInt(tmpString);
			}

			// reconnect to the database in case the configuration has changed.
			disconnectFromDatabase();
			connectToDatabase();

			// connection has been established ... initialization completed!
			initialized = true;
			
			logger.debug("mySQL configuration complete.");
		}

	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		if (!initialized) {
			logger.debug("Query aborted on item {} - mySQL not initialised!", filter.getItemName());
			return Collections.emptyList();
		}

		if (!isConnected())
			connectToDatabase();

		if (!isConnected()) {
			logger.debug("Query aborted on item {} - mySQL not connected!", filter.getItemName());
			return Collections.emptyList();
		}

		SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Get the item name from the filter
		// Also get the Item object so we can determine the type
		Item item = null;
		String itemName = filter.getItemName();
		logger.debug("mySQL query: item is {}", itemName);
		try {
			if (itemRegistry != null) {
				item = itemRegistry.getItem(itemName);
			}
		} catch (ItemNotFoundException e1) {
			logger.error("Unable to get item type for {}", itemName);

			// Set type to null - data will be returned as StringType
			item = null;
		}
                   
        if(item instanceof GroupItem){
            // For Group Items is BaseItem needed to get correct Type of Value.
            item = GroupItem.class.cast(item).getBaseItem();
        }

		String table = sqlTables.get(itemName);
		if (table == null) {
			logger.error("mySQL: Unable to find table for query '" + itemName + "'.");
			return Collections.emptyList();
		}

		String filterString = new String();

		if (filter.getBeginDate() != null) {
			if (filterString.isEmpty())
				filterString += " WHERE";
			else
				filterString += " AND";
			filterString += " TIME>'" + mysqlDateFormat.format(filter.getBeginDate()) + "'";
		}
		if (filter.getEndDate() != null) {
			if (filterString.isEmpty())
				filterString += " WHERE";
			else
				filterString += " AND";
			filterString += " TIME<'" + mysqlDateFormat.format(filter.getEndDate().getTime()) + "'";
		}

		if (filter.getOrdering() == Ordering.ASCENDING) {
			filterString += " ORDER BY Time ASC";
		} else {
			filterString += " ORDER BY Time DESC";
		}

		if (filter.getPageSize() != 0x7fffffff)
			filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + "," + filter.getPageSize();

		try {
			long timerStart = System.currentTimeMillis();

			// Retrieve the table array
			Statement st = connection.createStatement();

			String queryString = new String();
			queryString = "SELECT Time, Value FROM " + table;
			if (!filterString.isEmpty())
				queryString += filterString;

			logger.debug("mySQL: " + queryString);

			// Turn use of the cursor on.
			st.setFetchSize(50);

			ResultSet rs = st.executeQuery(queryString);

			long count = 0;
			List<HistoricItem> items = new ArrayList<HistoricItem>();
			State state;
			while (rs.next()) {
				count++;

				if (item instanceof NumberItem)
					state = new DecimalType(rs.getDouble(2));
				else if (item instanceof ColorItem)
					state = new HSBType(rs.getString(2));
				else if (item instanceof DimmerItem)
					state = new PercentType(rs.getInt(2));
				else if (item instanceof SwitchItem)
					state = OnOffType.valueOf(rs.getString(2));
				else if (item instanceof ContactItem)
					state = OpenClosedType.valueOf(rs.getString(2));
				else if (item instanceof RollershutterItem)
					state = new PercentType(rs.getInt(2));
				else if (item instanceof DateTimeItem) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(rs.getTimestamp(2).getTime());
					state = new DateTimeType(calendar);
				} else
					state = new StringType(rs.getString(2));

				MysqlItem mysqlItem = new MysqlItem(itemName, state, rs.getTimestamp(1));
				items.add(mysqlItem);
			}

			rs.close();
			st.close();

			long timerStop = System.currentTimeMillis();
			logger.debug("mySQL: query returned {} rows in {}ms", count, timerStop - timerStart);

			// Success
			errCnt = 0;

			return items;
		} catch (SQLException e) {
			errCnt++;
			logger.error("mySQL: Error running querying : " + e.getMessage());
		}
		return null;
	}
}
