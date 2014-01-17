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
 * A configured parameter. Part of the xml based configured devices.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */
@XmlType(name = "parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredParameter {

    @XmlAttribute
    private String name;

    @XmlElement(name = "converter")
    private List<ConfiguredConverter> converter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConfiguredConverter> getConverter() {
        return converter;
    }

    public void setConverter(List<ConfiguredConverter> converter) {
        this.converter = converter;
    }

}
