/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jdbc.internal;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.StringUtils;
import org.openhab.persistence.jdbc.db.JdbcBaseDAO;
import org.openhab.persistence.jdbc.utils.MovingAverage;
import org.openhab.persistence.jdbc.utils.StringUtilsExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class JdbcConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(JdbcConfiguration.class);

    private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");
    private static final String DB_DAO_PACKAGE = "org.openhab.persistence.jdbc.db.Jdbc";

    private JdbcBaseDAO dBDAO = null;
    private String dbName = null;
    boolean dbConnected = false;
    boolean driverAvailable = false;

    private String serviceName;
    private String name = "jdbc";

    // private String url;
    // private String user;
    // private String password;
    private int numberDecimalcount = 3;
    private boolean tableUseRealItemNames = false;
    private String tableNamePrefix = "item";
    private int tableIdDigitCount = 4;
    private boolean rebuildTableNames = false;

    private int errReconnectThreshold = 0;

    public int timerCount = 0;
    public int time1000Statements = 0;
    public long timer1000 = 0;
    public MovingAverage timeAverage50arr = new MovingAverage(50);
    public MovingAverage timeAverage100arr = new MovingAverage(100);
    public MovingAverage timeAverage200arr = new MovingAverage(200);
    public boolean enableLogTime = false;

    public JdbcConfiguration(Map<Object, Object> configuration) {
        logger.debug("JDBC::JdbcConfiguration");
        updateConfig(configuration);
    }

    /**
     * @{inheritDoc
     */
    public void updateConfig(Map<Object, Object> configuration) {
        logger.debug("JDBC::updateConfig: configuration.size = " + configuration.size());

        // Database-Url jdbc:h2:./testH2
        String url = (String) configuration.get("url");
        Properties parsedURL = StringUtilsExt.parseJdbcURL(url);
        logger.debug("JDBC::updateConfig: url={}", url);
        if (StringUtils.isBlank(url) || parsedURL.getProperty("parseValid") == "false") {
            logger.error(
                    "JDBC::updateConfig: url The SQL database URL is missing - please configure the jdbc:url parameter in openhab.cfg");
        } else {
            dbName = parsedURL.getProperty("databaseName");
        }

        // Which DB-Type to use
        serviceName = parsedURL.getProperty("dbShortcut"); // derby, h2, hsqldb, mariadb, mysql, postgresql, sqlite
        logger.debug("JDBC::updateConfig: found serviceName = '{}'", serviceName);
        if (StringUtils.isBlank(serviceName) || serviceName.length() < 2) {
            serviceName = "no";
            logger.error(
                    "JDBC::updateConfig: url Required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]' - please configure the jdbc:url parameter in openhab.cfg");
        }

        // DB Class
        String ddp = DB_DAO_PACKAGE + serviceName.toUpperCase().charAt(0) + serviceName.toLowerCase().substring(1)
                + "DAO";

        logger.debug("JDBC::updateConfig: Init Data Access Object Class: '{}'", ddp);
        try {

            dBDAO = (JdbcBaseDAO) Class.forName(ddp).newInstance();
            // dBDAO.databaseProps.setProperty("jdbcUrl", url);
            // dBDAO.databaseProps.setProperty("dataSource.url", url);

            logger.debug("JDBC::updateConfig: dBDAO ClassName={}", dBDAO.getClass().getName());
        } catch (InstantiationException e) {
            logger.error("JDBC::updateConfig: InstantiationException: {}", e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("JDBC::updateConfig: IllegalAccessException: {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warn("JDBC::updateConfig: no Configuration for serviceName '{}' found. ClassNotFoundException: {}",
                    serviceName, e.getMessage());
            logger.debug("JDBC::updateConfig: using default Database Configuration: JdbcBaseDAO !!");
            dBDAO = new JdbcBaseDAO();
            logger.debug("JDBC::updateConfig: dBConfig done");
        }

        @SuppressWarnings("unchecked")
        Enumeration<String> keys = new IteratorEnumeration(configuration.keySet().iterator());

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

            Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

            if (!matcher.matches()) {
                continue;
            }

            matcher.reset();
            matcher.find();

            if (!matcher.group(1).equals("sqltype"))
                continue;

            String itemType = matcher.group(2).toUpperCase() + "ITEM";
            String value = (String) configuration.get(key);

            logger.debug("JDBC::updateConfig: set sqlTypes: itemType={} value={}", itemType, value);
            dBDAO.sqlTypes.put(itemType, value);
        }

        String user = (String) configuration.get("user");
        logger.debug("JDBC::updateConfig:  user={}", user);
        if (StringUtils.isBlank(user)) {
            logger.error(
                    "JDBC::updateConfig: SQL user is missing - please configure the jdbc:user parameter in openhab.cfg");
        } else {
            dBDAO.databaseProps.setProperty("dataSource.user", user);
        }

        String password = (String) configuration.get("password");
        if (StringUtils.isBlank(password)) {
            logger.error("JDBC::updateConfig: SQL password is missing. Attempting to connect without password. "
                    + "To specify a password configure the jdbc:password parameter in openhab.cfg.");
        } else {
            logger.debug("JDBC::updateConfig:  password=<masked> password.length={}", password.length());
            dBDAO.databaseProps.setProperty("dataSource.password", password);
        }

        String et = (String) configuration.get("reconnectCnt");
        if (StringUtils.isNotBlank(et)) {
            errReconnectThreshold = Integer.parseInt(et);
            logger.debug("JDBC::updateConfig: errReconnectThreshold={}", errReconnectThreshold);
        }

        String np = (String) configuration.get("tableNamePrefix");
        if (StringUtils.isNotBlank(np)) {
            dBDAO.databaseProps.setProperty("tableNamePrefix", np);
        }

        String dd = (String) configuration.get("numberDecimalcount");
        if (StringUtils.isNotBlank(dd)) {
            numberDecimalcount = Integer.parseInt(dd);
            logger.debug("JDBC::updateConfig: numberDecimalcount={}", numberDecimalcount);
        }

        String rn = (String) configuration.get("tableUseRealItemNames");
        if (StringUtils.isNotBlank(rn)) {
            tableUseRealItemNames = "true".equals(rn) ? Boolean.parseBoolean(rn) : false;
            logger.debug("JDBC::updateConfig: tableUseRealItemNames={}", tableUseRealItemNames);
        }

        String td = (String) configuration.get("tableIdDigitCount");
        if (StringUtils.isNotBlank(td)) {
            tableIdDigitCount = Integer.parseInt(td);
            logger.debug("JDBC::updateConfig: tableIdDigitCount={}", tableIdDigitCount);
        }

        String rt = (String) configuration.get("rebuildTableNames");
        if (StringUtils.isNotBlank(rt)) {
            rebuildTableNames = "true".equals(rt) ? Boolean.parseBoolean(rt) : false;
            logger.debug("JDBC::updateConfig: rebuildTableNames={}", rebuildTableNames);
        }
        // undocumented
        String ac = (String) configuration.get("maximumPoolSize");
        if (StringUtils.isNotBlank(ac)) {
            dBDAO.databaseProps.setProperty("maximumPoolSize", ac);
        }
        // undocumented
        String ic = (String) configuration.get("minimumIdle");
        if (StringUtils.isNotBlank(ic)) {
            dBDAO.databaseProps.setProperty("minimumIdle", ic);
        }
        // undocumented
        String it = (String) configuration.get("idleTimeout");
        if (StringUtils.isNotBlank(it)) {
            dBDAO.databaseProps.setProperty("idleTimeout", it);
        }
        // undocumented
        String ent = (String) configuration.get("enableLogTime");
        if (StringUtils.isNotBlank(ent)) {
            enableLogTime = "true".equals(ent) ? Boolean.parseBoolean(ent) : false;
        }
        logger.debug("JDBC::updateConfig: enableLogTime {}", enableLogTime);

        // undocumented
        String fd = (String) configuration.get("driverClassName");
        if (StringUtils.isNotBlank(fd)) {
            dBDAO.databaseProps.setProperty("driverClassName", fd);
        }
        // undocumented
        String ds = (String) configuration.get("dataSourceClassName");
        if (StringUtils.isNotBlank(ds)) {
            dBDAO.databaseProps.setProperty("dataSourceClassName", ds);
        }

        driverAvailable = true;
        String dn = dBDAO.databaseProps.getProperty("driverClassName");
        if (dn == null) {
            dn = dBDAO.databaseProps.getProperty("dataSourceClassName");
        } else {
            dBDAO.databaseProps.setProperty("jdbcUrl", url);
        }

        logger.warn("JDBC::updateConfig: try to load JDBC-driverClass: '{}'", dn);
        try {
            Class.forName(dn);
            logger.debug("JDBC::updateConfig: load JDBC-driverClass was successful: '{}'", dn);
        } catch (ClassNotFoundException e) {
            driverAvailable = false;
            logger.error(
                    "JDBC::updateConfig: could NOT load JDBC-driverClassName or JDBC-dataSourceClassName ClassNotFoundException: '{}'",
                    e.getMessage());
            String warn = ""
                    + "\n\n\t!!!\n\tTo avoid this error, place a appropriate JDBC driver file for serviceName '{}' in addons directory.\n"
                    + "\tCopy missing JDBC-Driver-jar to your OpenHab/addons Folder.\n\t!!!\n" + "\tDOWNLOAD: \n";
            if (serviceName.equals("derby"))
                warn += "\tDerby:     version >= 10.11.1.1 from          http://mvnrepository.com/artifact/org.apache.derby/derby\n";
            else if (serviceName.equals("h2"))
                warn += "\tH2:        version >= 1.4.189 from            http://mvnrepository.com/artifact/com.h2database/h2\n";
            else if (serviceName.equals("hsqldb"))
                warn += "\tHSQLDB:    version >= 2.3.3 from              http://mvnrepository.com/artifact/org.hsqldb/hsqldb\n";
            else if (serviceName.equals("mariadb"))
                warn += "\tMariaDB:   version >= 1.2.0 from              http://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client\n";
            else if (serviceName.equals("mysql"))
                warn += "\tMySQL:     version >= 5.1.36 from             http://mvnrepository.com/artifact/mysql/mysql-connector-java\n";
            else if (serviceName.equals("postgresql"))
                warn += "\tPostgreSQL:version >= 9.4-1201-jdbc41 from    http://mvnrepository.com/artifact/org.postgresql/postgresql\n";
            else if (serviceName.equals("sqlite"))
                warn += "\tSQLite:    version >= 3.8.11.1 from           http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc\n";
            logger.warn(warn, serviceName);
        }

        logger.debug("JDBC::updateConfig: configuration complete. service={}", getName());
    }

    public Properties getHikariConfiguration() {
        return dBDAO.databaseProps;
    }

    public String getName() {
        // return serviceName;
        return name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public int getErrReconnectThreshold() {
        return errReconnectThreshold;
    }

    public boolean getRebuildTableNames() {
        return rebuildTableNames;
    }

    public int getNumberDecimalcount() {
        return numberDecimalcount;
    }

    public boolean getTableUseRealItemNames() {
        return tableUseRealItemNames;
    }

    public int getTableIdDigitCount() {
        return tableIdDigitCount;
    }

    public JdbcBaseDAO getDBDAO() {
        return dBDAO;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isDbConnected() {
        return dbConnected;
    }

    public void setDbConnected(boolean dbConnected) {
        logger.debug("JDBC::setDbConnected {}", dbConnected);
        this.dbConnected = dbConnected;
    }

    public boolean isDriverAvailable() {
        return driverAvailable;
    }

}