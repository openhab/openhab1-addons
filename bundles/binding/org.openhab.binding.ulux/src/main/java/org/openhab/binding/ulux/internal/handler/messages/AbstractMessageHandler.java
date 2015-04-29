/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler.messages;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingConfigType;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public abstract class AbstractMessageHandler<T extends UluxMessage> implements UluxMessageHandler<T> {

	private final Logger LOG = LoggerFactory.getLogger(AbstractMessageHandler.class);

	protected Collection<UluxBindingProvider> providers;

	protected EventPublisher eventPublisher;

	private ItemRegistry itemRegistry;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void setProviders(Collection<UluxBindingProvider> providers) {
		this.providers = providers;
	}

	/**
	 * TODO RollershutterItem handles UpDownType as well as StopMoveType and PercentType...
	 * 
	 * @return <code>null</code> if command cannot be created
	 */
	protected final Command createCommand(final String itemName, final short value) {
		final Item item;

		try {
			item = itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e) {
			LOG.debug("Tried to create command for non-existing item: {}", e.getMessage());
			return null;
		}

		Set<Class<? extends Command>> acceptedCommandTypes = new HashSet<Class<? extends Command>>(
				item.getAcceptedCommandTypes());

		if (acceptedCommandTypes.contains(IncreaseDecreaseType.class)) {
			if (value == 0) {
				return IncreaseDecreaseType.DECREASE;
			} else if (value == 1) {
				return IncreaseDecreaseType.INCREASE;
			} else {
				LOG.debug("Unsupported value for IncreaseDecreaseType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(OpenClosedType.class)) {
			if (value == 0) {
				return OpenClosedType.CLOSED;
			} else if (value == 1) {
				return OpenClosedType.OPEN;
			} else {
				LOG.debug("Unsupported value for OpenClosedType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(UpDownType.class)) {
			if (value == 0) {
				return UpDownType.DOWN;
			} else if (value == 1) {
				return UpDownType.UP;
			} else {
				LOG.debug("Unsupported value for UpDownType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(StopMoveType.class)) {
			if (value == 0) {
				return StopMoveType.STOP;
			} else if (value == 1) {
				return StopMoveType.MOVE;
			} else {
				LOG.debug("Unsupported value for StopMoveType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(PercentType.class)) {
			if (value >= 0 || value <= 100) {
				return new PercentType(value);
			} else {
				LOG.debug("Unsupported value for PercentType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(OnOffType.class)) {
			if (value == 0) {
				return OnOffType.OFF;
			} else if (value == 1) {
				return OnOffType.ON;
			} else {
				LOG.debug("Unsupported value for OnOffType: {}", value);
			}
		}

		if (acceptedCommandTypes.contains(DecimalType.class)) {
			return new DecimalType(value);
		}

		LOG.debug("Unsupported value '{}' for item '{}'!", value, item);

		return null;
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

	protected final Map<String, UluxBindingConfig> getBindingConfigs(UluxBindingConfigType type) {
		final Map<String, UluxBindingConfig> configs = new HashMap<String, UluxBindingConfig>();

		// TODO provide better methods in UluxBindingProvider
		for (final UluxBindingProvider provider : this.providers) {
			for (final String itemName : provider.getItemNames()) {
				final UluxBindingConfig binding = provider.getBinding(itemName);

				if (binding.getType() == type) {
					configs.put(itemName, binding);
				}
			}
		}

		return configs;
	}

}
