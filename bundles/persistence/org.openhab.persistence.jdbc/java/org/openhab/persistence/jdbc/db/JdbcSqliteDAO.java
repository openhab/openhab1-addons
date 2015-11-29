/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jdbc.db;

import org.openhab.core.items.Item;
import org.openhab.persistence.jdbc.model.ItemVO;
import org.openhab.persistence.jdbc.model.ItemsVO;
import org.openhab.persistence.jdbc.utils.StringUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.yank.Yank;

/**
 * Extended Database Configuration class. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from JdbcBaseDAO. Enter only the differences to JdbcBaseDAO here.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class JdbcSqliteDAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcSqliteDAO.class);

    /********
     * INIT *
     ********/
    public JdbcSqliteDAO() {
        super();
        initSqlQueries();
        initSqlTypes();
        initDbProps();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
        SQL_IF_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name='#searchTable#'";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE IF NOT EXISTS #itemsManageTable# (ItemId INTEGER PRIMARY KEY AUTOINCREMENT, #colname# #coltype# NOT NULL)";
        SQL_INSERT_ITEM_VALUE = "INSERT OR IGNORE INTO #tableName# (TIME, VALUE) VALUES( DATETIME('now'), CAST( ? as #dbType#) )";
    }

    /**
     * INFO: http://www.java2s.com/Code/Java/Database-SQL-JDBC/StandardSQLDataTypeswithTheirJavaEquivalents.htm
     */
    private void initSqlTypes() {
    }

    /**
     * INFO: https://github.com/brettwooldridge/HikariCP
     */
    private void initDbProps() {

        // Properties for HikariCP
        databaseProps.setProperty("driverClassName", "org.sqlite.JDBC");
        // driverClassName OR BETTER USE dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
    }

    /**************
     * ITEMS DAOs *
     **************/
    @Override
    public ItemsVO doCreateItemsTableIfNot(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEMS_TABLE_IF_NOT,
                new String[] { "#itemsManageTable#", "#colname#", "#coltype#" },
                new String[] { vo.getItemsManageTable(), vo.getColname(), vo.getColtype() });
        logger.debug("JDBC::doCreateItemsTableIfNot sql={}", sql);
        Yank.execute(sql, null);
        return vo;
    }

    /*************
     * ITEM DAOs *
     *************/
    @Override
    public void doStoreItemValue(Item item, ItemVO vo) {
        vo = storeItemValueProvider(item, vo);
        String sql = StringUtilsExt.replaceArrayMerge(SQL_INSERT_ITEM_VALUE, new String[] { "#tableName#", "#dbType#" },
                new String[] { vo.getTableName(), vo.getDbType() });
        Object[] params = new Object[] { vo.getValue() };
        logger.debug("JDBC::doStoreItemValue sql={} value='{}'", sql, vo.getValue());
        Yank.execute(sql, params);
    }

    /****************************
     * SQL generation Providers *
     ****************************/

    /*****************
     * H E L P E R S *
     *****************/

    /******************************
     * public Getters and Setters *
     ******************************/
}
