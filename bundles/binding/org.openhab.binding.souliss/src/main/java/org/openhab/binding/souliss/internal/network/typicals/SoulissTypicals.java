/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class push and pop typicals from two hash tables with different keys: IP
 * Address + VNET Address + slot AND item
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissTypicals {

	private ConcurrentHashMap <String, SoulissGenericTypical> hashTableAddressToTypicals = new ConcurrentHashMap <String, SoulissGenericTypical>();
	private ConcurrentHashMap <String, String> hashTableItemToAddress = new ConcurrentHashMap <String, String>();

	private static Logger logger = LoggerFactory
			.getLogger(SoulissTypicals.class);

	/**
	 * Add type SoulissGenericTypical in two hash tables, first one use ITEM as
	 * index, the other IP+NodeID+slot
	 * 
	 * @param sItem
	 * @param typical
	 */
	public void addTypical(String sItem, SoulissGenericTypical typical) {
			int iBit = 0;
			String sUseOfSlot = "";
			if (typical.getType() == Constants.Souliss_T1A) {
				logger.info("Add Item: {} - Typ: {}, Node:{}, Slot: {}, Bit: {}", sItem , Integer.toHexString(typical.getType()), typical.getSoulissNodeID(), typical.getSlot(),((SoulissT1A) typical).getBit());  
				
				iBit = ((SoulissT1A) typical).getBit();
			} else {
				logger.info("Add Item: {} - Typ: {}, Node: {}, Slot: {}",sItem, Integer.toHexString(typical.getType()),typical.getSoulissNodeID(), typical.getSlot()); 
			}
			typical.setName(sItem);
			// Index is : node + slot + iBit + sUseOfSlot
			logger.info("hashTableItemToAddress <-- [key: {} - value: {} - Slot {}]", sItem, String.valueOf(typical.getSoulissNodeID()),String.valueOf(typical.getSlot())); 
			hashTableItemToAddress.put(
					sItem,
					String.valueOf(typical.getSoulissNodeID())
							+ String.valueOf(typical.getSlot()) + iBit + sUseOfSlot);
			// Index is : item
			logger.info("hashTableAddressToTypicals <-- [key: {} - value: {}]",typical.getSoulissNodeID()+ String.valueOf(typical.getSlot()) + iBit, typical); 
	
			hashTableAddressToTypicals
					.put(String.valueOf(typical.getSoulissNodeID())
							+ String.valueOf(typical.getSlot()) + iBit + sUseOfSlot, typical);
}

	/**
	 * Delete the hash tables
	 */
	public void clear() {
		logger.debug("Clear hashtable");
		hashTableAddressToTypicals.clear();
		hashTableItemToAddress.clear();
	}

	/**
	 * Add a souliss' typical in the hash table using NODE + SLOT + IBIT as index
	 * 
	 * @param sSoulissNodeIPAddress
	 * @param getSoulissNodeID
	 * @param iSlot
	 * @return
	 */
	public SoulissGenericTypical getTypicalFromAddress(int getSoulissNodeID,
			int iSlot, Integer iBit) {
		if (iBit == null)
			iBit = 0;
		return hashTableAddressToTypicals.get(String.valueOf(getSoulissNodeID)
				+ String.valueOf(iSlot) + iBit);
	}
	
	/**
	 * Add a souliss' typical in the hash table using ITEM as index
	 * 
	 * @param String
	 *            sItem
	 * @return SoulissGenericTypical
	 */
	public SoulissGenericTypical getTypicalFromItem(String sItem) {
		String sKey = hashTableItemToAddress.get(sItem);
		if (sKey == null)
			return null;
		return hashTableAddressToTypicals.get(sKey);
	}

	/**
	 * Returns Iterator
	 */
	public Iterator<Entry<String, SoulissGenericTypical>> getIterator() {
		return hashTableAddressToTypicals.entrySet().iterator();
	}

	/**
	 * Returns the number of nodes parsing the hash table
	 * 
	 * @return integer
	 */
	public int getNodeNumbers() {
		SoulissGenericTypical typ;
		int iTmp = 0;
		Iterator<Entry<String, SoulissGenericTypical>> iteratorTypicals = getIterator();
			while (iteratorTypicals.hasNext()) {
				typ = iteratorTypicals.next().getValue();
				if (typ.getSoulissNodeID() > iTmp)
					iTmp = typ.getSoulissNodeID();
			}
		return iTmp + 1;
	}

}
