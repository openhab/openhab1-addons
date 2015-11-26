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

/**
 * Extended Database Configuration class. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from JdbcBaseDAO. Enter only the differences to JdbcBaseDAO here.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class JdbcMariadbDAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMariadbDAO.class);

    /********
     * INIT *
     ********/
    public JdbcMariadbDAO() {
        super();
        initSqlQueries();
        initSqlTypes();
        initDbProps();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
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

        // Performance:
        databaseProps.setProperty("dataSource.cachePrepStmts", "true");
        databaseProps.setProperty("dataSource.prepStmtCacheSize", "250");
        databaseProps.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");

        // Properties for HikariCP
        databaseProps.setProperty("driverClassName", "org.mariadb.jdbc.Driver");
        // driverClassName OR BETTER USE dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "org.mariadb.jdbc.MySQLDataSource");
        databaseProps.setProperty("maximumPoolSize", "3");
        databaseProps.setProperty("minimumIdle", "2");
    }

    /**************
     * ITEMS DAOs *
     **************/

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
