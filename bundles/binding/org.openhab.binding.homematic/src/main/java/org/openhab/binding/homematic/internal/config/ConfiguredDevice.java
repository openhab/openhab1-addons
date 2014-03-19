/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A configured device. Will be parsed from xml files.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */

@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredDevice {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    @XmlElement(name = "channel")
    private List<ConfiguredChannel> channels;

    public List<ConfiguredChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<ConfiguredChannel> channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
