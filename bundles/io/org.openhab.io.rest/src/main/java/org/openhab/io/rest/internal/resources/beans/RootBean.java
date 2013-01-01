/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
