/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sql.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
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

	private static final Logger logger = LoggerFactory.getLogger(SqlPersistenceService.class);

	private String driverClass;
	private String url;
	private String user;
	private String password;

	private boolean initialized = false;

	private Connection connection = null;

	private Map<String, String> sqlTables = new HashMap<String, String>();
	
	
	public void activate() {
	}

	public void deactivate() {
		logger.debug("SQL persistence bundle stopping. Disconnecting from database.");
		disconnectFromDatabase();
	}

	/**
	 * @{inheritDoc
	 */
	public String getName() {
		return "sql";
	}

	private String getTable(String itemName) {
		Statement statement = null;
		String sqlCmd = null;
		int rowId = 0;

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
				throw new SQLException("Creating table for item '" + itemName + "' failed.");
			}

			// Create the table name
			tableName = new String("Item" + rowId);
		} catch (SQLException e) {
			logger.error("Could not create table for item '" + itemName + "': "	+ e.getMessage());
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

		// We have a rowId, create the table for the data
		sqlCmd = new String("CREATE TABLE " + tableName + " (Time BIGINT, Value DOUBLE, PRIMARY KEY(Time));");
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlCmd);

			logger.debug("Table created for item '" + itemName + "' in SQL database.");
			sqlTables.put(itemName, tableName);
		} catch (Exception e) {
			logger.error("Could not create table for item '" + itemName + "' with statement '" + sqlCmd + "': "
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
		final String name = alias == null ? item.getName() : alias;

		if (initialized) {
			if (!isConnected()) {
				connectToDatabase();
			}

			if (isConnected()) {
				String tableName = getTable(name);
				if (tableName == null) {
					logger.error("Unable to store item '{}'.", item.getName());
					return;
				}

				String sqlCmd = null;
				Statement statement = null;
				try {
					double itemValue;

					itemValue = Double.parseDouble(item.getState().toString());
					// if(Double.isNaN(itemValue))
					statement = connection.createStatement();
					sqlCmd = new String("INSERT INTO " + tableName + " (TIME, VALUE) VALUES('"
							+ System.currentTimeMillis() + "','" + itemValue + "');");
					statement.executeUpdate(sqlCmd);

					logger.debug("Stored item '{}' as '{}'[{}] in SQL database at {}.", new Object[] { item.getName(),
							item.getState().toString(), Double.toString(itemValue), (new java.util.Date()).toString() });
					logger.debug("SQL: {}", sqlCmd);
				} catch (Exception e) {
					logger.error("Could not store item '{}' in database with statement '{}': {}",
							new Object[] { item.getName(), sqlCmd, e.getMessage() });
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
					"No connection to database. Can not persist item '{}'! Will retry connecting to database next time.", item);
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
		return connection != null;
	}

	/**
	 * Connects to the database
	 */
	private void connectToDatabase() {
		try {
			logger.debug("Attempting to connect to database " + url);
			Class.forName(driverClass).newInstance();
			connection = DriverManager.getConnection(url, user, password);
			logger.debug("Connected to database " + url);

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
			logger.error("Failed connecting to the SQL database using: driverClass=" + driverClass + ", url=" + url
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
				logger.debug("Disconnected from database " + url);
				connection = null;
			} catch (Exception e) {
				logger.warn("Failed disconnecting from the SQL database", e);
			}
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
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
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
				throw new ConfigurationException("sql:password",
					"The SQL password is missing. Attempting to connect without password. To specify a password configure the sql:password parameter in openhab.cfg.");
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

			if (isConnected()) {
				String itemName = filter.getItemName();

				String table = getTable(itemName);
				if (table == null) {
					logger.error("Unable to find table for query '" + itemName + "'.");
					return Collections.emptyList();
				}

				long start = filter.getBeginDate() == null ? 0L : filter.getBeginDate().getTime();
				long end = filter.getEndDate() == null ? System.currentTimeMillis() : filter.getEndDate().getTime();
				try {
					// Retrieve the table array
					Statement st = connection.createStatement();

					// Turn use of the cursor on.
					st.setFetchSize(50);
					String query = new String("SELECT Time, Value FROM " + table + " WHERE TIME>" + start
							+ " AND TIME<" + end + " LIMIT " + filter.getPageSize());
					logger.debug("SQL query: " + query);
					ResultSet rs = st.executeQuery(query);

					double value;
					List<HistoricItem> items = new ArrayList<HistoricItem>();
					while (rs.next()) {
						value = rs.getDouble(2);
						State v = new DecimalType(value);
						SqlItem sqlItem = new SqlItem(itemName, v, new Date(rs.getLong(1)));
						items.add(sqlItem);
					}

					rs.close();
					st.close();

					return items;
				} catch (SQLException e) {
					logger.error("Error running SQL querying : " + e.getMessage());
				}
			}
		}
		return Collections.emptyList();
	}
	
}
