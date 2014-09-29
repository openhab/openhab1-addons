/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
