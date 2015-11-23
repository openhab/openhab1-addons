/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.persistence.jdbc.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ############################ JDBC Persistence Service ##################################
 * # I N S T A L L   J D B C   P E R S I S T E N C E   S E R V I C E 
 * # To use this JDBC-service-bundle (org.openhab.persistence.jdbc-X.X.X.jar),
 * # a appropriate JDBC database-driver is needed in OpenHab addons Folder.
 * # Copy both (JDBC-service-bundle and a JDBC database-driver) to your OpenHab '[OpenHab]/addons' Folder to make it work. 
 * #
 * # Driver jars:
 * # Derby:     derby-10.11.1.1.jar               http://mvnrepository.com/artifact/org.apache.derby/derby
 * # H2:        h2-1.4.189.jar                    http://mvnrepository.com/artifact/com.h2database/h2
 * # HSQLDB:    hsqldb-2.3.3.jar                  http://mvnrepository.com/artifact/org.hsqldb/hsqldb
 * # MariaDB:   mariadb-java-client-1.2.0.jar     http://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
 * # MySQL      mysql-connector-java-5.1.36.jar   http://mvnrepository.com/artifact/mysql/mysql-connector-java
 * # PostgreSQL:postgresql-9.4-1201-jdbc41.jar    http://mvnrepository.com/artifact/org.postgresql/postgresql
 * # SQLite:    sqlite-jdbc-3.8.11.1.jar          http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
 * #
 * # Tested databases/url-prefix: jdbc:derby, jdbc:h2, jdbc:hsqldb, jdbc:mariadb, jdbc:mysql, jdbc:postgresql, jdbc:sqlite
 * # 
 * # derby, h2, hsqldb, sqlite can be embedded, 
 * # If no database is available, for Example the url 'jdbc:h2:./testH2' creates a new DB in OpenHab Folder. 
 * #
 * # Create new database, for example on a MySQL-Server use: 
 * # CREATE DATABASE 'yourDB' CHARACTER SET utf8 COLLATE utf8_general_ci;
 * 
 * # D A T A B A S E  C O N F I G
 * # Some URL-Examples, 'service' identifies and activates internally the correct jdbc driver.
 * # required database url like 'jdbc:<service>:<host>[:<port>;<attributes>]'
 * # jdbc:url=jdbc:derby:./testDerby;create=true
 * # jdbc:url=jdbc:h2:./testH2
 * # jdbc:url=jdbc:hsqldb:./testHsqlDb
 * # jdbc:url=jdbc:mariadb://192.168.0.1:3306/testMariadb
 * # jdbc:url=jdbc:mysql://192.168.0.1:3306/testMysql
 * # jdbc:url=jdbc:postgresql://192.168.0.1:5432/testPostgresql
 * # jdbc:url=jdbc:sqlite:./testSqlite.db
 * 
 * # required database user
 * #jdbc:user=
 * jdbc:user=test
 * 
 * # required database password
 * #jdbc:password=
 * jdbc:password=test
 * 
 * # E R R O R   H A N D L I N G
 * # optional when Service is deactivated (optional, default: 0 -> ignore) 
 * #jdbc:errReconnectThreshold=
 * 
 * # I T E M   O P E R A T I O N S
 * # optional tweaking SQL datatypes
 * # see: https://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/type/JdbcType.html	
 * # see: http://www.h2database.com/html/datatypes.html
 * # see: http://www.postgresql.org/docs/9.3/static/datatype.html
 * # defaults:
 * #jdbc:sqltype.CALL           =	VARCHAR(200)
 * #jdbc:sqltype.COLOR          =	VARCHAR(70)
 * #jdbc:sqltype.CONTACT        =	VARCHAR(6)
 * #jdbc:sqltype.DATETIME       =	DATETIME
 * #jdbc:sqltype.DIMMER         =	TINYINT
 * #jdbc:sqltype.LOCATION       =	VARCHAR(30)
 * #jdbc:sqltype.NUMBER         =	DOUBLE
 * #jdbc:sqltype.ROLLERSHUTTER  =	TINYINT
 * #jdbc:sqltype.STRING         =	VARCHAR(65500)
 * #jdbc:sqltype.SWITCH         =	VARCHAR(6)
 * 
 * # For Itemtype "Number" default decimal digit count (optional, default: 3) 
 * #jdbc:numberDecimalcount=
 * 
 * # T A B L E   O P E R A T I O N S
 * # Tablename Prefix String (optional, default: "item") 
 * # for Migration from MYSQL-Bundle set to 'Item'.
 * #jdbc:tableNamePrefix=Item
 * 
 * # Tablename Prefix generation, using Item real names or "item" (optional, default: false -> "item") 
 * # If set to true, 'tableNamePrefix' is ignored.
 * #jdbc:tableUseRealItemNames=
 * jdbc:tableUseRealItemNames=true
 * 
 * # Tablename Suffix length (optional, default: 4 -> 0001-9999) 
 * # For Migration from MYSQL-Bundle set to 0.
 * #jdbc:tableIdDigitCount=
 * 
 * # Rename existing Tables using tableUseRealItemNames and tableIdDigitCount (optional, default: false) 
 * # USE WITH CARE! Deactivate after Renaming is done!
 * #jdbc:rebuildTableNames=true
 * 
 * # D A T A B A S E  C O N N E C T I O N S
 * # Some embeded Databases can handle only one Connection (optional, default: configured per database in packet org.openhab.persistence.jdbc.db. * )
 * # see: https://github.com/brettwooldridge/HikariCP/issues/256
 * # jdbc.maximumPoolSize = 1
 * # jdbc.minimumIdle = 1
 * 
 * # T I M E K E E P I N G
 * # (optional, default: false) 
 * #jdbc:enableLogTime=true
 * #######################################################################################
 * 
 * 
 * DEVELOPER INFO TO ADD A NEW DATABASE:
 * 1. Duplicate org.openhab.persistence.jdbc.config.db.JdbcMysql.java with database name Jdbc[database].java
 * 2. Minimum you have to configure: setDriverClassName("JDBC DRIVERNAME STRING")
 * 3. Setup OpenHab *.cfg File jdbc:user=, jdbc:password=, jdbc:url=jdbc:[database]:
 * 4. Run and Test
 * 5. For adjustments, use default communication-code from JdbcBaseDAO and copy parts to your new Jdbc[databaseName]DAO class an customize it.
 * 6. Run and Test
 * 
 *
 * @author Helmut Lehmeyer
 * @since 1.8.0
 * 
 * 
 */
