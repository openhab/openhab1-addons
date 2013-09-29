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
package org.openhab.binding.snmp;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and SNMP items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson - modified binding to support polling SNMP OIDs (SNMP GET) and setting values (SNMP SET).
 * @since 0.9.0
 */
public interface SnmpBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Returns the configured OID prefix for the given <code>itemName</code>. If
	 * no OID has been configured an OID with the empty String '' is returned 
	 * instead.
	 * 
	 * @param itemName the Item to find an OID for
	 * @return the configured OID
	 */
	OID getOID(String itemName);
	OID getOID(String itemName, Command command);
	
	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by HTTP-In-Binding.
	 *  
	 * @param itemName the item for which to find a refresh interval
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 * refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);
	
	/**
	 * Returns all items which are mapped to a SNMP-In-Binding
	 * @return item which are mapped to a SNMP-In-Binding
	 */
	List<String> getInBindingItemNames();

	/**
	 * Returns the IP address of the SNMP binding
	 * @return address for SNMP binding
	 */
	Address getAddress(String itemName);
	Address getAddress(String itemName, Command command);

	/**
	 * Returns the SNMP community of the SNMP binding
	 * @return community for SNMP binding
	 */
	OctetString getCommunity(String itemName);
	OctetString getCommunity(String itemName, Command command);

	/**
	 * Returns the SNMP value for the command
	 * @return value for the command
	 */
	Integer32 getValue(String itemName, Command command);

	/**
	 * Performs a data transformation, if a transformation service was specified in the item config 
	 * @return transformed value
	 * @throws TransformationException 
	 */
	String doTransformation(String itemName, String value) throws TransformationException;
}
