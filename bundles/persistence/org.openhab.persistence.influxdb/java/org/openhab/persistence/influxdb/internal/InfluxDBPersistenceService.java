/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.influxdb.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Serie;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RetrofitError;

/**
 * This is the implementation of the InfluxDB {@link PersistenceService}. It persists item values
 * using the <a href="http://influxdb.org">InfluxDB</a> time series database. The states (
 * {@link State}) of an {@link Item} are persisted in a time series with names equal to the name of
 * the item. All values are stored using integers or doubles, {@link OnOffType} and
 * {@link OpenClosedType} are stored using 0 or 1.
 * 
 * The defaults for the database name, the database user and the database url are "openhab",
 * "openhab" and "http://127.0.0.1:8086".
 * 
 * @author Theo Weiss - Initial Contribution
 * @author Ben Jones - Upgraded influxdb-java version
 * @since 1.5.0
 */
public class InfluxDBPersistenceService implements QueryablePersistenceService, ManagedService {

  private static final String DEFAULT_URL = "http://127.0.0.1:8086";
  private static final String DEFAULT_DB = "openhab";
  private static final String DEFAULT_USER = "openhab";
  private static final String OK_STATUS = "ok";
  private static final String DIGITAL_VALUE_OFF = "0";
  private static final String DIGITAL_VALUE_ON = "1";
  private static final String VALUE_COLUMN_NAME = "value";
  private ItemRegistry itemRegistry;
  private InfluxDB influxDB;
  private static final Logger logger = LoggerFactory.getLogger(InfluxDBPersistenceService.class);
  private static final Object TIME_COLUMN_NAME = "time";
  private String dbName;
  private String url;
  private String user;
  private String password;
  private boolean isProperlyConfigured;
  private boolean connected;

  public void setItemRegistry(ItemRegistry itemRegistry) {
    this.itemRegistry = itemRegistry;
  }

  public void unsetItemRegistry(ItemRegistry itemRegistry) {
    this.itemRegistry = null;
  }

  public void activate() {
    logger.debug("influxdb persistence service activated");
  }

  public void deactivate() {
    logger.debug("influxdb persistence service deactivated");
    disconnect();
  }

  private void connect() {
    if (influxDB == null) {
      // reuse an existing InfluxDB object because it has no state concerning the database
      // connection
      influxDB = InfluxDBFactory.connect(url, user, password);
    }
    connected = true;
  }

  private boolean checkConnection() {
    boolean dbStatus = false;
    if (! connected) {
      logger.error("checkConnection: database is not connected");
      dbStatus = false;
    } else {
      try {
        Pong pong = influxDB.ping();
        if (pong.getStatus().equalsIgnoreCase(OK_STATUS)) {
          dbStatus = true;
          logger.debug("database status is OK");
        } else {
          logger.error("database connection failed with status: \"{}\" response time was \"{}\"",
              pong.getStatus(), pong.getResponseTime());
          dbStatus = false;
        }
      } catch (RuntimeException e) {
        dbStatus = false;
        logger.error("database connection failed throwing an exception");
        handleDatabaseException(e);
      }
    }
    return dbStatus;
  }

  private void disconnect() {
    influxDB = null;
    connected = false;
  }

  private boolean isConnected() {
    return connected;
  }

  @Override
  public String getName() {
    return "influxdb";
  }

