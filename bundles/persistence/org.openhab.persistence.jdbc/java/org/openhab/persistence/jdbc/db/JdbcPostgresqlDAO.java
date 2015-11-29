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

import com.xeiam.yank.Yank;

/**
 * Extended Database Configuration class. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from JdbcBaseDAO. Enter only the differences to JdbcBaseDAO here.
 *
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class JdbcPostgresqlDAO extends JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcPostgresqlDAO.class);

    /********
     * INIT *
     ********/
    public JdbcPostgresqlDAO() {
        super();
        initSqlQueries();
        initSqlTypes();
        initDbProps();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
        SQL_IF_TABLE_EXISTS = "SELECT * FROM PG_TABLES WHERE TABLENAME='#searchTable#'";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE IF NOT EXISTS #itemsManageTable# (itemid SERIAL NOT NULL, #colname# #coltype# NOT NULL, CONSTRAINT #itemsManageTable#_pkey PRIMARY KEY (itemid))";
        SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE = "INSERT INTO items (itemname) SELECT itemname FROM #itemsManageTable# UNION VALUES ('#itemname#') EXCEPT SELECT itemname FROM items";
        SQL_GET_ITEM_TABLES = "SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema='public' AND NOT table_name=#itemsManageTable#";
        // http://stackoverflow.com/questions/17267417/how-do-i-do-an-upsert-merge-insert-on-duplicate-update-in-postgresql
        // for later use, PostgreSql > 9.5 to prevent PRIMARY key violation use:
        // SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( NOW(), CAST( ? as #dbType#) ) ON
        // CONFLICT DO NOTHING";
        SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( NOW(), CAST( ? as #dbType#) )";
    }

    /**
     * INFO: http://www.java2s.com/Code/Java/Database-SQL-JDBC/StandardSQLDataTypeswithTheirJavaEquivalents.htm
     */
    private void initSqlTypes() {
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
        logger.debug("JDBC::initSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
    }

    /**
     * INFO: https://github.com/brettwooldridge/HikariCP
     */
    private void initDbProps() {

        // Performance:
        // databaseProps.setProperty("dataSource.cachePrepStmts", "true");
        // databaseProps.setProperty("dataSource.prepStmtCacheSize", "250");
        // databaseProps.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");

        // Properties for HikariCP
        databaseProps.setProperty("driverClassName", "org.postgresql.Driver");
        // driverClassName OR BETTER USE dataSourceClassName
        // databaseProps.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        // databaseProps.setProperty("maximumPoolSize", "3");
        // databaseProps.setProperty("minimumIdle", "2");
    }

    /**************
     * ITEMS DAOs *
     **************/
    @Override
    public ItemsVO doCreateItemsTableIfNot(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEMS_TABLE_IF_NOT,
                new String[] { "#itemsManageTable#", "#colname#", "#coltype#", "#itemsManageTable#" },
                new String[] { vo.getItemsManageTable(), vo.getColname(), vo.getColtype(), vo.getItemsManageTable() });
        logger.debug("JDBC::doCreateItemsTableIfNot sql={}", sql);
        Yank.execute(sql, null);
        return vo;
    }

    @Override
    public Long doCreateNewEntryInItemsTable(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE,
                new String[] { "#itemsManageTable#", "#itemname#" },
                new String[] { vo.getItemsManageTable(), vo.getItemname() });
        logger.debug("JDBC::doCreateNewEntryInItemsTable sql={}", sql);
        return Yank.insert(sql, null);
    }

    @Override
    public List<ItemsVO> doGetItemTables(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_GET_ITEM_TABLES, new String[] { "#itemsManageTable#" },
                new String[] { vo.getItemsManageTable() });
        logger.debug("JDBC::doGetItemTables sql={}", sql);
        return Yank.queryBeanList(sql, ItemsVO.class, null);
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
            filterString += " TIME<'" + jdbcDateFormat.print(new DateTime(filter.getEndDate().getTime())) + "'";
        }
        filterString += (filter.getOrdering() == Ordering.ASCENDING) ? " ORDER BY time ASC" : " ORDER BY time DESC";
        if (filter.getPageSize() != 0x7fffffff) {
            // see:
            // http://www.jooq.org/doc/3.5/manual/sql-building/sql-statements/select-statement/limit-clause/
            filterString += " OFFSET " + filter.getPageNumber() * filter.getPageSize() + " LIMIT "
                    + filter.getPageSize();
        }
        String queryString = "NUMBERITEM".equalsIgnoreCase(simpleName) && numberDecimalcount > -1
                ? "SELECT time, ROUND(CAST (value AS numeric)," + numberDecimalcount + ") FROM " + table
                : "SELECT time, value FROM " + table;
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
