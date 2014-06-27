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
import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts between openHAB OpenClosedType and Homematic values.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class OpenClosedTypeConverter extends AbstractEnumTypeConverter<OpenClosedType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OpenClosedType getFalseType() {
		return OpenClosedType.CLOSED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OpenClosedType getTrueType() {
		return OpenClosedType.OPEN;
	}

	/**
	 * Invert only boolean values which are not from a sensor or a state from
	 * some devices.
	 */
	@Override
	protected boolean isInvert(HmValueItem hmValueItem) {
		return !isName(hmValueItem, "SENSOR") && !isStateInvertDevice(hmValueItem) && hmValueItem.isBooleanValue();
	}

}
