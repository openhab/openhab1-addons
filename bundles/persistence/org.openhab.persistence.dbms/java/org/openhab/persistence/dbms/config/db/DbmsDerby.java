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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.ResultHandler;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
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
public class DbmsDerby extends DbmsBaseDB {
	private static final Logger logger = LoggerFactory.getLogger(DbmsDerby.class);

	public DbmsDerby() {
		super();
		initEtendedSqlTypes();
		setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
	}

	public void initEtendedSqlTypes() {
		sqlTypes.put("DATETIMEITEM", "DATE");
		sqlTypes.put("DIMMERITEM", "SMALLINT");
		sqlTypes.put("ROLLERSHUTTERITEM", "SMALLINT");
		sqlTypes.put("STRINGITEM", "VARCHAR(32000)");
		logger.debug("DBMS::initEtendedSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
	}

	@Override
	public int checkMaxConnections(int i) {
		return 1;
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
		filterString += (filter.getOrdering() == Ordering.ASCENDING) ? " ORDER BY time ASC" : " ORDER BY time DESC";
		if (filter.getPageSize() != 0x7fffffff) {
			// TODO: TESTING!!!
			// filterString += " LIMIT " + filter.getPageNumber() *
			// filter.getPageSize() + "," + filter.getPageSize();
			// SELECT time, value FROM ohscriptfiles_sw_ace_paths_0001 ORDER BY
			// time DESC OFFSET 1 ROWS FETCH NEXT 0 ROWS ONLY
			// filterString += " OFFSET " + filter.getPageSize() +" ROWS FETCH
			// FIRST||NEXT " + filter.getPageNumber() * filter.getPageSize() + "
			// ROWS ONLY";
			filterString += " OFFSET " + filter.getPageSize() + " ROWS FETCH FIRST "
					+ (filter.getPageNumber() * filter.getPageSize() + 1) + " ROWS ONLY";
		}
		String queryString = "SELECT time, value FROM " + table;
		if (!filterString.isEmpty()) {
			queryString += filterString;
		}
		logger.debug("DBMS::query queryString = {}", queryString);
		return queryString;
	}

	public interface DbDAO extends BaseInnerDAO {
		
		String SQL_PING_DB                         = "values 1";
		String SQL_CREATE_ITEMS_TABLE_IF_NOT       = "CREATE TABLE ${itemsManageTable} ( ItemId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ${colname} ${coltype} NOT NULL)";
		String SQL_CREATE_ITEM_TABLE               = "CREATE TABLE ${tableName} (time TIMESTAMP NOT NULL, value ${dbType}, PRIMARY KEY(time))";
		String SQL_INSERT_ITEM_VALUE               = "INSERT INTO ${tableName} (TIME, VALUE) VALUES( CURRENT_TIMESTAMP, CAST( #{value, javaType=${javaType}, jdbcType=${jdbcType}} as ${dbType}) )";

		// see:
		// http://stackoverflow.com/questions/3668506/efficient-sql-test-query-or-validation-query-that-will-work-across-all-or-most
		@Select(SQL_PING_DB)
		public int doPingDB() throws Exception;

		@Insert(SQL_CREATE_ITEMS_TABLE_IF_NOT)
		public boolean doCreateItemsTableIfNot(ItemsVO vo) throws Exception;

		@Insert(SQL_CREATE_ITEM_TABLE)
		public void doCreateItemTable(ItemVO vo) throws Exception;

		@Insert(SQL_INSERT_ITEM_VALUE)
		public void doInsertItemValue(ItemVO vo) throws Exception;

		@SelectProvider(type = DbmsDerby.class, method = "getHistItemFilterQueryProvider")
		@ResultType(value = HashMap.class)
		public void doGetHistItemFilterQuery(@Param("ItemResultHandler") ResultHandler<?> handler,
				@Param("filter") FilterCriteria filter, @Param("numberDecimalcount") int numberDecimalcount,
				@Param("table") String table, @Param("simpleName") String simpleName) throws Exception;
	}
}
