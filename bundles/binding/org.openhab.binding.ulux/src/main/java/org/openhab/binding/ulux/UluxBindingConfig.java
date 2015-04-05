/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.core.binding.BindingConfig;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxBindingConfig implements BindingConfig {

	private short actorId;
	private short switchId;
	private UluxBindingConfigType type;

	public short getActorId() {
		return this.actorId;
	}

	public short getSwitchId() {
		return this.switchId;
	}

	public UluxBindingConfigType getType() {
		return this.type;
	}

	public void setActorId(short actorId) {
		this.actorId = actorId;
	}

	public void setSwitchId(short switchId) {
		this.switchId = switchId;
	}

	public void setType(UluxBindingConfigType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("switchId", this.switchId);
		builder.append("actorId", this.actorId);
		builder.append("type", this.type);

		return builder.toString();
	}
}