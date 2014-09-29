/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.exec;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Exec items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Pauli Anttila
 * @since 0.6.0
 */
public interface ExecBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the commandLine to execute according to <code>itemName</code> and
	 * <code>command</code>. If there is no direct match a second attempt with
	 * command <code>*</code> is triggered.
	 * 
	 * 
	 * @param itemName
	 *            the item for which to find a commandLine
	 * @param command
	 *            the openHAB command for which to find a commandLine
	 * 
	 * @return the matching commandLine or <code>null</code> if no matching
	 *         commandLine could be found.
	 */
	String getCommandLine(String itemName, Command command);

	/**
	 * Returns the commandLine to execute according to <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a commandLine
	 * 
	 * @return the matching commandLine or <code>null</code> if no matching
	 *         commandLine could be found.
	 */
	String getCommandLine(String itemName);

	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by In-Binding.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 *         refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);

	/**
	 * Returns the transformation rule to use according to <code>itemName</code>
	 * . Is used by In-Binding.
	 * 
	 * @param itemName
	 *            the item for which to find a transformation rule
	 * 
	 * @return the matching transformation rule or <code>null</code> if no
	 *         matching transformation rule could be found.
	 */
	String getTransformation(String itemName);

	/**
	 * Returns all items which are mapped to a Exec-In-Binding
	 * 
	 * @return item which are mapped to a Exec-In-Binding
	 */
	List<String> getInBindingItemNames();

}
