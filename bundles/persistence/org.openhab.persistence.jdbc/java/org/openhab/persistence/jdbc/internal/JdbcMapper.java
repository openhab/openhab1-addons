package org.openhab.persistence.jdbc.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.yank.Yank;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.persistence.jdbc.model.ItemVO;
import org.openhab.persistence.jdbc.model.ItemsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcMapper {
    static final Logger logger = LoggerFactory.getLogger(JdbcMapper.class);

    // Error counter - used to reconnect to database on error
    protected int errCnt;
    protected boolean initialized = false;
    protected JdbcConfiguration conf = null;
    protected Map<String, String> sqlTables = new HashMap<String, String>();
    private long afterAccessMin = 10000;
    private long afterAccessMax = 0;
    private static final String ITEM_NAME_PATTERN = "[^a-zA-Z_0-9\\-]";

    /*****************
     * MAPPER ITEMS *
     *****************/
    public boolean pingDB() {
        logger.debug("JDBC::pingDB");
        boolean ret = false;
        long timerStart = System.currentTimeMillis();
        if (openConnection()) {
            if (conf.getDbName() == null) {
                String dbName = conf.getDBDAO().doGetDB();
                conf.setDbName(dbName);
                ret = dbName.length() > 0;
            } else {
                ret = conf.getDBDAO().doPingDB() > 0;
            }
        }
        logTime("pingDB", timerStart, System.currentTimeMillis());
        return ret;
    }

    public String getDB() {
        logger.debug("JDBC::getDB");
        long timerStart = System.currentTimeMillis();
        String res = conf.getDBDAO().doGetDB();
        logTime("pingDB", timerStart, System.currentTimeMillis());
        return res;
    }

    public ItemsVO createNewEntryInItemsTable(ItemsVO vo) {
        logger.debug("JDBC::createNewEntryInItemsTable");
        long timerStart = System.currentTimeMillis();
        Long i = conf.getDBDAO().doCreateNewEntryInItemsTable(vo);
        vo.setItemid(i.intValue());
        logTime("doCreateNewEntryInItemsTable", timerStart, System.currentTimeMillis());
        return vo;
    }

    public boolean createItemsTableIfNot(ItemsVO vo) {
        logger.debug("JDBC::createItemsTableIfNot");
        long timerStart = System.currentTimeMillis();
        conf.getDBDAO().doCreateItemsTableIfNot(vo);
        logTime("doCreateItemsTableIfNot", timerStart, System.currentTimeMillis());
        return true;
    }

    public ItemsVO deleteItemsEntry(ItemsVO vo) {
        logger.debug("JDBC::deleteItemsEntry");
        long timerStart = System.currentTimeMillis();
        conf.getDBDAO().doDeleteItemsEntry(vo);
        logTime("deleteItemsEntry", timerStart, System.currentTimeMillis());
        return vo;
    }

    public List<ItemsVO> getItemIDTableNames() {
        logger.debug("JDBC::getItemIDTableNames");
        long timerStart = System.currentTimeMillis();
        List<ItemsVO> vo = conf.getDBDAO().doGetItemIDTableNames(new ItemsVO());
        logTime("getItemIDTableNames", timerStart, System.currentTimeMillis());
        return vo;
    }

    public List<ItemsVO> getItemTables() {
        logger.debug("JDBC::getItemTables");
        long timerStart = System.currentTimeMillis();
        ItemsVO vo = new ItemsVO();
        vo.setJdbcUriDatabaseName(conf.getDbName());
        List<ItemsVO> vol = conf.getDBDAO().doGetItemTables(vo);
        logTime("getItemTables", timerStart, System.currentTimeMillis());
        return vol;
    }

    /****************
     * MAPPERS ITEM *
     ****************/
    public void updateItemTableNames(List<ItemVO> vol) {
        logger.debug("JDBC::createItemTable");
        long timerStart = System.currentTimeMillis();
        conf.getDBDAO().doUpdateItemTableNames(vol);
        logTime("createItemTable", timerStart, System.currentTimeMillis());
    }

    public ItemVO createItemTable(ItemVO vo) {
        logger.debug("JDBC::createItemTable");
        long timerStart = System.currentTimeMillis();
        conf.getDBDAO().doCreateItemTable(vo);
        logTime("createItemTable", timerStart, System.currentTimeMillis());
        return vo;
    }

    public Item storeItemValue(Item item) {
        logger.debug("JDBC::storeItemValue: Item={}", item.toString());
        String tableName = getTable(item);
        if (tableName == null) {
            logger.error("JDBC::store: Unable to store item '{}'.", item.getName());
            return item;
        }
        long timerStart = System.currentTimeMillis();
        conf.getDBDAO().doStoreItemValue(item, new ItemVO(tableName, null));
        logTime("storeItemValue", timerStart, System.currentTimeMillis());
        errCnt = 0;
        return item;
    }

    public List<HistoricItem> getHistItemFilterQuery(FilterCriteria filter, int numberDecimalcount, String table,
            Item item) {
        logger.debug(
                "JDBC::getHistItemFilterQuery filter='{}' numberDecimalcount='{}' table='{}' item='{}' itemName='{}'",
                (filter != null), numberDecimalcount, table, item, item.getName());
        if (table != null) {
            long timerStart = System.currentTimeMillis();
            List<HistoricItem> r = conf.getDBDAO().doGetHistItemFilterQuery(item, filter, numberDecimalcount, table,
                    item.getName());
            logTime("insertItemValue", timerStart, System.currentTimeMillis());
            return r;
        } else {
            logger.error("JDBC::getHistItemFilterQuery: TABLE is NULL, cannot get Data from not existing Table.");
        }
        return null;
    }

    /***********************
     * DATABASE CONNECTION *
     ***********************/
    protected boolean openConnection() {
        logger.debug("JDBC::openConnection isDriverAvailable: {}", conf.isDriverAvailable());
        if (conf.isDriverAvailable() && !conf.isDbConnected()) {
            logger.warn("JDBC::openConnection: setupDataSource.");
            Yank.setupDataSource(conf.getHikariConfiguration());
            conf.setDbConnected(true);
            return true;
        } else if (!conf.isDriverAvailable()) {
            logger.warn("JDBC::openConnection: no driver available!");
            initialized = false;
            return false;
        }
        return true;
    }

    protected void closeConnection() {
        logger.debug("JDBC::closeConnection");
        // Closes all open connection pools
        Yank.releaseDataSource();
        conf.setDbConnected(false);
    }

    protected boolean checkDBAcessability() {
        // Check if connection is valid
        if (initialized)
            return true;
        // first
        boolean p = pingDB();
        if (p) {
            return (p && !(conf.getErrReconnectThreshold() > 0 && errCnt <= conf.getErrReconnectThreshold()));
        } else {
            // second
            return (pingDB() && !(conf.getErrReconnectThreshold() > 0 && errCnt <= conf.getErrReconnectThreshold()));
        }
    }

    /**************************
     * DATABASE TABLEHANDLING *
     **************************/
    protected void checkDBSchema() {
        // Create Items Table if does not exist
        createItemsTableIfNot(new ItemsVO());
        if (conf.getRebuildTableNames()) {
            formatTableNames();
        } else {
            List<ItemsVO> al;
            // Reset the error counter
            errCnt = 0;
            al = getItemIDTableNames();
            for (int i = 0; i < al.size(); i++) {
                String t = getTableName(al.get(i).getItemid(), al.get(i).getItemname());
                sqlTables.put(al.get(i).getItemname(), t);
            }
        }
    }

    protected String getTable(Item item) {
        int rowId = 0;
        ItemsVO isvo;
        ItemVO ivo;

        String itemName = item.getName();
        String tableName = sqlTables.get(itemName);

        // Table already exists - return the name
        if (tableName != null) {
            return tableName;
        }

        logger.debug("JDBC::getTable: no Table found for itemName={} in sqlTables", itemName);

        // Create a new entry in items table
        isvo = new ItemsVO();
        isvo.setItemname(itemName);
        isvo = createNewEntryInItemsTable(isvo);
        rowId = isvo.getItemid();
        if (rowId == 0) {
            logger.error("JDBC::getTable: Creating table for item '" + itemName + "' failed.");
        }
        // Create the table name
        logger.debug("JDBC::getTable: getTableName with rowId={} itemName={}", rowId, itemName);
        tableName = getTableName(rowId, itemName);

        // An error occurred adding the item name into the index list!
        if (tableName == null) {
            logger.error("JDBC::getTable: tableName was null, could not create a table for item '{}'", itemName);
            return null;
        }

        // Create table for item
        String dataType = conf.getDBDAO().getDataType(item);
        ivo = new ItemVO(tableName, itemName);
        ivo.setDbType(dataType);
        ivo = createItemTable(ivo);
        logger.debug("JDBC::getTable: Table created for item '{}' with dataType {} in SQL database.", itemName,
                dataType);
        sqlTables.put(itemName, tableName);

        // Check if the new entry is in the table list
        // If it's not in the list, then there was an error and we need to do
        // some tidying up
        // The item needs to be removed from the index table to avoid duplicates
        if (sqlTables.get(itemName) == null) {
            logger.error("JDBC::getTable: Item '{}' was not added to the table - removing index", itemName);
            isvo = new ItemsVO();
            isvo.setItemname(itemName);
            deleteItemsEntry(isvo);
        }

        return tableName;
    }

    private void formatTableNames() {

        boolean tmpinit = initialized;
        if (tmpinit)
            initialized = false;

        List<ItemsVO> al;
        HashMap<Integer, String> tableIds = new HashMap<Integer, String>();

        //
        al = getItemIDTableNames();
        for (int i = 0; i < al.size(); i++) {
            String t = getTableName(al.get(i).getItemid(), al.get(i).getItemname());
            sqlTables.put(al.get(i).getItemname(), t);
            tableIds.put(al.get(i).getItemid(), t);
        }

        //
        al = getItemTables();

        String oldName = "";
        String newName = "";
        List<ItemVO> oldNewTablenames = new ArrayList<ItemVO>();
        for (int i = 0; i < al.size(); i++) {

            int id = -1;
            oldName = al.get(i).getTable_name();
            logger.warn("JDBC::formatTableNames: found Table Name= {}", oldName);

            if (oldName.startsWith(conf.getTableNamePrefix()) && !oldName.contains("_")) {
                id = Integer.parseInt(oldName.substring(conf.getTableNamePrefix().length()));
                logger.warn("JDBC::formatTableNames: found Table with Prefix '{}' Name= {} id={}",
                        conf.getTableNamePrefix(), oldName, (id));
            } else if (oldName.contains("_")) {
                id = Integer.parseInt(oldName.substring(oldName.lastIndexOf("_") + 1));
                logger.warn("JDBC::formatTableNames: found Table Name= {} id={}", oldName, (id));
            }
            logger.warn("JDBC::formatTableNames: found Table id= {}", id);

            newName = tableIds.get(id);
            logger.warn("JDBC::formatTableNames: found Table newName= {}", newName);

            if (newName != null) {
                if (!oldName.equalsIgnoreCase(newName)) {
                    oldNewTablenames.add(new ItemVO(oldName, newName));
                    logger.warn("JDBC::formatTableNames: Table '{}' will be renamed to '{}'", oldName, newName);
                } else
                    logger.warn("JDBC::formatTableNames: Table oldName='{}' newName='{}' nothing to rename", oldName,
                            newName);
            } else {
                logger.error("JDBC::formatTableNames: Table '{}' could NOT be renamed to '{}'", oldName, newName);
                break;
            }
        }

        //
        updateItemTableNames(oldNewTablenames);
        initialized = tmpinit;
    }

    private String getTableName(int rowId, String itemName) {
        return getTableNamePrefix(itemName) + formatRight(rowId, conf.getTableIdDigitCount());
    }

    private String getTableNamePrefix(String itemName) {
        String name = conf.getTableNamePrefix();
        if (conf.getTableUseRealItemNames()) {
            // Create the table name with real Item Names
            name = (itemName.replaceAll(ITEM_NAME_PATTERN, "") + "_").toLowerCase();
        }
        return name;
    }

    private static String formatRight(final Object value, final int len) {
        final String valueAsString = String.valueOf(value);
        if (valueAsString.length() < len) {
            final StringBuffer result = new StringBuffer(len);
            for (int i = len - valueAsString.length(); i > 0; i--) {
                result.append('0');
            }
            result.append(valueAsString);
            return result.toString();
        } else {
            return valueAsString;
        }
    }

    /*****************
     * H E L P E R S *
     *****************/
    private void logTime(String me, long timerStart, long timerStop) {
        if (conf.enableLogTime) {
            conf.timerCount++;
            int timerDiff = (int) (timerStop - timerStart);
            if (timerDiff < afterAccessMin) {
                afterAccessMin = timerDiff;
            }
            if (timerDiff > afterAccessMax) {
                afterAccessMax = timerDiff;
            }
            conf.timeAverage50arr.add(timerDiff);
            conf.timeAverage100arr.add(timerDiff);
            conf.timeAverage200arr.add(timerDiff);
            if (conf.timerCount == 1)
                conf.timer1000 = System.currentTimeMillis();
            if (conf.timerCount == 1001) {
                conf.time1000Statements = Math.round(((int) (System.currentTimeMillis() - conf.timer1000)) / 1000);// Seconds
                conf.timerCount = 0;
            }
            logger.info(
                    "JDBC::logTime: '{}':\n afterAccess     = {} ms\n timeAverage50  = {} ms\n timeAverage100 = {} ms\n timeAverage200 = {} ms\n afterAccessMin  = {} ms\n afterAccessMax  = {} ms\n 1000Statements = {} sec\n statementCount = {}\n",
                    me, timerDiff, conf.timeAverage50arr.getAverageInteger(),
                    conf.timeAverage100arr.getAverageInteger(), conf.timeAverage200arr.getAverageInteger(),
                    afterAccessMin, afterAccessMax, conf.time1000Statements, conf.timerCount);
        }
    }

}
