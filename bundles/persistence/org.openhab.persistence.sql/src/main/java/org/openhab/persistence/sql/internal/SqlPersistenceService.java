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
package org.openhab.persistence.sql.internal;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
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
 * This is the implementation of the SQL {@link PersistenceService}.
 * 
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson
 * @since 1.1.0
 */
public class SqlPersistenceService implements QueryablePersistenceService, ManagedService {

	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");

	private static final Logger logger = LoggerFactory.getLogger(SqlPersistenceService.class);

	private String driverClass;
	private String url;
	private String user;
	private String password;

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;
	
	// Error counter - used to reconnect to database on error
	private int errCnt;
	private int errReconnectThreshold = 0;

	private Connection connection = null;

	private Map<String, String> sqlTables = new HashMap<String, String>();
	private Map<String, String> sqlTypes = new HashMap<String, String>();

	public void activate() {
		// Initialise the type array
		sqlTypes.put("COLORITEM", "VARCHAR(50)");
		sqlTypes.put("CONTACTITEM", "VARCHAR(50)");
		sqlTypes.put("DATETIMEITEM", "DATETIME");
		sqlTypes.put("DIMMERITEM", "DOUBLE");
		sqlTypes.put("GROUPITEM", "VARCHAR(50)");
		sqlTypes.put("NUMBERITEM", "DOUBLE");
		sqlTypes.put("ROLERSHUTTERITEM", "DOUBLE");
		sqlTypes.put("STRINGITEM", "VARCHAR(50)");
		sqlTypes.put("SWITCHITEM", "DOUBLE");
	}

	public void deactivate() {
		logger.debug("SQL persistence bundle stopping. Disconnecting from database.");
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
		return "sql";
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
				throw new SQLException("SQL: Creating table for item '" + itemName + "' failed.");
			}

