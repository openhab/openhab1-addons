/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velux.things;

import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product characteristics: Product Position.
 * <P>
 * See <a href=
 * "https://velcdn.azureedge.net/~/media/com/api/klf200/technical%20specification%20for%20klf%20200%20api.pdf#page=110">KLF200
 * Standard Parameter definition</a>
 * <P>
 * Methods in handle this type of information:
 * <UL>
 * <LI>{@link #VeluxProductPosition(int)} to convert a Velux value into the characteristic.</LI>
 * <LI>{@link #VeluxProductPosition(PercentType)} to convert an openHAB value into the characteristic.</LI>
 * <LI>{@link #VeluxProductPosition()} to convert an openHAB STOP value into the characteristic.</LI>
 * <LI>{@link #isKnown} to determine whether the characteristic has got a valid value.</LI>
 * <LI>{@link #getPositionAsPercentType()} to convert the characteristic into an openHAB value.</LI>
 * <LI>{@link #getPositionAsVeluxType()} to convert the characteristic into a Velux value.</LI>
 * <LI>{@link #toString} to retrieve a human-readable description of this characteristic.</LI>
 * </UL>
 *
 * @see VeluxKLFAPI
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public class VeluxProductPosition {
    private static final Logger LOGGER = LoggerFactory.getLogger(VeluxProductPosition.class);

    // Make sure that the calculation are done as non-integer
    private static final float ONE = 1;

    private static final int VPP_OPENHAB_MIN = 0;
    private static final int VPP_OPENHAB_MAX = 100;
    private static final int VPP_VELUX_MIN = 0x0000;
    private static final int VPP_VELUX_MAX = 0xc800;
    public static final int VPP_VELUX_STOP = 0xD200;
    public static final int VPP_VELUX_UNKNOWN = 0xF7FF;

    private static final int VPP_VELUX_PERCENTAGE_MIN = 0xc900;
    private static final int VPP_VELUX_PERCENTAGE_MAX = 0xd0d0;

    // Class internal

    private PercentType position;

    // Constructor

    public VeluxProductPosition(PercentType position) {
        LOGGER.trace("VeluxProductPosition({} as PercentType) created.", position.intValue());
        this.position = position;
    }

    public VeluxProductPosition(int veluxPosition) {
        LOGGER.trace("VeluxProductPosition(constructur with {} as veluxPosition) called.", veluxPosition);
        if ((veluxPosition == VPP_VELUX_UNKNOWN) || (veluxPosition == VPP_VELUX_STOP) || (veluxPosition < VPP_VELUX_MIN)
                || (veluxPosition > VPP_VELUX_MAX)) {
            LOGGER.trace("VeluxProductPosition() gives up.");
            this.position = null;
        } else {
            float result = (ONE * veluxPosition - VPP_VELUX_MIN) / (VPP_VELUX_MAX - VPP_VELUX_MIN);
            result = VPP_OPENHAB_MIN + result * (VPP_OPENHAB_MAX - VPP_OPENHAB_MIN);
            LOGGER.trace("VeluxProductPosition() created with percent-type {}.", (int) result);
            this.position = new PercentType((int) result);
        }
    }

    public VeluxProductPosition() {
        LOGGER.trace("VeluxProductPosition() as STOP position created.");
        this.position = null;
    }

    // Class access methods

    public boolean isKnown() {
        return position != null;
    }

    public PercentType getPositionAsPercentType() {
        return position;
    }

    public PercentType getPositionAsPercentType(boolean toBeInverted) {
        return toBeInverted ? new PercentType(PercentType.HUNDRED.intValue() - position.intValue()) : position;
    }

    public int getPositionAsVeluxType() {
        if (this.position != null) {
            float result = (ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX - VPP_OPENHAB_MIN);
            result = VPP_VELUX_MIN + result * (VPP_VELUX_MAX - VPP_VELUX_MIN);
            return (int) result;
        } else {
            return VPP_VELUX_STOP;
        }
    }

    @Override
    public String toString() {
        if (this.position != null) {
            return String.format("%d", position.intValue());
        } else {
            return new String("*unknown*");
        }
    }

    // Helper methods

    public static int getRelativePositionAsVeluxType(boolean upwards, PercentType position) {
        LOGGER.trace("getRelativePositionAsVeluxType(upwards={},{}) created.", upwards, position);
        float result = (VPP_VELUX_PERCENTAGE_MAX + VPP_VELUX_PERCENTAGE_MIN) / 2;
        if (upwards) {
            result = result + (ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX - VPP_OPENHAB_MIN)
                    * ((VPP_VELUX_PERCENTAGE_MAX - VPP_VELUX_PERCENTAGE_MIN) / 2);
        } else {
            result = result - (ONE * position.intValue() - VPP_OPENHAB_MIN) / (VPP_OPENHAB_MAX - VPP_OPENHAB_MIN)
                    * ((VPP_VELUX_PERCENTAGE_MAX - VPP_VELUX_PERCENTAGE_MIN) / 2);
        }
        LOGGER.trace("getRelativePositionAsVeluxType() returns {}.", (int) result);
        return (int) result;
    }

}
