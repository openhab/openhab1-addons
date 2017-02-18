/**
 * Copyright (c) 2010-2016, openHAB.org and others.
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
 * Common base class for ip based endpoints. Endpoint differentiates different modbus slaves only by the ip address
 * (string) and port name.
 *
 */
public abstract class ModbusIPSlaveEndpoint implements ModbusSlaveEndpoint {

    private String address;
    private int port;

    private static StandardToStringStyle toStringStyle = new StandardToStringStyle();

    static {
        toStringStyle.setUseShortClassName(true);
    }

    public ModbusIPSlaveEndpoint(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        // differentiate different protocols using the class name, and after that use address and port
        int protocolHash = this.getClass().getName().hashCode();
        if (protocolHash % 2 == 0) {
            protocolHash += 1;
        }
        return new HashCodeBuilder(11, protocolHash).append(address).append(port).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, toStringStyle).append("address", address).append("port", port).toString();
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
            // different protocol -> not equal!
            return false;
        }
        ModbusIPSlaveEndpoint rhs = (ModbusIPSlaveEndpoint) obj;
        return new EqualsBuilder().append(address, rhs.address).append(port, rhs.port).isEquals();
    }
}
