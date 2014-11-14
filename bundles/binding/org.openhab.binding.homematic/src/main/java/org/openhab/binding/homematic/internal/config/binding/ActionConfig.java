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

/**
 * Class with the action bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ActionConfig extends HomematicBindingConfig {

	/**
	 * Creates a action config.
	 */
	public ActionConfig(BindingAction action) {
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(action).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ActionConfig)) {
			return false;
		}
		ActionConfig comp = (ActionConfig) obj;
		return new EqualsBuilder().append(action, comp.getAction()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("action", action).toString();
	}

}
