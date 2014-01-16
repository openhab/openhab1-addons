/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave;

import java.util.Date;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;

/**
 * Binding Configuration class. Represents a binding configuration in
 * the items file to a Z-Wave node.
 * @author Victor Belov 
 * @author Brian Crosby
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveBindingConfig implements BindingConfig {
	/**
	 * Constructor. Creates a new instance of the ZWaveBindingConfig class.
	 * @param nodeId the NodeId the item is bound to
	 * @param endpoint the end point in a multi channel node the item is bound to
	 * @param arguments the arguments for the binding as a {@link HashMap} of key-value pairs
	 */
	public ZWaveBindingConfig(int nodeId, int endpoint, Integer refreshInterval, Map<String, String> arguments) {
		this.nodeId = nodeId;
		this.endpoint = endpoint;
		this.refreshInterval = refreshInterval;
		this.arguments = arguments;
	}

	private final int nodeId;
	private final int	endpoint;
	private final Map<String, String> arguments;
	private Integer refreshInterval;
	private Date lastRefreshed;

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
	 * Returns the arguments entered in the binding string.
	 * @return a map of arguments
	 */
	public Map<String,String> getArguments() {
		return arguments;
	}
	
	/**
	 * Returns the interval at which the item should be refreshed.
	 * 0 (zero) indicates that the item should not be refreshed.
	 * null indicates that the default should be used.
	 * @return the refreshInterval
	 */
	public Integer getRefreshInterval() {
		return refreshInterval;
	}
	
	/**
	 * Sets the interval at which the item should be refreshed.
	 * 0 (zero) indicates that the item should not be refreshed.
	 * null indicates that the default should be used.
	 * @param refreshInterval the refreshInterval to set
	 */
	public void setRefreshInterval(Integer refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	/**
	 * Returns the date when the item was last refreshed.
	 * @return the lastRefreshed
	 */
	public Date getLastRefreshed() {
		return lastRefreshed;
	}

	/**
	 * Sets the date when the item was last refreshed.
	 * @param lastRefreshed the lastRefreshed to set
	 */
	public void setLastRefreshed(Date lastRefreshed) {
		this.lastRefreshed = lastRefreshed;
	}	
}
