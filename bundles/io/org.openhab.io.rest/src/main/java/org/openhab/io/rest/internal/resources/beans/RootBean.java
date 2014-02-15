/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.resources.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * This is a java bean that is used with JAXB to define the root entry
 * page of the REST interface.
 *  
 * @author Kai Kreuzer
 * @since 0.8.0
 *
 */
@XmlRootElement(name="openhab")
@XmlAccessorType(XmlAccessType.NONE)
public class RootBean {

	final public Map<String, String> links = new HashMap<String, String>();
    
    @XmlElement(name = "link")
    public MapEntry[] getMap() {
        List<MapEntry> list = new ArrayList<MapEntry>();
        for (Entry<String, String> entry : links.entrySet()) {
            MapEntry mapEntry =new MapEntry();
            mapEntry.type = entry.getKey();
            mapEntry.value = entry.getValue();
            list.add(mapEntry);
        }
        return list.toArray(new MapEntry[list.size()]);
    }
 
    public static class MapEntry {
        @XmlAttribute
        public String type;
        @XmlValue
        public String value;
    }
}
