/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config.db;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.ResultHandler;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.persistence.dbms.model.ItemVO;
import org.openhab.persistence.dbms.model.ItemsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extended Database Configuration class and its inner-classes. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from DbmsBaseDB. Enter only the differences DbmsBaseDB here.
 * Overrides all
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsPostgresql extends DbmsBaseDB {
	private static final Logger logger = LoggerFactory.getLogger(DbmsPostgresql.class);
	protected int maxActiveConnections = 3;
	protected int maxIdleConnections = 2;

	public DbmsPostgresql() {
		super();
		initEtendedSqlTypes();
		setDriverClassName("org.postgresql.Driver");
	}

	public void initEtendedSqlTypes() {
		// Initialize the type array
		sqlTypes.put("CALLITEM", "VARCHAR");
		sqlTypes.put("COLORITEM", "VARCHAR");
		sqlTypes.put("CONTACTITEM", "VARCHAR");
		sqlTypes.put("DATETIMEITEM", "TIMESTAMP");
		sqlTypes.put("DIMMERITEM", "SMALLINT");
		sqlTypes.put("LOCATIONITEM", "VARCHAR");
		sqlTypes.put("NUMBERITEM", "DOUBLE PRECISION");
		sqlTypes.put("ROLLERSHUTTERITEM", "SMALLINT");
		sqlTypes.put("STRINGITEM", "VARCHAR");
		sqlTypes.put("SWITCHITEM", "VARCHAR");
		logger.debug("DBMS::initPostgreSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
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
		String simpleName = (String) params.get("simpleName");

		logger.debug(
				"DBMS::getHistItemFilterQueryProvider filter = {}, numberDecimalcount = {}, table = {}, simpleName = {}",
				filter, numberDecimalcount, table, simpleName);
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
		filterString += (filter.getOrdering() == Ordering.ASCENDING) ? " ORDER BY time ASC" : " ORDER BY time DESC";
		if (filter.getPageSize() != 0x7fffffff) {
			// see:
			// http://www.jooq.org/doc/3.5/manual/sql-building/sql-statements/select-statement/limit-clause/
			filterString += " OFFSET " + filter.getPageNumber() * filter.getPageSize() + " LIMIT "
					+ filter.getPageSize();
		}
		String queryString = "NUMBERITEM".equalsIgnoreCase(simpleName)
				? "SELECT time, ROUND(CAST (value AS numeric)," + numberDecimalcount + ") FROM " + table
				: "SELECT time, value FROM " + table;
		if (!filterString.isEmpty()) {
			queryString += filterString;
		}
		logger.debug("DBMS::query queryString = {}", queryString);
		return queryString;
	}

	public interface DbDAO extends BaseInnerDAO {

		String SQL_CREATE_ITEMS_TABLE_IF_NOT       = "CREATE TABLE IF NOT EXISTS ${itemsManageTable} (itemid SERIAL NOT NULL, ${colname} ${coltype} NOT NULL, CONSTRAINT ${itemsManageTable}_pkey PRIMARY KEY (itemid))";
		String SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE = "INSERT INTO items (itemname) SELECT itemname FROM items UNION VALUES (#{itemname}) EXCEPT SELECT itemname FROM items";
		String SQL_INSERT_ITEM_VALUE               = "INSERT INTO ${tableName} (TIME, VALUE) VALUES( NOW(), CAST( #{value, javaType=${javaType}, jdbcType=${jdbcType}} as ${dbType}) )";

		@Override
		@Insert(SQL_CREATE_ITEMS_TABLE_IF_NOT)
		public boolean doCreateItemsTableIfNot(ItemsVO vo) throws Exception;

		@Override
		@Insert(SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE)
		@Options(useGeneratedKeys = true, keyProperty = "itemid")
		public int doCreateNewEntryInItemsTable(ItemsVO vo) throws Exception;

		@Override
		@Insert(SQL_INSERT_ITEM_VALUE)
		public void doInsertItemValue(ItemVO vo) throws Exception;

		/**
		 * USE type=DbmsPostgresql.class here!
		 */
		@Override
		@SelectProvider(type = DbmsPostgresql.class, method = "getHistItemFilterQueryProvider")
		@ResultType(value = HashMap.class)
		public void doGetHistItemFilterQuery(@Param("ItemResultHandler") ResultHandler<?> handler,
				@Param("filter") FilterCriteria filter, @Param("numberDecimalcount") int numberDecimalcount,
				@Param("table") String table, @Param("simpleName") String simpleName) throws Exception;
	}
}
