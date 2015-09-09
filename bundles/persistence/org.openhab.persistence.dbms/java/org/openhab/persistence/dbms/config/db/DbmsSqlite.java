/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config.db;

import org.apache.ibatis.annotations.Insert;
import org.openhab.persistence.dbms.model.ItemVO;
import org.openhab.persistence.dbms.model.ItemsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extended Database Configuration class and its inner-classes.
 * Class represents the extended database-specific configuration.
 * Overrides and supplements the default settings from DbmsBaseDB.
 * Enter only the differences DbmsBaseDB here.
 * Overrides all 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsSqlite extends DbmsBaseDB {
	private static final Logger logger = LoggerFactory.getLogger(DbmsSqlite.class);

	public DbmsSqlite() {
		super();
		initEtendedSqlTypes();
		setDriverClassName("org.sqlite.JDBC");
	}
	
	@Override
	public int checkMaxConnections(int i) {
		return 1;
	}

	public void initEtendedSqlTypes() {
		// Initialize the type array
		logger.debug("DBMS::initEtendedSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
	}

	public interface DbDAO extends BaseInnerDAO { 
		
		String SQL_CREATE_ITEMS_TABLE_IF_NOT       = "CREATE TABLE IF NOT EXISTS ${itemsManageTable} (ItemId INTEGER PRIMARY KEY AUTOINCREMENT,${colname} ${coltype} NOT NULL)";
		String SQL_INSERT_ITEM_VALUE               = "INSERT OR IGNORE INTO ${tableName} (TIME, VALUE) VALUES( DATETIME('now'), CAST( #{value, javaType=${javaType}, jdbcType=${jdbcType}} as ${dbType}) )";

		@Insert(SQL_CREATE_ITEMS_TABLE_IF_NOT)
		public boolean doCreateItemsTableIfNot(ItemsVO vo) throws Exception;		
		
		@Insert(SQL_INSERT_ITEM_VALUE)
		public void doInsertItemValue(ItemVO vo) throws Exception;  
	}
}
