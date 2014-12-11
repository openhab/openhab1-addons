/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config.binding;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.homematic.internal.config.BindingAction;
import org.openhab.binding.homematic.internal.converter.state.Converter;

/**
 * Class with the variable bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class VariableConfig extends ValueBindingConfig {
	private String name;

	public VariableConfig(String name) {
		this.name = name;
	}

	/**
	 * Creates a variable config from the binding parser.
	 */
	public VariableConfig(String name, Converter<?> converter, BindingAction action, boolean forceUpdate) {
		this.name = name;
		this.converter = converter;
		this.action = action;
		this.forceUpdate = forceUpdate;
	}

	/**
	 * Returns the name of the variable.
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(this.getClass().getName()).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof VariableConfig)) {
			return false;
		}
		VariableConfig comp = (VariableConfig) obj;
		return new EqualsBuilder().append(name, comp.getName())
				.append(this.getClass().getName(), comp.getClass().getName()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("name", name);
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
