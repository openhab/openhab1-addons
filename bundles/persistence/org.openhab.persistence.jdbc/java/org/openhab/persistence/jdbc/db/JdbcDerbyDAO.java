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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.knowm.yank.Yank;
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
        SQL_GET_DB = "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY( 'DataDictionaryVersion' )"; // returns version
        SQL_IF_TABLE_EXISTS = "SELECT * FROM SYS.SYSTABLES WHERE TABLENAME='#searchTable#'";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE #itemsManageTable# ( ItemId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), #colname# #coltype# NOT NULL)";
        SQL_CREATE_ITEM_TABLE = "CREATE TABLE #tableName# (time #tablePrimaryKey# NOT NULL, value #dbType#, PRIMARY KEY(time))";
        // Prevent error against duplicate time value (seldom): No powerful Merge found:
        // http://www.codeproject.com/Questions/162627/how-to-insert-new-record-in-my-table-if-not-exists
        SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( #tablePrimaryValue#, CAST( ? as #dbType#) )";
    }

    private void initSqlTypes() {
        sqlTypes.put("DATETIMEITEM", "TIMESTAMP");
        sqlTypes.put("DIMMERITEM", "SMALLINT");
        sqlTypes.put("ROLLERSHUTTERITEM", "SMALLINT");
        sqlTypes.put("STRINGITEM", "VARCHAR(32000)");
        sqlTypes.put("tablePrimaryValue", "CURRENT_TIMESTAMP");
        logger.debug("JDBC::initSqlTypes: Initialized the type array sqlTypes={}", sqlTypes.values());
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

    @Override
    public void initAfterFirstDbConnection() {
        logger.debug("JDBC::initAfterFirstDbConnection: Initializing step, after db is connected.");
        // Initialize sqlTypes, depending on DB version for example
        // derby does not like this... dbMeta = new DbMetaData();// get DB information
    }

    /**************
     * ITEMS DAOs *
     **************/
    @Override
    public Integer doPingDB() {
        return Yank.queryScalar(SQL_PING_DB, Integer.class, null);
    }

    @Override
    public boolean doIfTableExists(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_IF_TABLE_EXISTS, new String[] { "#searchTable#" },
                new String[] { vo.getItemsManageTable().toUpperCase() });
        logger.debug("JDBC::doIfTableExists sql={}", sql);
        return Yank.queryScalar(sql, String.class, null) != null;
    }

    @Override
    public Long doCreateNewEntryInItemsTable(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE,
                new String[] { "#itemsManageTable#", "#itemname#" },
                new String[] { vo.getItemsManageTable().toUpperCase(), vo.getItemname() });
        logger.debug("JDBC::doCreateNewEntryInItemsTable sql={}", sql);
        return Yank.insert(sql, null);
    }

    @Override
    public ItemsVO doCreateItemsTableIfNot(ItemsVO vo) {
        // boolean tableExists = Yank.queryScalar(SQL_IF_TABLE_EXISTS.replace("#searchTable#",
        // vo.getItemsManageTable().toUpperCase()), String.class, null) == null;
        boolean tableExists = doIfTableExists(vo);
        if (!tableExists) {
            String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEMS_TABLE_IF_NOT,
                    new String[] { "#itemsManageTable#", "#colname#", "#coltype#" },
                    new String[] { vo.getItemsManageTable().toUpperCase(), vo.getColname(), vo.getColtype() });
            logger.debug("JDBC::doCreateItemsTableIfNot tableExists={} therefore sql={}", tableExists, sql);
            Yank.execute(sql, null);
        } else {
            logger.debug("JDBC::doCreateItemsTableIfNot tableExists={}, did not CREATE TABLE", tableExists);
        }
        return vo;
    }

    /*************
     * ITEM DAOs *
     *************/
    @Override
    public void doCreateItemTable(ItemVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEM_TABLE,
                new String[] { "#tableName#", "#dbType#", "#tablePrimaryKey#" },
                new String[] { vo.getTableName(), vo.getDbType(), sqlTypes.get("tablePrimaryKey") });
        Yank.execute(sql, null);
    }

    @Override
    public void doStoreItemValue(Item item, ItemVO vo) {
        vo = storeItemValueProvider(item, vo);
        String sql = StringUtilsExt.replaceArrayMerge(SQL_INSERT_ITEM_VALUE,
                new String[] { "#tableName#", "#dbType#", "#tablePrimaryValue#" },
                new String[] { vo.getTableName().toUpperCase(), vo.getDbType(), sqlTypes.get("tablePrimaryValue") });
        Object[] params = new Object[] { vo.getValue() };
        logger.debug("JDBC::doStoreItemValue sql={} value='{}'", sql, vo.getValue());
        Yank.execute(sql, params);
    }

    @Override
    public List<HistoricItem> doGetHistItemFilterQuery(Item item, FilterCriteria filter, int numberDecimalcount,
            String table, String name) {
        String sql = histItemFilterQueryProvider(filter, numberDecimalcount, table, name);
        List<Object[]> m = Yank.queryObjectArrays(sql, null);

        logger.debug("JDBC::doGetHistItemFilterQuery got Array length={}", m.size());

        List<HistoricItem> items = new ArrayList<HistoricItem>();
        for (int i = 0; i < m.size(); i++) {
            logger.debug("JDBC::doGetHistItemFilterQuery 0='{}' 1='{}'", m.get(i)[0], m.get(i)[1]);
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
                StringUtilsExt.filterToString(filter), numberDecimalcount, table, simpleName);

        String filterString = "";
        if (filter.getBeginDate() != null) {
            filterString += filterString.isEmpty() ? " WHERE" : " AND";
            filterString += " TIME>'" + jdbcDateFormat.print(new DateTime(filter.getBeginDate().getTime())) + "'";
        }
        if (filter.getEndDate() != null) {
            filterString += filterString.isEmpty() ? " WHERE" : " AND";
            filterString += " TIME<'" + jdbcDateFormat.print(new DateTime(filter.getEndDate().getTime())) + "'";
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
            for (int i = 0; i < numberDecimalcount; i++) {
                queryString += "0";
            }
            queryString += "5 AS DECIMAL(31," + numberDecimalcount + "))"; // 31 is DECIMAL max precision
                                                                           // https://db.apache.org/derby/docs/10.0/manuals/develop/develop151.html
        } else {
            queryString += " value FROM " + table.toUpperCase();
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
