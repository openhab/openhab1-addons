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
package org.openhab.binding.homematic.internal.xmlrpc.impl;

import java.util.Map;

import org.openhab.binding.homematic.internal.xmlrpc.AbstractXmlRpcObject;

/**
 * Paramset represent attribute names and their values as defined by a device.
 * They are used to access information on the device and to interact with the
 * device (e.g. turn switch on and off). The values can the accessed directly
 * through the getValue and setValue methods provided by the XML-RPC interface.
 * 
 * The values defined by a Paramset vary greatly from device to device, so no
 * better getter methods can be provided. Paramset are only supposed to be used
 * by their respective devices and such devices should know the exact names of
 * the attributes they want to access.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class Paramset extends AbstractXmlRpcObject {

    public Paramset(Map<String, Object> values) {
        super(values);
    }

    @Override
    public String toString() {
        return "Paramset [getValues()=" + getValues() + "]";
    }

}
