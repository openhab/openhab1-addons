/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vdr;

import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and VDR items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public interface VDRBindingProvider extends BindingProvider {

	/**
	 * Returns a <code>List</code> of matching VDR ids (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching VDR ids or <code>null</code> if no matching VDR id
	 *         could be found.
	 */
	public List<String> getVDRId(String itemName);

	/**
	 * Returns a <code>List</code> of VDR commands (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find VDR commands
	 * 
	 * @return a List of matching VDR commands or <code>null</code> if no matching VDR
	 *         command could be found.
	 */
	public List<String> getVDRCommand(String itemName);

	/**
	 * Returns associated item to <code>vdrId</code> and <code>vdrCommand</code> 
	 * 
	 * @param vdrId the id of the vdr for which items should be returned
	 * @param vdrCommand the vdr command for which items should be returned
	 * 
	 * @return the name of the item which is associated to <code>vdrId</code>
	 * and <code>vdrComannd</code>
	 */
	public String getBindingItemName(String vdrId, VDRCommandType vdrCommand);

}
