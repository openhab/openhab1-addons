/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.rfxcom;

import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and RFXCOM items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public interface RFXComBindingProvider extends BindingProvider {

	/**
	 * Returns the id to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a id.
	 * 
	 * @return the corresponding id to the given <code>itemName</code>.
	 */
	public String getId(String itemName);

	/**
	 * Returns the value selector to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a unit code.
	 * 
	 * @return the corresponding value selector to the given
	 *         <code>itemName</code>.
	 */
	public RFXComValueSelector getValueSelector(String itemName);

	/**
	 * Returns item direction to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a binding mode.
	 * 
	 * @return true if item is in binding.
	 */
	public boolean isInBinding(String itemName);

	/**
	 * Returns item packet type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a packet type.
	 * 
	 * @return the corresponding packet type to the given <code>itemName</code>.
	 */
	public PacketType getPacketType(String itemName);

	/**
	 * Returns item sub type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a sub type.
	 * 
	 * @return the corresponding sub type to the given <code>itemName</code>.
	 */
	public Object getSubType(String itemName);

}
