/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
