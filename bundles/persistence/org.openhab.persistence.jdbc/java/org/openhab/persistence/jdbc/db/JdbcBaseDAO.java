package org.openhab.persistence.jdbc.db;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;
import org.openhab.persistence.jdbc.model.ItemVO;
import org.openhab.persistence.jdbc.model.ItemsVO;
import org.openhab.persistence.jdbc.model.JdbcItem;
import org.openhab.persistence.jdbc.utils.StringUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.yank.Yank;

public class JdbcBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(JdbcBaseDAO.class);

    public Properties databaseProps = new Properties();
    protected String urlSuffix = "";
    public Map<String, String> sqlTypes = new HashMap<String, String>();

    protected String SQL_PING_DB;
    protected String SQL_GET_DB;
    protected String SQL_IF_TABLE_EXISTS;
    protected String SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE;
    protected String SQL_CREATE_ITEMS_TABLE_IF_NOT;
    protected String SQL_DELETE_ITEMS_ENTRY;
    protected String SQL_GET_ITEMID_TABLE_NAMES;
    protected String SQL_GET_ITEM_TABLES;
    protected String SQL_CREATE_ITEM_TABLE;
    protected String SQL_INSERT_ITEM_VALUE;

    /********
     * INIT *
     ********/
    public JdbcBaseDAO() {
        initSqlTypes();
        initDbProps();
        initSqlQueries();
    }

    private void initSqlQueries() {
        logger.debug("JDBC::initSqlQueries: '{}'", this.getClass().getSimpleName());
        SQL_PING_DB = "SELECT 1";
        SQL_GET_DB = "SELECT DATABASE()";
        SQL_IF_TABLE_EXISTS = "SHOW TABLES LIKE '#searchTable#'";

        // Derby SQL_IF_TABLE_EXISTS = "SELECT * FROM SYS.SYSTABLES WHERE TABLENAME='#searchTable#'";
        // h2 SQL_IF_TABLE_EXISTS = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='#searchTable#'";
        // hsqldb SQL_IF_TABLE_EXISTS = "SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE
        // TABLE_NAME='#searchTable#'";
        // hsqldb SQL_IF_TABLE_EXISTS = "SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE
        // TABLE_NAME='#searchTable#'";
        // mysql SQL_IF_TABLE_EXISTS = "SHOW TABLES LIKE '#searchTable#'";
        // postgresql SQL_IF_TABLE_EXISTS = "SELECT * FROM PG_TABLES WHERE TABLENAME='#searchTable#'";
        // sqlite SQL_IF_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name='#searchTable#'";
        SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE = "INSERT INTO #itemsManageTable# (ItemName) VALUES ('#itemname#')";
        SQL_CREATE_ITEMS_TABLE_IF_NOT = "CREATE TABLE IF NOT EXISTS #itemsManageTable# (ItemId INT NOT NULL AUTO_INCREMENT,#colname# #coltype# NOT NULL,PRIMARY KEY (ItemId))";
        SQL_DELETE_ITEMS_ENTRY = "DELETE FROM items WHERE ItemName=#itemname#";
        SQL_GET_ITEMID_TABLE_NAMES = "SELECT itemid, itemname FROM #itemsManageTable#";
        SQL_GET_ITEM_TABLES = "SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema=#jdbcUriDatabaseName# AND NOT table_name=#itemsManageTable#";
        SQL_CREATE_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS #tableName# (time TIMESTAMP NOT NULL, value #dbType#, PRIMARY KEY(time))";
        SQL_INSERT_ITEM_VALUE = "INSERT INTO #tableName# (TIME, VALUE) VALUES( NOW(), ? ) ON DUPLICATE KEY UPDATE VALUE= ?";
    }

    /**
     * INFO: http://www.java2s.com/Code/Java/Database-SQL-JDBC/StandardSQLDataTypeswithTheirJavaEquivalents.htm
     */
    private void initSqlTypes() {
        logger.debug("JDBC::initSqlTypes: Initialize the type array");
        sqlTypes.put("CALLITEM", "VARCHAR(200)");
        sqlTypes.put("COLORITEM", "VARCHAR(70)");
        sqlTypes.put("CONTACTITEM", "VARCHAR(6)");
        sqlTypes.put("DATETIMEITEM", "DATETIME");
        sqlTypes.put("DIMMERITEM", "TINYINT");
        sqlTypes.put("LOCATIONITEM", "VARCHAR(30)");
        sqlTypes.put("NUMBERITEM", "DOUBLE");
        sqlTypes.put("ROLLERSHUTTERITEM", "TINYINT");
        sqlTypes.put("STRINGITEM", "VARCHAR(65500)");// jdbc max 21845
        sqlTypes.put("SWITCHITEM", "VARCHAR(6)");
    }

    /**
     * INFO: https://github.com/brettwooldridge/HikariCP
     *
     * driverClassName (used with jdbcUrl):
     * Derby: org.apache.derby.jdbc.EmbeddedDriver
     * H2: org.h2.Driver
     * HSQLDB: org.hsqldb.jdbcDriver
     * Jaybird: org.firebirdsql.jdbc.FBDriver
     * MariaDB: org.mariadb.jdbc.Driver
     * MySQL: com.mysql.jdbc.Driver
     * MaxDB: com.sap.dbtech.jdbc.DriverSapDB
     * PostgreSQL: org.postgresql.Driver
     * SyBase: com.sybase.jdbc3.jdbc.SybDriver
     * SqLite: org.sqlite.JDBC
     *
     * dataSourceClassName (for alternative Configuration):
     * Derby: org.apache.derby.jdbc.ClientDataSource
     * H2: org.h2.jdbcx.JdbcDataSource
     * HSQLDB: org.hsqldb.jdbc.JDBCDataSource
     * Jaybird: org.firebirdsql.pool.FBSimpleDataSource
     * MariaDB, MySQL: org.mariadb.jdbc.MySQLDataSource
     * MaxDB: com.sap.dbtech.jdbc.DriverSapDB
     * PostgreSQL: org.postgresql.ds.PGSimpleDataSource
     * SyBase: com.sybase.jdbc4.jdbc.SybDataSource
     * SqLite: org.sqlite.SQLiteDataSource
     *
     * HikariPool - configuration Example:
     * allowPoolSuspension.............false
     * autoCommit......................true
     * catalog.........................
     * connectionInitSql...............
     * connectionTestQuery.............
     * connectionTimeout...............30000
     * dataSource......................
     * dataSourceClassName.............
     * dataSourceJNDI..................
     * dataSourceProperties............{password=<masked>}
     * driverClassName.................
     * healthCheckProperties...........{}
     * healthCheckRegistry.............
     * idleTimeout.....................600000
     * initializationFailFast..........true
     * isolateInternalQueries..........false
     * jdbc4ConnectionTest.............false
     * jdbcUrl.........................jdbc:mysql://192.168.0.1:3306/test
     * leakDetectionThreshold..........0
     * maxLifetime.....................1800000
     * maximumPoolSize.................10
     * metricRegistry..................
     * metricsTrackerFactory...........
     * minimumIdle.....................10
     * password........................<masked>
     * poolName........................HikariPool-0
     * readOnly........................false
     * registerMbeans..................false
     * scheduledExecutorService........
     * threadFactory...................
     * transactionIsolation............
     * username........................xxxx
     * validationTimeout...............5000
     */
    private void initDbProps() {
        // databaseProps.setProperty("dataSource.url", "jdbc:mysql://192.168.0.1:3306/test");
        // databaseProps.setProperty("dataSource.user", "test");
        // databaseProps.setProperty("dataSource.password", "test");

        // Most relevant Performance values
        // maximumPoolSize to 20, minimumIdle to 5, and idleTimeout to 2 minutes.
        // databaseProps.setProperty("maximumPoolSize", ""+maximumPoolSize);
        // databaseProps.setProperty("minimumIdle", ""+minimumIdle);
        // databaseProps.setProperty("idleTimeout", ""+idleTimeout);
        // databaseProps.setProperty("connectionTimeout",""+connectionTimeout);
        // databaseProps.setProperty("idleTimeout", ""+idleTimeout);
        // databaseProps.setProperty("maxLifetime", ""+maxLifetime);
        // databaseProps.setProperty("validationTimeout",""+validationTimeout);

    }

    /**************
     * ITEMS DAOs *
     **************/
    public Integer doPingDB() {
        return Yank.queryScalar(SQL_PING_DB, Integer.class, null);
    }

    public String doGetDB() {
        return Yank.queryScalar(SQL_GET_DB, String.class, null);
    }

    public boolean doIfTableExists(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_IF_TABLE_EXISTS, new String[] { "#searchTable#" },
                new String[] { vo.getItemsManageTable() });
        logger.debug("JDBC::doCreateNewEntryInItemsTable sql={}", sql);
        return Yank.queryScalar(sql, String.class, null) != null;
    }

    public Long doCreateNewEntryInItemsTable(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_NEW_ENTRY_IN_ITEMS_TABLE,
                new String[] { "#itemsManageTable#", "#itemname#" },
                new String[] { vo.getItemsManageTable(), vo.getItemname() });
        logger.debug("JDBC::doCreateNewEntryInItemsTable sql={}", sql);
        return Yank.insert(sql, null);
    }

    public ItemsVO doCreateItemsTableIfNot(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEMS_TABLE_IF_NOT,
                new String[] { "#itemsManageTable#", "#colname#", "#coltype#" },
                new String[] { vo.getItemsManageTable(), vo.getColname(), vo.getColtype() });
        logger.debug("JDBC::doCreateItemsTableIfNot sql={}", sql);
        Yank.execute(sql, null);
        return vo;
    }

    public void doDeleteItemsEntry(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_DELETE_ITEMS_ENTRY, new String[] { "#itemname#" },
                new String[] { vo.getItemname() });
        logger.debug("JDBC::doDeleteItemsEntry sql={}", sql);
        Yank.execute(sql, null);
    }

    public List<ItemsVO> doGetItemIDTableNames(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_GET_ITEMID_TABLE_NAMES, new String[] { "#itemsManageTable#" },
                new String[] { vo.getItemsManageTable() });
        logger.debug("JDBC::doGetItemIDTableNames sql={}", sql);
        return Yank.queryBeanList(sql, ItemsVO.class, null);
    }

    public List<ItemsVO> doGetItemTables(ItemsVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_GET_ITEM_TABLES,
                new String[] { "#jdbcUriDatabaseName#", "#itemsManageTable#" },
                new String[] { vo.getJdbcUriDatabaseName(), vo.getItemsManageTable() });
        logger.debug("JDBC::doGetItemTables sql={}", sql);
        return Yank.queryBeanList(sql, ItemsVO.class, null);
    }

    /*************
     * ITEM DAOs *
     *************/
    public void doUpdateItemTableNames(List<ItemVO> vol) {
        String sql = updateItemTableNamesProvider(vol);
        Yank.execute(sql, null);
    }

    public void doCreateItemTable(ItemVO vo) {
        String sql = StringUtilsExt.replaceArrayMerge(SQL_CREATE_ITEM_TABLE, new String[] { "#tableName#", "#dbType#" },
                new String[] { vo.getTableName(), vo.getDbType() });
        Yank.execute(sql, null);
    }

    public void doStoreItemValue(Item item, ItemVO vo) {
        vo = storeItemValueProvider(item, vo);
        String sql = StringUtilsExt.replaceArrayMerge(SQL_INSERT_ITEM_VALUE, new String[] { "#tableName#" },
                new String[] { vo.getTableName() });
        Object[] params = new Object[] { vo.getValue(), vo.getValue() };
        logger.debug("JDBC::doStoreItemValue sql={} value='{}'", sql, vo.getValue());
        Yank.execute(sql, params);
    }

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

    /*************
     * Providers *
     *************/
    static final DateTimeFormatter jdbcDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

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
        filterString += (filter.getOrdering() == Ordering.ASCENDING) ? " ORDER BY time ASC" : " ORDER BY time DESC ";
        if (filter.getPageSize() != 0x7fffffff) {
            filterString += " LIMIT " + filter.getPageNumber() * filter.getPageSize() + "," + filter.getPageSize();
        }
        // SELECT time, ROUND(value,3) FROM number_item_0114 ORDER BY time DESC LIMIT 0,1
        // rounding HALF UP
        String queryString = "NUMBERITEM".equalsIgnoreCase(simpleName) && numberDecimalcount > -1
                ? "SELECT time, ROUND(value," + numberDecimalcount + ") FROM " + table
                : "SELECT time, value FROM " + table;
        if (!filterString.isEmpty()) {
            queryString += filterString;
        }
        logger.debug("JDBC::query queryString = {}", queryString);
        return queryString;
    }

    private String updateItemTableNamesProvider(List<ItemVO> namesList) {
        logger.debug("JDBC::updateItemTableNamesProvider namesList.size = {}", namesList.size());
        String queryString = "";
        for (int i = 0; i < namesList.size(); i++) {
            ItemVO it = namesList.get(i);
            queryString += "ALTER TABLE " + it.getTableName() + " RENAME TO " + it.getNewTableName() + ";";
        }
        logger.debug("JDBC::query queryString = {}", queryString);
        return queryString;
    }

    protected ItemVO storeItemValueProvider(Item item, ItemVO vo) {
        String itemType = getItemType(item);

        logger.debug("JDBC::storeItemValueProvider: item '{}' as Type '{}' in '{}' with state '{}'", item.getName(),
                itemType, vo.getTableName(), item.getState().toString());

        // insertItemValue
        logger.debug("JDBC::storeItemValueProvider: getState: '{}'", item.getState().toString());
        if ("COLORITEM".equals(itemType)) {
            vo.setValueTypes(getSqlTypes().get(itemType), java.lang.String.class);
            vo.setValue(item.getState().toString());
        } else if ("NUMBERITEM".equals(itemType)) {
            String it = getSqlTypes().get(itemType);
            if (it.toUpperCase().contains("DOUBLE")) {
                vo.setValueTypes(it, java.lang.Double.class);
                Number newVal = ((DecimalType) item.getState());
                logger.debug("JDBC::storeItemValueProvider: newVal.doubleValue: '{}'", newVal.doubleValue());
                vo.setValue(newVal.doubleValue());
            } else if (it.toUpperCase().contains("DECIMAL") || it.toUpperCase().contains("NUMERIC")) {
                vo.setValueTypes(it, java.math.BigDecimal.class);
                DecimalType newVal = ((DecimalType) item.getState());
                logger.debug("JDBC::storeItemValueProvider: newVal.toBigDecimal: '{}'", newVal.toBigDecimal());
                vo.setValue(newVal.toBigDecimal());
            } else if (it.toUpperCase().contains("INT")) {
                vo.setValueTypes(it, java.lang.Integer.class);
                Number newVal = ((DecimalType) item.getState());
                logger.debug("JDBC::storeItemValueProvider: newVal.intValue: '{}'", newVal.intValue());
                vo.setValue(newVal.intValue());
            } else {// fall back to String
                vo.setValueTypes(it, java.lang.String.class);
                logger.warn("JDBC::storeItemValueProvider: item.getState().toString(): '{}'",
                        item.getState().toString());
                vo.setValue(item.getState().toString());
            }
        } else if ("ROLLERSHUTTERITEM".equals(itemType) || "DIMMERITEM".equals(itemType)) {
            vo.setValueTypes(getSqlTypes().get(itemType), java.lang.Integer.class);
            Number newVal = ((DecimalType) item.getState());
            logger.debug("JDBC::storeItemValueProvider: newVal.intValue: '{}'", newVal.intValue());
            vo.setValue(newVal.intValue());
        } else if ("DATETIMEITEM".equals(itemType)) {
            // vo.setValueTypes(getSqlTypes().get(itemType), java.util.Date.class);
            vo.setValueTypes(getSqlTypes().get(itemType), java.sql.Date.class);
            Calendar x = ((DateTimeType) item.getState()).getCalendar();
            java.sql.Date d = new java.sql.Date(x.getTimeInMillis());
            logger.debug("JDBC::storeItemValueProvider: DateTimeItem: '{}'", d);
            vo.setValue(d);
        } else {
            /*
             * !!ATTENTION!!
             *
             * 1. DimmerItem.getStateAs(PercentType.class).toString() always
             * returns 0
             * RollershutterItem.getStateAs(PercentType.class).toString() works
             * as expected
             *
             * 2. (item instanceof ColorItem) == (item instanceof DimmerItem) =
             * true Therefore for instance tests ColorItem always has to be
             * tested before DimmerItem
             *
             * !!ATTENTION!!
             */
            // All other items should return the best format by default
            vo.setValueTypes(getSqlTypes().get(itemType), java.lang.String.class);
            logger.debug("JDBC::storeItemValueProvider: other: item.getState().toString(): '{}'",
                    item.getState().toString());
            vo.setValue(item.getState().toString());
        }
        return vo;
    }

    /*****************
     * H E L P E R S *
     *****************/
    protected State getState(Item item, Object v) {
        String clazz = v.getClass().getSimpleName();
        logger.debug("JDBC::ItemResultHandler::handleResult getState value = '{}', getClass = '{}', clazz = '{}'",
                v.toString(), v.getClass(), clazz);
        if (item instanceof NumberItem) {
            String it = getSqlTypes().get("NUMBERITEM");
            if (it.toUpperCase().contains("DOUBLE")) {
                return new DecimalType(((Number) v).doubleValue());
            } else if (it.toUpperCase().contains("DECIMAL") || it.toUpperCase().contains("NUMERIC")) {
                return new DecimalType((BigDecimal) v);
            } else if (it.toUpperCase().contains("INT")) {
                return new DecimalType(((Integer) v).intValue());
            }
            return DecimalType.valueOf(((String) v).toString());

        } else if (item instanceof ColorItem) {
            return HSBType.valueOf(((String) v).toString());

        } else if (item instanceof DimmerItem) {
            return new PercentType(objectAsInteger(v));

        } else if (item instanceof SwitchItem) {
            return OnOffType.valueOf(((String) v).toString().trim());

        } else if (item instanceof ContactItem) {
            return OpenClosedType.valueOf(((String) v).toString().trim());

        } else if (item instanceof RollershutterItem) {
            return new PercentType(objectAsInteger(v));

        } else if (item instanceof DateTimeItem) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(objectAsLong(v));
            return new DateTimeType(calendar);

        } else if (item instanceof StringItem) {
            return StringType.valueOf(((String) v).toString());

        } else {// Call, Location, String
            return StringType.valueOf(((String) v).toString());

        }
    }

    protected Date objectAsDate(Object v) {
        if (v instanceof java.lang.String) {
            // toInstant is Java8 only: return Date.from(Timestamp.valueOf(v.toString()).toInstant());
            return new Date(Timestamp.valueOf(v.toString()).getTime());
        }
        // toInstant is Java8 only: return Date.from(((Timestamp) v).toInstant());
        return new Date(((Timestamp) v).getTime());
    }

    protected Long objectAsLong(Object v) {
        if (v instanceof Long) {
            return ((Number) v).longValue();
        } else if (v instanceof java.sql.Date) {
            return ((java.sql.Date) v).getTime();
        }
        return ((java.sql.Timestamp) v).getTime();
    }

    protected Integer objectAsInteger(Object v) {
        if (v instanceof Byte) {
            return ((Byte) v).intValue();
        }
        return ((Integer) v).intValue();
    }

    public String getItemType(Item i) {
        Item item = i;
        String def = "STRINGITEM";
        if (i instanceof GroupItem) {
            item = ((GroupItem) i).getBaseItem();
            if (item == null) {
                // if GroupItem:<ItemType> is not defined in
                // *.items using StringType
                // logger.debug("JDBC: BaseItem GroupItem:<ItemType> is not
                // defined in *.items searching for first Member and try to use
                // as ItemType");
                logger.debug(
                        "JDBC::getItemType: Cannot detect ItemType for {} because the GroupItems' base type isn't set in *.items File.",
                        i.getName());
                item = ((GroupItem) i).getMembers().get(0);
                if (item == null) {
                    logger.debug(
                            "JDBC::getItemType: No ItemType found for first Child-Member of GroupItem {}, use ItemType for STRINGITEM as Fallback",
                            i.getName());
                    return def;
                }
            }
        }
        String itemType = item.getClass().getSimpleName().toUpperCase();
        logger.debug("JDBC::getItemType: Try to use ItemType {} for Item {}", itemType, i.getName());
        if (sqlTypes.get(itemType) == null) {
            logger.warn(
                    "JDBC::getItemType: No sqlType found for ItemType {}, use ItemType for STRINGITEM as Fallback for {}",
                    itemType, i.getName());
            return def;
        }
        return itemType;
    }

    /******************************
     * public Getters and Setters *
     ******************************/
    public Map<String, String> getSqlTypes() {
        return sqlTypes;
    }

    public String getDataType(Item item) {
        return sqlTypes.get(getItemType(item));
    }

}
