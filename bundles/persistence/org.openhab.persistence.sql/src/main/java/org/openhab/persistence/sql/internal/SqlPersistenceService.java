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
package org.openhab.persistence.sql.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Formatter;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the implementation of the SQL {@link PersistenceService}.
 * 
 * @author Henrik Sjöstrand
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.1.0
 */
public class SqlPersistenceService implements PersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SqlPersistenceService.class);

	private String driverClass;
	private String url;
	private String user;
	private String password;

	private boolean initialized = false;

	private Connection connection = null;
	
	
	public void activate() {
	}
	
	public void deactivate() {
		logger.debug("SQL persistence bundle stopping. Disconnecting from database.");
		disconnectFromDatabase();
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "sql";
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item, String alias) {
		if (initialized) {
			if (!isConnected()) {
				connectToDatabase();
			}

			if (isConnected()) {
				Statement statement = null;
				try {
					statement = connection.createStatement();
					String sqlCmd = formatAlias(alias, 
						item.getState().toString(), Calendar.getInstance().getTime());
					statement.executeUpdate(sqlCmd);

					logger.debug("Stored item '{}' as '{}' in SQL database at {}.",
							new String[] { item.getName(), item.getState().toString(), (new java.util.Date()).toString() });

				} catch (Exception e) {
					logger.error("Could not store item " + item + " in database.", e);
				} finally {
					if (statement != null) {
						try {
							statement.close();
						} catch (Exception hidden) {
						}
					}
				}
			} else {
				logger.warn("No connection to database. Can not persist item! Will retry connecting to database next time." + item);
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		throw new UnsupportedOperationException(
				"The SQL service requires aliases for persistence configurations that should match the SQL statement. Please configure sql.persist properly.");
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
		} catch (Exception e) {
			logger.error("Failed connecting to the SQL database using: driverClass="
					+ driverClass + ", url=" + url
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
	 * @param alias the alias String which contains format strings
	 * @param values the values which will be replaced in the alias String
	 * 
	 * @return the formatted value. All format strings are replaced by 
	 * appropriate values
	 * @see java.util.Formatter for detailed information on format Strings.
	 */
	protected String formatAlias(String alias, Object... values) {
		return String.format(alias, values);
	}


	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			driverClass = (String) config.get("driverClass");
			if (StringUtils.isBlank(driverClass)) {
				throw new ConfigurationException("sql:driverClass", "The SQL driver class is missing - please configure the sql:driverClass parameter in openhab.cfg");
			}

			url = (String) config.get("url");
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException("sql:url", "The SQL database URL is missing - please configure the sql:url parameter in openhab.cfg");
			}

			user = (String) config.get("user");
			if (StringUtils.isBlank(user)) {
				throw new ConfigurationException("sql:user", "The SQL user is missing - please configure the sql:user parameter in openhab.cfg");
			}

			password = (String) config.get("password");
			if (StringUtils.isBlank(password)) {
				throw new ConfigurationException("sql:password", "The SQL password is missing. Attempting to connect without password. To specify a password configure the sql:password parameter in openhab.cfg.");
			}

			disconnectFromDatabase();
			connectToDatabase();
			
			// connection has been established ... initialization completed!
			initialized = true;
		}
	}
	

}
