/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxProductPosition {
	private final Logger logger = LoggerFactory.getLogger(VeluxProductPosition.class);
	private static final Logger loggerStatic = LoggerFactory.getLogger(VeluxProductPosition.class);

	final static public int VPP_VELUX_UNKNOWN = -1;

	// Make sure that the calculation are done as non-integer
	private static final float ONE = 1;

	private static final int VPP_OPENHAB_MIN = 0;
	private static final int VPP_OPENHAB_MAX = 100;
	private static final int VPP_VELUX_MIN = 0x0000;
	private static final int VPP_VELUX_MAX = 0xc800;
	private static final int VPP_VELUX_STOP = 0xD200;

	private static final int VPP_VELUX_PERCENTAGE_MIN = 0xc900;
	private static final int VPP_VELUX_PERCENTAGE_MAX = 0xd0d0;


	// Class internal

	private PercentType position;

	// Constructor

	public VeluxProductPosition(PercentType position) {
		logger.trace("VeluxProductPosition({} as PercentType) created.",position.intValue());
		this.position = position;
	}

	public VeluxProductPosition(int veluxPosition) {
		logger.trace("VeluxProductPosition(constructur with {} as veluxPosition) called.",veluxPosition);
		if ((veluxPosition == VPP_VELUX_UNKNOWN) || (veluxPosition == VPP_VELUX_STOP) ||
			(veluxPosition < VPP_VELUX_MIN) || (veluxPosition > VPP_VELUX_MAX)) {
			logger.trace("VeluxProductPosition() gives up.");
			this.position = null;
		} else {
			float result = (ONE * veluxPosition - VPP_VELUX_MIN) / (VPP_VELUX_MAX-VPP_VELUX_MIN);
			result = VPP_OPENHAB_MIN + result * (VPP_OPENHAB_MAX-VPP_OPENHAB_MIN);
			logger.trace("VeluxProductPosition() created with percent-type {}.",(int)result);
			this.position = new PercentType((int)result);
		}
	}

	public VeluxProductPosition() {
		logger.trace("VeluxProductPosition() as STOP position created.");
		this.position = null;
	}


	// Class access methods

	public boolean isKnown() {
		return position != null;
	}

	public PercentType getPositionAsPercentType() {
		return position;
	}

	public int getPositionAsVeluxType() {
		if (this.position != null) {
			float result = (ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX-VPP_OPENHAB_MIN);
			result = VPP_VELUX_MIN + result * (VPP_VELUX_MAX-VPP_VELUX_MIN);
			return (int)result;
		} else {
			return VPP_VELUX_STOP;
		}
	}

	public String toString() {
		if (this.position != null) {
			return String.format("%d", position.intValue());
		} else {
			return new String("*unknown*");
		}
	}

	// Helper methods

	public static int getRelativePositionAsVeluxType(boolean upwards, PercentType position) {
		loggerStatic.trace("getRelativePositionAsVeluxType(upwards={},{}) created.",upwards,position);
		float result = (VPP_VELUX_PERCENTAGE_MAX + VPP_VELUX_PERCENTAGE_MIN) / 2;
		if (upwards) {
			result = result +
					(ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX-VPP_OPENHAB_MIN) *
					((VPP_VELUX_PERCENTAGE_MAX-VPP_VELUX_PERCENTAGE_MIN) / 2);
		} else {
			result = result -
					(ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX-VPP_OPENHAB_MIN) *
					((VPP_VELUX_PERCENTAGE_MAX-VPP_VELUX_PERCENTAGE_MIN) / 2);
		}
		loggerStatic.trace("getRelativePositionAsVeluxType() returns {}.",(int)result);
		return (int)result;
	}

}
/**
 * end-of-VeluxProductPosition.java
 */
