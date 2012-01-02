/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.snmp;

import org.openhab.core.binding.BindingProvider;
import org.snmp4j.smi.OID;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and SNMP items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public interface SnmpBindingProvider extends BindingProvider {

	/**
	 * Returns the configured OID prefix for the given <code>itemName</code>. If
	 * no OID has been configured an OID with the empty String '' is returned 
	 * instead.
	 * 
	 * @param itemName the Item to find an OID for
	 * @return the configured OID
	 */
	OID getOID(String itemName);
	
	/**
	 * Returns all items which are mapped to a SNMP-binding
	 * @return items which are mapped to a SNMP-binding
	 */
	Iterable<String> getItemNames();
	
}
