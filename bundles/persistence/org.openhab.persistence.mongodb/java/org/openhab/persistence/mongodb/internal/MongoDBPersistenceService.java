/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.mongodb.internal;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
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
import org.openhab.core.persistence.FilterCriteria.Operator;
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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * This is the implementation of the MongoDB {@link PersistenceService}.
 * 
 * @author Thorsten Hoeger
 * @since 1.5.0
 */
public class MongoDBPersistenceService implements QueryablePersistenceService,
		ManagedService {

	private static final String FIELD_ID = "_id";
	private static final String FIELD_ITEM = "item";
	private static final String FIELD_REALNAME = "realName";
	private static final String FIELD_TIMESTAMP = "timestamp";
	private static final String FIELD_VALUE = "value";

	private static final Logger logger = LoggerFactory
			.getLogger(MongoDBPersistenceService.class);

	private String url;
	private String db;
	private String collection;

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;

	private MongoClient cl;
	private DBCollection mongoCollection;

	public void activate() {
		//
	}

	public void deactivate() {
		logger.debug("MongoDB persistence bundle stopping. Disconnecting from database.");
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
		return "mongodb";
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item, String alias) {
		// Don't log undefined/uninitialised data
		if (item.getState() instanceof UnDefType) {
			return;
		}

		// If we've not initialised the bundle, then return
		if (initialized == false) {
			logger.warn("MongoDB not initialized");
			return;
		}

		// Connect to mongodb server if we're not already connected
		if (!isConnected()) {
			connectToDatabase();
		}

		// If we still didn't manage to connect, then return!
		if (!isConnected()) {
			logger.warn(
					"mongodb: No connection to database. Can not persist item '{}'! Will retry connecting to database next time.",
					item);
			return;
		}

		String realName = item.getName();
		String name = (alias != null) ? alias : realName;
		Object value = this.convertValue(item.getState());

		DBObject obj = new BasicDBObject();
		obj.put(FIELD_ID, new ObjectId());
		obj.put(FIELD_ITEM, name);
		obj.put(FIELD_REALNAME, realName);
		obj.put(FIELD_TIMESTAMP, new Date());
		obj.put(FIELD_VALUE, value);
		this.mongoCollection.save(obj);

		logger.debug("MongoDB save {}={}", name, value);
	}

	private Object convertValue(State state) {
		Object value;
		if (state instanceof PercentType) {
			value = ((PercentType) state).toBigDecimal().doubleValue();
		} else if (state instanceof DateTimeType) {
			value = ((DateTimeType) state).getCalendar().getTime();
		} else if (state instanceof DecimalType) {
			value = ((DecimalType) state).toBigDecimal().doubleValue();
		} else {
			value = state.toString();
		}
		return value;
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
		return cl != null;
	}

	/**
	 * Connects to the database
	 */
	private void connectToDatabase() {
		try {
			logger.debug("Connect MongoDB");
			this.cl = new MongoClient(new MongoClientURI(this.url));
			mongoCollection = cl.getDB(this.db).getCollection(this.collection);

			BasicDBObject idx = new BasicDBObject();
			idx.append(FIELD_TIMESTAMP, 1).append(FIELD_ITEM, 1);
			this.mongoCollection.createIndex(idx);
			logger.debug("Connect MongoDB ... done");
		} catch (Exception e) {
			logger.error("Failed to connect to database {}", this.url);
			throw new RuntimeException("Cannot connect to database", e);
		}
	}

	/**
	 * Disconnects from the database
	 */
	private void disconnectFromDatabase() {
		this.mongoCollection = null;
		if (this.cl != null) {
			this.cl.close();
		}
		cl = null;
	}

	/**
	 * @{inheritDoc}
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (config != null) {
			url = (String) config.get("url");
			logger.debug("MongoDB URL {}", url);
			if (StringUtils.isBlank(url)) {
				throw new ConfigurationException(
						"mongodb:url",
						"The MongoDB database URL is missing - please configure the mongodb:url parameter in openhab.cfg");
			}
			db = (String) config.get("database");
			logger.debug("MongoDB database {}", db);
			if (StringUtils.isBlank(db)) {
				throw new ConfigurationException(
						"mongodb:database",
						"The MongoDB database name is missing - please configure the mongodb:database parameter in openhab.cfg");
			}
			collection = (String) config.get("collection");
			logger.debug("MongoDB collection {}", collection);
			if (StringUtils.isBlank(collection)) {
				throw new ConfigurationException(
						"mongodb:collection",
						"The MongoDB database collection is missing - please configure the mongodb:collection parameter in openhab.cfg");
			}

			disconnectFromDatabase();
			connectToDatabase();

			// connection has been established ... initialization completed!
			initialized = true;
		}

	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		if (!initialized)
			return Collections.emptyList();

		if (!isConnected())
			connectToDatabase();

		if (!isConnected())
			return Collections.emptyList();

		String name = filter.getItemName();
		Item item = getItem(name);

		List<HistoricItem> items = new ArrayList<HistoricItem>();
		DBObject query = new BasicDBObject();
		if (filter.getItemName() != null) {
			query.put(FIELD_ITEM, filter.getItemName());
		}
		if (filter.getState() != null && filter.getOperator() != null) {
			String op = convertOperator(filter.getOperator());
			Object value = convertValue(filter.getState());
			query.put(FIELD_VALUE, new BasicDBObject(op, value));
		}
		if (filter.getBeginDate() != null) {
			query.put(FIELD_TIMESTAMP,
					new BasicDBObject("$gte", filter.getBeginDate()));
		}
		if (filter.getEndDate() != null) {
			query.put(FIELD_TIMESTAMP,
					new BasicDBObject("$lte", filter.getEndDate()));
		}

		Integer sortDir = (filter.getOrdering() == Ordering.ASCENDING) ? 1 : -1;
		DBCursor cursor = this.mongoCollection.find(query)
				.sort(new BasicDBObject(FIELD_TIMESTAMP, sortDir))
				.skip(filter.getPageNumber() * filter.getPageSize())
				.limit(filter.getPageSize());

		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();

			final State state;
			if (item instanceof NumberItem) {
				state = new DecimalType(obj.getDouble(FIELD_VALUE));
			} else if (item instanceof DimmerItem) {
				state = new PercentType(obj.getInt(FIELD_VALUE));
			} else if (item instanceof SwitchItem) {
				state = OnOffType.valueOf(obj.getString(FIELD_VALUE));
			} else if (item instanceof ContactItem) {
				state = OpenClosedType.valueOf(obj.getString(FIELD_VALUE));
			} else if (item instanceof RollershutterItem) {
				state = new PercentType(obj.getInt(FIELD_VALUE));
			} else if (item instanceof ColorItem) {
				state = new HSBType(obj.getString(FIELD_VALUE));
			} else if (item instanceof DateTimeItem) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(obj.getDate(FIELD_VALUE));
				state = new DateTimeType(cal);
			} else {
				state = new StringType(obj.getString(FIELD_VALUE));
			}

			items.add(new MongoDBItem(name, state, obj.getDate(FIELD_TIMESTAMP)));
		}

		return items;
	}

	private String convertOperator(Operator operator) {
		switch (operator) {
		case EQ:
			return "$eq";
		case GT:
			return "$gt";
		case GTE:
			return "$gte";
		case LT:
			return "$lt";
		case LTE:
			return "$lte";
		case NEQ:
			return "$neq";
		default:
			return null;
		}
	}

	private Item getItem(String itemName) {
		Item item = null;
		try {
			if (itemRegistry != null) {
				item = itemRegistry.getItem(itemName);
			}
		} catch (ItemNotFoundException e1) {
			logger.error("Unable to get item type for {}", itemName);
			// Set type to null - data will be returned as StringType
			item = null;
		}
		return item;
	}

	public static class MongoDBItem implements HistoricItem {

		final private String name;
		final private State state;
		final private Date timestamp;

		public MongoDBItem(String name, State state, Date timestamp) {
			this.name = name;
			this.state = state;
			this.timestamp = timestamp;
		}

		public String getName() {
			return name;
		}

		public State getState() {
			return state;
		}

		public Date getTimestamp() {
			return timestamp;
		}

		@Override
		public String toString() {
			return DateFormat.getDateTimeInstance().format(timestamp) + ": "
					+ name + " -> " + state.toString();
		}

	}
}
