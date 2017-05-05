/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config;

import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.StringUtils;
import org.openhab.persistence.dbms.config.db.DbmsBaseDB;
import org.openhab.persistence.dbms.utils.MovingAverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding Configuration class. Represents a binding configuration in the items
 * file to a Z-Wave node.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(DbmsConfiguration.class);

	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.([0-9.a-zA-Z]+)$");
	private static final String CONFIG_DB_PACKAGE = "org.openhab.persistence.dbms.config.db.Dbms";

	private DbmsBaseDB dBConfig = null;
	private Class<?> dBDAOClass = null;

	private String serviceName;
	private String name = "dbms";

	private String url;
	private String user;
	private String password;
	private int numberDecimalcount = 3;
	private boolean tableUseRealItemNames = false;
	private String tableNamePrefix = "item";
	private int tableIdDigitCount = 4;
	private boolean rebuildTableNames = false;

	private int maxActiveConnections;// Defaults are in DbmsBaseDB
	private int maxIdleConnections;// Defaults are in DbmsBaseDB

	private int errReconnectThreshold = 0;

	public int timerCount = 0;
	public int time1000Statements = 0;
	public long timer1000 = 0;
	public MovingAverage timeAverage50arr = new MovingAverage(50);
	public MovingAverage timeAverage100arr = new MovingAverage(100);
	public MovingAverage timeAverage200arr = new MovingAverage(200);
	public boolean enableLogTime = true;

	public DbmsConfiguration(Map<Object, Object> configuration) {
		logger.debug("DBMS::DbmsConfiguration");
		updateConfig(configuration);
	}

	/**
	 * @{inheritDoc
	 */
	public void updateConfig(Map<Object, Object> configuration) {
		logger.debug("DBMS::updateConfig called. configuration.size = " + configuration.size());

		// Database-Url jdbc:h2:./testH2
		url = (String) configuration.get("url");
		logger.debug("DBMS:  url={}", url);
		if (StringUtils.isBlank(url)) {
			logger.error(
					"DBMS: updateConfig:url The SQL database URL is missing - please configure the dbms:url parameter in openhab.cfg");
		}

		// Which DB-Type to use
		serviceName = url.split(":")[1]; // derby, h2, hsqldb, mariadb, mysql,
											// postgresql, sqlite
		logger.debug("DBMS:  found serviceName = " + serviceName);
		if (StringUtils.isBlank(serviceName)) {
			logger.error(
					"DBMS: updateConfig:url Required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]' - please configure the dbms:url parameter in openhab.cfg");
		}

		// DB Class
		String dbtc = CONFIG_DB_PACKAGE + serviceName.toUpperCase().charAt(0) + serviceName.toLowerCase().substring(1);
		logger.debug("DBMS: Init Class for {} = {}  ", serviceName, dbtc);
		try {
			dBConfig = (DbmsBaseDB) Class.forName(dbtc).newInstance();
			dBDAOClass = Class.forName(dbtc + "$DbDAO");
			logger.debug("DBMS::dBConfig done dBDAOClass={}", dBDAOClass.getName());
		} catch (InstantiationException e) {
			logger.error("DBMS: InstantiationException: {}", e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("DBMS: IllegalAccessException: {}", e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.warn("DBMS: No Configuration for serviceName '{}' found. ClassNotFoundException: {}", serviceName,
					e.getMessage());
			logger.debug("DBMS::!! Using default Database Configuration: DbmsBaseDB !!");
			dBConfig = new DbmsBaseDB();
			dBDAOClass = DbmsBaseDB.DbDAO.class;
			logger.debug("DBMS::dBConfig done");
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

			logger.debug("DBMS: set sqlTypes: itemType={} value={}", itemType, value);
			dBConfig.sqlTypes.put(itemType, value);
		}

		user = (String) configuration.get("user");
		logger.debug("DBMS:  user={}", user);
		if (StringUtils.isBlank(user)) {
			logger.error(
					"DBMS: updateConfig:dbms:user The SQL user is missing - please configure the dbms:user parameter in openhab.cfg");
		}

		password = (String) configuration.get("password");
		logger.debug("DBMS:  password={}{}...", password.subSequence(0, 3), password.length() - 3);
		if (StringUtils.isBlank(password)) {
			logger.error(
					"DBMS: updateConfig:password The SQL password is missing. Attempting to connect without password. To specify a password configure the dbms:password parameter in openhab.cfg.");
		}

		String et = (String) configuration.get("reconnectCnt");
		if (StringUtils.isNotBlank(et)) {
			errReconnectThreshold = Integer.parseInt(et);
			logger.debug("DBMS:  errReconnectThreshold={}", errReconnectThreshold);
		}

		String dd = (String) configuration.get("numberDecimalcount");
		if (StringUtils.isNotBlank(dd)) {
			numberDecimalcount = Integer.parseInt(dd);
			logger.debug("DBMS:  numberDecimalcount={}", numberDecimalcount);
		}

		String rn = (String) configuration.get("tableUseRealItemNames");
		if (StringUtils.isNotBlank(rn)) {
			tableUseRealItemNames = "true".equals(rn) ? Boolean.parseBoolean(rn) : false;
			logger.debug("DBMS:  tableUseRealItemNames={}", tableUseRealItemNames);
		}

		String td = (String) configuration.get("tableIdDigitCount");
		if (StringUtils.isNotBlank(td)) {
			tableIdDigitCount = Integer.parseInt(td);
			logger.debug("DBMS:  tableIdDigitCount={}", tableIdDigitCount);
		}

		String rt = (String) configuration.get("rebuildTableNames");
		if (StringUtils.isNotBlank(rt)) {
			rebuildTableNames = "true".equals(rt) ? Boolean.parseBoolean(rt) : false;
			logger.debug("DBMS:  rebuildTableNames={}", rebuildTableNames);
		}

		maxActiveConnections = dBConfig.getMaxActiveConnections();
		logger.debug("DBMS:updateConfig. maxActiveConnections {}", maxActiveConnections);
		String ac = (String) configuration.get("maxActiveConnections");
		if (StringUtils.isNotBlank(ac)) {
			maxActiveConnections = dBConfig.checkMaxConnections(Integer.parseInt(ac));
		}

		maxIdleConnections = dBConfig.getMaxIdleConnections();
		String ic = (String) configuration.get("maxIdleConnections");
		if (StringUtils.isNotBlank(ic)) {
			maxIdleConnections = dBConfig.checkMaxConnections(Integer.parseInt(ic));
		}

		// undocumented
		String ent = (String) configuration.get("enableLogTime");
		if (StringUtils.isNotBlank(rt)) {
			enableLogTime = "true".equals(ent) ? Boolean.parseBoolean(ent) : false;
		}

		// undocumented
		String fd = (String) configuration.get("forceJdbcDriver");
		if (StringUtils.isNotBlank(fd)) {
			dBConfig.setDriverClassName(fd);
		}

		logger.debug("DBMS:updateConfig. Try to load JDBC-driver for {}", dBConfig.getDriverClassName());
		//
		try {
			Class.forName(dBConfig.getDriverClassName());
			logger.debug("DBMS:updateConfig. Could load JDBC-driver for {}", dBConfig.getDriverClassName());
		} catch (ClassNotFoundException e) {
			logger.error("DBMS:updateConfig. Tried to load JDBC-driver for {}, but got ClassNotFoundException: {}",
					dBConfig.getDriverClassName(), e);
			logger.warn(
					"\n\n\t!!!\n\tTo avoid this error, place a appropriate JDBC driver file in addons directory.\n"
				  + "\tCopy missing org.openhab.persistence.dbms.jdbc.{}-X.X.X.jar to your OpenHab/addons Folder.\n\t!!!\n",
					serviceName);
		}

		logger.debug("DBMS:updateConfig DbmsConnectionFactory.setConf");
		DbmsConnectionFactory.setConf(dBConfig.getDriverClassName(), url, user, password, maxActiveConnections,
				maxIdleConnections);

		logger.debug("DBMS:updateConfig configuration complete. service={}", getName());
	}

	public String getName() {
		// return serviceName;
		return name;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
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

	public DbmsBaseDB getdBConfig() {
		return dBConfig;
	}

	public Class<?> getdBDAOClass() {
		return dBDAOClass;
	}

	public String getSqlType(String itemType) {
		return dBConfig.getSqlTypes().get(itemType);
	}

}