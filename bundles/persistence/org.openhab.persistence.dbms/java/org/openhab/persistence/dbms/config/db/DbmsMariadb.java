/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extended Database Configuration class and its inner-classes. Class represents
 * the extended database-specific configuration. Overrides and supplements the
 * default settings from DbmsBaseDB. Enter only the differences DbmsBaseDB here.
 * Overrides all
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsMariadb extends DbmsBaseDB {
	private static final Logger logger = LoggerFactory.getLogger(DbmsMariadb.class);
	protected int maxActiveConnections = 3;
	protected int maxIdleConnections = 2;

	public DbmsMariadb() {
		super();
		initEtendedSqlTypes();
		setDriverClassName("com.mysql.jdbc.Driver");
	}

	public void initEtendedSqlTypes() {
		logger.debug("DBMS::initEtendedSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
	}
}
