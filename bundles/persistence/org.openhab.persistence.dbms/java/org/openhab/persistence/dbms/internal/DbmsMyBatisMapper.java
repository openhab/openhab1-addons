/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.internal;

import java.util.List;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.persistence.dbms.config.DbmsConfiguration;
import org.openhab.persistence.dbms.config.DbmsConnectionFactory;
import org.openhab.persistence.dbms.config.db.DbmsBaseDB;
import org.openhab.persistence.dbms.config.db.DbmsBaseDB.ItemResultHandler;
import org.openhab.persistence.dbms.model.ItemVO;
import org.openhab.persistence.dbms.model.ItemsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface between OpenHab-service, configuration and database(Mybatis/JDBC).
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsMyBatisMapper {
	static final Logger logger = LoggerFactory.getLogger(DbmsMyBatisMapper.class);

	long afterCloseMin = 10000;
	long afterCloseMax = 0;
	long count = 0;
	protected DbmsConfiguration conf = null;

	public DbmsMyBatisMapper() {
		super();
	}

	public int pingDB() throws Exception {
		logger.debug("DBMS::pingDB");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		int i = dao.doPingDB();
		commitAndClose(session);
		logTime("pingDB", timerStart, System.currentTimeMillis());
		return i;
	}

	/**
	 * ITEMS
	 */
	public ItemsVO createNewEntryInItemsTable(ItemsVO i) throws Exception {
		logger.debug("DBMS::createNewEntryInItemsTable");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		dao.doCreateNewEntryInItemsTable(i);
		commitAndClose(session);
		logTime("createNewEntryInItemsTable", timerStart, System.currentTimeMillis());
		return i;
	}

	public ItemsVO createItemsTableIfNot(ItemsVO i) {
		logger.debug("DBMS::createItemsTableIfNot");
		long timerStart = System.currentTimeMillis();
		SqlSession session;
		try {
			session = DbmsConnectionFactory.getSession().openSession();
			DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
			dao.doCreateItemsTableIfNot(i);
			commitAndClose(session);
		} catch (ClassNotFoundException e) {
			logger.error("DBMS::createItemsTableIfNot ClassNotFoundException e={}", e.getMessage());
		} catch (Exception e) {
			logger.warn("DBMS::createItemsTableIfNot Table {} already exists.", i.getTable_name());
		}
		logTime("createItemsTableIfNot", timerStart, System.currentTimeMillis());
		return i;
	}

	public ItemsVO deleteItemsEntry(ItemsVO i) throws Exception {
		logger.debug("DBMS::deleteItemsEntry");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		dao.doDeleteItemsEntry(i);
		commitAndClose(session);
		logTime("deleteItemsEntry", timerStart, System.currentTimeMillis());
		return i;
	}

	public List<ItemsVO> getItemIDTableNames() throws Exception {
		logger.debug("DBMS::getItemIDTableNames");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		List<ItemsVO> r = dao.doGetItemIDTableNames(new ItemsVO());
		commitAndClose(session);
		logTime("getItemIDTableNames", timerStart, System.currentTimeMillis());
		return r;
	}

	public List<ItemsVO> getItemTables() throws Exception {
		logger.debug("DBMS::getItemTables");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		List<ItemsVO> r = dao.doGetItemTables();
		commitAndClose(session);
		logTime("getItemTables", timerStart, System.currentTimeMillis());
		return r;
	}

	/**
	 * ITEM
	 */
	public void updateItemTableNames(List<ItemVO> i) throws Exception {
		logger.debug("DBMS::createItemTable");
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		dao.doUpdateItemTableNames(i);
		commitAndClose(session);
		logTime("createItemTable", timerStart, System.currentTimeMillis());
		// return i;
	}

	public ItemVO createItemTable(ItemVO i) {
		logger.debug("DBMS::createItemTable");
		long timerStart = System.currentTimeMillis();
		SqlSession session;
		try {
			session = DbmsConnectionFactory.getSession().openSession();
			DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
			dao.doCreateItemTable(i);
			commitAndClose(session);
		} catch (ClassNotFoundException e) {
			logger.error("DBMS::createItemsTableIfNot ClassNotFoundException e={}", e.getMessage());
		} catch (Exception e) {
			logger.debug("DBMS::createItemTable Table {} already exists? Exception={}", i.getNewTableName(),
					e.getMessage());
		}
		logTime("createItemTable", timerStart, System.currentTimeMillis());
		return i;
	}

	public ItemVO insertItemValue(ItemVO i) throws Exception {
		logger.debug("DBMS::insertItemValue, getDbType={}, getJavaType={}, getJdbcType={}, getValue={}", i.getDbType(),
				i.getJavaType(), i.getJdbcType(), i.getValue().toString());
		long timerStart = System.currentTimeMillis();
		SqlSession session = DbmsConnectionFactory.getSession().openSession();
		DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
		dao.doInsertItemValue(i);
		commitAndClose(session);
		logTime("insertItemValue", timerStart, System.currentTimeMillis());
		return i;
	}

	/**
	 * Works with a ResultHandler
	 * 
	 * @param filter
	 * @param numberDecimalcount
	 * @param table
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public List<HistoricItem> getHistItemFilterQuery(FilterCriteria filter, int numberDecimalcount, String table,
			Item item) throws Exception {
		logger.debug("DBMS::getHistItemFilterQuery");
		if (table != null) {
			long timerStart = System.currentTimeMillis();
			SqlSession session = DbmsConnectionFactory.getSession().openSession();
			ItemResultHandler rh = new DbmsBaseDB().new ItemResultHandler(item);
			DbmsBaseDB.BaseInnerDAO dao = useInnerMapperClass(session);
			dao.doGetHistItemFilterQuery(rh, filter, numberDecimalcount, table, item.getName());
			List<HistoricItem> r = rh.getItems();
			commitAndClose(session);
			logTime("getHistItemFilterQuery", timerStart, System.currentTimeMillis());
			return r;
		} else {
			throw new Exception("TABLE is NULL, cannot get Data from not existing Table.");
		}
	}

	/**
	 * DB-close
	 */
	private void commitAndClose(SqlSession session) {
		session.commit();
		session.close();
	}

	/**
	 * HELPERS
	 */
	private DbmsBaseDB.BaseInnerDAO useInnerMapperClass(SqlSession session) {

		Configuration c = session.getConfiguration();

		// loading default database Configuratuion
		if (!c.hasMapper(DbmsBaseDB.BaseInnerDAO.class)) {
			c.addMapper(DbmsBaseDB.BaseInnerDAO.class);
			logger.debug("DBMS: useMapperClass added Mapper BaseInnerDAO");
		}

		// loading specified database Configuratuion
		Class<?> mc = conf.getdBDAOClass();
		logger.debug("DBMS: useInnerMapperClass mc.getName={}, mc.getSimpleName={}", mc.getName(), mc.getSimpleName());
		if (!c.hasMapper(mc)) {
			c.addMapper(mc);
			logger.debug("DBMS: useMapperClass added Mapper Dynamic getInnerDBMapperClass=", mc);
		}

		return (DbmsBaseDB.BaseInnerDAO) session.getMapper(mc);
	}

	private void logTime(String me, long timerStart, long timerStop) {
		if (conf.enableLogTime) {
			conf.timerCount++;
			int timerDiff = (int) (timerStop - timerStart);
			if (timerDiff < afterCloseMin) {
				afterCloseMin = timerDiff;
			}
			if (timerDiff > afterCloseMax) {
				afterCloseMax = timerDiff;
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
					"DBMS::commitAndClose '{}':\n afterClose     = {} ms\n timeAverage50  = {} ms\n timeAverage100 = {} ms\n timeAverage200 = {} ms\n afterCloseMin  = {} ms\n afterCloseMax  = {} ms\n 1000Statements = {} sec\n statementCount = {}\n",
					me, timerDiff, conf.timeAverage50arr.getAverageInteger(),
					conf.timeAverage100arr.getAverageInteger(), conf.timeAverage200arr.getAverageInteger(),
					afterCloseMin, afterCloseMax, conf.time1000Statements, conf.timerCount);
		}
	}
}