/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
