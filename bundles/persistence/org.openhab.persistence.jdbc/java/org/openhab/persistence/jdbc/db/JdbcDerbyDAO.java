/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jdbc.db;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.persistence.jdbc.model.ItemVO;
import org.openhab.persistence.jdbc.model.ItemsVO;
import org.openhab.persistence.jdbc.model.JdbcItem;
import org.openhab.persistence.jdbc.utils.StringUtilsExt;
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
public class JdbcDerbyDAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDerbyDAO.class);

    /********
     * INIT *
     ********/
    public JdbcDerbyDAO() {
        super();
        initSqlTypes();
        initDbProps();
        initSqlQueries();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
        SQL_PING_DB = "values 1";
        SQL_IF_TABLE_EXISTS = "SELECT * FROM SYS.SYSTABLES WHERE TABLENAME='#searchTable#'";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE #itemsManageTable# ( ItemId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), #colname# #coltype# NOT NULL)";
        SQL_CREATE_ITEM_TABLE = "CREATE TABLE #tableName# (time TIMESTAMP NOT NULL, value #dbType#, PRIMARY KEY(time))";
        // Prevent error against duplicate time value (seldom): No powerful Merge found:
        // http://www.codeproject.com/Questions/162627/how-to-insert-new-record-in-my-table-if-not-exists
        SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( CURRENT_TIMESTAMP, CAST( ? as #dbType#) )";
    }

    private void initSqlTypes() {
        sqlTypes.put("DATETIMEITEM", "DATE");
        sqlTypes.put("DIMMERITEM", "SMALLINT");
        sqlTypes.put("ROLLERSHUTTERITEM", "SMALLINT");
        sqlTypes.put("STRINGITEM", "VARCHAR(32000)");
        logger.debug("JDBC::initEtendedSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
    }

    /**
     * INFO: https://github.com/brettwooldridge/HikariCP
     */
    private void initDbProps() {

        // Properties for HikariCP
        // Use driverClassName
        databaseProps.setProperty("driverClassName", "org.apache.derby.jdbc.EmbeddedDriver");
        // OR dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "org.apache.derby.jdbc.EmbeddedDataSource");
        databaseProps.setProperty("maximumPoolSize", "1");
        databaseProps.setProperty("minimumIdle", "1");

    }

    /**************
     * ITEMS DAOs *
     **************/
    @Override
    public Integer doPingDB() {
        return Yank.queryScalar(SQL_PING_DB, Integer.class, null);
    }

    @Override
    public ItemsVO doCreateItemsTableIfNot(ItemsVO vo) {
        // boolean tableExists = Yank.queryScalar(SQL_IF_TABLE_EXISTS.replace("#searchTable#",
        // vo.getItemsManageTable().toUpperCase()), String.class, null) == null;
        boolean tableExists = doIfTableExists(vo);
        if (tableExists) {
            String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEMS_TABLE_IF_NOT,
                    new String[] { "#itemsManageTable#", "#colname#", "#coltype#" },
                    new String[] { vo.getItemsManageTable().toUpperCase(), vo.getColname(), vo.getColtype() });
            logger.debug("JDBC::doCreateItemsTableIfNot tableExists={} therefore sql={}", sql);
            Yank.execute(sql, null);
        } else
            logger.debug("JDBC::doCreateItemsTableIfNot tableExists={}, did no CREATE TABLE", tableExists);
        return vo;
    }

    /*************
     * ITEM DAOs *
     *************/
    @Override
    public void doCreateItemTable(ItemVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEM_TABLE, new String[] { "#tableName#", "#dbType#" },
                new String[] { vo.getTableName(), vo.getDbType() });
        Yank.execute(sql, null);
    }

    @Override
    public void doStoreItemValue(Item item, ItemVO vo) {
        vo = storeItemValueProvider(item, vo);
        String sql = StringUtilsExt.replaceArrayMerge(SQL_INSERT_ITEM_VALUE, new String[] { "#tableName#", "#dbType#" },
                new String[] { vo.getTableName().toUpperCase(), vo.getDbType() });
        Object[] params = new Object[] { vo.getValue() };
        logger.debug("JDBC::doStoreItemValue sql={} value='{}'", sql, vo.getValue());
        Yank.execute(sql, params);
    }

    @Override
    public List<HistoricItem> doGetHistItemFilterQuery(Item item, FilterCriteria filter, int numberDecimalcount,
            String table, String name) {
        String sql = histItemFilterQueryProvider(filter, numberDecimalcount, table, name);
        logger.debug("JDBC::doGetHistItemFilterQuery sql={}", sql);
        List<Object[]> m = Yank.queryObjectArrays(sql, null);

        List<HistoricItem> items = new ArrayList<HistoricItem>();
        for (int i = 0; i < m.size(); i++) {
            items.add(new JdbcItem(item.getName(), getState(item, m.get(i)[1]), objectAsDate(m.get(i)[0])));
        }
        return items;
    }

    /****************************
     * SQL generation Providers *
     ****************************/
    static final DateTimeFormatter jdbcDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @param filter
     * @param numberDecimalcount
     * @param table
     * @return
     */
    private String histItemFilterQueryProvider(FilterCriteria filter, int numberDecimalcount, String table,
            String simpleName) {
        logger.debug(
                "JDBC::getHistItemFilterQueryProvider filter = {}, numberDecimalcount = {}, table = {}, simpleName = {}",
                filter.toString(), numberDecimalcount, table, simpleName);

        String filterString = "";
        if (filter.getBeginDate() != null) {
            filterString += filterString.isEmpty() ? " WHERE" : " AND";
            filterString += " TIME>'" + jdbcDateFormat.print(new DateTime(filter.getBeginDate().getTime())) + "'";
        }
        if (filter.getEndDate() != null) {
            filterString += filterString.isEmpty() ? " WHERE" : " AND";
            filterString += " TIME>'" + jdbcDateFormat.print(new DateTime(filter.getEndDate().getTime())) + "'";
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

        // http://www.seemoredata.com/en/showthread.php?132-Round-function-in-Apache-Derby
        // simulated round function in Derby: CAST(value 0.0005 AS DECIMAL(15,3))
        // simulated round function in Derby: "CAST(value 0.0005 AS DECIMAL(15,"+numberDecimalcount+"))"

        String queryString = "SELECT time,";
        if ("NUMBERITEM".equalsIgnoreCase(simpleName) && numberDecimalcount > -1) {
            // rounding HALF UP
            queryString += "CAST(value 0.";
            for (int i = 0; i < numberDecimalcount; i++)
                queryString += "0";
            queryString += "5 AS DECIMAL(31," + numberDecimalcount + "))"; // 31 is DECIMAL max precision
                                                                           // https://db.apache.org/derby/docs/10.0/manuals/develop/develop151.html
        } else {
            queryString += "SELECT time, value FROM " + table;
        }

        if (!filterString.isEmpty()) {
            queryString += filterString;
        }
        logger.debug("JDBC::query queryString = {}", queryString);
        return queryString;
    }

    /*****************
     * H E L P E R S *
     *****************/

    /******************************
     * public Getters and Setters *
     ******************************/

}
