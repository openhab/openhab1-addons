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
package org.openhab.binding.zwave;

import org.openhab.core.binding.BindingConfig;

/**
 * Binding Configuration class. Represents a binding configuration in the items file 
 * to a Z-Wave node.
 * 
 * @author Victor Belov 
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveBindingConfig implements BindingConfig {
	
	private int nodeId;
	private int	endpoint;
	
	private ZWaveBindingAction action;

	/**
	 * Constructor. Creates a new instance of the ZWaveBindingConfig class.
	 * @param nodeId the NodeId the item is bound to
	 * @param endpoint the end point in a multi channel node the item is bound to
	 * @param action the action that is run to populate the item value
	 */
	public ZWaveBindingConfig(int nodeId, int endpoint, ZWaveBindingAction action) {
		this.nodeId = nodeId;
		this.action = action;
		this.endpoint = endpoint;
	}
	
	/**
	 * Returns NodeId of bound node.
	 * @return the NodeId the item is bound to.
	 */
	public int getNodeId() {
		return nodeId;
	}
	
	/**
	 * Returns Multi channel Endpoint of a bound node.
	 * @return endpoint the end point in a multi channel node the item is bound to.
	 */
	public int getEndpoint() {
		return endpoint;
	}

	/**
	 * Returns the action that is run on the node by the binding.
	 * @return the action to run.
	 */
	public ZWaveBindingAction getAction() {
		return action;
	}
	
}
