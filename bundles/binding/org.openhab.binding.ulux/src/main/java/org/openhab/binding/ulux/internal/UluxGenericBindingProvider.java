/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingConfigType;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxGenericBindingProvider extends AbstractGenericBindingProvider implements UluxBindingProvider {

	private static final Logger LOG = LoggerFactory.getLogger(UluxGenericBindingProvider.class);

	private static final Map<String, UluxBindingConfigType> TYPES = new HashMap<String, UluxBindingConfigType>();

	static {
		TYPES.put("AmbientLight", UluxBindingConfigType.AMBIENT_LIGHT);
		TYPES.put("Audio", UluxBindingConfigType.AUDIO);
		TYPES.put("AudioRecord", UluxBindingConfigType.AUDIO_RECORD);
		TYPES.put("AudioVolume", UluxBindingConfigType.AUDIO_VOLUME);
		TYPES.put("Display", UluxBindingConfigType.DISPLAY);
		TYPES.put("Image", UluxBindingConfigType.IMAGE);
		TYPES.put("Key", UluxBindingConfigType.KEY);
		TYPES.put("Led", UluxBindingConfigType.LED);
		TYPES.put("Lux", UluxBindingConfigType.LUX);
		TYPES.put("PageIndex", UluxBindingConfigType.PAGE_INDEX);
		TYPES.put("Proximity", UluxBindingConfigType.PROXIMITY);
		TYPES.put("AudioPlayLocal", UluxBindingConfigType.AUDIO_PLAY_LOCAL);
		TYPES.put("EditValue", UluxBindingConfigType.EDIT_VALUE);
		TYPES.put("Video", UluxBindingConfigType.VIDEO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UluxBindingConfig getBinding(String itemName) {
		return (UluxBindingConfig) this.bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "ulux";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		LOG.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		final String[] configParts = bindingConfig.split(":");

		if (configParts.length < 2 || configParts.length > 4) {
			throw new BindingConfigParseException("Unexpected number of parts: " + configParts.length);
		}

		final UluxBindingConfig config = new UluxBindingConfig();
		config.setSwitchId(Short.valueOf(configParts[0]));

		// TODO backgroundLight
		// TODO lockMode
		// TODO page
		// TODO display
		// TODO proximity

		// configuration for an actor
		config.setActorId(Short.valueOf(configParts[1]));

		if (configParts.length > 2) {
			if (!TYPES.containsKey(configParts[2])) {
				throw new BindingConfigParseException("Unknown type: " + configParts[2]);
			}
			config.setType(TYPES.get(configParts[2]));
		} else {
			config.setType(UluxBindingConfigType.EDIT_VALUE);
		}

		if (configParts.length > 3) {
			config.setAdditionalConfiguration(configParts[3]);
		}

		LOG.debug("Adding binding: {}", config);

		addBindingConfig(item, config);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
		// if (!(item instanceof NumberItem)) {
		// throw new BindingConfigParseException(
		// "item '"
		// + item.getName()
		// + "' is of type '"
		// + item.getClass().getSimpleName()
		// +
		// "', only NumberItems are allowed - please check your *.items configuration");
		// }
	}

}