public class JdbcPersistenceService extends JdbcMapper implements QueryablePersistenceService {
	static final Logger logger = LoggerFactory.getLogger(JdbcPersistenceService.class);

	protected ItemRegistry itemRegistry;

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(BundleContext bundleContext, Map<Object, Object> configuration) {
		logger.debug("JDBC::activate: persistence service activated");
		this.bundleContext = bundleContext;
		updateConfig(configuration);
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		logger.debug("JDBC::deactivate:  persistence bundle stopping. Disconnecting from database. reason={}", reason);
		//closeConnection();
		this.bundleContext = null;
		initialized = false;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("JDBC::setItemRegistry");
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("JDBC::unsetItemRegistry");
		//closeConnection();
		this.itemRegistry = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public String getName() {
		logger.debug("JDBC::getName: returning name 'jdbc' for queryable persistence service.");
		return "jdbc";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void store(Item item) {
		store(item, null);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void store(Item item, String alias) {
		// Don not store undefined/uninitialised data
		if (item.getState() instanceof UnDefType) {
			logger.warn("JDBC::store: ignore Item because it is UnDefType");
			return;
		}
		if (!checkDBAcessability()) {
			logger.warn(
					"JDBC::store:  No connection to database. Can not persist item '{}'! Will retry connecting to database when error count:{} equals errReconnectThreshold:{}",
					item, errCnt, conf.getErrReconnectThreshold());
			return;
		}
		long timerStart = System.currentTimeMillis();
		storeItemValue(item);
		logger.debug("JDBC: Stored item '{}' as '{}' in SQL database at {} in {}ms.", item.getName(),
				item.getState().toString(), (new java.util.Date()).toString(),
				System.currentTimeMillis() - timerStart);
	}

	/**
	 * Queries the {@link PersistenceService} for data with a given filter
	 * criteria
	 * 
	 * @param filter
	 *            the filter to apply to the query
	 * @return a time series of items
	 */
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {

		if (!checkDBAcessability()) {
			logger.warn("JDBC::query: db not connected, query aborted for item '{}'", filter.getItemName());
			return Collections.emptyList();
		}
		if (itemRegistry == null) {
			logger.error("JDBC::query: itemRegistry == null Ignore and Give up!");
			return Collections.emptyList();
		}

		// Get the item name from the filter
		// Also get the Item object so we can determine the type
		Item item = null;
		String itemName = filter.getItemName();
		logger.debug("JDBC::query: item is {}", itemName);
		try {
			item = itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e1) {
			logger.error("JDBC::query: unable to get item for itemName: '{}'. Ignore and Give up!", itemName);
			return Collections.emptyList();
		}

		if (item instanceof GroupItem) {
			// For Group Item is BaseItem needed to get correct Type of Value.
			item = GroupItem.class.cast(item).getBaseItem();
			logger.debug("JDBC::query: item is instanceof GroupItem '{}'", itemName);
			if (item == null){
				logger.debug("JDBC::query: BaseItem of GroupItem is null. Ignore and Give up!");
				return Collections.emptyList();
			}
			if (item instanceof GroupItem){
				logger.debug("JDBC::query: BaseItem of GroupItem is a GroupItem too. Ignore and Give up!");
				return Collections.emptyList();
			}
		}

		String table = sqlTables.get(itemName);
		if (table == null) {
			logger.warn("JDBC::query: unable to find table for query, no Data in Database for Item '{}'", itemName);
			// if enabled, table will be created immediately
			if (item != null) {
				logger.warn("JDBC::query: try to generate the table for item '{}'", itemName);
				table = getTable(item);
			} else {
				logger.warn("JDBC::query: no way to generate the table for item '{}'", itemName);
				return Collections.emptyList();
			}
		}

		long timerStart = System.currentTimeMillis();
		List<HistoricItem> items = new ArrayList<HistoricItem>();
		items = getHistItemFilterQuery(filter, conf.getNumberDecimalcount(), table, item);

		logger.debug("JDBC::query: query for {} returned {} rows in {}ms", item.getName(), items.size(),
				System.currentTimeMillis() - timerStart);
		
		// Success
		errCnt = 0;
		return items;
	}

	/**
	 * @{inheritDoc
	 */
	public void updateConfig(Map<Object, Object> configuration) {
		logger.debug("JDBC::updateConfig");

		conf = new JdbcConfiguration(configuration);
		if(checkDBAcessability()){
			checkDBSchema();
			// connection has been established ... initialization completed!
			initialized = true;
		}else{
			initialized = false;
		}

		logger.debug("JDBC::updateConfig: configuration complete for service={}.", getName());
	}
}
