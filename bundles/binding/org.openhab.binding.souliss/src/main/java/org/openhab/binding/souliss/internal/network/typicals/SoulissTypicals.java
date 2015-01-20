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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class push and pop typicals from two hash tables with different keys: IP Address + VNET Address + slot AND item
 * @author Antonino-Fazio
 */
public class SoulissTypicals {

private Map<String, SoulissGenericTypical> hashTableAddressToTypicals = Collections.synchronizedMap(new Hashtable<String, SoulissGenericTypical>());
private Map<String, String> hashTableItemToAddress =Collections.synchronizedMap(new Hashtable<String, String>());
private static Logger LOGGER = LoggerFactory.getLogger(SoulissTypicals.class);

/**
 * Add type SoulissGenericTypical in two hash tables, first one use ITEM as index, the other IP+NodeID+slot 
 * @param sItem
 * @param typical
 */
	public void addTypical(String sItem, SoulissGenericTypical typical){
		synchronized (typical) {
			int iBit=0;
			if(typical.getType()==Constants.Souliss_T1A){
				LOGGER.info("Add Item: " +  sItem + " - Typ: " + Integer.toHexString(typical.getType()) + ", Node: "+ typical.getSoulissNodeID() + ", Slot: " + typical.getSlot()+ ", Bit: " + ((SoulissT1A)typical).getBit());
				iBit=((SoulissT1A)typical).getBit();
			} else {
				LOGGER.info("Add Item: " +  sItem + " - Typ: " + Integer.toHexString(typical.getType()) + ", Node: "+ typical.getSoulissNodeID() + ", Slot: " + typical.getSlot());
			}
			typical.setName(sItem);
			// Index is :  IP Address + VNET Address + slot
			LOGGER.info("hashTableItemToAddress <-- [key: " + sItem + " - value: " + String.valueOf(typical.getSoulissNodeID()) + String.valueOf(typical.getSlot()) +iBit +"]");
			hashTableItemToAddress.put(sItem, String.valueOf(typical.getSoulissNodeID()) + String.valueOf(typical.getSlot())+ iBit);
			// Index is :  item
			LOGGER.info("hashTableAddressToTypicals <-- [key: " + typical.getSoulissNodeID() + String.valueOf(typical.getSlot()) + iBit + " - value: " + typical + "]");
			hashTableAddressToTypicals.put(String.valueOf(typical.getSoulissNodeID()) + String.valueOf(typical.getSlot()) + iBit, typical);	
		}
		
	}
	/**
	 * Delete the hash tables
	 */
	public void clear(){
		LOGGER.debug("Clear hashtable");
		hashTableAddressToTypicals.clear();
		hashTableItemToAddress.clear();
	}
	
	/**
	 * Add a souliss' typical in the hash table using IP+Nodo+Slot as index
	 * @param sSoulissNodeIPAddress
	 * @param getSoulissNodeID
	 * @param iSlot
	 * @return
	 */
	public SoulissGenericTypical getTypicalFromAddress(int getSoulissNodeID, int iSlot, Integer iBit){
		if (iBit==null) iBit=0;
		return hashTableAddressToTypicals.get(String.valueOf(getSoulissNodeID) + String.valueOf(iSlot)+iBit);
	}
	
	
	/**
	 * Add a souliss' typical in the hash table using ITEM as index
	 * @param sItem
	 * @return
	 */
	public SoulissGenericTypical getTypicalFromItem(String sItem){
		String sKey=hashTableItemToAddress.get(sItem);
		if(sKey==null) return null;
		return hashTableAddressToTypicals.get(sKey);
	}
/**
 * Returns Iterator
 */
	public Iterator<Entry<String, SoulissGenericTypical>> getIterator(){
			return hashTableAddressToTypicals.entrySet().iterator();	
	}

/**
 * Returns the number of nodes parsing the hash table
 * @return integer
 */
	public int getNodeNumbers() {
		SoulissGenericTypical typ;
		int iTmp=0;
		Iterator<Entry<String, SoulissGenericTypical>> iteratorTypicals=getIterator();
		synchronized (iteratorTypicals) {
			while (iteratorTypicals.hasNext()){
				typ = iteratorTypicals.next().getValue();
			if(typ.getSoulissNodeID()>iTmp) iTmp=typ.getSoulissNodeID();
			}	
		}
		
		return iTmp+1;
	}

}
