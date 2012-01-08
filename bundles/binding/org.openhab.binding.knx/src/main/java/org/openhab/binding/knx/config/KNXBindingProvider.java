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
package org.openhab.binding.knx.config;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * This interface is implemented by classes that can provide mapping information between
 * openHAB items and KNX items (datapoints). An openHAB item can be associated to multiple KNX datapoints,
 * e.g. a rollerblind can have a boolean datapoint to say up or down and a numeric datapoint to address a 
 * specific position (30 % closed).
 * 
 * Implementing classes should register themselves as a service in order to be taken into account.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public interface KNXBindingProvider extends BindingProvider {

	/**
	 * This method returns the datapoint for an item, which corresponds to the given group address.
	 * 
	 * @param itemName the item name for which the datapoint is requested
	 * @param groupAddress a group address that is assigned to the datapoint in question
	 * @return the datapoint for the item, which corresponds to the given group address
	 */
	public Iterable<Datapoint> getDatapoints(String itemName, GroupAddress groupAddress);

	/**
	 * This method returns the datapoint for an item, which corresponds to the given type class.
	 * 
	 * @param itemName the item name for which the datapoint is requested
	 * @param typeClass the typeClass (e.g. OnOffType.class), which is mapped to the datapoint in question
	 * @return the datapoint for the item, which is mapped to the given type class
	 */
	public Iterable<Datapoint> getDatapoints(String itemName, Class<? extends Type> typeClass);
	
	/**
	 * This method determines, what openHAB items listen to a given group address.
	 * 
	 * @param groupAddress the group address that the items listen to
	 * @return all item names that listen to the given group address
	 */
	public Iterable<String> getListeningItemNames(GroupAddress groupAddress);
	
	/**
	 * This method returns all datapoints, which accept a read request on the KNX bus,
	 * i.e. their current status can be requested (which is not necessarily always possible
	 * in KNX).
	 * 
	 * @return all datapoints which accept a read request
	 */
	public Iterable<Datapoint> getReadableDatapoints();
	
	/**
	 * Checks whether the given <code>groupAddress</code> is to be interpreted as
	 * CommandGA or not. Returns <code>true</code> if <code>groupAddress</code>
	 * is the first GA in the KNX configuration binding for a datapoint type and 
	 * <code>false</code> in all other cases.
	 * 
	 * @param groupAddress the group address to check
	 * 
	 * @return <code>true</code> if <code>groupAddress</code> is the first GA 
	 * in the KNX configuration binding for a datapoint type and <code>false</code>
	 * in all other cases.
	 */
	public boolean isCommandGA(final GroupAddress groupAddress);


}
