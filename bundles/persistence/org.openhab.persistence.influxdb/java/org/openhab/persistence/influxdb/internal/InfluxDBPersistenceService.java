/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.influxdb.internal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
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

/**
 * This is the implementation of the InfluxDB {@link PersistenceService}. It persists item values
 * using the <a href="http://influxdb.org">InfluxDB</a> time series database. The states (
 * {@link State}) of an {@link Item} are persisted in a time series with names equal to the name of
 * the item. All values are stored using integers or doubles, {@link OnOffType} and
 * {@link OpenClosedType} are stored using 0 or 1.
 * 
 * @author Theo Weiss - Initial Contribution
 * @since 1.5.0
 */
public class InfluxDBPersistenceService implements QueryablePersistenceService, ManagedService {


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
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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
    influxDB = InfluxDBFactory.connect(url, user, password);
  }

  private void disconnect() {
    influxDB = null;
  }

  private boolean isConnected() {
    return influxDB != null;
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
      logger.error("Configuration for influxdb is missing or not yet loaded");
      return;
    }

    if (!isConnected()) {
      connect();
    }

    // If we still didn't manage to connect, then return!
    if (!isConnected()) {
      logger
          .error(
              "influxdb: No connection to database. Can not persist item '{}'! Will retry connecting to database next time.",
              item);
      return;
    }
    String realName = item.getName();
    String name = (alias != null) ? alias : realName;
    Object value = stateToObject(item.getState());
    logger.trace("storing {} in influxdb {}", name, value);
    Serie serie = new Serie(name);
    // For now time is calculated by influxdb, may be this should be configurable?
    // serie.setColumns(new String[] {"time", VALUE_COLUMN_NAME});
    // Object[] point = new Object[] {System.currentTimeMillis(), value};

    serie.setColumns(new String[] {VALUE_COLUMN_NAME});
    Object[] point = new Object[] {value};
    serie.setPoints(new Object[][] {point});
    Serie[] series = new Serie[] {serie};
    influxDB.write(dbName, series, TimeUnit.MILLISECONDS);
  }

  @Override
  public void updated(Dictionary<String, ?> config) throws ConfigurationException {
    if (config == null) {
      throw new ConfigurationException("influxdb",
          "The configuration for influxdb is missing fix openhab.cfg");
    }

    url = (String) config.get("url");
    if (StringUtils.isBlank(url)) {
      throw new ConfigurationException("influxdb:url",
          "The database URL is missing - please configure the url parameter in openhab.cfg");
    }

    user = (String) config.get("user");
    if (StringUtils.isBlank(user)) {
      throw new ConfigurationException("influxdb:user",
          "The user is missing - please configure the user parameter in openhab.cfg");
    }

    password = (String) config.get("password");
    if (StringUtils.isBlank(password)) {
      throw new ConfigurationException("influxdb:password",
          "The password is missing. To specify a password configure the password parameter in openhab.cfg.");
    }

    dbName = (String) config.get("db");
    if (StringUtils.isBlank(dbName)) {
      throw new ConfigurationException("influxdb:db",
          "The db name is missing. To specify a db name configure the db parameter in openhab.cfg.");
    }

    disconnect();
    connect();
    isProperlyConfigured = true;
  }

  @Override
  public Iterable<HistoricItem> query(FilterCriteria filter) {
    Integer pageSize = null;
    Integer pageNumber = null;
    logger.debug("got a query");
    if (!isProperlyConfigured) {
      return Collections.emptyList();
    }
    if (!isConnected()) {
      connect();
    }

    if (!isConnected()) {
      return Collections.emptyList();
    }

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
        query.append(" > '");
        query.append(dateFormat.format(filter.getBeginDate()));
        query.append("'");
      }

      if (filter.getEndDate() != null) {
        if (foundState || foundBeginDate) {
          query.append(" and");
        }
        query.append(" ");
        query.append(TIME_COLUMN_NAME);
        query.append(" < '");
        query.append(dateFormat.format(filter.getEndDate().getTime()));
        query.append("'");
      }

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
    List<Serie> results = influxDB.Query(dbName, query.toString(), TimeUnit.MILLISECONDS);
    List<HistoricItem> historicItems = new ArrayList<HistoricItem>();
    for (Serie result : results) {
      String historicItemName = result.getName();
      logger.trace("item name ", historicItemName);
      String[] columns = result.getColumns();
      int timeColumnNum = 0;
      int valueColumnNum = 0;
      for (int i = 0; i < columns.length; i++) {
        String column = columns[i];
        logger.trace("column name: ", column);
        if (column.equals(TIME_COLUMN_NAME)) {
          timeColumnNum = i;
        } else if (column.equals(VALUE_COLUMN_NAME)) {
          valueColumnNum = i;
        }
      }
      Object[][] points = result.getPoints();
      for (int i = 0; i < points.length; i++) {
        if (pageSize != null && pageNumber == null && pageSize < i) {
          logger.debug("returning no more points pageSize {} pageNumber {} i {}", pageSize,
              pageNumber, i);
          break;
        }
        Object[] objects = points[i];
        logger.trace("adding historic item {}: time {} value {}", historicItemName,
            (Double) objects[timeColumnNum], String.valueOf(objects[valueColumnNum]));
        historicItems.add(new InfluxdbItem(historicItemName, stringToState(
            String.valueOf(objects[valueColumnNum]), historicItemName), new Date(
            ((Double) objects[timeColumnNum]).longValue())));;
      }
    }
    return historicItems;
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
   * @return integer or double value for DecimalType and PercentType, an integer for DateTimeType and
   *         0 or 1 for OnOffType and OpenClosedType.
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
  private State stringToState(String value, String itemName) {
    if (itemRegistry != null) {
      try {
        Item item = itemRegistry.getItem(itemName);
        if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
          return value.equals(DIGITAL_VALUE_OFF) ? OnOffType.OFF : OnOffType.ON;
        } else if (item instanceof ContactItem) {
          return value.equals(DIGITAL_VALUE_OFF) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
        }
      } catch (ItemNotFoundException e) {
        logger.warn("Could not find item '{}' in registry", itemName);
      }
    }
    // just return a DecimalType as a fallback
    return new DecimalType(value);
  }

}
