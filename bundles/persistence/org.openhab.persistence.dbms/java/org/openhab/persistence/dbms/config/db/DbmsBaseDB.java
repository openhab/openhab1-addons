/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config.db;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.openhab.core.items.Item;
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
import org.openhab.core.types.State;
import org.openhab.persistence.dbms.model.DbmsItem;
import org.openhab.persistence.dbms.model.ItemVO;
import org.openhab.persistence.dbms.model.ItemsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default Database Configuration class and its inner-classes. Class represents
 * the entire database configuration, it is none specific, but like MySql.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsBaseDB {

	private static final Logger logger = LoggerFactory.getLogger(DbmsBaseDB.class);
	private String driverClassName = null;
	public Map<String, String> sqlTypes = new HashMap<String, String>();
	protected int maxActiveConnections = 1;
	protected int maxIdleConnections = 1;

	public DbmsBaseDB() {
		initSqlTypes();
	}

	public int getMaxActiveConnections() {
		return maxActiveConnections;
	}

	public int getMaxIdleConnections() {
		return maxIdleConnections;
	}

	public int checkMaxConnections(int i) {
		return i;
	}

	public void setDriverClassName(String dcn) {
		logger.debug("DBMS: setDriverClass: {}", dcn);
		driverClassName = dcn;
	}

	public String getDriverClassName() {
		logger.debug("DBMS: getDriverClass: {}", driverClassName);
		return driverClassName;
	}

	public Map<String, String> getSqlTypes() {
		return sqlTypes;
	}

	// https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html
	public void initSqlTypes() {
		logger.debug("DBMS: initSqlTypes: Initialize the type array");
		// Initialize the type array
		sqlTypes.put("CALLITEM", "VARCHAR(200)");
		sqlTypes.put("COLORITEM", "VARCHAR(70)");
		sqlTypes.put("CONTACTITEM", "VARCHAR(6)");
		sqlTypes.put("DATETIMEITEM", "DATETIME");
		sqlTypes.put("DIMMERITEM", "TINYINT");
		sqlTypes.put("LOCATIONITEM", "VARCHAR(30)");
		sqlTypes.put("NUMBERITEM", "DOUBLE");
		sqlTypes.put("ROLLERSHUTTERITEM", "TINYINT");
		sqlTypes.put("STRINGITEM", "VARCHAR(65500)");
		sqlTypes.put("SWITCHITEM", "VARCHAR(6)");
	}

	public static String getHistItemFilterQueryProvider(Map<String, Object> params) {
		/*
		 * public List<ItemVO> doGetHistItemFilterQuery(
		 * 
		 * @Param("filter") FilterCriteria filter,
		 * 
		 * @Param("numberDecimalcount") int numberDecimalcount,
		 * 
		 * @Param("table") String table,
		 * 
		 * @Param("simpleName") String simpleName ) throws Exception;
		 */
		FilterCriteria filter = (FilterCriteria) params.get("filter");
		int numberDecimalcount = (Integer) params.get("numberDecimalcount");
		String table = (String) params.get("table");
		// String simpleName = (String) params.get("simpleName");

		logger.debug(
				"DBMS::getHistItemFilterQueryProvider filter = {}, numberDecimalcount = {}, table = {}, simpleName = {}",
				filter, numberDecimalcount, table);
		SimpleDateFormat jdbcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String filterString = "";
		if (filter.getBeginDate() != null) {
			filterString += filterString.isEmpty() ? " WHERE" : " AND";
			filterString += " TIME>'" + jdbcDateFormat.format(filter.getBeginDate()) + "'";
		}
		if (filter.getEndDate() != null) {
			filterString += filterString.isEmpty() ? " WHERE" : " AND";
			filterString += " TIME<'" + jdbcDateFormat.format(filter.getEndDate().getTime()) + "'";
		}
		filterString += (filter.getOrdering() == Ordering.ASCENDING) ? " ORDER BY time ASC" : " ORDER BY time DESC ";
		if (filter.getPageSize() != 0x7fffffff) {
			filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + "," + filter.getPageSize();
		}
		String queryString = "SELECT time, value FROM " + table;
		if (!filterString.isEmpty()) {
			queryString += filterString;
		}
		logger.debug("DBMS::query queryString = {}", queryString);
		return queryString;
	}

	public static String updateItemTableNamesProvider(Map<String, Object> params) {
		/*
		 * @UpdateProvider(type=DbmsBaseDB.class,
		 * method="updateItemTableNamesProvider") public void
		 * doUpdateItemTableNames(
		 * 
		 * @Param("namesList") List<ItemVO> namesList ) throws Exception;
		 */
		List<?> namesList = (List<?>) params.get("namesList");

		logger.debug("DBMS::updateItemTableNamesProvider namesList.size = {}", namesList.size());
		String queryString = "";
		for (int i = 0; i < namesList.size(); i++) {
			ItemVO it = (ItemVO) namesList.get(i);
			queryString += "ALTER TABLE " + it.getTableName() + " RENAME TO " + it.getNewTableName() + ";";
		}
		logger.debug("DBMS::query queryString = {}", queryString);
		return queryString;
	}

	/**
	 * Default MyBatis Data Access Object and SQL as inner-classes.
	 */
	public interface BaseInnerDAO {

		String SQL_PING_DB                         = "SELECT 1";
		String SQL_CREATE_ITEMS_TABLE_IF_NOT       = "CREATE TABLE IF NOT EXISTS ${itemsManageTable} (ItemId INT NOT NULL AUTO_INCREMENT,${colname} ${coltype} NOT NULL,PRIMARY KEY (ItemId))";
		String SQL_GET_ITEMID_TABLE_NAMES          = "SELECT itemid, itemname FROM ${itemsManageTable}";
		String SQL_GET_ITEM_TABLES                 = "SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema='public' AND NOT table_name='items'";
		String SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE = "INSERT INTO ${itemsManageTable} (ItemName) VALUES (#{itemname})";
		String SQL_CREATE_ITEM_TABLE               = "CREATE TABLE IF NOT EXISTS ${tableName} (time TIMESTAMP NOT NULL, value ${dbType}, PRIMARY KEY(time))";
		String SQL_DELETE_ITEMS_ENTRY              = "DELETE FROM items WHERE ItemName=#{itemname}";
		String SQL_INSERT_ITEM_VALUE               = "INSERT INTO ${tableName} (TIME, VALUE) VALUES( NOW(), #{value, javaType=${javaType}, jdbcType=${jdbcType}} ) ON DUPLICATE KEY UPDATE VALUE= #{value, javaType=${javaType}, jdbcType=${jdbcType}}";

		// see:
		// http://stackoverflow.com/questions/3668506/efficient-sql-test-query-or-validation-query-that-will-work-across-all-or-most
		@Select(SQL_PING_DB)
		public int doPingDB() throws Exception;

		@Insert(SQL_CREATE_ITEMS_TABLE_IF_NOT)
		public boolean doCreateItemsTableIfNot(ItemsVO vo) throws Exception;

		@Select(SQL_GET_ITEMID_TABLE_NAMES)
		public List<ItemsVO> doGetItemIDTableNames(ItemsVO vo) throws Exception;

		@Select(SQL_GET_ITEM_TABLES)
		public List<ItemsVO> doGetItemTables() throws Exception;

		@UpdateProvider(type = DbmsBaseDB.class, method = "updateItemTableNamesProvider")
		public void doUpdateItemTableNames(@Param("namesList") List<ItemVO> namesList) throws Exception;

		@Insert(SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE)
		@Options(useGeneratedKeys = true, keyProperty = "itemid")
		public int doCreateNewEntryInItemsTable(ItemsVO vo) throws Exception;

		@Insert(SQL_CREATE_ITEM_TABLE)
		public void doCreateItemTable(ItemVO vo) throws Exception;

		@Delete(SQL_DELETE_ITEMS_ENTRY)
		public void doDeleteItemsEntry(ItemsVO vo) throws Exception;

		@Insert(SQL_INSERT_ITEM_VALUE)
		public void doInsertItemValue(ItemVO vo) throws Exception;

		@SelectProvider(type = DbmsBaseDB.class, method = "getHistItemFilterQueryProvider")
		@ResultType(value = HashMap.class)
		public void doGetHistItemFilterQuery(@Param("ItemResultHandler") ResultHandler<?> handler,
				@Param("filter") FilterCriteria filter, @Param("numberDecimalcount") int numberDecimalcount,
				@Param("table") String table, @Param("simpleName") String simpleName) throws Exception;

	}

	/**
	 * Database specific configuration class as inner-classes.
	 */
	public interface DbDAO extends BaseInnerDAO {
	}

	/**
	 * Main MyBatis Result Handler as inner-classes.
	 */
	public class ItemResultHandler implements ResultHandler<Object> {

		private Item item;
		List<HistoricItem> items = new ArrayList<HistoricItem>();

		public ItemResultHandler(Item item) {
			this.item = item;
			logger.debug("DBMS::ItemResultHandler Item Name = {}", item.getName());
		}

		@Override
		public void handleResult(ResultContext<?> rc) {
			logger.debug("DBMS::ItemResultHandler::handleResult rc.getResultCount = {}", rc.getResultCount());
			HashMap<?, ?> m = (HashMap<?, ?>) rc.getResultObject();
			String[] cols = m.containsKey("value") ? new String[] { "time", "value" }
					: new String[] { "TIME", "VALUE" };
			items.add(new DbmsItem(item.getName(), getState(m.get(cols[1])), objectAsDate(m.get(cols[0]))));
		}

		private State getState(Object v) {
			String clazz = v.getClass().getSimpleName();
			logger.debug("DBMS::ItemResultHandler::handleResult getState v = {}, getClass = {}, clazz = {}",
					v.toString(), v.getClass(), clazz);
			if (item instanceof NumberItem) {
				return new DecimalType(((Number) v).doubleValue());

			} else if (item instanceof ColorItem) {
				return new HSBType(((String) v).toString());

			} else if (item instanceof DimmerItem) {
				return new PercentType(objectAsInteger(v));

			} else if (item instanceof SwitchItem) {
				return OnOffType.valueOf(((String) v).toString());

			} else if (item instanceof ContactItem) {
				return OpenClosedType.valueOf(((String) v).toString());

			} else if (item instanceof RollershutterItem) {
				return new PercentType(objectAsInteger(v));

			} else if (item instanceof DateTimeItem) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(objectAsLong(v));
				return new DateTimeType(calendar);

			} else if (item instanceof StringItem) {
				return new StringType(((String) v).toString());

			} else {// Call, Location, String
				return new StringType(((String) v).toString());

			}
		}

		public List<HistoricItem> getItems() {
			return items;
		}

		private Date objectAsDate(Object v) {
			if (v instanceof java.lang.String) {
				return Date.from(Timestamp.valueOf(v.toString()).toInstant());
			}
			return Date.from(((Timestamp) v).toInstant());
		}

		private Long objectAsLong(Object v) {
			if (v instanceof Long) {
				return ((Number) v).longValue();
			} else if (v instanceof java.sql.Date) {
				return ((java.sql.Date) v).getTime();
			}
			return ((java.sql.Timestamp) v).getTime();
		}

		private Integer objectAsInteger(Object v) {
			if (v instanceof Byte) {
				return ((Byte) v).intValue();
			}
			return ((Integer) v).intValue();
		}
	}
}