			// Create the table name
			tableName = new String("Item" + rowId);
			logger.debug("SQL: new item " + itemName + " is Item" + rowId);
		} catch (SQLException e) {
			logger.error("SQL: Could not create table for item '" + itemName + "': "	+ e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException logOrIgnore) {
				}
			}
		}

		// An error occurred!
		if (tableName == null)
			return null;

		// Default the type to double
		String mysqlType = new String("DOUBLE");
		String itemType = item.getClass().toString().toUpperCase();
		itemType = itemType.substring(itemType.lastIndexOf('.')+1);
		if(sqlTypes.get(itemType) != null) {
			mysqlType = sqlTypes.get(itemType);
		}

		// We have a rowId, create the table for the data
		sqlCmd = new String("CREATE TABLE " + tableName + " (Time DATETIME, Value " + mysqlType + ", PRIMARY KEY(Time));");
		logger.debug("SQL: " + sqlCmd);
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlCmd);

			logger.debug("SQL: Table created for item '" + itemName + "' with datatype " + mysqlType + " in SQL database.");
			sqlTables.put(itemName, tableName);
		} catch (Exception e) {
			logger.error("SQL: Could not create table for item '" + itemName + "' with statement '" + sqlCmd + "': "
					+ e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception hidden) {
				}
			}
		}

		return tableName;
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item, String alias) {
		if (initialized) {

			if (!isConnected())
				connectToDatabase();

			if (isConnected()) {

				String tableName = getTable(item);
				if (tableName == null) {
					logger.error("Unable to store item '{}'.", item.getName());
					return;
				}

				String sqlCmd = null;
				Statement statement = null;
				try {
					statement = connection.createStatement();
					sqlCmd = new String("INSERT INTO " + tableName + " (TIME, VALUE) VALUES(NOW(),'" + item.getState().toString() + "');");
					statement.executeUpdate(sqlCmd);

					logger.debug("SQL: Stored item '{}' as '{}'[{}] in SQL database at {}.", item.getName(),
							item.getState().toString(), item.getState().toString(), (new java.util.Date()).toString());
					logger.debug("SQL: {}", sqlCmd);

					// Success
					errCnt = 0;
				} catch (Exception e) {
					errCnt++;

					logger.error("SQL: Could not store item '{}' in database with statement '{}': {}",
							item.getName(), sqlCmd, e.getMessage());
				} finally {
					if (statement != null) {
						try {
							statement.close();
						} catch (Exception hidden) {
						}
					}
				}
			} else {
				logger.warn(
						"SQL: No connection to database. Can not persist item '{}'! Will retry connecting to database next time.",
						item);
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
		// Error check. If we have 'errReconnectThreshold' errors in a row, then reconnect to the database
		if(errReconnectThreshold != 0 && errCnt > errReconnectThreshold) {
			logger.debug("SQL: Error count exceeded " + errReconnectThreshold + ". Disconnecting database.");
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
			
			logger.debug("SQL: Attempting to connect to database " + url);
			Class.forName(driverClass).newInstance();
			connection = DriverManager.getConnection(url, user, password);
			logger.debug("SQL: Connected to database " + url);

			Statement st = connection.createStatement();
			int result = st.executeUpdate("SHOW TABLES LIKE 'Items'");
			st.close();
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
			logger.error("SQL: Failed connecting to the SQL database using: driverClass=" + driverClass + ", url=" + url
					+ ", user=" + user + ", password=" + password, e);
		}
	}

	/**
	 * Disconnects from the database
	 */
	private void disconnectFromDatabase() {
		if (isConnected()) {
			try {
				connection.close();
				logger.debug("SQL: Disconnected from database " + url);
			} catch (Exception e) {
				logger.warn("SQL: Failed disconnecting from the SQL database", e);
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

				if(!matcher.group(1).equals("sqltype"))
					continue;

				String itemType = matcher.group(2).toUpperCase() + "ITEM";
				String value = (String) config.get(key);

				sqlTypes.put(itemType, value);	
			}
			
			driverClass = (String) config.get("driverClass");
			if (StringUtils.isBlank(driverClass)) {
				throw new ConfigurationException("sql:driverClass",
						"The SQL driver class is missing - please configure the sql:driverClass parameter in openhab.cfg");
			}

			url = (String) config.get("url");
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException("sql:url",
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
						"sql:password",
						"The SQL password is missing. Attempting to connect without password. To specify a password configure the sql:password parameter in openhab.cfg.");
			}

			String errorThresholdString = (String) config.get("reconnectCnt");
			if (StringUtils.isNotBlank(errorThresholdString)) {
				errReconnectThreshold = Integer.parseInt(errorThresholdString);
			}

			disconnectFromDatabase();
			connectToDatabase();

			// connection has been established ... initialization completed!
			initialized = true;
		}
		
	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		if (initialized) {
			if (!isConnected()) {
				connectToDatabase();
			}

			SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (isConnected()) {
				String itemName = filter.getItemName();

				String table= sqlTables.get(itemName);
				if (table == null) {
					logger.error("SQL: Unable to find table for query '" + itemName + "'.");
					return Collections.emptyList();
				}

				String filterString = new String();

				if (filter.getBeginDate()!=null) {
					if(filterString.isEmpty())
						filterString += " WHERE";
					else
						filterString += " AND";
					filterString += " TIME>'" + mysqlDateFormat.format(filter.getBeginDate()) + "'";
				}
				if (filter.getEndDate()!=null) {
					if(filterString.isEmpty())
						filterString += " WHERE";
					else
						filterString += " AND";
					filterString += " TIME<'" + mysqlDateFormat.format(filter.getEndDate().getTime()) + "'";
				}

				if(filter.getOrdering()==Ordering.ASCENDING) {
					filterString += " ORDER BY 'Time' ASC";
				} else {
					filterString += " ORDER BY Time DESC";
				}

				if(filter.getPageSize() != 0x7fffffff)
					filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + "," + filter.getPageSize();
				
				try {
					long timerStart = System.currentTimeMillis();

					// Retrieve the table array
					Statement st = connection.createStatement();

					String queryString = new String();
					queryString = "SELECT Time, Value FROM " + table;
					if(!filterString.isEmpty())
						queryString += filterString;
					
					logger.debug("SQL: "+queryString);

					// Turn use of the cursor on.
					st.setFetchSize(50);

					ResultSet rs = st.executeQuery(queryString);

					long count = 0;
					double value;
					List<HistoricItem> items = new ArrayList<HistoricItem>();
					while (rs.next()) {
						count++;

						//TODO: Make this type specific ???
						value = rs.getDouble(2);
						State v = new DecimalType(value);

						SqlItem sqlItem = new SqlItem(itemName, v, rs.getTimestamp(1));
						items.add(sqlItem);
					}

					rs.close();
					st.close();

					long timerStop = System.currentTimeMillis();
					logger.debug("SQL: query returned {} rows in {}ms", count, timerStop - timerStart);
					
					// Success
					errCnt = 0;

					return items;
				} catch (SQLException e) {
					errCnt++;
					logger.error("SQL: Error running querying : " + e.getMessage());
				}
			}
		}
		return Collections.emptyList();
	}
}
