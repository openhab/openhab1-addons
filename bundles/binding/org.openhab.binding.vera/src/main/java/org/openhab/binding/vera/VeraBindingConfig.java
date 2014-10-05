/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera;

import java.util.Map;

import org.fourthline.cling.model.types.ServiceId;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;

/**
 * Represents a single binding config provided by the {@link VeraBindingConfigProvider}.
 * 
 * Each config represents one {@link Item} to Vera device/service/variable mapping.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraBindingConfig implements BindingConfig {
	
	/**
	 * The {@link Item} type.
	 */
	private Class<? extends Item> itemType;
	
	/**
	 * The {@link Item} name.
	 */
	private String itemName;
	
	/**
	 * The uniId of the {@link VeraUnit} defined in <code>openhab.cfg</code>.
	 */
	private String unitId;
	
	/**
	 * The Vera device id.
	 */
	private int deviceId;
	
	/**
	 * Optional binding arguments.
	 */
	private Map<String, String> arguments;
	
	/**
	 * The {@link ServiceId} discovered during parsing.
	 */
	private ServiceId serviceId;
	
	/**
	 * The {@link VeraBindingVariable} discovered during parsing.
	 */
	private VeraBindingVariable variable;
	
	/**
	 * Creates a new binding config. All arguments are required.
	 * 
	 * @param itemType the item type
	 * @param itemName the item name
	 * @param unitId the vera unit id
	 * @param deviceId the vera device id
	 * @param arguments optional binding arguments
	 * @param serviceId the service id discovered during parsing
	 * @param variable the binding variable discovered during parsing
	 */
	public VeraBindingConfig(Class<? extends Item> itemType, String itemName, 
			String unitId, int deviceId, Map<String, String> arguments, 
			ServiceId serviceId, VeraBindingVariable variable) {
		this.itemType = itemType;
		this.itemName = itemName;
		this.unitId = unitId;
		this.deviceId = deviceId;
		this.arguments = arguments;
		this.serviceId = serviceId;
		this.variable = variable;
	}

	/**
	 * Gets the action argument name.
	 * @return the action argument name
	 */
	public String getActionArgumentName() {
		return variable.getActionArgumentName();
	}

	/**
	 * Gets the action name.
	 * @return the action name
	 */
	public String getActionName() {
		return variable.getActionName();
	}
	
	/**
	 * Gets the binding arguments.
	 * @return the binding arguments
	 */
	public Map<String, String> getArguments() {
		return arguments;
	}
	
	/**
	 * Gets the <code>deviceId</code>.
	 * @return the <code>deviceId</code>
	 */
	public int getDeviceId() {
		return deviceId;
	}
	
	/**
	 * Gets the item name.
	 * @return the item name
	 */
	public String getItemName() {
		return itemName;
	}
	
	/**
	 * Gets the {@link ServiceId}.
	 * @return the {@link ServiceId}
	 */
	public ServiceId getServiceId() {
		return serviceId;
	}

	/**
	 * Gets the {@link State} type.
	 * @return the {@link State} type
	 */
	public Class<? extends State> getStateType() {
		return variable.getStateType();
	}
	
	/**
	 * Gets the state variable name.
	 * @return the state variable name
	 */
	public String getStateVariableName() {
		return variable.getStateVariableName();
	}
	
	/**
	 * Gets the <code>unitId</code>.
	 * @return the <code>unitId</code>
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * Gets the upnp type.
	 * @return the upnp type
	 */
	public Class<?> getUpnpType() {
		return variable.getUpnpType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("{ type = %s, name = %s, unit = %s, device = %d, args = %s, service = %s, variable = %s }", 
				itemType.getSimpleName(), itemName, unitId, deviceId, arguments, serviceId, variable);
	}
	
}
