/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.influxdb.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.isBlank;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
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
 * @author Theo Weiss - Initial Contribution, rewrite of org.openhab.persistence.influxdb > 0.9
 *         support
 * @since 1.8.0
 */
public class InfluxDBPersistenceService implements QueryablePersistenceService {

  private static final String DEFAULT_URL = "http://127.0.0.1:8086";
  private static final String DEFAULT_DB = "openhab";
  private static final String DEFAULT_USER = "openhab";
  private static final String DIGITAL_VALUE_OFF = "0";
  private static final String DIGITAL_VALUE_ON = "1";
  private static final String VALUE_COLUMN_NAME = "value";
  private ItemRegistry itemRegistry;
  private InfluxDB influxDB;
  private static final Logger logger = LoggerFactory.getLogger(InfluxDBPersistenceService.class);
  private static final String TIME_COLUMN_NAME = "time";
  private static final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
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

  public void activate(final BundleContext bundleContext, final Map<String, Object> config) {
    logger.debug("influxdb persistence service activated");
    disconnect();
    password = (String) config.get("password");
    if (isBlank(password)) {
      isProperlyConfigured = false;
      logger
          .error("influxdb:password",
              "The password is missing. To specify a password configure the password parameter in openhab.cfg.");
      return;
    }

    url = (String) config.get("url");
    if (isBlank(url)) {
      url = DEFAULT_URL;
      logger.debug("using default url {}", DEFAULT_URL);
    }

    user = (String) config.get("user");
    if (isBlank(user)) {
      user = DEFAULT_USER;
      logger.debug("using default user {}", DEFAULT_USER);
    }

    dbName = (String) config.get("db");
    if (isBlank(dbName)) {
      dbName = DEFAULT_DB;
      logger.debug("using default db name {}", DEFAULT_DB);
    }

    isProperlyConfigured = true;

    connect();

    // check connection; errors will only be logged, hoping the connection will work at a later
    // time.
    if (!checkConnection()) {
      logger.error("database connection does not work for now, will retry to use the database.");
    }
  }

  public void deactivate() {
    logger.debug("influxdb persistence service deactivated");
    disconnect();
  }

  private void connect() {
    if (influxDB == null) {
      // reuse an existing InfluxDB object because concerning the database it has no state
      // connection
      influxDB = InfluxDBFactory.connect(url, user, password);
      influxDB.enableBatch(200, 100, timeUnit);
    }
    connected = true;
  }

