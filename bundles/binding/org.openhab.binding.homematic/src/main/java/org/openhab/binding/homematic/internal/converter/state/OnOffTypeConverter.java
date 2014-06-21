/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.library.types.OnOffType;

/**
 * Converts between openHAB OnOffType and Homematic values.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class OnOffTypeConverter extends AbstractEnumTypeConverter<OnOffType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OnOffType getFalseType() {
		return OnOffType.OFF;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OnOffType getTrueType() {
		return OnOffType.ON;
	}

	/**
	 * If the item is a sensor or a state from some devices, then OnOff must be
	 * inverted.
	 */
	@Override
	protected boolean isInvert(HmValueItem hmValueItem) {
		return isName(hmValueItem, "SENSOR") || isStateInvertDevice(hmValueItem);
	}
}
