/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.h2sql.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
//import org.openhab.core.persistence.PersistentStateRestorer;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This is the implementation of the H2 SQL {@link PersistenceService}.
 * See http://h2database.com
 * 
 * H2 database licensed under EPL (http://h2database.com/html/license.html)
 * 
 * Data is persisted with the following conversions -:
 * 
 * Item-Type         Data-Type    H2SQL-Type
 * =========         =========    ==========
 * ColorItem         HSBType      CHAR(25)
 * ContactItem       OnOffType    CHAR(6)
 * DateTimeItem      DateTimeType DATETIME
 * DimmerItem        PercentType  TINYINT
 * NumberItem        DecimalType  DOUBLE
 * RollershutterItem PercentType  TINYINT
 * StringItem        StringType   VARCHAR(20000)
 * SwitchItem        OnOffType    CHAR(3)
 * 
 * In the store method, type conversion is performed where the default type for
 * an item is not as above For example, DimmerType can return OnOffType, so to
 * keep the best resolution, we store as a number in SQL and convert to
 * DecimalType before persisting to H2SQL.
 * 
 * @author Chris Jackson - Initial contribution
 * @since 1.8.0
 */
public class H2SqlPersistenceService implements QueryablePersistenceService, ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(H2SqlPersistenceService.class);

    private String driverClass = "org.h2.Driver";

    protected ItemRegistry itemRegistry;

    private Connection connection = null;

    private Map<String, String> sqlTypes = new HashMap<String, String>();

    public void activate() {
        logger.debug("H2SQL: Persistence bundle activate.");

        // Initialise the type array
        sqlTypes.put("COLORITEM", "VARCHAR(70)");
        sqlTypes.put("CONTACTITEM", "VARCHAR(6)");
        sqlTypes.put("DATETIMEITEM", "DATETIME");
        sqlTypes.put("DIMMERITEM", "TINYINT");
        sqlTypes.put("GROUPITEM", "DOUBLE");
        sqlTypes.put("NUMBERITEM", "DOUBLE");
        sqlTypes.put("ROLERSHUTTERITEM", "TINYINT");
        sqlTypes.put("STRINGITEM", "VARCHAR(20000)");
        sqlTypes.put("SWITCHITEM", "CHAR(3)");
    }

    public void deactivate() {
        logger.debug("H2SQL: Persistence bundle deactivate. Disconnecting from database.");
        disconnectFromDatabase();
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

    // public void setPersistentStateRestorer(PersistentStateRestorer persistentStateRestorer) {
    // this.persistentStateRestorer = persistentStateRestorer;
    // }

    // public void unsetPersistentStateRestorer(PersistentStateRestorer persistentStateRestorer) {
    // this.persistentStateRestorer = null;
    // }

    /**
     * @{inheritDoc
     */
    public String getName() {
        return "h2sql";
    }

    /**
     * @{inheritDoc
     */
    public void store(Item item) {
        if (item.getState() == null || item.getState() instanceof UnDefType) {
            logger.debug("H2SQL: State of {} [{}] is {}. Store aborted", item.getName(), item.getClass()
                    .getSimpleName(), item.getState());
            return;
        }

        // Connect to H2SQL server if we're not already connected
        if (!isConnected()) {
            connectToDatabase();
        }

        // If we still didn't manage to connect, then return!
        if (!isConnected()) {
            logger.warn("H2SQL: No connection to database. Can not persist item '{}'", item);
            return;
        }

        // A bit of profiling!
        long timerStart = System.currentTimeMillis();

        // Default the type to double
        String sqlType = new String("DOUBLE");
        String itemType = item.getClass().toString().toUpperCase();
        itemType = itemType.substring(itemType.lastIndexOf('.') + 1);
        if (sqlTypes.get(itemType) != null) {
            sqlType = sqlTypes.get(itemType);
        }

        Statement statement = null;
        String sqlCmd = null;

        // Create the table for the data
        sqlCmd = new String("CREATE TABLE IF NOT EXISTS openhab." + item.getName() + " (Time DATETIME, Value " + sqlType
                + ", PRIMARY KEY(Time));");
        logger.debug("H2SQL: " + sqlCmd);

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCmd);

            logger.debug("H2SQL: Table created for item '{}' with datatype '{}'", item.getName(), sqlType);
        } catch (Exception e) {
            logger.error("H2SQL: Could not create table for item '{}' with statement '{}'", item.getName(), sqlCmd);
            logger.error("     : " + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception hidden) {
                }
            }
        }

        // Do some type conversion to ensure we know the data type.
        // This is necessary for items that have multiple types and may return their
        // state in a format that's not preferred or compatible with the H2SQL type.
        // eg. DimmerItem can return OnOffType (ON, OFF), or PercentType (0-100).
        // We need to make sure we cover the best type for serialisation.
        String value;
        if (item instanceof DimmerItem || item instanceof RollershutterItem) {
            value = item.getStateAs(PercentType.class).toString();
            logger.debug("Got as Percent: {}", value);
        } else if (item instanceof ColorItem) {
            value = item.getStateAs(HSBType.class).toString();
            logger.debug("Got as HSB: {}", value);
        } else {
            // All other items should return the best format by default
            value = item.getState().toString();
        }
        logger.debug("H2SQL: State is {}::{}", item.getState(), value);

        try {
            statement = connection.createStatement();
            sqlCmd = new String("INSERT INTO openhab." + item.getName() + " (TIME, VALUE) VALUES(NOW(),'" + value + "');");
            statement.executeUpdate(sqlCmd);

            long timerStop = System.currentTimeMillis();
            logger.debug("H2SQL: Stored item '{}' as '{}'[{}] in {}ms", item.getName(), value,
                    item.getState().toString(), timerStop - timerStart);
            logger.debug("H2SQL: {}", sqlCmd);
        } catch (Exception e) {
            logger.error("H2SQL: Could not store item '{}' in database with statement '{}'", item.getName(), sqlCmd);
            logger.error("     : " + e.getMessage());
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
    public void store(Item item, String alias) {
        store(item);
    }

    /**
     * Checks if we have a database connection
     * 
     * @return true if connection has been established, false otherwise
     */
    private boolean isConnected() {
        // Check if connection is valid
        try {
            if (connection != null && !connection.isValid(5000)) {
                logger.error("H2SQL: Connection is not valid!");
            }
        } catch (SQLException e) {
            logger.error("H2SQL: Error while checking connection");
            logger.error("     : " + e.getMessage());
        }
        return connection != null;
    }

    /**
     * Connects to the database
     */
    private void connectToDatabase() {
        try {
            logger.debug("H2SQL: Connecting to database");
            Class.forName(driverClass).newInstance();

            final String USERDATA_DIR_PROG_ARGUMENT = "smarthome.userdata";
            final String eshUserDataFolder = System.getProperty(USERDATA_DIR_PROG_ARGUMENT);
            String databaseFileName = "etc/";
            if (eshUserDataFolder != null) {
                databaseFileName = eshUserDataFolder + "/";
            }
            databaseFileName += "h2sql/openhab";

            String url = "jdbc:h2:file:" + databaseFileName;

            // Disable logging
            // TODO: Look at using slf4j
            url += ";TRACE_LEVEL_FILE=0;TRACE_LEVEL_SYSTEM_OUT=0;";
            connection = DriverManager.getConnection(url);

            logger.debug("H2SQL: Connected to database {}", databaseFileName);

            // TODO: Move these into the connection URL
            Statement statement = connection.createStatement();
            String sqlCmd = new String("CREATE SCHEMA IF NOT EXISTS OPENHAB;");
            statement.executeUpdate(sqlCmd);
            sqlCmd = new String("SET SCHEMA OPENHAB;");
            statement.executeUpdate(sqlCmd);
            statement.close();
        } catch (Exception e) {
            logger.error("H2SQL: Failed connecting to the SQL database");
            logger.error("     : " + e.getMessage());
        }
    }

    /**
     * Disconnects from the database
     */
    private void disconnectFromDatabase() {
        logger.debug("H2SQL: Disconnecting from database.");
        if (connection != null) {
            try {
                connection.close();
                logger.debug("H2SQL: Disconnected from database.");
            } catch (Exception e) {
                logger.error("H2SQL: Failed disconnecting from the SQL database");
                logger.error("     : " + e.getMessage());
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
    }

    @Override
    public Iterable<HistoricItem> query(FilterCriteria filter) {
        if (!isConnected()) {
            connectToDatabase();
        }

        if (!isConnected()) {
            logger.debug("Query aborted on item {} - H2SQL not connected!", filter.getItemName());
            return Collections.emptyList();
        }

        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get the item name from the filter
        // Also get the Item object so we can determine the type
        Item item = null;
        String itemName = filter.getItemName();
        logger.debug("H2SQL: Query item is {}", itemName);
        try {
            if (itemRegistry != null) {
                item = itemRegistry.getItem(itemName);
            }
        } catch (ItemNotFoundException e) {
            logger.error("H2SQL: Unable to get item type for {}", itemName);
            logger.error("     : " + e.getMessage());

            // Set type to null - data will be returned as StringType
            item = null;
        }

        if (item instanceof GroupItem) {
            // For Group Items is BaseItem needed to get correct Type of Value.
            item = GroupItem.class.cast(item).getBaseItem();
        }

        String filterString = new String();

        if (filter.getBeginDate() != null) {
            if (filterString.isEmpty()) {
                filterString += " WHERE";
            } else {
                filterString += " AND";
            }
            filterString += " TIME>'" + sqlDateFormat.format(filter.getBeginDate()) + "'";
        }
        if (filter.getEndDate() != null) {
            if (filterString.isEmpty()) {
                filterString += " WHERE";
            } else {
                filterString += " AND";
            }
            filterString += " TIME<'" + sqlDateFormat.format(filter.getEndDate().getTime()) + "'";
        }

        if (filter.getOrdering() == Ordering.ASCENDING) {
            filterString += " ORDER BY Time ASC";
        } else {
            filterString += " ORDER BY Time DESC";
        }

        if (filter.getPageSize() != 0x7fffffff) {
            filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + "," + filter.getPageSize();
        }

        try {
            long timerStart = System.currentTimeMillis();

            // Retrieve the table array
            Statement st = connection.createStatement();

            String queryString = new String();
            queryString = "SELECT Time, Value FROM openhab." + filter.getItemName();
            if (!filterString.isEmpty()) {
                queryString += filterString;
            }

            logger.debug("H2SQL: " + queryString);

            // Turn use of the cursor on.
            st.setFetchSize(50);

            ResultSet rs = st.executeQuery(queryString);

            long count = 0;
            List<HistoricItem> items = new ArrayList<HistoricItem>();
            State state;
            while (rs.next()) {
                count++;

                if (item instanceof NumberItem) {
                    state = new DecimalType(rs.getDouble(2));
                } else if (item instanceof ColorItem) {
                    state = new HSBType(rs.getString(2));
                } else if (item instanceof DimmerItem) {
                    state = new PercentType(rs.getInt(2));
                } else if (item instanceof SwitchItem) {
                    state = OnOffType.valueOf(rs.getString(2));
                } else if (item instanceof ContactItem) {
                    state = OpenClosedType.valueOf(rs.getString(2));
                } else if (item instanceof RollershutterItem) {
                    state = new PercentType(rs.getInt(2));
                } else if (item instanceof DateTimeItem) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(rs.getTimestamp(2).getTime());
                    state = new DateTimeType(calendar);
                } else {
                    state = new StringType(rs.getString(2));
                }

                H2SqlItem sqlItem = new H2SqlItem(itemName, state, rs.getTimestamp(1));
                items.add(sqlItem);
            }

            rs.close();
            st.close();

            long timerStop = System.currentTimeMillis();
            logger.debug("H2SQL: query returned {} rows in {}ms", count, timerStop - timerStart);

            return items;
        } catch (SQLException e) {
            logger.error("H2SQL: Error running query");
            logger.error("     : " + e.getMessage());

            return Collections.emptyList();
        }
    }
}
