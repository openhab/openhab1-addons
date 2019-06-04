/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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

import java.util.HashMap;
import java.util.Map;

/**
 * <B>Velux</B> product characteristics: Velocity.
 * <P>
 * See <a href=
 * "https://velcdn.azureedge.net/~/media/com/api/klf200/technical%20specification%20for%20klf%20200%20api.pdf#page=45">KLF200
 * Velocity parameter</a>
 * <P>
 * Methods in handle this type of information:
 * <UL>
 * <LI>{@link #getVelocity()} to retrieve the value of the characteristic.</LI>
 * <LI>{@link #get(int)} to convert a value into the characteristic.</LI>
 * <LI>{@link #dump} to retrieve a human-readable description of all values.</LI>
 * </UL>
 *
 * @see VeluxKLFAPI
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public enum VeluxProductVelocity {
    DEFAULT((short) 0),
    SILENT((short) 1),
    FAST((short) 2),
    VELOCITY_NOT_AVAILABLE((short) 255),
    UNDEFTYPE((short) 0xffff);

    // Type definitions, class-internal variables

    private static String outputSeparator = ",";

    // Class internal

    private short velocity;

    // Reverse-lookup map for getting a VeluxProductType from an TypeId
    private static final Map<Integer, VeluxProductVelocity> LOOKUPTYPEID2ENUM = new HashMap<Integer, VeluxProductVelocity>();

    static {
        for (VeluxProductVelocity typeId : VeluxProductVelocity.values()) {
            LOOKUPTYPEID2ENUM.put((int) typeId.getVelocity(), typeId);
        }
    }

    // Constructor

    private VeluxProductVelocity(short velocity) {
        this.velocity = velocity;
    }

    VeluxProductVelocity(String categoryString) {
        try {
            this.velocity = VeluxProductVelocity.valueOf(categoryString).getVelocity();
        } catch (IllegalArgumentException e) {
            try {
                this.velocity = VeluxProductVelocity.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
                        .getVelocity();
            } catch (IllegalArgumentException e2) {
                this.velocity = -1;
            }
        }
    }

    // Class access methods

    public short getVelocity() {
        return velocity;
    }

    public static VeluxProductVelocity get(int velocity) {
        VeluxProductVelocity typeId = LOOKUPTYPEID2ENUM.get(velocity);
        return (typeId == null) ? VeluxProductVelocity.UNDEFTYPE : typeId;
    }

    public static String dump() {
        StringBuilder sb = new StringBuilder();
        for (VeluxProductVelocity typeId : VeluxProductVelocity.values()) {
            sb.append(typeId).append(outputSeparator);
        }
        if (sb.lastIndexOf(outputSeparator) > 0) {
            sb.deleteCharAt(sb.lastIndexOf(outputSeparator));
        }
        return sb.toString();
    }

}
