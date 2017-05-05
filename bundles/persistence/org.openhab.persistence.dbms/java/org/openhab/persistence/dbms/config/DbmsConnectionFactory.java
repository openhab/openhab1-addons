/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config;

import java.time.LocalDateTime;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database connection class.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsConnectionFactory {
	private static final Logger logger = LoggerFactory.getLogger(DbmsConnectionFactory.class);

	public static void setConf(String driver, String jdbcUrl, String user, String password, int ac, int ic) {
		logger.debug("DBMS::setConf driver={}", driver);
		DbmsConnectionFactory.driver = driver;
		DbmsConnectionFactory.jdbcUrl = jdbcUrl;
		DbmsConnectionFactory.user = user;
		DbmsConnectionFactory.password = password;
		DbmsConnectionFactory.maxActiveConnections = ac;
		DbmsConnectionFactory.maxIdleConnections = ic;

		sqlMapper = new SqlSessionFactoryBuilder().build(_init());
	}

	private static String driver;
	private static String jdbcUrl;
	private static String user;
	private static String password;
	private static int maxActiveConnections;
	private static int maxIdleConnections;

	private static SqlSessionFactory sqlMapper;

	LocalDateTime timePoint = LocalDateTime.now();

	private static Configuration _init() {
		Configuration config = new Configuration();
		PooledDataSource s = new org.apache.ibatis.datasource.pooled.PooledDataSource(driver, jdbcUrl, user, password);
		s.setPoolMaximumActiveConnections(maxActiveConnections);
		s.setPoolMaximumIdleConnections(maxIdleConnections);
		config.setEnvironment(new Environment("development", new JdbcTransactionFactory(), s));
		sqlMapper = new SqlSessionFactoryBuilder().build(config);
		return config;
	}

	static {
		sqlMapper = new SqlSessionFactoryBuilder().build(_init());
	}

	public static SqlSessionFactory getSession() throws ClassNotFoundException {
		return sqlMapper;
	}
}