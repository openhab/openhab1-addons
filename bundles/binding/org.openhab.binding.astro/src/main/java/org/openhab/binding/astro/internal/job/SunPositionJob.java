/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import org.openhab.binding.astro.internal.calc.AstroCalculator;
import org.openhab.binding.astro.internal.calc.SunPosition;
import org.openhab.binding.astro.internal.common.AstroConfig;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.core.library.types.DecimalType;

/**
 * Publishes the current azimuth and elevation.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class SunPositionJob extends AbstractBaseJob {

	@Override
	protected void executeJob() {
		AstroConfig config = AstroContext.getInstance().getConfig();
		AstroCalculator calc = new AstroCalculator();
		SunPosition sunPosition = calc.getSunPosition(Calendar.getInstance(), config.getLatitude(),
				config.getLongitude());

		publishState(AstroType.AZIMUTH, createDecimalType(sunPosition.getAzimuth()));
		publishState(AstroType.ELEVATION, createDecimalType(sunPosition.getElevation()));
	}

	private DecimalType createDecimalType(double value) {
		BigDecimal decimalValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
		return new DecimalType(decimalValue);
	}

}
