/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.plex.internal.communication;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of {@link MediaContainer}. This object contains information about a Plex device.
 *
 * @author Jeroen Idserda
 * @since 1.8.0
 */
@XmlRootElement(name = "Server")
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String productVersion;

    @XmlAttribute
    private String provides;

    @XmlAttribute
    private boolean httpsRequired;

    @XmlElement(name = "Connection")
    private List<Connection> connections = new ArrayList<Connection>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getProvides() {
        return provides;
    }

    public void setProvides(String provides) {
        this.provides = provides;
    }

    public boolean isHttpsRequired() {
        return httpsRequired;
    }

    public void setHttpsRequired(boolean httpsRequired) {
        this.httpsRequired = httpsRequired;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

}
