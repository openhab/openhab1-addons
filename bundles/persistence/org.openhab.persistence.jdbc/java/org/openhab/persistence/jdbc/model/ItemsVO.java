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

/**
 * Represents the table naming data.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class ItemsVO implements Serializable {

    private static final long serialVersionUID = 2871961811177601520L;

    private static final String STR_FILTER = "[^a-zA-Z0-9]";

    private String coltype = "VARCHAR(500)";
    private String colname = "itemname";
    private String itemsManageTable = "items";
    private int itemid;
    private String itemname;
    private String table_name;
    private String jdbcUriDatabaseName;

    public String getColtype() {
        return coltype;
    }

    public void setColtype(String coltype) {
        this.coltype = coltype.replaceAll(STR_FILTER, "");
    }

    public String getColname() {
        return colname;
    }

    public void setColname(String colname) {
        this.colname = colname.replaceAll(STR_FILTER, "");
    }

    public String getItemsManageTable() {
        return itemsManageTable;
    }

    public void setItemsManageTable(String itemsManageTable) {
        this.itemsManageTable = itemsManageTable.replaceAll(STR_FILTER, "");
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getJdbcUriDatabaseName() {
        return jdbcUriDatabaseName;
    }

    public void setJdbcUriDatabaseName(String jdbcUriDatabaseName) {
        this.jdbcUriDatabaseName = jdbcUriDatabaseName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((itemname == null) ? 0 : itemname.hashCode());
        result = prime * result + (int) (itemid ^ (itemid >>> 32));
        return result;
    }

    /*
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
        ItemsVO other = (ItemsVO) obj;
        if (itemname == null) {
            if (other.itemname != null)
                return false;
        } else if (!itemname.equals(other.itemname))
            return false;
        if (itemid != other.itemid)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemsVO [coltype=");
        builder.append(coltype);
        builder.append(", colname=");
        builder.append(colname);
        builder.append(", itemsManageTable=");
        builder.append(itemsManageTable);
        builder.append(", itemid=");
        builder.append(itemid);
        builder.append(", itemname=");
        builder.append(itemname);
        builder.append(", table_name=");
        builder.append(table_name);
        builder.append("]");
        return builder.toString();
    }

}
