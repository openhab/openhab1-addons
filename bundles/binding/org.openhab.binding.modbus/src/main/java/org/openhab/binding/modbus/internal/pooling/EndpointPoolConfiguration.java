/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal.pooling;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class representing pooling related configuration of a single endpoint
 *
 * This class implements equals hashcode constract, and thus is suitable for use as keys in HashMaps, for example.
 *
 */
public class EndpointPoolConfiguration {

    /**
     * How long should be the minimum duration between passivate and
     * consecutive borrow of connected connection
     * In milliseconds.
     */
    private long passivateBorrowMinMillis;

    /**
     * How long should be the minimum duration between connection-establishments from the pool. In milliseconds.
     */
    private long interConnectDelayMillis;

    /**
     * How many times we want to try connecting to the endpoint before giving up. One means that connection
     * establishment is tried once.
     */
    private int connectMaxTries = 1;

    /**
     * Re-connect connection every X milliseconds. Negative means that connection is not disconnected automatically.
     * One can use 0ms to denote reconnection after every transaction (default).
     */
    private int reconnectAfterMillis;

    /**
     * How long before we give up establishing the connection. In milliseconds. Default of 0 means that system/OS
     * default is respected.
     */
    private int connectTimeoutMillis;

    private static StandardToStringStyle toStringStyle = new StandardToStringStyle();

    static {
        toStringStyle.setUseShortClassName(true);
    }

    public long getInterConnectDelayMillis() {
        return interConnectDelayMillis;
    }

    public void setInterConnectDelayMillis(long interConnectDelayMillis) {
        this.interConnectDelayMillis = interConnectDelayMillis;
    }

    public int getConnectMaxTries() {
        return connectMaxTries;
    }

    public void setConnectMaxTries(int connectMaxTries) {
        this.connectMaxTries = connectMaxTries;
    }

    public int getReconnectAfterMillis() {
        return reconnectAfterMillis;
    }

    public void setReconnectAfterMillis(int reconnectAfterMillis) {
        this.reconnectAfterMillis = reconnectAfterMillis;
    }

    public long getPassivateBorrowMinMillis() {
        return passivateBorrowMinMillis;
    }

    public void setPassivateBorrowMinMillis(long passivateBorrowMinMillis) {
        this.passivateBorrowMinMillis = passivateBorrowMinMillis;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(2149, 3117).append(passivateBorrowMinMillis).append(interConnectDelayMillis)
                .append(connectMaxTries).append(reconnectAfterMillis).append(connectTimeoutMillis).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, toStringStyle).append("passivateBorrowMinMillis", passivateBorrowMinMillis)
                .append("interConnectDelayMillis", interConnectDelayMillis).append("connectMaxTries", connectMaxTries)
                .append("reconnectAfterMillis", reconnectAfterMillis)
                .append("connectTimeoutMillis", connectTimeoutMillis).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        EndpointPoolConfiguration rhs = (EndpointPoolConfiguration) obj;
        return new EqualsBuilder().append(passivateBorrowMinMillis, rhs.passivateBorrowMinMillis)
                .append(interConnectDelayMillis, rhs.interConnectDelayMillis)
                .append(connectMaxTries, rhs.connectMaxTries).append(reconnectAfterMillis, rhs.reconnectAfterMillis)
                .append(connectTimeoutMillis, rhs.connectTimeoutMillis).isEquals();
    }

}
