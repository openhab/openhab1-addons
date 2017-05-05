/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.dbms.config.db;

import org.apache.ibatis.annotations.Insert;
import org.openhab.persistence.dbms.model.ItemVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default Database Configuration class and its inner-classes. Class represents
 * the entire database configuration, it is none specific, but like MySql.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class DbmsH2 extends DbmsBaseDB {
	private static final Logger logger = LoggerFactory.getLogger(DbmsH2.class);

	public DbmsH2() {
		super();
		initEtendedSqlTypes();
		setDriverClassName("org.h2.Driver");
	}

	public void initEtendedSqlTypes() {
		logger.debug("DBMS::initEtendedSqlTypes: Initialize the type array sqlTypes={}", sqlTypes.values());
	}

	@Override
	public int checkMaxConnections(int i) {
		return 1;
	}

	public interface DbDAO extends BaseInnerDAO {
		
		String SQL_INSERT_ITEM_VALUE                  = "INSERT INTO ${tableName} (TIME, VALUE) VALUES( NOW(), CAST( #{value, javaType=${javaType}, jdbcType=${jdbcType}} as ${dbType}) )";

		@Insert(SQL_INSERT_ITEM_VALUE)
		public void doInsertItemValue(ItemVO vo) throws Exception;
	}
}
