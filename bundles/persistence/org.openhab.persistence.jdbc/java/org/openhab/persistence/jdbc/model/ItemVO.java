/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jdbc.model;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the Item-data on the part of MyBatis/database.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class ItemVO implements Serializable {
    static final Logger logger = LoggerFactory.getLogger(ItemVO.class);

    private static final long serialVersionUID = 1871441039821454890L;

    private String tableName;
    private String newTableName;
    private String dbType;
    private String jdbcType;
    private String itemType;
    private Class<?> javaType;
    private Date time;
    private Object value;

    public ItemVO(String tableName, String newTableName) {
        logger.debug("JDBC:ItemVO tableName={}; newTableName={}; ", tableName, newTableName);
        this.tableName = tableName;
        this.newTableName = newTableName;
    }

    public ItemVO() {
    }

    public void setValueTypes(String dbType, Class<?> javaType) {
        logger.debug("JDBC:ItemVO setValueTypes dbType={}; javaType={};", dbType, javaType);
        this.dbType = dbType;
        this.javaType = javaType;
        // this.jdbcType = jdbcType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNewTableName() {
        return newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getJavaType() {
        return javaType.getName();
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemVO other = (ItemVO) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (time != other.time)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemVO [tableName=");
        builder.append(tableName);
        builder.append(", newTableName=");
        builder.append(newTableName);
        builder.append(", dbType=");
        builder.append(dbType);
        builder.append(", javaType=");
        builder.append(javaType);
        builder.append(", time=");
        builder.append(time);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
}