  private boolean checkConnection() {
    boolean dbStatus = false;
    if (!connected) {
      logger.error("checkConnection: database is not connected");
      dbStatus = false;
    } else {
      try {
        Pong pong = influxDB.ping();
        String version = pong.getVersion();
        // may be check for version >= 0.9
        if (version != null && !version.contains("unknown")) {
          dbStatus = true;
          logger.debug("database status is OK, version is {}", version);
        } else {
          logger.error("database ping error, version is: \"{}\" response time was \"{}\"",
              version, pong.getResponseTime());
          dbStatus = false;
        }
      } catch (RuntimeException e) {
        dbStatus = false;
        logger.error("database connection failed", e);
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
      logger.warn("Configuration for influxdb not yet loaded or broken.");
      return;
    }

    if (!isConnected()) {
      logger.warn("InfluxDB is not yet connected");
      return;
    }

    String realName = item.getName();
    String name = (alias != null) ? alias : realName;

    State state = null;
    if (item.getAcceptedCommandTypes().contains(HSBType.class)) {
      state = item.getStateAs(HSBType.class);
      logger.trace("Tried to get item as {}, state is {}", HSBType.class, state.toString());
    } else if (item.getAcceptedDataTypes().contains(PercentType.class)) {
      state = item.getStateAs(PercentType.class);
      logger.trace("Tried to get item as {}, state is {}", PercentType.class, state.toString());
    } else {
      // All other items should return the best format by default
      state = item.getState();
      logger.trace("Tried to get item from item class {}, state is {}", item.getClass(), state.toString());
    }
    Object value = stateToObject(state);
    logger.trace("storing {} in influxdb value {}, {}", name, value, item);
    Point point =
        Point.measurement(name).field(VALUE_COLUMN_NAME, value)
            .time(System.currentTimeMillis(), timeUnit).build();
    try {
      influxDB.write(dbName, "default", point);
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
      logger.error("database error: {}", e.getMessage());
    }
  }

  @Override
  public Iterable<HistoricItem> query(FilterCriteria filter) {
    logger.debug("got a query");

    if (!isProperlyConfigured) {
      logger.warn("Configuration for influxdb not yet loaded or broken.");
      return Collections.emptyList();
    }

    if (!isConnected()) {
      logger.warn("InfluxDB is not yet connected");
      return Collections.emptyList();
    }

    List<HistoricItem> historicItems = new ArrayList<HistoricItem>();

    StringBuffer query = new StringBuffer();
    query.append("select ");
    query.append(VALUE_COLUMN_NAME);
    query.append(" ");
    query.append("from ");

    if (filter.getItemName() != null) {
      query.append(filter.getItemName());
    } else {
      query.append("/.*/");
    }

    logger
        .trace(
            "Filter: itemname: {}, ordering: {}, state: {},  operator: {}, getBeginDate: {}, getEndDate: {}, getPageSize: {}, getPageNumber: {}",
            filter.getItemName(), filter.getOrdering().toString(), filter.getState(),
            filter.getOperator(), filter.getBeginDate(), filter.getEndDate(), filter.getPageSize(),
            filter.getPageNumber());

    if ((filter.getState() != null && filter.getOperator() != null)
        || filter.getBeginDate() != null || filter.getEndDate() != null) {
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

    }

    if (filter.getOrdering() == Ordering.DESCENDING) {
      query.append(String.format(" ORDER BY %s DESC", TIME_COLUMN_NAME));
      logger.debug("descending ordering ");
    }

    int limit = (filter.getPageNumber() + 1) * filter.getPageSize();
    query.append(" limit " + limit);
    logger.trace("appending limit {}", limit);

    int totalEntriesAffected = ((filter.getPageNumber() + 1) * filter.getPageSize());
    int startEntryNum =
        totalEntriesAffected
            - (totalEntriesAffected - (filter.getPageSize() * filter.getPageNumber()));
    logger.trace("startEntryNum {}", startEntryNum);

    logger.debug("query string: {}", query.toString());
    Query influxdbQuery = new Query(query.toString(), dbName);

    List<Result> results = Collections.emptyList();
    results = influxDB.query(influxdbQuery, timeUnit).getResults();
    for (Result result : results) {
      List<Series> seriess = result.getSeries();
      if (result.getError() != null) {
        logger.error(result.getError());
        continue;
      }
      if (seriess == null) {
        logger.debug("query returned no series");
      } else {
        for (Series series : seriess) {
          logger.trace("series {}", series.toString());
          String historicItemName = series.getName();
          List<List<Object>> valuess = series.getValues();
          if (valuess == null) {
            logger.debug("query returned no values");
          } else {
            List<String> columns = series.getColumns();
            logger.trace("columns {}", columns);
            Integer timestampColumn = null;
            Integer valueColumn = null;
            for (int i = 0; i < columns.size(); i++) {
              String columnName = columns.get(i);
              if (columnName.equals(TIME_COLUMN_NAME)) {
                timestampColumn = i;
              } else if (columnName.equals(VALUE_COLUMN_NAME)) {
                valueColumn = i;
              }
            }
            if (valueColumn == null || timestampColumn == null) {
              throw new RuntimeException("missing column");
            }
            for (int i = 0; i < valuess.size(); i++) {
              Double rawTime = (Double) valuess.get(i).get(timestampColumn);
              Date time = new Date(rawTime.longValue());
              State value = objectToState(valuess.get(i).get(valueColumn), historicItemName);
              logger.trace("adding historic item {}: time {} value {}", historicItemName, time,
                  value);
              historicItems.add(new InfluxdbItem(historicItemName, value, time));
            }
          }
        }
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
   * @return integer or double value for DecimalType, 0 or 1 for OnOffType and OpenClosedType,
   *         integer for DateTimeType, String for all others
   */
  private Object stateToObject(State state) {
    Object value;
    if (state instanceof HSBType) {
      value = ((HSBType) state).toString();
      logger.debug("got HSBType value {}", value);
    } else if (state instanceof PointType) {
      value = point2String((PointType) state);
      logger.debug("got PointType value {}", value);
    } else if (state instanceof DecimalType) {
      value = convertBigDecimalToNum(((DecimalType) state).toBigDecimal());
      logger.debug("got DecimalType value {}", value);
    } else if (state instanceof OnOffType) {
      value = (OnOffType) state == OnOffType.ON ? 1 : 0;
      logger.debug("got OnOffType value {}", value);
    } else if (state instanceof OpenClosedType) {
      value = (OpenClosedType) state == OpenClosedType.OPEN ? 1 : 0;
      logger.debug("got OpenClosedType value {}", value);
    } else if (state instanceof DateTimeType) {
      value = ((DateTimeType) state).getCalendar().getTime().getTime();
      logger.debug("got DateTimeType value {}", value);
    } else {
      value = state.toString();
      logger.debug("got String value {}", value);
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
    if (state instanceof DecimalType) {
      value = ((DecimalType) state).toBigDecimal().toString();
    } else if (state instanceof PointType) {
      value = point2String((PointType) state);
    } else if (state instanceof OnOffType) {
      value = ((OnOffType) state) == OnOffType.ON ? DIGITAL_VALUE_ON : DIGITAL_VALUE_OFF;
    } else if (state instanceof OpenClosedType) {
      value =
          ((OpenClosedType) state) == OpenClosedType.OPEN ? DIGITAL_VALUE_ON : DIGITAL_VALUE_OFF;
    } else if (state instanceof HSBType) {
      value = ((HSBType) state).toString();
    } else if (state instanceof DateTimeType) {
      value = String.valueOf(((DateTimeType) state).getCalendar().getTime().getTime());
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
   * @return the state of the item represented by the itemName parameter, else the string value of
   *         the Object parameter
   */
  private State objectToState(Object value, String itemName) {
    String valueStr = String.valueOf(value);
    if (itemRegistry != null) {
      try {
        Item item = itemRegistry.getItem(itemName);
        if (item instanceof ColorItem) {
          logger.debug("objectToState found a ColorItem {}", valueStr);
          return new HSBType(valueStr);
        } else if (item instanceof LocationItem){
          logger.debug("objectToState found a LocationItem");
          return new PointType(valueStr);
        } else if (item instanceof NumberItem) {
          logger.debug("objectToState found a NumberItem");
          return new DecimalType(valueStr);
        } else if (item instanceof DimmerItem) {
          logger.debug("objectToState found a DimmerItem");
          return new PercentType(valueStr);
        } else if (item instanceof SwitchItem) {
          logger.debug("objectToState found a SwitchItem");
          return string2DigitalValue(valueStr).equals(DIGITAL_VALUE_OFF)
              ? OnOffType.OFF
              : OnOffType.ON;
        } else if (item instanceof ContactItem) {
          logger.debug("objectToState found a ContactItem");
          return (string2DigitalValue(valueStr).equals(DIGITAL_VALUE_OFF))
              ? OpenClosedType.CLOSED
              : OpenClosedType.OPEN;
        } else if (item instanceof RollershutterItem) {
          logger.debug("objectToState found a RollershutterItem");
          return new PercentType(valueStr);
        } else if (item instanceof DateTimeItem) {
          logger.debug("objectToState found a DateItem");
          Calendar calendar = Calendar.getInstance();
          calendar.setTimeInMillis(new BigDecimal(valueStr).longValue());
          return new DateTimeType(calendar);
        } else {
          logger.debug("objectToState found a other Item");
          return new StringType(valueStr);
        }
      } catch (ItemNotFoundException e) {
        logger.warn("Could not find item '{}' in registry", itemName);
      }
    }
    // just return a StringType as a fallback
    return new StringType(valueStr);
  }

  /**
   * Maps a string value which expresses a {@link BigDecimal.ZERO } to DIGITAL_VALUE_OFF, all others
   * to DIGITAL_VALUE_ON
   * 
   * @param value to be mapped
   * @return
   */
  private String string2DigitalValue(String value) {
    BigDecimal num = new BigDecimal(value);
    if (num.compareTo(BigDecimal.ZERO) == 0) {
      logger.trace("digitalvalue {}", DIGITAL_VALUE_OFF);
      return DIGITAL_VALUE_OFF;
    } else {
      logger.trace("digitalvalue {}", DIGITAL_VALUE_ON);
      return DIGITAL_VALUE_ON;
    }
  }

  private String point2String(PointType point) {
    StringBuffer buf = new StringBuffer();
    buf.append(point.getLatitude().toString());
    buf.append(",");
    buf.append(point.getLongitude().toString());
    if (point.getAltitude().equals(0)) {
      buf.append(",");
      buf.append(point.getAltitude().toString());
    }
    return buf.toString(); // latitude, longitude, altitude
  }
}
