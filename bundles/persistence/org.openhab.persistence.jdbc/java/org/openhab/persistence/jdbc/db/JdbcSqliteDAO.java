/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.persistence.jdbc.db;

import org.knowm.yank.Yank;
import org.openhab.core.items.Item;
import org.openhab.persistence.jdbc.model.ItemVO;
import org.openhab.persistence.jdbc.model.ItemsVO;
import org.openhab.persistence.jdbc.utils.StringUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        SQL_GET_DB = "PRAGMA DATABASE_LIST"; // "SELECT SQLITE_VERSION()"; // "PRAGMA DATABASE_LIST"->db Path/Name
                                             // "PRAGMA SCHEMA_VERSION";
        SQL_IF_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name='#searchTable#'";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE IF NOT EXISTS #itemsManageTable# (ItemId INTEGER PRIMARY KEY AUTOINCREMENT, #colname# #coltype# NOT NULL)";
        SQL_INSERT_ITEM_VALUE = "INSERT OR IGNORE INTO #tableName# (TIME, VALUE) VALUES( #tablePrimaryValue#, CAST( ? as #dbType#) )";
    }

    /**
     * INFO: http://www.java2s.com/Code/Java/Database-SQL-JDBC/StandardSQLDataTypeswithTheirJavaEquivalents.htm
     */
    private void initSqlTypes() {
        logger.debug("JDBC::initSqlTypes: Initialize the type array");
        sqlTypes.put("tablePrimaryValue", "strftime('%Y-%m-%d %H:%M:%f' , 'now' , 'localtime')");
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
    public String doGetDB() {
        return Yank.queryColumn(SQL_GET_DB, "file", String.class, null).get(0);
    }

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
        String sql = StringUtilsExt.replaceArrayMerge(SQL_INSERT_ITEM_VALUE,
                new String[] { "#tableName#", "#dbType#", "#tablePrimaryValue#" },
                new String[] { vo.getTableName(), vo.getDbType(), sqlTypes.get("tablePrimaryValue") });
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
