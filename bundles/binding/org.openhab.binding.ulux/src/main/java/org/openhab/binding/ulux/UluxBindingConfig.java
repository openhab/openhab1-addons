/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage;
import org.openhab.binding.ulux.internal.ump.messages.LedMessage;
import org.openhab.core.binding.BindingConfig;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxBindingConfig implements BindingConfig {

	private short actorId;
	private short switchId;
	private UluxBindingConfigType type;
	private String additionalConfiguration;

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

	public void setAdditionalConfiguration(String additionalConfiguration) {
		this.additionalConfiguration = additionalConfiguration;
	}

	public EventMessage.Key getKey() {
		if (this.type == UluxBindingConfigType.KEY) {
			if (StringUtils.equals(additionalConfiguration, "1")) {
				return EventMessage.Key.KEY_1;
			}
			if (StringUtils.equals(additionalConfiguration, "2")) {
				return EventMessage.Key.KEY_2;
			}
			if (StringUtils.equals(additionalConfiguration, "3")) {
				return EventMessage.Key.KEY_3;
			}
			if (StringUtils.equals(additionalConfiguration, "4")) {
				return EventMessage.Key.KEY_4;
			}
		}

		return null;
	}

	public LedMessage.Led getLed() {
		if (this.type == UluxBindingConfigType.LED) {
			if (StringUtils.equals(additionalConfiguration, "1")) {
				return LedMessage.Led.LED_1;
			}
			if (StringUtils.equals(additionalConfiguration, "2")) {
				return LedMessage.Led.LED_2;
			}
			if (StringUtils.equals(additionalConfiguration, "3")) {
				return LedMessage.Led.LED_3;
			}
			if (StringUtils.equals(additionalConfiguration, "4")) {
				return LedMessage.Led.LED_4;
			}
		}

		return null;

	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("switchId", this.switchId);
		builder.append("actorId", this.actorId);
		builder.append("type", this.type);
		if (this.additionalConfiguration != null) {
			builder.append("additionalConfiguration", this.additionalConfiguration);
		}

		return builder.toString();
	}
}