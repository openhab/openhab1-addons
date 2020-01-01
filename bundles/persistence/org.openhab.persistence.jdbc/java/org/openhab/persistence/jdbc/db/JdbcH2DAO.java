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
public class JdbcH2DAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcH2DAO.class);

    /********
     * INIT *
     ********/
    public JdbcH2DAO() {
        super();
        initSqlQueries();
        initSqlTypes();
        initDbProps();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
        SQL_IF_TABLE_EXISTS = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='#searchTable#'";
        // SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( NOW(), CAST( ? as #dbType#) )";
        // http://stackoverflow.com/questions/19768051/h2-sql-database-insert-if-the-record-does-not-exist
        SQL_INSERT_ITEM_VALUE = "MERGE INTO #tableName# (TIME, VALUE) VALUES( #tablePrimaryValue#, CAST( ? as #dbType#) )";
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
        databaseProps.setProperty("driverClassName", "org.h2.Driver");
        // driverClassName OR BETTER USE dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
    }

    /**************
     * ITEMS DAOs *
     **************/

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
