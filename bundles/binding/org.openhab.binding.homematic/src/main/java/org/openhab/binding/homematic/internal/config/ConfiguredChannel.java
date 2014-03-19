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
import javax.xml.bind.annotation.XmlType;

/**
 * A configured channel. A Device has several channels.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
@XmlType(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredChannel {

    @XmlAttribute
    private String name;

    @XmlElement(name = "parameter")
    private List<ConfiguredParameter> parameter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConfiguredParameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<ConfiguredParameter> parameter) {
        this.parameter = parameter;
    }

}
