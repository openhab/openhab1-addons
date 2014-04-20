/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.common;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
/**
 * Types that are supported for binding by an item.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public enum AstroType {
	AZIMUTH, ELEVATION, SUNRISE, NOON, SUNSET, SUNRISE_TIME, NOON_TIME, SUNSET_TIME;

	/**
	 * Returns the accepted datatype class for the AstroType.
	 */
	public Class<? extends State> getAcceptedDataType() {
		switch (this) {
		case AZIMUTH:
		case ELEVATION:
			return DecimalType.class;
		case SUNRISE:
		case NOON:
		case SUNSET:
			return OnOffType.class;
		case SUNRISE_TIME:
		case NOON_TIME:
		case SUNSET_TIME:
			return DateTimeType.class;
		default:
			return null;
		}
	}
}