  @Override
  public void store(Item item) {
    store(item, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void store(Item item, String alias) {
    if (item.getState() instanceof UnDefType) {
      return;
    }

    if (!isProperlyConfigured) {
      logger.error("Configuration for influxdb not yet loaded or broken.");
      return;
    }

    if (!isConnected()) {
      logger.error("InfluxDB is not yet connected");
      return;
    }

    String realName = item.getName();
    String name = (alias != null) ? alias : realName;
    Object value = stateToObject(item.getState());
    logger.trace("storing {} in influxdb {}", name, value);

    // For now time is calculated by influxdb, may be this should be configurable?
    Serie serie = new Serie.Builder(name)
    		.columns(VALUE_COLUMN_NAME)
    		.values(value)
    		.build();
    // serie.setColumns(new String[] {"time", VALUE_COLUMN_NAME});
    // Object[] point = new Object[] {System.currentTimeMillis(), value};

    try {
      influxDB.write(dbName, TimeUnit.MILLISECONDS, serie);
    } catch (RuntimeException e) {
      logger.error("storing failed with exception for item: {}", name);
      handleDatabaseException(e);
    }
  }

  private void handleDatabaseException(Exception e) {
    if (e instanceof RetrofitError) {
      // e.g. raised if influxdb is not running
      logger.error("database connection error {}", e.getMessage());
    } else if (e instanceof RuntimeException) {
      // e.g. raised by authentication errors
      logger
          .error(
              "database connection error may be wrong password, username or dbname: {}",
              e);
    }
  }

  @Override
  public void updated(Dictionary<String, ?> config) throws ConfigurationException {
    disconnect();

    if (config == null) {
      throw new ConfigurationException("influxdb",
          "The configuration for influxdb is missing fix openhab.cfg");
    }

    url = (String) config.get("url");
    if (StringUtils.isBlank(url)) {
      url = DEFAULT_URL;
      logger.debug("using default url {}", DEFAULT_URL);
    }

    user = (String) config.get("user");
    if (StringUtils.isBlank(user)) {
      user = DEFAULT_USER;
      logger.debug("using default user {}", DEFAULT_USER);
    }

    password = (String) config.get("password");
    if (StringUtils.isBlank(password)) {
      throw new ConfigurationException("influxdb:password",
          "The password is missing. To specify a password configure the password parameter in openhab.cfg.");
    }

    dbName = (String) config.get("db");
    if (StringUtils.isBlank(dbName)) {
      dbName = DEFAULT_DB;
      logger.debug("using default db name {}", DEFAULT_DB);
    }

    isProperlyConfigured = true;

    connect();

    // check connection; errors will only be logged, hoping the connection will work at a later time. 
    if ( ! checkConnection()){
      logger.error("database connection does not work for now, will retry to use the database.");
    }
  }

  @Override
  public Iterable<HistoricItem> query(FilterCriteria filter) {
    Integer pageSize = null;
    Integer pageNumber = null;
    logger.debug("got a query");

    if (!isProperlyConfigured) {
      logger.error("Configuration for influxdb not yet loaded or broken.");
      return Collections.emptyList();
    }

    if (!isConnected()) {
      logger.error("InfluxDB is not yet connected");
      return Collections.emptyList();
    }

    List<HistoricItem> historicItems = new ArrayList<HistoricItem>();

    StringBuffer query = new StringBuffer();
    query.append("select ");
    query.append(VALUE_COLUMN_NAME);
    query.append(", ");
    query.append(TIME_COLUMN_NAME);
    query.append(" ");
    query.append("from ");

    if (filter.getItemName() != null) {
      query.append(filter.getItemName());
    } else {
      query.append("/.*/");
    }

    if (filter.getState() != null || filter.getOperator() != null || filter.getBeginDate() != null
        || filter.getEndDate() != null) {
      query.append(" where ");
      boolean foundState = false;
      boolean foundBeginDate = false;
      if (filter.getState() != null && filter.getOperator() != null) {
        String value = stateToString(filter.getState());
        if (value != null) {
          foundState = true;
          query.append(VALUE_COLUMN_NAME);
          query.append(" ");
          query.append(filter.getOperator().toString());
          query.append(" ");
          query.append(value);
        }
      }

      if (filter.getBeginDate() != null) {
        foundBeginDate = true;
        if (foundState) {
          query.append(" and");
        }
        query.append(" ");
        query.append(TIME_COLUMN_NAME);
        query.append(" > ");
        query.append(getTimeFilter(filter.getBeginDate()));
        query.append(" ");
      }

      if (filter.getEndDate() != null) {
        if (foundState || foundBeginDate) {
          query.append(" and");
        }
        query.append(" ");
        query.append(TIME_COLUMN_NAME);
        query.append(" < ");
        query.append(getTimeFilter(filter.getEndDate()));
        query.append(" ");
      }

      // InfluxDB returns results in DESCENDING order by default
      // http://influxdb.com/docs/v0.7/api/query_language.html#select-and-time-ranges
      if (filter.getOrdering() == Ordering.ASCENDING) {
        query.append(" order asc");
      }

      if (filter.getPageSize() != 0) {
        logger.debug("got page size {}", filter.getPageSize());
        pageSize = filter.getPageSize();
      }

      if (filter.getPageNumber() != 0) {
        logger.debug("got page number {}", filter.getPageNumber());
        pageNumber = filter.getPageNumber();
      }
    }
    logger.debug("query string: {}", query.toString());
    List<Serie> results = Collections.emptyList();
    try {
      results = influxDB.query(dbName, query.toString(), TimeUnit.MILLISECONDS);
    } catch (RuntimeException e) {
      logger.error("query failed with database error");
      handleDatabaseException(e);
    }
    for (Serie result : results) {
      String historicItemName = result.getName();
      logger.trace("item name ", historicItemName);

      int pageCount = 0;
      for (Map<String, Object> row : result.getRows()) {
    	pageCount++;
        if (pageSize != null && pageNumber == null && pageSize < pageCount) {
          logger.debug("returning no more points pageSize {} pageCount {}", 
        		  pageSize, pageCount);
          break;
        }
        
        Double rawTime = (Double) row.get(TIME_COLUMN_NAME);
        Object rawValue = row.get(VALUE_COLUMN_NAME);
                
        logger.trace("adding historic item {}: time {} value {}", 
        		historicItemName, rawTime, rawValue);
        
        Date time = new Date(rawTime.longValue());
        State value = objectToState(rawValue, historicItemName);
        
        historicItems.add(new InfluxdbItem(historicItemName, value, time));
      }
    }

    return historicItems;
  }

  private String getTimeFilter(Date time) {
	  // for some reason we need to query using 'seconds' only
	  // passing milli seconds causes no results to be returned
	  long milliSeconds = time.getTime();
	  long seconds = milliSeconds / 1000;
	  return seconds + "s";
  }
  
  /**
   * This method returns an integer if possible if not a double is returned. This is an optimization
   * for influxdb because integers have less overhead.
   * 
   * @param value the BigDecimal to be converted
   * @return A double if possible else a double is returned.
   */
  private Object convertBigDecimalToNum(BigDecimal value) {
    Object convertedValue;
    if (value.scale() == 0) {
      logger.trace("found no fractional part");
      convertedValue = value.toBigInteger();
    } else {
      logger.trace("found fractional part");
      convertedValue = value.doubleValue();
    }
    return convertedValue;
  }

  /**
   * Converts {@link State} to objects fitting into influxdb values.
   * 
   * @param state to be converted
   * @return integer or double value for DecimalType and PercentType, an integer for DateTimeType
   *         and 0 or 1 for OnOffType and OpenClosedType.
   */
  private Object stateToObject(State state) {
    Object value;
    if (state instanceof PercentType) {
      value = convertBigDecimalToNum(((PercentType) state).toBigDecimal());
    } else if (state instanceof DecimalType) {
      value = convertBigDecimalToNum(((DecimalType) state).toBigDecimal());
    } else if (state instanceof DateTimeType) {
      value = ((DateTimeType) state).getCalendar().getTime().getTime();
    } else if (state instanceof OnOffType) {
      value = (OnOffType) state == OnOffType.ON ? 1 : 0;
    } else if (state instanceof OpenClosedType) {
      value = (OpenClosedType) state == OpenClosedType.OPEN ? 1 : 0;
    } else {
      value = state.toString();
    }
    return value;
  }

  /**
   * Converts {@link State} to a String suitable for influxdb queries.
   * 
   * @param state to be converted
   * @return {@link String} equivalent of the {@link State}
   */
  private String stateToString(State state) {
    String value;
    if (state instanceof PercentType) {
      value = ((PercentType) state).toBigDecimal().toString();
    } else if (state instanceof DateTimeType) {
      value = String.valueOf(((DateTimeType) state).getCalendar().getTime().getTime());
    } else if (state instanceof DecimalType) {
      value = ((DecimalType) state).toBigDecimal().toString();
    } else if (state instanceof OnOffType) {
      value = ((OnOffType) state) == OnOffType.ON ? DIGITAL_VALUE_ON : DIGITAL_VALUE_OFF;
    } else {
      value = state.toString();
    }
    return value;
  }

  /**
   * Converts a value to a {@link State} which is suitable for the given {@link Item}. This is
   * needed for querying a {@link HistoricState}.
   * 
   * @param value to be converted to a {@link State}
   * @param itemName name of the {@link Item} to get the {@link State} for
   * @return
   */
  private State objectToState(Object value, String itemName) {
	String valueStr = String.valueOf(value);
    if (itemRegistry != null) {
      try {
        Item item = itemRegistry.getItem(itemName);
        if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
          return valueStr.equals(DIGITAL_VALUE_OFF) ? OnOffType.OFF : OnOffType.ON;
        } else if (item instanceof ContactItem) {
          return valueStr.equals(DIGITAL_VALUE_OFF) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
        }
      } catch (ItemNotFoundException e) {
        logger.warn("Could not find item '{}' in registry", itemName);
      }
    }
    // just return a DecimalType as a fallback
    return new DecimalType(valueStr);
  }

}
