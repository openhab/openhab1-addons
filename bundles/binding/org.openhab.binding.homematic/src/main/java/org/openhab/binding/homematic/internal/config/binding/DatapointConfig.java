/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config.binding;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.homematic.internal.config.BindingAction;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.binding.homematic.internal.model.HmInterface;

/**
 * Class with the datapoint bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class DatapointConfig extends ValueBindingConfig {
	private HmInterface hmInterface;
	private String address;
	private String channel;
	private String parameter;

	/**
	 * Creates a datapoint config from an Homematic server event.
	 */
	public DatapointConfig(HmInterface hmInterface, String addressWithChannel, String parameter) {
		this.hmInterface = hmInterface;
		String[] configParts = StringUtils.trimToEmpty(addressWithChannel).split(":");
		setAddress(configParts[0]);
		this.channel = configParts[1];
		this.parameter = parameter;
	}

	/**
	 * Creates a datapoint config.
	 */
	public DatapointConfig(String address, String channel, String parameter) {
		this(address, channel, parameter, null, null, false);
	}

	/**
	 * Creates a datapoint config from the binding parser.
	 */
	public DatapointConfig(String address, String channel, String parameter, Converter<?> converter,
			BindingAction action, boolean forceUpdate) {
		setAddress(address);
		this.channel = channel;
		this.parameter = parameter;
		this.converter = converter;
		this.action = action;
		this.forceUpdate = forceUpdate;
	}

	/**
	 * Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address, strips a leading CCU team marker.
	 */
	private void setAddress(String address) {
		this.address = StringUtils.stripStart(address, "*");
	}

	/**
	 * Returns the channel.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Returns the parameter.
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Returns the interface.
	 */
	public HmInterface getHmInterface() {
		return hmInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(address).append(channel).append(parameter).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DatapointConfig)) {
			return false;
		}
		DatapointConfig comp = (DatapointConfig) obj;
		return new EqualsBuilder().append(address, comp.getAddress()).append(channel, comp.getChannel())
				.append(parameter, comp.getParameter()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("address", address).append("channel", channel).append("parameter", parameter);
		if (converter != null) {
			tsb.append("converter", converter);
		}
		if (action != null) {
			tsb.append("action", action);
		}
		if (forceUpdate) {
			tsb.append("forceUpdate", forceUpdate);
		}
		return tsb.toString();
	}

}
