/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jdbc.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.knowm.yank.Yank;

/**
 * Extended Database Configuration class. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from JdbcBaseDAO. Enter only the differences to JdbcBaseDAO here.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class JdbcMysqlDAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMysqlDAO.class);

    /********
     * INIT *
     ********/
    public JdbcMysqlDAO() {
        super();
        initSqlTypes();
        initDbProps();
        initSqlQueries();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
    }

    /**
     * INFO: http://www.java2s.com/Code/Java/Database-SQL-JDBC/StandardSQLDataTypeswithTheirJavaEquivalents.htm
     */
    private void initSqlTypes() {
        logger.debug("JDBC::initSqlTypes: Initialize the type array");
        // sqlTypes.put("STRINGITEM", "VARCHAR(65500)");//jdbc max 21845
    }

    /**
     * INFO: https://github.com/brettwooldridge/HikariCP
     */
    private void initDbProps() {

        // Performancetuning
        databaseProps.setProperty("dataSource.cachePrepStmts", "true");
        databaseProps.setProperty("dataSource.prepStmtCacheSize", "250");
        databaseProps.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        databaseProps.setProperty("dataSource.jdbcCompliantTruncation", "false");// jdbc standard max varchar max length
                                                                                 // of 21845

        // Properties for HikariCP
        // Use driverClassName
        databaseProps.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        // OR dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        databaseProps.setProperty("maximumPoolSize", "3");
        databaseProps.setProperty("minimumIdle", "2");
    }

    /**************
     * ITEMS DAOs *
     **************/
    @Override
    public Integer doPingDB() {
        return Yank.queryScalar(SQL_PING_DB, Long.class, null).intValue();
    }

    /*************
     * ITEM DAOs *
     *************/

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