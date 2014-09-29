/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal.model;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageUtils;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AuxSensorProperties;

/**
 * An Auxiliary is a temperature and/or humidity sensors
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Auxiliary extends OmnilinkDevice {
	private static final Logger logger = LoggerFactory.getLogger(Auxiliary.class);

	private AuxSensorProperties properties;
	private boolean celsius;

	public Auxiliary(AuxSensorProperties properties, boolean celius) {
		this.properties = properties;
		this.celsius = celius;
	}

	@Override
	public AuxSensorProperties getProperties() {
		return properties;
	}

	public void setProperties(AuxSensorProperties properties) {
		this.properties = properties;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int setting = 0;
		switch (config.getObjectType()) {
		case AUX_CURRENT:
			setting = celsius ? MessageUtils.omniToC(properties.getCurrent())
					: MessageUtils.omniToF(properties.getCurrent());
			break;
		case AUX_HIGH:
			setting = celsius ? MessageUtils.omniToC(properties
					.getHighSetpoint()) : MessageUtils.omniToF(properties
					.getCurrent());
			break;
		case AUX_LOW:
			setting = celsius ? MessageUtils.omniToC(properties
					.getLowSetpoint()) : MessageUtils.omniToF(properties
					.getCurrent());
			break;
		case AUX_STATUS:
			setting = properties.getStatus();
			break;
		default:
			return;
		}
		logger.debug("updating item {} for type {} to  {}", item.getName(),
				config.getObjectType(), setting);
		if (item instanceof NumberItem) {
			publisher.postUpdate(item.getName(), new DecimalType(setting));
		}
	}

	public boolean isCelsius() {
		return celsius;
	}

	public void setCelsius(boolean celsius) {
		this.celsius = celsius;
	}

}
