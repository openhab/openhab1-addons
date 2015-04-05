/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.UluxException;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
abstract class AbstractMessageHandler<T extends UluxMessage> implements UluxMessageHandler<T> {

	protected Collection<UluxBindingProvider> providers;

	protected EventPublisher eventPublisher;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void setProviders(Collection<UluxBindingProvider> providers) {
		this.providers = providers;
	}

	protected final Command createCommand(final String type, final short value) {
		final Command command;

		if (StringUtils.equalsIgnoreCase(type, "OnOffType")) {
			if (value == 0) {
				command = OnOffType.OFF;
			} else if (value == 1) {
				command = OnOffType.ON;
			} else {
				throw new UluxException("Unsupported value for OnOffType: " + value);
			}
		} else if (StringUtils.equalsIgnoreCase(type, "IncreaseDecreaseType")) {
			if (value == 0) {
				command = IncreaseDecreaseType.DECREASE;
			} else if (value == 1) {
				command = IncreaseDecreaseType.INCREASE;
			} else {
				throw new UluxException("Unsupported value for IncreaseDecreaseType: " + value);
			}
		} else if (StringUtils.equalsIgnoreCase(type, "OpenClosedType")) {
			if (value == 0) {
				command = OpenClosedType.CLOSED;
			} else if (value == 1) {
				command = OpenClosedType.OPEN;
			} else {
				throw new UluxException("Unsupported value for OpenClosedType: " + value);
			}
		} else if (StringUtils.equalsIgnoreCase(type, "StopMoveType")) {
			if (value == 0) {
				command = StopMoveType.STOP;
			} else if (value == 1) {
				command = StopMoveType.MOVE;
			} else {
				throw new UluxException("Unsupported value for StopMoveType: " + value);
			}
		} else if (StringUtils.equalsIgnoreCase(type, "UpDownType")) {
			if (value == 0) {
				command = UpDownType.DOWN;
			} else if (value == 1) {
				command = UpDownType.UP;
			} else {
				throw new UluxException("Unsupported value for UpDownType: " + value);
			}
		} else if (StringUtils.equalsIgnoreCase(type, "PercentType")) {
			command = new PercentType(value);
		} else if (StringUtils.equalsIgnoreCase(type, "DecimalType")) {
			command = new DecimalType(value);
		} else {
			command = new DecimalType(value);
		}

		return command;
	}

	protected final Map<String, UluxBindingConfig> getBindingConfigs(short actorId) {
		final Map<String, UluxBindingConfig> configs = new HashMap<String, UluxBindingConfig>();

		// TODO provide better methods in UluxBindingProvider
		for (final UluxBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final UluxBindingConfig binding = provider.getBinding(itemName);

				if (binding.getActorId() == actorId) {
					configs.put(itemName, binding);
				}
			}
		}

		return configs;
	}

	protected final Map<String, UluxBindingConfig> getBindingConfigs(String message) {
		final Map<String, UluxBindingConfig> configs = new HashMap<String, UluxBindingConfig>();

		// TODO provide better methods in UluxBindingProvider
		for (final UluxBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final UluxBindingConfig binding = provider.getBinding(itemName);

				if (StringUtils.equals(binding.getMessage(), message)) {
					configs.put(itemName, binding);
				}
			}
		}

		return configs;
	}

}
